package test.artificialdata.onedimensional;

public class LongConstantLinearConstantData extends DataSource1D {

	@Override
	public double[][] getInputs() {
		double[][] res = new double[700][1];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = (i / 100.0) - 3.0;
		}
		return res;
	}

	@Override
	public double[][] getOutputs(double[][] inputs) {
		double[][] res = new double[700][1];
		int i = 0;
		for (; i < 300; i++) {
			res[i][0] = 0.0;
		}
		for (; i < 400; i++) {
			res[i][0] = inputs[i][0];
		}
		for (; i < 700; i++) {
			res[i][0] = 1.0;
		}
		return res;
	}

	@Override
	public String getName() {
		return "ConstLinConst(inst=700,in1=-1.0..2.0,out1=in1<0.0:0.0,in1>1.0:1.0,in1)";
	}

}
