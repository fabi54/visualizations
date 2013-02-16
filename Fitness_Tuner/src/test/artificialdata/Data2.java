package test.artificialdata;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.math.array.StatisticSample;

public class Data2 implements DataSource {

	double[][] inputs;
	double[][] outputs;
	
	@Override
	public double[][] getInputDataVectors() {
		if (inputs == null) {
			double[] attr1 = StatisticSample.randomUniform(20000, 2.0, 5.0);
			double[] attr2 = StatisticSample.randomUniform(20000, 5.0, 15.0);
			inputs = new double[20000][2];
			for (int i = 0; i < 20000; i++) {
				inputs[i][0] = attr1[i];
				inputs[i][1] = attr2[i];
			}
		}
		return inputs;
	}

	@Override
	public double[][] getOutputDataVectors() {
		if (outputs == null) {
			double[] noise = StatisticSample.randomNormal(20000, 0.0, 10.0);
			getInputDataVectors();
			outputs = new double[20000][1];
			for (int i = 0; i < 20000; i++) {
				outputs[i][0] = 5 * inputs[i][0] + noise[i] * Math.pow(inputs[i][1] - 10.0, 2.0);
			}
		}
		return outputs;
	}

	@Override
	public int inputsNumber() {
		return 2;
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
