package test.fitness;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.additional.HistogramAdditionalDrawer;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import test.artificialdata.onedimensional.*;

import configuration.CfgTemplate;
import configuration.models.single.PolynomialModelConfig;

public class LocalFitnessTest {

	protected static DataSource getData() {
		return new ConstantLinearConstantData();
	}
	
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
	
	protected static ModelSource[] getModels(DataSource data) {
		CfgTemplate[] templates = new CfgTemplate[10];
		for (int i = 0; i < templates.length; i++) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i + 3);
			templates[i] = cfg;
		}
		ModelSource[] ms = new ModelSource[10];
		for (int i = 0; i < ms.length; i++) {
			ms[i] = ModGenTools.learnRegressionModel(templates[i], new BootstrappedDataSource(data));
		}
		return ms;
	}
	
	public static void main(String[] args) {
		DataSource ds = getData();
		final ModelSource[] models = getModels(ds);
		final ScatterplotVisualization vis = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{ds}, models));
		vis.setColorModel(new ColorModel() {
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
		vis.setxAxisRangeLower(-1.5);
		vis.setxAxisRangeUpper(2.5);
		
		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.adjustFitness(ds, models);
		
		LocalFitness.MODEL_OUTPUT_PRECISION = 50;

		double[][][] responses = LocalFitness.getResponses(vis, models);
		double[][] inputs = LocalFitness.getModelInputs(vis);
		double[][] histogram = new double[inputs.length - 1][2];
		for (int z = 0; z < histogram.length; z++) {
			histogram[z][0] = inputs[z + 1][vis.getxAxisAttributeIndex()];
			histogram[z][1] = org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.evaluateLocalAt(responses, z + 1, vis);
		}
		double w = inputs[1][vis.getxAxisAttributeIndex()] - inputs[0][vis.getxAxisAttributeIndex()];
		vis.setAdditionalDrawers(new AdditionalDrawer[]{
				new HistogramAdditionalDrawer(vis, "Fitness", new Color(1.0f, 0.0f, 0.0f, 1.0f), histogram, w, 0.1)});
		
		
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(vis.getVisualizationAsComponent(), BorderLayout.CENTER);
//		final JSpinner spinner = new JSpinner();
//		spinner.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent arg0) {
//				CONST = (Double) spinner.getValue();
//				update(vis, models);
//			}
//		});
//		spinner.setModel(new SpinnerNumberModel(CONST, 0.0, 10000.0, 0.0001));
//		f.add(spinner, BorderLayout.NORTH);
//		final JSpinner spinner2 = new JSpinner();
//		spinner2.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent arg0) {
//				CONST2 = (Double) spinner.getValue();
//				update(vis, models);
//			}
//		});
//		spinner2.setModel(new SpinnerNumberModel(CONST2, 0.0, 10000.0, 0.0001));
//		f.add(spinner2, BorderLayout.EAST);
//		final JSpinner spinner3 = new JSpinner();
//		spinner3.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent arg0) {
//				CONST3 = (Double) spinner.getValue();
//				update(vis, models);
//			}
//		});
//		spinner3.setModel(new SpinnerNumberModel(CONST3, 0.0, 10000.0, 0.0001));
//		f.add(spinner3, BorderLayout.SOUTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(new Dimension(1024, 768));
		f.setVisible(true);
	}
	
	protected static void update(ScatterplotVisualization vis, ModelSource[] models) {
		double[][][] responses = LocalFitness.getResponses(vis, models);
		double[][] inputs = LocalFitness.getModelInputs(vis);
		double[][] histogram = new double[inputs.length - 1][2];
		for (int z = 0; z < histogram.length; z++) {
			histogram[z][0] = inputs[z + 1][vis.getxAxisAttributeIndex()];
			histogram[z][1] = evaluateLocalAt(responses, z + 1, vis);
		}
		double w = inputs[1][vis.getxAxisAttributeIndex()] - inputs[0][vis.getxAxisAttributeIndex()];
		vis.setAdditionalDrawers(new AdditionalDrawer[]{
				new HistogramAdditionalDrawer(vis, "Fitness", new Color(1.0f, 0.0f, 0.0f, 1.0f), histogram, w, 0.1)});
	}
	
    public static double evaluateLocalAt(double[][][] responses, int i, ScatterplotVisualization vis) {
		if (i == 0) { // TODO
			return 0.0;
		}
    	
    	double res = 0.0;
		double[] values = new double[responses.length];
		double[] prevValues = new double[responses.length];
		for (int j = 0; j < responses.length; j++) {
			prevValues[j] = responses[j][i - 1][0];
			values[j] = responses[j][i][0];
		}
		java.util.Arrays.sort(values);
		java.util.Arrays.sort(prevValues);
		
		// interestingness
		double prevAvg = 0.0;
		for (int j = 1; j < responses.length - 1; j++) {
			if (!Double.isNaN(values[j])) {
				res += values[j];
				prevAvg += prevValues[j];
			}
		}
		res /= responses.length;
		prevAvg /= responses.length;

		res -= prevAvg;
		res = Math.abs(res);
		
		// similarity
		double similarity = 0.0;
    	for (int j = 0; j < responses.length; j++) {
			values[j] = responses[j][i][0];
		}
		java.util.Arrays.sort(values);
		int begin = 1;
		int end = values.length - 2;
		double div = 1;
		while (begin != end) {
			similarity += Math.abs(values[end] - values[begin]);
			similarity *= 2;
			if (Math.abs(values[begin + 1] - values[begin])
					> Math.abs(values[end] - values[end - 1])) {
				begin++;
			} else {
				end--;
			}
			div *= 2;
		}
		similarity /= div;
		similarity = 0.25 - similarity;
//		similarity = Math.pow(CONST2 / (CONST + similarity) + CONST3, CONST4);
//		if (similarity < 0) {
//			similarity = Math.pow(similarity, CONST5);
//		}
		res *= similarity;
		return res;
    }
    
    protected static double CONST = -1;
    protected static double CONST2 = 1;
    protected static double CONST3 = 1;
    protected static double CONST4 = 1;
    protected static double CONST5 = 1;
}
