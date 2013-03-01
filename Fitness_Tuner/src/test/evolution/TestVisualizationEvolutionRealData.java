package test.evolution;

import game.data.AbstractGameData;
import game.evolution.treeEvolution.evolutionControl.EvolutionUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.fabi.visualizations.evolution.scatterplot.VisualizationEvolution;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

import configuration.CfgTemplate;
import configuration.models.single.PolynomialModelConfig;

import test.evolution.TestEvolution.AreaAdditionalDrawer;

/*
 * 2013-02-16 22:35 VisualizationEvolution: added line "ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = 0.5;"
 * 2013-02-16 22:38 VisualizationEvolution: added line "ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = 2.0;"
 * 2013-02-16 22:41 VisualizationEvolution: changes:
 * 		"ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = 0.5
 *		 ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE2 = 0.5;
 *		 ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = 5.0;"
 */

public class TestVisualizationEvolutionRealData {
	

	protected static String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Evolution_Real_Data\\";
	protected static String DATA = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\External\\data\\bosthouse.txt";
	
	
	public static final Logger logger = Logger.getLogger("Test Visualization Evolution");
	
	public static void main(String[] args) throws IOException {
		String s = "Run_";
		File dir;
		int i = 0;
		do {
			dir = new File(PATH + s + (i++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		
		final AbstractGameData d = EvolutionUtils.readDataFromFile(DATA);
		
		DataSource data = new DataSource() {
			
			@Override
			public int outputsNumber() {
				return d.getONumber();
			}
			
			@Override
			public int inputsNumber() {
				return d.getINumber();
			}
			
			@Override
			public double[][] getOutputDataVectors() {
				return d.getOutputAttrs();
			}
			
			@Override
			public String getName() {
				return "data";
			}
			
			@Override
			public double[][] getInputDataVectors() {
				return d.getInputVectors();
			}
		};
		
		double minWeightedDistance = 0.1;
		double s1 = 0.5, s2 = 1.0, v = 2.0, si = 1.5;
		VisualizationEvolution.setMinWeightedDistance(minWeightedDistance);
		VisualizationEvolution.setSimilaritySignificance(s1);
		VisualizationEvolution.setSimilaritySignificance2(s2);
		VisualizationEvolution.setVarianceSignificance(v);
		VisualizationEvolution.setSizeSignificance(si);
		
		VisualizationEvolution evolution = new VisualizationEvolution();
		
		evolution.setModeller(new Modeller() {
			
			CfgTemplate[] templates;
						
			@Override
			public ModelSource[] getModels(DataSource data) {
				if (templates == null) {
					templates = new CfgTemplate[10];
					for (int i = 0; i < templates.length; i++) {
						PolynomialModelConfig cfg = new PolynomialModelConfig();
						cfg.setMaxDegree(i + 3);
						templates[i] = cfg;
					}
				}
				ModelSource[] ms = new ModelSource[10];
				for (int i = 0; i < ms.length; i++) {
					ms[i] = ModGenTools.learnRegressionModel(templates[i], data);
				}
				return ms;
			}
		});
		
//		
		logger.log(Level.INFO, "Starting visualization evolution.");
		ScatterplotVisualization[] vis = evolution.evolve(data);
		
		for (int j = 0; j < vis.length; j++) {
			vis[j].getVisualizationAsComponent();
			double[] yBounds = TestEvolution.getyBounds(vis[j]);
			vis[j].setAdditionalDrawers(new AdditionalDrawer[]{new AreaAdditionalDrawer(
					vis[j].getxAxisRangeLower(),
					vis[j].getxAxisRangeUpper(),
					yBounds[0],
					yBounds[1])});
			double[] xBounds = TestEvolution.getxBounds(vis[j]);
			double offset = (xBounds[1] - xBounds[0]) * 0.1;
			vis[j].setxAxisRangeLower(xBounds[0] - offset);
			vis[j].setxAxisRangeUpper(xBounds[1] + offset);
			yBounds = TestEvolution.getyBounds(vis[j]);
			offset = (yBounds[1] - yBounds[0]) * 0.1;
			vis[j].setyAxisRangeLower(yBounds[0] - offset);
			vis[j].setyAxisRangeUpper(yBounds[1] + offset);
			vis[j].setGridVisible(false);
			vis[j].setBackground(Color.BLACK);
			vis[j].setDotSizeModel(TestEvolution.getDotSizeModel(vis[j]));
			vis[j].setColorModel(new ColorModel() {
				@Override public void init(ScatterplotSource source) { }
				@Override public void init(ScatterplotVisualization visualization) { }
				@Override public String getName() { return ""; }
				@Override public JComponent getControls() { return null; }
				
				@Override
				public Color getColor(double[] inputs, double[] outputs, boolean data,
						int index, int[] inputsIndices, int[] outputsIndices) {
					if (data) {
						return new Color(1.0f, 1.0f, 1.0f, 0.25f);
					} else {
						return new Color(0.0f, 1.0f, 1.0f, 1.0f);
					}
				}
			});
			logger.log(Level.INFO, "Saving visualization #" + j + ".");
			BufferedImage img = vis[j].getVisualizationAsImage(800, 600);
			String n = (j < 10) ? ("0" + Integer.toString(j)) : Integer.toString(j);
			ImageIO.write(img, "png",new File(path + n + ".png"));
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(path + "date.txt"));
		bw.write(new Date().toString());
		bw.newLine();
		bw.write(DATA);
		bw.newLine();
		bw.write("MinWeightedDistance: " + minWeightedDistance);
		bw.newLine();
		bw.write("Evol. params: " + s1 + " " + s2 + " " + v + " " + si);
		bw.newLine();
		bw.close();
		logger.log(Level.INFO, "Terminated.");
	}
}
