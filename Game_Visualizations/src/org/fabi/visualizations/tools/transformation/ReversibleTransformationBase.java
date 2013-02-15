package org.fabi.visualizations.tools.transformation;

import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

public abstract class ReversibleTransformationBase implements ReversibleTransformation {

	@Override
	public double[] transformForwards(double[] originalVector) {
		return transformForwards(new double[][] {originalVector})[0];
	}
	
	@Override
	public double[] transformBackwards(double[] transformedVector) {
		return transformBackwards(new double[][] {transformedVector})[0];
	}
}
