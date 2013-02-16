package test.artificialdata;

import org.fabi.visualizations.scatter.sources.DataSource;

public class Data11D implements DataSource {

	double[][] inputs;
	double[][] outputs;
	
	@Override
	public double[][] getInputDataVectors() {
		if (inputs == null) {
			inputs = new double[10000][1];
			double d = 0.0;
			for (int i = 0; i < 10000; i++) {
				inputs[i][0] = d++ / 1000.0;
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
		return 1;
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
