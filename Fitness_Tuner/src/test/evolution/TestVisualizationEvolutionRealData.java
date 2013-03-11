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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.evolution.observers.BestFitnessVisualizationObserver;
import org.fabi.visualizations.evolution.observers.EvolutionObserver;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.VisualizationEvolution;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.additional.HistogramAdditionalDrawer;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

import configuration.CfgTemplate;
import configuration.models.single.PolynomialModelConfig;

import test.evolution.TestEvolution.AreaAdditionalDrawer;
import test.fitness.LocalFitness;

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
	

	protected static class BootstrappedDataSource implements DataSource {
		
		protected double[][] inputs;
		protected double[][] outputs;
		
		public BootstrappedDataSource(DataSource orig) {
			double[][] oi = orig.getInputDataVectors();
			double[][] oo = orig.getOutputDataVectors();
			inputs = new double[oi.length / 2][];
			outputs = new double[oo.length / 2][];
			int rnd;
			Random r = new Random(System.currentTimeMillis());
			for (int i = 0; i < inputs.length; i++) {
				rnd = r.nextInt(oi.length);
				inputs[i] = oi[rnd];
				outputs[i] = oo[rnd];
			}
		}
		
		public double[][] getInputDataVectors() { return inputs; }
		public double[][] getOutputDataVectors() { return outputs; }
		public int inputsNumber() { return inputs[0].length; }
		public int outputsNumber() { return outputs[0].length; }
		public String getName() { return "Data"; }
	}
	
	
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
		
//		double minWeightedDistance = 0.1;
//		double s1 = 0.5, s2 = 1.0, v = 2.0, si = 1.5;
//		VisualizationEvolution.setMinWeightedDistance(minWeightedDistance);
//		VisualizationEvolution.setSimilaritySignificance(s1);
//		VisualizationEvolution.setSimilaritySignificance2(s2);
//		VisualizationEvolution.setVarianceSignificance(v);
//		VisualizationEvolution.setSizeSignificance(si);
		VisualizationEvolution.POPULATION_SIZE = 100;
		VisualizationEvolution.STEPS = 250;
		
		VisualizationEvolution evolution = new VisualizationEvolution();
		CfgTemplate[] templates = new CfgTemplate[10];
		for (int i2 = 0; i2 < templates.length; i2++) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i2 + 3);
			templates[i2] = cfg;
		}
		final ModelSource[] ms = new ModelSource[10];
		for (int i2 = 0; i2 < ms.length; i2++) {
			ms[i2] = ModGenTools.learnRegressionModel(templates[i2], new BootstrappedDataSource(data)); // bootstrapping added 2013-03-08 23:18
		}

//		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE = 1.5;
		
		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.adjustFitness(data, ms);
		
		
		evolution.setModeller(new Modeller() {
						
			@Override
			public ModelSource[] getModels(DataSource data) {
				return ms;
			}
		});
		evolution.setObservers(new EvolutionObserver[]{new BestFitnessVisualizationObserver(path + "fitness.png", false)});
		
		logger.log(Level.INFO, "Starting visualization evolution.");
		ScatterplotVisualization[] vis = evolution.evolve(data);
		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction ff = new org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction(ms, data);
		
		for (int j = 0; j < vis.length; j++) {
			String n = (j < 10) ? ("0" + Integer.toString(j)) : Integer.toString(j);
			final ScatterplotVisualization v = vis[j];
			Chromosome c = new ChromosomeBase() {
				
				@Override
				public int compareTo(Chromosome arg0) { return 0;}
				
				@Override
				public void mutate(double probability) { }
				
				@Override
				public Object getPhenotype() { return v ;}
				
				@Override
				public void cross(Chromosome other) {}
				
				@Override
				public Chromosome copy() {return null;	}
			};
			double ft = ff.getFitness(c);
			vis[j].getVisualizationAsComponent();
			double[][] bounds = vis[j].getActualAxesBounds();
			double[] yBounds = TestEvolution.getyBounds(vis[j]);
			double[][][] responses = LocalFitness.getResponses(vis[j], ms);
			double[][] inputs = LocalFitness.getModelInputs(vis[j]);
			
			double[][] histogram = new double[inputs.length - 1][2];
			for (int z = 0; z < histogram.length; z++) {
				histogram[z][0] = inputs[z + 1][vis[j].getxAxisAttributeIndex()];
				histogram[z][1] = ScatterplotChromosomeFitnessFunction.evaluateLocalAt(responses, z + 1, vis[j]);
			}
			double w = inputs[1][vis[j].getxAxisAttributeIndex()] - inputs[0][vis[j].getxAxisAttributeIndex()];
			AdditionalDrawer ad = new AreaAdditionalDrawer(
					bounds[0][0],
					bounds[0][1],
					yBounds[0], yBounds[1], Color.BLACK);
			double[] xBounds = TestEvolution.getxBounds(vis[j]);
			double offset = (xBounds[1] - xBounds[0]) * 0.1;
			vis[j].setxAxisRangeLower(xBounds[0] - offset);
			vis[j].setxAxisRangeUpper(xBounds[1] + offset);
//			yBounds = TestEvolution.getyBounds(vis[j]);
			offset = (yBounds[1] - yBounds[0]) * 0.1;
			vis[j].setyAxisRangeLower(yBounds[0] - offset);
			vis[j].setyAxisRangeUpper(yBounds[1] + offset);
//			vis[j].setGridVisible(false);
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
						return new Color(0.0f, 0.0f, 0.0f, 0.25f);
					} else {
						return new Color(1.0f, 0.0f, 0.0f, 1.0f);
					}
				}
			});
			vis[j].setOutputPrecision(200);
			vis[j].setAdditionalDrawers(new AdditionalDrawer[]{ad,
					new HistogramAdditionalDrawer(vis[j], "Fitness", new Color(1.0f, 0.0f, 0.0f, 1.0f), histogram, w, 0.1)});
			logger.log(Level.INFO, "Saving visualization #" + j + ".");
			BufferedImage img = vis[j].getVisualizationAsImage(800, 600);
			ImageIO.write(img, "png",new File(path + n + "_" + ft + ".png"));
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(path + "date.txt"));
		bw.write(new Date().toString());
		bw.newLine();
		bw.write(DATA);
		bw.newLine();
		bw.write("100 individuals x 250 generations");
//		bw.newLine();
//		bw.write("MinWeightedDistance: " + minWeightedDistance);
//		bw.newLine();
//		bw.write("Evol. params: " + s1 + " " + s2 + " " + v + " " + si);
		bw.newLine();
		bw.close();
		logger.log(Level.INFO, "Terminated.");
	}
}
