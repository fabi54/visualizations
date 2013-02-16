package org.fabi.visualizations.scatter_old.sources;

import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.tools.math.ManyToOne;

@Deprecated
public interface MultiModelSource extends ModelSource {
	public void setCountMethod(ManyToOne method);
	public double[][] getModelResponses(int index, double[][] inputs);
	public double[][] getMaxResponses(double[][] inputs);
	public double[][] getMinResponses(double[][] inputs);
	public int getModelCount();
	public ModelSource[] getModels();
	public ModelSource getModel(int index);
}
