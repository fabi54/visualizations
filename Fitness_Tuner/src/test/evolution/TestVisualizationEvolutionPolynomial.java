package test.evolution;

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

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution.AreaAdditionalDrawer;

public class TestVisualizationEvolutionPolynomial {
	

	protected static String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Evolution_Selected_Polynomial\\";
	
	protected static DataSource data = new ConstantLinearConstantData();
	
	public static final Logger logger = Logger.getLogger("Test Visualization Evolution Modgen");
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String s = "Run_";
		File dir;
		int i = 0;
		do {
			dir = new File(PATH + s + (i++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		
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
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(path + "date.txt"));
		bw2.write(new Date().toString());
		bw2.newLine();
		bw2.write("Local fitness evaluation.");
		bw2.newLine();
		bw2.close();
		logger.log(Level.INFO, "Terminated.");
	}
}
