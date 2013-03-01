package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeBoundsHolder;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.EvolutionModeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;
import org.math.plot.render.AbstractDrawer;

import configuration.models.single.PolynomialModelConfig;

import test.artificialdata.onedimensional.ConstantLinearConstantData;
import test.evolution.TestEvolution;

public class VariousTests {

	public static final DataSource data = new ConstantLinearConstantData();
	
	public static void main(String[] args) {
		one();
		two();
	}
	
	public static void two() {
		EvolutionModeller modeller = new EvolutionModeller();
		ModelSource[] models = modeller.getModels(data);	
//		int[] indices = new int[]{14, 3, 1, 4, 2, 13, 13, 14, 7, 14}; // sample best
//		CfgTemplate[] templates = new CfgTemplate[10];
//		templates = ModelPool.getTemplates().toArray(templates);
//		ModelSource[] models = new ModelSource[indices.length];
//		for (int i = 0; i < models.length; i++) {
//			models[i] = ModGenTools.learnRegressionModel(templates[indices[i]], data);
//		}
		ScatterplotChromosomeFitnessFunction f = new ScatterplotChromosomeFitnessFunction(models, data);
		ScatterplotChromosomeBoundsHolder bh = new ScatterplotChromosomeBoundsHolder(data.getInputDataVectors());
		ScatterplotChromosome c1 = new ScatterplotChromosome(f, new int[]{0}, new double[]{0.0}, new double[]{1.0}, new double[]{0.0}, false, bh);
		ScatterplotChromosome c2 = new ScatterplotChromosome(f, new int[]{0}, new double[]{1.0/3.0}, new double[]{1.0/2.0}, new double[]{0.0}, false, bh);
		
		ScatterplotVisualization v1 = (ScatterplotVisualization) c1.getPhenotype();
		ScatterplotVisualization v2 = (ScatterplotVisualization) c2.getPhenotype();
		v1.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		v2.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		v1 = prepareVisualization(v1);
		v2 = prepareVisualization(v2);
		JFrame frame = new JFrame();
		frame.setLayout(new GridBagLayout());
        frame.setSize(1024, 768);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		JComponent jc1 = v1.getVisualizationAsComponent();
		jc1.setMinimumSize(new Dimension(400, 400));
		jc1.setPreferredSize(new Dimension(600, 600));
		frame.add(jc1, c);
		c.gridx = 1;
		JComponent jc2 = v2.getVisualizationAsComponent();
		jc2.setMinimumSize(new Dimension(400, 400));
		jc2.setPreferredSize(new Dimension(600, 600));
		frame.add(jc2, c);
		c.gridy = 1;
		c.gridx = 0;
		frame.add(new JLabel(Double.toString(c1.getFitness())), c);
		c.gridx = 1;
		frame.add(new JLabel(Double.toString(c2.getFitness())), c);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);	
	}
	public static void one() {
		PolynomialModelConfig cfg = new PolynomialModelConfig();
		cfg.setMaxDegree(10);
		ModelSource[] models = new ModelSource[10];
		for (int i = 0; i < models.length; i++) {
			models[i] = ModGenTools.learnRegressionModel(cfg, data);
		}
		ScatterplotChromosomeFitnessFunction f = new ScatterplotChromosomeFitnessFunction(models, data);
		ScatterplotChromosomeBoundsHolder bh = new ScatterplotChromosomeBoundsHolder(data.getInputDataVectors());
		ScatterplotChromosome c1 = new ScatterplotChromosome(f, new int[]{0}, new double[]{0.0}, new double[]{1.0}, new double[]{0.0}, false, bh);
		ScatterplotChromosome c2 = new ScatterplotChromosome(f, new int[]{0}, new double[]{1.0/3.0}, new double[]{1.0/2.0}, new double[]{0.0}, false, bh);
		
		ScatterplotVisualization v1 = (ScatterplotVisualization) c1.getPhenotype();
		ScatterplotVisualization v2 = (ScatterplotVisualization) c2.getPhenotype();
		v1.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		v2.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		v1 = prepareVisualization(v1);
		v2 = prepareVisualization(v2);
		JFrame frame = new JFrame();
		frame.setLayout(new GridBagLayout());
        frame.setSize(1024, 768);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		JComponent jc1 = v1.getVisualizationAsComponent();
		jc1.setMinimumSize(new Dimension(400, 400));
		jc1.setPreferredSize(new Dimension(600, 600));
		frame.add(jc1, c);
		c.gridx = 1;
		JComponent jc2 = v2.getVisualizationAsComponent();
		jc2.setMinimumSize(new Dimension(400, 400));
		jc2.setPreferredSize(new Dimension(600, 600));
		frame.add(jc2, c);
		c.gridy = 1;
		c.gridx = 0;
		frame.add(new JLabel(Double.toString(c1.getFitness())), c);
		c.gridx = 1;
		frame.add(new JLabel(Double.toString(c2.getFitness())), c);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
	protected static ScatterplotVisualization prepareVisualization(ScatterplotVisualization v1) {
		v1.getVisualizationAsComponent();
		double[] yBounds = getyBounds(v1);
		v1.setAdditionalDrawers(new AdditionalDrawer[]{new AreaAdditionalDrawer(
				v1.getxAxisRangeLower(),
				v1.getxAxisRangeUpper(),
				yBounds[0],
				yBounds[1])});
		double[] xBounds = getxBounds(v1);
		double offset = (xBounds[1] - xBounds[0]) * 0.1;
		v1.setxAxisRangeLower(xBounds[0] - offset);
		v1.setxAxisRangeUpper(xBounds[1] + offset);
		yBounds = getyBounds(v1);
		offset = (yBounds[1] - yBounds[0]) * 0.1;
		v1.setyAxisRangeLower(yBounds[0] - offset);
		v1.setyAxisRangeUpper(yBounds[1] + offset);
		v1.setGridVisible(false);
		v1.setBackground(Color.BLACK);
		v1.setDotSizeModel(TestEvolution.getDotSizeModel(v1));
		v1.setColorModel(new ColorModel() {
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
		return v1;
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
