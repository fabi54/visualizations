package test.artificialdata;

import org.fabi.visualizations.scatter.sources.DataSource;
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

}
