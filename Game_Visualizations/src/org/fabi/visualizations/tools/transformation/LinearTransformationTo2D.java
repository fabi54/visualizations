package org.fabi.visualizations.tools.transformation;

public class LinearTransformationTo2D extends LinearTransformation {

	public LinearTransformationTo2D(double[][] matrix) {
		super();
		if (matrix.length != 2 || matrix[0].length != matrix[1].length) {
			throw new IllegalArgumentException("Invalid matrix size.");
		}
		int[] skippedAttributes = {-1, -1}; // if one of the two vector contains only one non-zero value,
											// this value must be skipped by the generated vectors
		for (int i = 0; i < matrix.length; i++) {
			int lowest = matrix[i].length, highest = -1;
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != 0) {
					highest = j;
					if (lowest == matrix[i].length) {
						lowest = j;
					}
				}
			}
			if (lowest == highest) {
				skippedAttributes[i] = lowest;
			}
		}
		double[][] result = new double[matrix[0].length][];
		int i = 0;
		int attribute = 0;
		for (; i < 2; i++) {
			result[i] = matrix[i];
		}
		for (; i < result.length; i++) {
			result[i] = new double[result.length];
			while (attribute == skippedAttributes[0] || attribute == skippedAttributes[1]) {
				attribute++; // skip index
			}
			result[i][attribute] = 1.0;
			attribute++;
		}
		init(result);
	}
}
