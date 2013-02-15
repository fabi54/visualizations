package org.fabi.visualizations.tools.transformation;

import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.fabi.visualizations.tools.transformation.PCATransformation;

public class TransformationProvider {
	public static final String NO_TRANSFORMATION = "<none>";
	public static final String PCA_TRANSFORMATION = "PCA";
	public static final String LINEAR_TRANSFORMATION = "Linear transformation";
	
	protected static TransformationProvider instance = null;
	
	public static TransformationProvider getInstance() {
		if (instance == null) {
			instance = new TransformationProvider();
		}
		return instance;
	}
	
	protected TransformationProvider() { }
	
	public ReversibleTransformation getTransformation(String name, double[][] data) {
		if (name.equals(PCA_TRANSFORMATION)) {
			return new PCATransformation(data);
		} else if (name.startsWith(LINEAR_TRANSFORMATION)) {
			double[][] matrix = Arrays.parseMatrix(name.substring(LINEAR_TRANSFORMATION.length()));
			if (matrix.length == 2) {
				return new LinearTransformationTo2D(matrix);
			} else {
				return new LinearTransformation(matrix);
			}
		} else {
			return null;
		}
	}
	
	public String getString(ReversibleTransformation transformation) {
		if (transformation == null) {
			return NO_TRANSFORMATION;
		} else if (transformation instanceof PCATransformation) {
			return PCA_TRANSFORMATION;
		} else if (transformation instanceof LinearTransformation) {
			return LINEAR_TRANSFORMATION + Arrays.matrixToCode(((LinearTransformation) transformation).M.transpose().getArray());
		} else {
			return "";
		}
	}
}
