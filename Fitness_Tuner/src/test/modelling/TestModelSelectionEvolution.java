package test.modelling;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.fabi.visualizations.evolution.AsymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModelGroupChromosomeGenerator;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModelGroupFitnessFunction;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import configuration.CfgTemplate;

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution;

public class TestModelSelectionEvolution {

	public static final DataSource data = new ConstantLinearConstantData();
	
	public static final String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Modelling\\SelectionEvolution\\";
	
	public static final Logger logger = Logger.getLogger("Test Model Selection Evolution");

	public static void main(String[] args) throws SecurityException, IOException {
		String s = "Run_";
		File dir;
		int idx = 0;
		do {
			dir = new File(PATH + s + (idx++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		logger.setLevel(Level.FINEST);
		logger.addHandler(new StreamHandler(new FileOutputStream(path + "log.txt"), new SimpleFormatter()));
		logger.log(Level.INFO, "Initializing evolution");
		logger.log(Level.INFO, "For data: " + data.getName());
		Population p = new PopulationBase(new ModelGroupChromosomeGenerator(10, new ModelGroupFitnessFunction(data)), 30);
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		algorithm.init(p, new AsymetricCrossoverEvolutionStrategy());
		logger.log(Level.INFO, "0: " + algorithm.getBest().getFitness() + " " + algorithm.getBest());
		createVisualization(new File(path + "00.png"), algorithm.getBest());
		for (int i = 0; i < 20; i++) {
			algorithm.optimize();
			logger.log(Level.INFO, (i + 1) + ": " + algorithm.getBest().getFitness() + " " + algorithm.getBest());
			String str = (i < 9) ? ("0" + (i + 1)) : Integer.toString(i + 1);
			createVisualization(new File(path + str + ".png"), algorithm.getBest());
		}
		logger.log(Level.INFO, "Terminated.");
	}
	
	protected static void createVisualization(File file, Chromosome c) throws IOException {
		CfgTemplate[] cfg = (CfgTemplate[]) c.getPhenotype();
		ModelSource[] models = new ModelSource[cfg.length];
		for (int i = 0; i < cfg.length; i++) {
			models[i] = ModGenTools.learnRegressionModel(cfg[i], data);
		}
		ScatterplotVisualization vis = new ScatterplotVisualization();
		vis.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		vis.setGridVisible(false);
		vis.setBackground(Color.BLACK);
		vis.setDotSizeModel(TestEvolution.getDotSizeModel(vis));
		vis.setColorModel(new ColorModel() {
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
		double[][] bounds = org.fabi.visualizations.tools.math.Arrays.getBasicStats(data.getInputDataVectors());
		vis.setxAxisRangeLower(bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][0] - bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0]);
		vis.setxAxisRangeUpper(bounds[org.fabi.visualizations.tools.math.Arrays.UPPER_BOUND][0] + bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0]);
		
		BufferedImage img = vis.getVisualizationAsImage(1024, 768);
		logger.log(Level.INFO, "  (... saving visualization)");
		ImageIO.write(img, "png",file);
	}

}
