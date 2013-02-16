package test.artificialdata;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;
import org.math.array.StatisticSample;

public class Data3 implements DataSource {

	double[][] inputs;
	double[][] outputs;
	
	@Override
	public double[][] getInputDataVectors() {
		if (inputs == null) {
			double[] attr1 = StatisticSample.randomUniform(10000, 2.0, 5.0);
			double[] attr2 = StatisticSample.randomUniform(10000, 2.0, 5.0);
			double[] attr3 = StatisticSample.randomUniform(10000, 10.0, 20.0);
			double[] attr4 = StatisticSample.randomUniform(10000, -60.0, 0.0);
			double[] attr5 = StatisticSample.randomUniform(10000, 50.0, 85.0);
			inputs = new double[20000][4];
			for (int i = 0; i < 10000; i++) {
				inputs[i][0] = attr1[i];
				inputs[i][1] = 5.0;
				inputs[i][2] = 8.0;
				inputs[i][3] = -6.0;
			}
			for (int i = 10000; i < 20000; i++) {
				inputs[i][0] = attr2[i - 10000];
				inputs[i][1] = attr3[i - 10000];
				inputs[i][2] = attr4[i - 10000];
				inputs[i][3] = attr5[i - 10000];
			}
		}
		return inputs;
	}

	@Override
	public double[][] getOutputDataVectors() {
		if (outputs == null) {
			getInputDataVectors();
			outputs = new double[20000][1];
			for (int i = 0; i < 10000; i++) {
				outputs[i][0] = inputs[i][0] * inputs[i][0];
			}
			for (int i = 1; i < 20000; i++) {
				outputs[i][0] = inputs[i][0] + inputs[i][1] - inputs[i][2] - inputs[i][3];
			}
		}
		return outputs;
	}

	@Override
	public int inputsNumber() {
		return 4;
	}

	@Override
	public int outputsNumber() {
		return 1;
	}

	@Override
	public String getName() {
		return "Simple artificial data";
	}

	public static void main(String[] args) {
		ScatterplotVisualization visualization = new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{new Data3()}));
		visualization.setColorModel(new RegressionRainbowColorModel(0.0, 1.0));
		visualization.setDotSizeModel(new MinkowskiDistanceDotSizeModel());
		visualization.setBackground(Color.BLACK);
		visualization.setDotSizeModel(TestEvolution.getDotSizeModel(visualization));
		ConstantColorModel model = new ConstantColorModel();
		model.setColor(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		visualization.setColorModel(model);
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(visualization.getVisualizationAsComponent(), BorderLayout.CENTER);
		f.add(visualization.getControls(), BorderLayout.WEST);
		f.setSize(1024, 768);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
