package test.artificialdata.onedimensional;

import org.fabi.visualizations.scatter.sources.DataSource;

public abstract class DataSource1D implements DataSource {

	double[][] in;
	double[][] out;
	
	public abstract double[][] getInputs();
	public abstract double[][] getOutputs(double[][] inputs);
	
	@Override
	public double[][] getInputDataVectors() {
		if (in == null) {
			in = getInputs();
		}
		return in;
	}

	@Override
	public double[][] getOutputDataVectors() {
		if (out == null) {
			out = getOutputs(getInputDataVectors());
		}
		return out;
	}
	
	@Override
	public int inputsNumber() {
		return 1;
	}

	@Override
	public int outputsNumber() {
		return 1;
	}

}
