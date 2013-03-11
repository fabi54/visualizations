package org.fabi.visualizations.evolution.observers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

public class BestFitnessVisualizationObserver extends BestFitnessObserver {

	protected File fileToSave;
	protected ScatterplotVisualization visualization;
	protected boolean overwrite;
	
	public BestFitnessVisualizationObserver() {
		fileToSave = null;
	}
	
	public BestFitnessVisualizationObserver(String path) {
		this(path, false);
	}
	
	public BestFitnessVisualizationObserver(String path, boolean overwrite) {
		fileToSave = new File(path);
		this.overwrite = overwrite;
	}
	
	@Override
	public void finalise() {
		visualization = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{new FitnessDataSource(fitnessValues)}));
		ConstantColorModel cm = new ConstantColorModel();
		cm.setColor(Color.RED);
		visualization.setColorModel(cm);
		if (fileToSave != null) {
			File f = fileToSave;
			BufferedImage img = visualization.getVisualizationAsImage(800, 600);
			try {
				if (f.exists()) {
					int i = 1;
					String orig = f.getAbsolutePath();
					orig = orig.substring(0, orig.length() - 4);
					while (f.exists()) {
						f = new File(orig + "_" + i++ + ".png");
					}
				}
				ImageIO.write(img, "png", f);
			} catch (IOException e) {
				throw new RuntimeException("IOException: " + e.getMessage());
			}
		}
	}
	
	protected static class FitnessDataSource implements DataSource {

		public FitnessDataSource(List<Double> values) {
			int size = values.size();
			inputs = new double[size][1];
			outputs = new double[size][1];
			Iterator<Double> iterator = values.iterator();
			for (int i = 0; i < size; i++) {
				inputs[i][0] = i;
				outputs[i][0] = iterator.next();
			}
		}
		
		double[][] inputs = null;
		double[][] outputs = null;
		
		@Override
		public double[][] getInputDataVectors() {
			return inputs;
		}

		@Override
		public double[][] getOutputDataVectors() {
			return outputs;
		}

		@Override
		public int inputsNumber() {
			return 1;
		}

		@Override
		public int outputsNumber() {
			return 1;
		}

		@Override
		public String getName() {
			return "Fitness";
		}
		
	}
}
