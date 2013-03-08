package test.fitness;

public class Test {

	public static double evaluateInterestingness(double[][][] responses) {
		double interestingness = 0.0;
		double acc = 0;
		double prevAvg = Double.NaN;
		int cntr = 0;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			double actAvg = 0.0, actDiff = 0.0;
			for (int j = 1; j < responses.length - 1; j++) {
//			for (int j = 0; j < responses.length; j++) {
				if (!Double.isNaN(values[j])) {
					actAvg += values[j];
				}
			}
			actAvg /= responses.length;
			if (!Double.isNaN(prevAvg)) {
				actDiff = prevAvg - actAvg; 
				if (acc == 0 || Math.signum(acc) == Math.signum(actDiff)) {
					acc += actDiff;
					cntr++;
					System.out.println(i + ": incr cntr .. " + cntr);
				} else {
					interestingness += cntr * Math.abs(acc);
					acc = actDiff;
					cntr = 1;
					System.out.println(i + ": empty acc ");
				}
			} else {
				System.out.println(i + ": ignoring");
			}
			prevAvg = actAvg;
			System.out.println(i + ": " + interestingness);
		}
		if (cntr > 0) {
			interestingness += cntr * Math.abs(acc);
		}
		System.out.println("end: " + interestingness);
		return interestingness / (responses[0].length);
	} 
	
	public static double evaluateInterestingnessWoAcc(double[][][] responses) {
		double interestingness = 0.0;
		double prevAvg = Double.NaN;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			double actAvg = 0.0;
			for (int j = 1; j < responses.length - 1; j++) {
//			for (int j = 0; j < responses.length; j++) {
				if (!Double.isNaN(values[j])) {
					actAvg += values[j];
				}
			}
			actAvg /= responses.length;
			if (!Double.isNaN(prevAvg)) {
				interestingness += Math.abs(prevAvg - actAvg);
			}
			System.out.println(i + ": " + interestingness);
			prevAvg = actAvg;
		}
		return interestingness / (responses[0].length);
	} 
	
	public static void main(String[] args) {
		double[][][] responses = new double[][][]{
				{
					{3.0},
					{6.0},
					{6.0},
					{9.0},
					{12.0},
					{9.0}
				},
				{
					{3.0},
					{6.0},
					{6.0},
					{9.0},
					{12.0},
					{9.0}
				},
				{
					{3.0},
					{6.0},
					{6.0},
					{9.0},
					{12.0},
					{9.0}
				}
		};
		System.out.println("ACC");
		evaluateInterestingness(responses);
		System.out.println("WO");
		evaluateInterestingnessWoAcc(responses);
	}
}
