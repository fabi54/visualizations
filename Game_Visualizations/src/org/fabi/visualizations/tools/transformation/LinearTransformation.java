package org.fabi.visualizations.tools.transformation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;

import Jama.Matrix;

public class LinearTransformation extends ReversibleTransformationBase {

	Matrix M;
	protected Matrix M_inverse;
	
	protected LinearTransformation() {
		M = null;
		M_inverse = null;
	}
	
	public LinearTransformation(double[][] matrix) {
		init(matrix);
	}
	
	protected void init(double[][] matrix) {
		this.M = new Matrix(matrix).transpose();
		int size = M.getRowDimension();
		if (size != M.getColumnDimension()) {
			throw new IllegalArgumentException("Matrix is not square matrix.");
		}
		Matrix B = Matrix.identity(M.getRowDimension(), M.getColumnDimension());
		try {
			M_inverse = M.lu().solve(B);
		} catch (RuntimeException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
	
	@Override
	public double[][] transformForwards(double[][] originalMatrix) {
		Matrix result = new Matrix(originalMatrix).times(M);
		return result.getArray();
	}

	@Override
	public double[][] transformBackwards(double[][] transformedMatrix) {
		Matrix result = new Matrix(transformedMatrix).times(M_inverse);
		return result.getArray();
	}

	@Override
	public List<AttributeInfo> getAttributeMetadata(
			List<AttributeInfo> originalMetadata) {
		int size = M.getRowDimension();
		if (originalMetadata.size() != size) {
			throw new IllegalArgumentException("Illegal metadata.");
		}
		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
		for (int i = 0; i < size; i++) {
			StringBuilder name = new StringBuilder();
			for (int j = 0; j < size; j++) {
				double coef = M.get(j, i);
				if (coef != 0) {
					if (name.length() > 0) {
						name.append("+");
					}
					DecimalFormat format = new DecimalFormat();
					format.setMaximumFractionDigits(1);
					name.append(format.format(coef)).append(".").append(originalMetadata.get(j).getName());
				}
			}
			attributes.add(new AttributeInfoBase(name.toString(), AttributeInfo.AttributeRole.STANDARD_INPUT));
		}
		return attributes;
	}

	@Override
	public String getName() {
		return TransformationProvider.LINEAR_TRANSFORMATION;
	}

}
