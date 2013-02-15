package org.fabi.visualizations.tools.math;

/**
 * Provides static methods for computing some statistical data required by the visualizations.
 * 
 * @author Jan Fabian
 *
 */
public abstract class Statistics {
	
	private Statistics() {}
	
	/**
	 * Computes the correlation between two arrays with at least two elements.
	 * 
	 * @param x first variable values
	 * @param y second variable values (the array length has to be same as <code>x.length</code>)
	 * @return correlation coefficient for <code>x</code> and <code>y</code>
	 * @throws IllegalArgumentException if <code>x.length != y.length</code> or <code>x.length < 2</code>
	 */
	public static double corr(double[] x, double[] y) {		
		if (x.length != y.length) throw new IllegalArgumentException("Array length must be same.");
		int n = x.length;
		if (n < 2) throw new IllegalArgumentException("At least 2 elements required in each array.");
		double xa = 0, xb = 0, ya = 0, yb = 0;
		for (int i = 0; i < n; i++) {
			xa += x[i] * x[i];
			xb += x[i];
			ya += y[i] * y[i];
			yb += y[i];
		}
		double xMean = xb / n, yMean = yb / n;
		// uses simplified standard deviation equation (approximation)
		double xStdev = Math.sqrt((xa - n * Math.pow(xb / n, 2.0)) / (n - 1));
		double yStdev = Math.sqrt((ya - n * Math.pow(yb / n, 2.0)) / (n - 1));
		/* alternative standard deviation
		double xStdev2 = 0;
		double yStdev2 = 0;
		for (int i = 0; i < n; i++) {
			xStdev2 += Math.pow(x[i] - xMean, 2.0);
			yStdev2 += Math.pow(y[i] - yMean, 2.0);
		}
		xStdev2 /= n;
		yStdev2 /= n;
		xStdev2 = Math.sqrt(xStdev2);
		yStdev2 = Math.sqrt(yStdev2);
		*/
		double cov = 0;
		for (int i = 0; i < n; i++) {
			cov += (x[i] - xMean) * (y[i] - yMean);
		}
		cov /= (n - 1);
		return cov / (xStdev * yStdev);
	}
	
	public static double corr(double[][] data, int xIndex, int yIndex) {
		return corr(data, xIndex, data, yIndex);
	}
	
	public static double corr(double[][] xData, int xIndex, double[][] yData, int yIndex) {
		double[] x = Arrays.getColumn(xData, xIndex);
		double[] y = Arrays.getColumn(yData, yIndex);
		return corr(x, y);
	}
}
