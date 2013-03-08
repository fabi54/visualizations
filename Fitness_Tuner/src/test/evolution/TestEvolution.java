package test.evolution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.fabi.visualizations.evolution.scatterplot.VisualizationEvolution;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;
import org.math.plot.render.AbstractDrawer;

import test.artificialdata.Data11D;

import configuration.CfgTemplate;

public class TestEvolution {
	
	protected static String PATH = "D:\\Data\\Dokumenty\\Skola\\FIT-MI\\misc\\fakegame\\Results\\evolution2a\\data1\\";
	
	
	public static void main(String[] args) throws IOException {
		DataSource data = new Data11D();
		CfgTemplate[][] templates = Tester.getTemplates();
		String[] names = Tester.getNames();
		for (int i = 0; i < templates.length; i++) {
			System.out.println("=== " + names[i] + " ===");
			ModelSource[] models = new ModelGroup(templates[i], data).getModels();
			ScatterplotSource source = new ScatterplotSourceBase(new DataSource[]{data}, models);
			org.fabi.visualizations.scatter.ScatterplotVisualization tmp = new org.fabi.visualizations.scatter.ScatterplotVisualization(source);
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.add(tmp.getVisualizationAsComponent(), BorderLayout.CENTER);
			frame.add(tmp.getControls(), BorderLayout.WEST);
			frame.setSize(800, 600);
			frame.setVisible(true);
			ScatterplotVisualization[] vis = new VisualizationEvolution().evolve(data, models);
			File dir = new File(PATH + names[i] + "\\");
			if (!dir.exists()) {
				System.out.println("Creating dir \"" + dir + "\"");
				dir.mkdir();
			}
			for (int j = 0; j < vis.length; j++) {
				vis[j].getVisualizationAsComponent();
				double[] yBounds = getyBounds(vis[j]);
				vis[j].setAdditionalDrawers(new AdditionalDrawer[]{new AreaAdditionalDrawer(
						vis[j].getxAxisRangeLower(),
						vis[j].getxAxisRangeUpper(),
						yBounds[0],
						yBounds[1])});
				double[] xBounds = getxBounds(vis[j]);
				double offset = (xBounds[1] - xBounds[0]) * 0.1;
				vis[j].setxAxisRangeLower(xBounds[0] - offset);
				vis[j].setxAxisRangeUpper(xBounds[1] + offset);
				yBounds = getyBounds(vis[j]);
				offset = (yBounds[1] - yBounds[0]) * 0.1;
				vis[j].setyAxisRangeLower(yBounds[0] - offset);
				vis[j].setyAxisRangeUpper(yBounds[1] + offset);
				vis[j].setGridVisible(false);
				vis[j].setBackground(Color.BLACK);
				vis[j].setDotSizeModel(getDotSizeModel(vis[j]));
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
				BufferedImage img = vis[j].getVisualizationAsImage(800, 600);
				String n = (j < 10) ? ("0" + Integer.toString(j)) : Integer.toString(j);
				ImageIO.write(img, "png",new File(PATH + names[i] + "\\" + n + ".png"));
			}
		}
	}
	
	// regression version
	public static MinkowskiDistanceDotSizeModel getDotSizeModel(ScatterplotVisualization visualization) {
		MinkowskiDistanceDotSizeModel model = new MinkowskiDistanceDotSizeModel();
		model.setP(2);
		model.setMinDist(0);
		int instNr = 0;
		ScatterplotSource source = visualization.getSource();
		double[] inputSetting = visualization.getInputsSetting();
		int xIndex = visualization.getxAxisAttributeIndex();
		double maxDist = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			instNr += inputs.length;
			for (int j = 0; j < inputs.length; j++) {
				double res = 0;
				for (int k = 0; k < inputs[k].length; k++) {
					if (k != xIndex) {
						res += Math.pow(inputs[j][k] - inputSetting[k], 2);
					}
				}
				maxDist = Math.max(maxDist, Math.sqrt(res));
			}
		}
		model.setRangeDist(maxDist);
		model.setRangeSize((int) Math.log10(instNr) );
		return model;
	}
	
	// regression version
	protected static double[] getxBounds(ScatterplotVisualization vis) {
		ScatterplotSource source = vis.getSource();
		int xIndex = vis.getxAxisAttributeIndex();
		double[] bounds = new double[]{vis.getxAxisRangeLower(), vis.getxAxisRangeUpper()};
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			for (int j = 0; j < inputs.length; j++) {
				bounds[0] = Math.min(bounds[0], inputs[j][xIndex]);
				bounds[1] = Math.max(bounds[1], inputs[j][xIndex]);
			}
		}
		return bounds;
	}
	
	// regression version
	protected static double[] getyBounds(ScatterplotVisualization vis) {
		ScatterplotSource source = vis.getSource();
		int xIndex = vis.getxAxisAttributeIndex();
		double xLower = vis.getxAxisRangeLower();
		double xUpper = vis.getxAxisRangeUpper();
		double[] bounds = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			double[][] outputs = source.getDataSource(i).getOutputDataVectors();
			for (int j = 0; j < inputs.length; j++) {
				if (inputs[j][xIndex] > xLower && inputs[j][xIndex] < xUpper) {
					bounds[0] = Math.min(bounds[0], outputs[j][0]);
					bounds[1] = Math.max(bounds[1], outputs[j][0]);
				}
			}
		}
		double[][] inputs = new double[20][source.getInputsNumber()];
		for (int i = 0; i < inputs.length; i++) {
			System.arraycopy(vis.getInputsSetting(), 0, inputs[i], 0, inputs[i].length);
		}
		inputs[0][xIndex] = xLower;
		double step = (xUpper - xLower) / 20;
		for (int i = 1; i < inputs.length; i++) {
			inputs[i][xIndex] = inputs[i - 1][xIndex] + step;
		}
		for (int i = 0 ; i < source.getModelSourceCount(); i++) {
			double[][] responses = source.getModelSource(i).getModelResponses(inputs);
			for (int j = 0; j < responses.length; j++) {
				bounds[0] = Math.min(bounds[0], responses[j][0]);
				bounds[1] = Math.max(bounds[1], responses[j][0]);
			}
		}
		return bounds;
	}
	
	protected static class AreaAdditionalDrawer implements AdditionalDrawer {
		
		protected double xl; protected double xu; protected double yl; protected double yu;
		protected Color clr = Color.WHITE;
		
		public AreaAdditionalDrawer(double xl, double xu, double yl, double yu) {
			this.xl = xl; this.xu = xu; this.yl = yl; this.yu = yu;
		}
		
		public AreaAdditionalDrawer(double xl, double xu, double yl, double yu, Color clr) {
			this.xl = xl; this.xu = xu; this.yl = yl; this.yu = yu;
			setColor(clr);
		}
		
		public void setColor(Color clr) {
			this.clr = clr;
		}
		
		@Override
		public void draw(AbstractDrawer draw) {
			draw.setColor(clr);
			draw.drawPolygon(new double[]{xl, yl}, new double[]{xl, yu}, new double[]{xu, yu}, new double[]{xu, yl});
//			System.out.println("drawing " + xl + "," + yl + "," + xu + "," + yu);
		}
	}
    
}
