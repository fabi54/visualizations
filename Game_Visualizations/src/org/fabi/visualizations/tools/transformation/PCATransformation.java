package org.fabi.visualizations.tools.transformation;

import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.tools.transformation.ReversibleTransformationBase;

import org.math.array.*;

import Jama.EigenvalueDecomposition;

public class PCATransformation extends ReversibleTransformationBase {

	protected double[][] X; // original data
	protected double[] meanX, stdevX;
	protected double[][] Z; // centered reduced X
	protected double[][] cov; // Z covariance matrix
	protected double[][] U; // projection matrix
	//protected double[] info; // information matrix
	
	/**
	 * 
	 * @param data first index instance, second index attribute
	 */
	public PCATransformation(double[][] data) {
		X = data;
		stdevX = StatisticSample.stddeviation(X);
		meanX = StatisticSample.mean(X);
		Z = centerReduce(X);
		cov = StatisticSample.covariance(Z);
		EigenvalueDecomposition e = LinearAlgebra.eigen(cov);
		U = LinearAlgebra.transpose(e.getV().getArray());
		//info = e.get1DRealD();
	}
	
	public double[][] centerReduce(double[][] x) {
		double[][] y = new double[x.length][x[0].length];
		for (int i = 0; i < y.length; i++)
			for (int j = 0; j < y[i].length; j++)
				y[i][j] = (x[i][j] - meanX[j]) / stdevX[j];
		return y;
	}
	
	// de-normalization of y relatively to X mean and standard deviation
	public double[] inv_center_reduce(double[] y) {
		return inv_center_reduce(new double[][] { y })[0];
	}
 
	// de-normalization of y relatively to X mean and standard deviation
	public double[][] inv_center_reduce(double[][] y) {
		double[][] x = new double[y.length][y[0].length];
		for (int i = 0; i < x.length; i++)
			for (int j = 0; j < x[i].length; j++)
				x[i][j] = (y[i][j] * stdevX[j]) + meanX[j];
		return x;
	}
	
	@Override
	public double[][] transformForwards(double[][] originalMatrix) {
		if (originalMatrix.length == 0) {
			return new double[0][0];
		}
		double[][] transformedMatrix = new double[originalMatrix.length][originalMatrix[0].length];
		for (int i = 0; i < originalMatrix.length; i++) {
			for (int j = 0; j < transformedMatrix[i].length; j++) {
				for (int k = 0; k < U[j].length; k++) {
					transformedMatrix[i][j] += (originalMatrix[i][k] - meanX[k]) * U[j][k];
				}
			}
		}
		return transformedMatrix;
	}

	@Override
	public double[][] transformBackwards(double[][] transformedMatrix) {
		if (transformedMatrix.length == 0) {
			return new double[0][0];
		}
		double[][] originalMatrix = new double[transformedMatrix.length][transformedMatrix[0].length];
		for (int i = 0; i < transformedMatrix.length; i++) {
			for (int j = 0; j < originalMatrix[i].length; j++) {
				for (int k = 0; k < U[j].length; k++) {
					originalMatrix[i][j] += transformedMatrix[i][k] * U[k][j];
				}
				originalMatrix[i][j] += meanX[j];
			}
		}
		return originalMatrix;
	}
	
	public String getName() {
		return TransformationProvider.PCA_TRANSFORMATION;
	}

	@Override
	public List<AttributeInfo> getAttributeMetadata(
			List<AttributeInfo> originalMetadata) {
		if (originalMetadata.size() != X[0].length) {
			throw new IllegalArgumentException("Illegal metadata.");
		}
		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
		for (int i = 0; i < originalMetadata.size(); i++) {
			attributes.add(new AttributeInfoBase("PCA" + (i + 1), AttributeInfo.AttributeRole.STANDARD_INPUT));
		}
		return attributes;
	}
	
}
