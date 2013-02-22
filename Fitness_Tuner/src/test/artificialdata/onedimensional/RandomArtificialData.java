package test.artificialdata.onedimensional;

import org.math.array.StatisticSample;

public class RandomArtificialData extends DataSource1D {

	protected int elementsNr = 30;
	protected int instNr = 1000;
	protected double noise = 0.01;
	
	public RandomArtificialData() { }
	
	public RandomArtificialData(double noise) { this.noise = noise; }
	
	public RandomArtificialData(int instNr, int elementsNr, double noise) {
		this.instNr = instNr;
		this.elementsNr = elementsNr;
		this.noise = noise;
	}
	
	@Override
	public String getName() {
//		return "Random(inst=" + instNr + ",in1=0.0..1.0,out1=fn:" + elementsNr + "x sin-gauss-poly noise " + noise + ")";
		return "GausPeakRnd(inst=" + instNr + ",in1=0.0..1.0,out1=fn:" + elementsNr + "x gauss noise " + noise + ")";
	}

	@Override
	public double[][] getInputs() {
		double[][] res = new double[instNr][1];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = i / (double) res.length;
		}
		return res;
	}

	
	
	@Override
	public double[][] getOutputs(double[][] inputs) {
		double[][] res = new double[instNr][1];
		double[] noise = StatisticSample.randomNormal(instNr, 0.0, this.noise);
		Function[] elems = new Function[elementsNr];
//		double rnd;
//		double onethird = 1.0 / 3.0;
//		double twothirds = 2.0 / 3.0;
		for (int i = 0; i < elems.length; i++) {
//			rnd = Math.random();
//			if (rnd < onethird) {
//				elems[i] = new Sine();
//			} else if (rnd < twothirds) {
				elems[i] = new Gaussian();
//			} else {
//				elems[i] = new Polynomial();
//			}
		}
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < elems.length; j++) {
				res[i][0] += elems[j].getOutput(inputs[i][0]);
			}
			res[i][0] /= elems.length;
			res[i][0] += noise[i];
		}
		return res;
	}
	
	protected static interface Function {
		public double getOutput(double input);
	}
	
	protected static class Sine implements Function {
		protected double xShift = Math.random();
		protected double yShift = Math.random();
		protected double amplitude = Math.random() * 2 - 0.5;
		
		public double getOutput(double input) { return amplitude * Math.sin(input + xShift) + yShift; }
	}
	
	protected static class Gaussian implements Function {
		protected double a = Math.random();
		protected double b = Math.random();
		protected double c = Math.random();

		public double getOutput(double input) { return a * Math.exp(- Math.pow((input - b), 2) / (2 * c * c)); }
	}
	
	protected static class Polynomial implements Function {
		protected double[] coefs;
		
		public Polynomial() {
			coefs = new double[(int) (Math.random() * 10)];
			for (int i = 0 ; i < coefs.length; i++) {
				coefs[i] = Math.random() * 2 - 0.5;
			}
		}
		
		public double getOutput(double input) {
			double res = 0.0;
			for (int i = 0; i < coefs.length; i++) {
				res += coefs[coefs.length - 1 - i] * Math.pow(input, i);
			}
			return res;
		}
	}

}
