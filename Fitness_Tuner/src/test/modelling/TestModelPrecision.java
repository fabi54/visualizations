package test.modelling;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.fabi.visualizations.evolution.scatterplot.modelling.ModelPool;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import test.artificialdata.onedimensional.*;
import test.evolution.TestEvolution;

import configuration.CfgTemplate;

public class TestModelPrecision {
	
	public static final DataSource data = new RandomArtificialData();
	
	public static final String PATH = "C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\Data\\Results\\Tests\\Modelling\\Precision\\";
	
	public static final Logger logger = Logger.getLogger("Test Model Precision");
	
	public static void main(String[] args) throws SecurityException, IOException {
		String s = "Run_";
		File dir;
		int i = 0;
		do {
			dir = new File(PATH + s + (i++));
		} while (dir.exists());
		dir.mkdir();
		String path = dir.getPath() + "\\";
		List<CfgTemplate> templates = ModelPool.getTemplates();
		ScatterplotVisualization vis = new ScatterplotVisualization();
		String dataName = data.getName();
		logger.addHandler(new StreamHandler(new FileOutputStream(path + "log.txt"), new SimpleFormatter()));
		File resultFile = new File(path + "results.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
		bw.write("data\ttemplate\tmean_square_error");
		bw.newLine();
		for (CfgTemplate t : templates) {
			logger.log(Level.INFO, "Learning model " + t + ".");
			ModelSource model = null;
			try {
				model = ModGenTools.learnRegressionModel(t, data);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, ex.getClass().getSimpleName() + ":\"" + ex.getMessage() + "\"");
			}
			vis.setSource(new ScatterplotSourceBase(new DataSource[]{data}, new ModelSource[]{model}));
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
			bw.write(dataName + "\t" + t.toString() + "\t" + meanSquareError(model, data));
			bw.newLine();
			BufferedImage img = vis.getVisualizationAsImage(1024, 768);
			logger.log(Level.INFO, "  (... saving visualization)");
			ImageIO.write(img, "png",new File(path + t.toString() + ".png"));
		}
		bw.close();
		logger.log(Level.INFO, "Terminated.");
	}
	
	// regression version
	static double meanSquareError(ModelSource model, DataSource data) {
		double res = 0.0;
		double[][] predicted = model.getModelResponses(data.getInputDataVectors());
		double[][] labeled = data.getOutputDataVectors();
		for (int i = 0; i < predicted.length; i++) {
			res += (Math.sqrt(Math.abs(Math.pow(predicted[i][0], 2) - Math.pow(labeled[i][0],2))));
		}
		return res / labeled.length;
	}
}
