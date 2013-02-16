package test.artificialdata.onedimensional;

public class SimpleLinearData extends DataSource1D {

	@Override
	public double[][] getInputs() {
		double[][] res = new double[100][1];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = i / 100.0;
		}
		return res;
	}

	@Override
	public double[][] getOutputs(double[][] inputs) {
		double[][] res = new double[100][1];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = inputs[i][0];
		}
		return res;
	}

	@Override
	public String getName() {
		return "Linear(inst=100,in1=0.0..1.0,out1=in1)";
	}
	
}
