package test.artificialdata;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.math.array.StatisticSample;


public class Data1 implements DataSource {

	double[][] inputs;
	double[][] outputs;
	
	@Override
	public double[][] getInputDataVectors() {
		if (inputs == null) {
			double[] attr1 = StatisticSample.randomNormal(10000, 0.0, 0.1);
			double[] attr3 = StatisticSample.randomUniform(10000, 0.0, 1.0);
			inputs = new double[10000][4];
			double d = 0.0;
			for (int i = 0; i < 10000; i++) {
				inputs[i][0] = d++ / 1000.0;
				inputs[i][1] = (inputs[i][0] * 2) + attr1[i];
				inputs[i][2] = attr3[i];
				inputs[i][3] = 0.5;
			}
		}
		return inputs;
	}

	@Override
	public double[][] getOutputDataVectors() {
		if (outputs == null) {
			getInputDataVectors();
			outputs = new double[10000][1];
			for (int i = 0; i < 10000; i++) {
				outputs[i][0] = inputs[i][0] /* * inputs[i][2] * inputs[i][2]*/;
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
