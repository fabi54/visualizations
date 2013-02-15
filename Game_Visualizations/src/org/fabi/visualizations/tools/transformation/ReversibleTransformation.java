package org.fabi.visualizations.tools.transformation;

import java.util.List;

import org.fabi.visualizations.scatter.sources.AttributeInfo;

public interface ReversibleTransformation {
	public double[] transformForwards(double[] originalVector);
	public double[] transformBackwards(double[] transformedVector);
	public double[][] transformForwards(double[][] originalMatrix);
	public double[][] transformBackwards(double[][] transformedMatrix);
	
	List<AttributeInfo> getAttributeMetadata(List<AttributeInfo> originalMetadata);
	public String getName();
}
