package test.artificialdata.onedimensional;

import org.math.array.StatisticSample;

public class HeavisideData extends DataSource1D {

	@Override
	public String getName() {
		return "Heaviside(inst=500,in1=-5.0..5.0,out1=heaviside)";
	}

	@Override
	public double[][] getInputs() {
		double[] data = StatisticSample.randomUniform(500, -5.0, 5.0);
		double[][] res = new double[500][1];
		for (int i = 0; i < 500; i++) {
			res[i][0] = data[i];
		}
		return res;
	}

	@Override
	public double[][] getOutputs(double[][] inputs) {
		double[][] res = new double[500][1];
		for (int i = 0; i < 500; i++) {
			res[i][0] = (inputs[i][0] < 0.0) ? 0.0 : 1.0;
		}
		return res;
	}

}
