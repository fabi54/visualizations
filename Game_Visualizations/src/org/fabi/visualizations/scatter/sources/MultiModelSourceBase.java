package org.fabi.visualizations.scatter.sources;

import org.fabi.visualizations.tools.math.ManyToOne;
import org.fabi.visualizations.tools.math.Maximum;
import org.fabi.visualizations.tools.math.Minimum;

public class MultiModelSourceBase implements MultiModelSource {

	protected ModelSource[] sources;
	protected ManyToOne method;
	
	public MultiModelSourceBase(ModelSource[] sources, ManyToOne countMethod) {
		if (sources.length < 1) {
			throw new IllegalArgumentException("At least one model required");
		}
		int iNumber = sources[0].inputsNumber();
		int oNumber = sources[0].outputsNumber();
		for (int i = 1; i < sources.length; i++) {
			if (sources[i].inputsNumber() != iNumber
				|| sources[i].outputsNumber() != oNumber) {
				throw new IllegalArgumentException("All models must have same feature space");
			}
		}
		this.sources = sources;
		this.method = countMethod;
	}
	
	@Override
	public int inputsNumber() {
		return sources[0].inputsNumber();
	}

	@Override
	public int outputsNumber() {
		return sources[0].outputsNumber();
	}

	@Override
	public void setCountMethod(ManyToOne method) {
		this.method = method;
	}

	@Override
	public double[][] getModelResponses(double[][] inputs) {
		double[][][] responses = new double[sources.length][][];
		for (int i = 0; i < sources.length; i++) {
			responses[i] = getModelResponses(i, inputs);
		}
		double[][] results = new double[inputs.length][outputsNumber()];
		double[] tmp = new double[sources.length];
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < results[i].length; j++) {
				for (int k = 0; k < sources.length; k++) {
					tmp[k] = responses[k][i][j];
				}
				results[i][j] = method.getResult(tmp);
			}
		}
		return results;
	}

	@Override
	public double[][] getMaxResponses(double[][] inputs) {
		ManyToOne bck = method;
		method = new Maximum();
		double[][] result = getModelResponses(inputs);
		method = bck;
		return result;
	}

	@Override
	public double[][] getMinResponses(double[][] inputs) {
		ManyToOne bck = method;
		method = new Minimum();
		double[][] result = getModelResponses(inputs);
		method = bck;
		return result;
	}

	@Override
	public double[][] getModelResponses(int index, double[][] inputs) {
		return sources[index].getModelResponses(inputs);
	}
	
	public ModelSource[] getModels() {
		return sources;
	}
	
	@Override
	public int getModelCount() {
		return sources.length;
	}

	@Override
	public ModelSource getModel(int index) {
		return sources[index];
	}

	@Override
	public String getName() {
		return "";
	}

}
