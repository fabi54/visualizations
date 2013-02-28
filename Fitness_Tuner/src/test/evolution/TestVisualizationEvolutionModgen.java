package test.evolution;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
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
import org.fabi.visualizations.scatter.AdditionalDrawer;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

import configuration.CfgTemplate;
import configuration.ConfigurationFactory;

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution.AreaAdditionalDrawer;

/*
 * 2013-02-16 22:35 VisualizationEvolution: added line "ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = 0.5;"
 * 2013-02-16 22:38 VisualizationEvolution: added line "ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = 2.0;"
 * 2013-02-16 22:41 VisualizationEvolution: changes:
 * 		"ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = 0.5
 *		 ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE2 = 0.5;
 *		 ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = 5.0;"
 */

public class TestVisualizationEvolutionModgen {
	

	protected static String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Evolution_Selected_Modgen\\";
	
	protected static DataSource data = new LongHeavisideData();
	
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
		
		File ed = new File("evolution");
		if (ed.exists()) {
			ed.delete();
		}
		ed.deleteOnExit();
		
		
		// write data
		java.util.Random random = new java.util.Random(System.currentTimeMillis());
		int rnd_t = random.nextInt();
		File f = new File(rnd_t + ".txt");
		while (f.exists() || Math.abs(rnd_t) < 10000) {
			rnd_t = random.nextInt();
			f = new File(rnd_t + ".txt");
		}
		logger.log(Level.INFO, "Computing models (" + rnd_t + ").");
		final int rnd = rnd_t;
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (int j = 0; j < data.inputsNumber(); j++) {
			bw.write("in" + j);
			bw.write("\t");
		}
		for (int k = 0; k < data.outputsNumber(); k++) {
			if (k > 0) { bw.write("\t"); }
			bw.write("!out" + k);
		}
		bw.newLine();
		double[][] in = data.getInputDataVectors();
		double[][] out = data.getOutputDataVectors();
		for (int j = 0; j < in.length; j++) {
			for (int k = 0; k < in[j].length; k++) {
				bw.write(Double.toString(in[j][k]));
				bw.write("\t");
			}
			for (int k = 0; k < out[j].length; k++) {
				if (k > 0) { bw.write("\t"); }
				bw.write(Double.toString(out[j][k]));
			}
			bw.newLine();
		}
		bw.close();
		for (int r = 0; r < 10; r++) {
			logger.log(Level.INFO, r + ": Running ModGen: \"java -jar C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\External\\ModGen_0.13.jar -file " + rnd + ".txt -time 60 -sc\"");
			Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\External\\ModGen_0.13.jar -file " + rnd + ".txt -time 60 -sc");
			java.io.InputStream ins = p.getInputStream();
	        //int n;
	        byte[] buffer = new byte[4096];
	        while ((/*n =*/ ins.read(buffer)) != -1) {
//	            System.out.write(buffer, 0, n);
//	            System.out.flush();
	        }
			//p.waitFor();
			logger.log(Level.INFO, r + ": ModGen finished.");
		}
		f.delete();
		
		File[] files = ed.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return (!arg0.isDirectory() && arg0.getName().contains(Integer.toString(rnd)));
			}
		});
		logger.log(Level.INFO, "Learning models.");
		final ModelSource[] ms = new ModelSource[files.length];
		for (int k = 0; k < files.length; k++) {
			CfgTemplate cfg = ConfigurationFactory.getConfiguration(files[i].getAbsolutePath());
			ms[k] = ModGenTools.learnRegressionModel(cfg, data);
			files[i].renameTo(new File(path + files[i].getName()));
		}
		
		evolution.setModeller(new Modeller() {
			
			@Override
			public ModelSource[] getModels(DataSource data) {
				return ms;
			}
		});
		
//		evolution.setModeller(new Modeller() {
//			
//			@Override
//			public ModelSource[] getModels(final DataSource data) {
//				logger.log(Level.INFO, "Initializing model selection evolution");
//				logger.log(Level.INFO, "For data: " + data.getName());
//				final ModelGroupFitnessFunction fitness = new ModelGroupFitnessFunction(data);
//				Population p = new PopulationBase(new ChromosomeGeneratorBase() {
//					
//					@Override
//					public Chromosome generate() {
//						ModelGroupChromosome c = new ModelGroupChromosome(10, fitness) {
//
//							@Override
//							protected CfgTemplate[] getTemplates() {
//								return templates;
//							}
//						};
//						return c;
//					}
//				}, 30);
//				GeneticAlgorithm algorithm = new GeneticAlgorithm();
//				algorithm.init(p, new AsymetricCrossoverEvolutionStrategy());
//				Chromosome c = algorithm.getBest();
//				logger.log(Level.INFO, "0: " + c.getFitness() + " " + c);
//				for (int i = 0; i < 20; i++) {
//					algorithm.optimize();
//					c = algorithm.getBest();
//					logger.log(Level.INFO, (i + 1) + ": " + c.getFitness() + " " + c);
//				}
//				logger.log(Level.INFO, "Model selection evolution finished.");
//				CfgTemplate[] cfg = (CfgTemplate[]) c.getPhenotype();
//				ModelSource[] models = new ModelSource[cfg.length];
//				for (int i = 0; i < models.length; i++) {
//					models[i] = ModGenTools.learnRegressionModel(cfg[i], data);
//				}
//				return models;
//			}
//		});
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
		bw2.close();
		logger.log(Level.INFO, "Terminated.");
	}
}
