package test.artificialdata.onedimensional;

import org.math.array.StatisticSample;

public class LongHeavisideData extends DataSource1D {

	@Override
	public String getName() {
		return "Heaviside(inst=2000,in1=-100.0..100.0,out1=heaviside)";
	}

	@Override
	public double[][] getInputs() {
		double[] data = StatisticSample.randomUniform(2000, -100.0, 100.0);
		double[][] res = new double[2000][1];
		for (int i = 0; i < 2000; i++) {
			res[i][0] = data[i];
		}
		return res;
	}

	@Override
	public double[][] getOutputs(double[][] inputs) {
		double[][] res = new double[2000][1];
		for (int i = 0; i < 2000; i++) {
			res[i][0] = (inputs[i][0] < 0.0) ? 0.0 : 1.0;
		}
		return res;
	}

}
