package test.evolution;

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

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution.AreaAdditionalDrawer;
import test.fitness.LocalFitness;

public class TestVisualizationEvolutionPolynomial {
	

	protected static String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Evolution_Selected_Polynomial\\";
	
	protected static DataSource data = new RandomArtificialData();
	
	public static final Logger logger = Logger.getLogger("Test Visualization Evolution Modgen");
	
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

	
	static CfgTemplate[] templates;
				
	public static ModelSource[] getModels(DataSource data) {
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
			ms[i] = ModGenTools.learnRegressionModel(templates[i], new BootstrappedDataSource(data)); // bootstrapping added 2013-03-09 09:38
		}
		return ms;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String s = "Run_";
		File dir;
		int i = 0;
		do {
			dir = new File(PATH + s + (i++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		
		final ModelSource[] ms = getModels(data);
//		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE = 1.5;
		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.adjustFitness(data, ms);
		
		VisualizationEvolution evolution = new VisualizationEvolution();
		VisualizationEvolution.POPULATION_SIZE = 100;
		VisualizationEvolution.STEPS = 1000;
		
		evolution.setModeller(new Modeller() {
			
			@Override
			public ModelSource[] getModels(DataSource data) {
				return ms;
			}
		});
		
//		
		logger.log(Level.INFO, "Starting visualization evolution.");
		ScatterplotVisualization[] vis = evolution.evolve(data);
		
		for (int j = 0; j < vis.length; j++) {
			vis[j].getVisualizationAsComponent();
			double[] xBoundsOrig = new double[]{vis[j].getxAxisRangeLower(), vis[j].getxAxisRangeUpper()};
			double[] yBoundsOrig = TestEvolution.getyBounds(vis[j]);
			

			ModelSource[] models = new ModelSource[vis[j].getSource().getModelSourceCount()];
			for (int z = 0; z < models.length; z++) {
				models[z] = vis[j].getSource().getModelSource(z);
			}
			double[][][] responses = LocalFitness.getResponses(vis[j], models);
			double[][] inputs = LocalFitness.getModelInputs(vis[j]);
			
			double[][] histogram = new double[inputs.length - 1][2];
			for (int z = 0; z < histogram.length; z++) {
				histogram[z][0] = inputs[z + 1][vis[j].getxAxisAttributeIndex()];
				histogram[z][1] = org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.evaluateLocalAt(responses, z + 1, vis[j]);
			}
			double w = inputs[1][vis[j].getxAxisAttributeIndex()] - inputs[0][vis[j].getxAxisAttributeIndex()];
			double[] xBounds = TestEvolution.getxBounds(vis[j]);
			double offset = (xBounds[1] - xBounds[0]) * 0.1;
			vis[j].setxAxisRangeLower(xBounds[0] - offset);
			vis[j].setxAxisRangeUpper(xBounds[1] + offset);
			double[] yBounds = TestEvolution.getyBounds(vis[j]);
			offset = (yBounds[1] - yBounds[0]) * 0.1;
			vis[j].setyAxisRangeLower(yBounds[0] - offset);
			vis[j].setyAxisRangeUpper(yBounds[1] + offset);
//			vis[j].setGridVisible(false);
//			vis[j].setBackground(Color.BLACK);
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
			
			vis[j].setAdditionalDrawers(new AdditionalDrawer[]{new AreaAdditionalDrawer(
					xBoundsOrig[0],
					xBoundsOrig[1],
					yBoundsOrig[0],
					yBoundsOrig[1], Color.BLACK),
					new HistogramAdditionalDrawer(vis[j], "Fitness", new Color(1.0f, 0.0f, 0.0f, 1.0f), histogram, w, 0.1)});
//			});
					
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
