package org.fabi.visualizations.scatter.sources;

import java.util.List;

import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

public interface Metadata {
	// TODO
	// information about data (e.g. for each attribute - numeric, nominal, ....)
	// maybe public AttributeInfo getInfo(int index), public DatasetInfo getGeneralInfo() ?
	
	public List<AttributeInfo> getAttributeInfo();
	
	public List<AttributeInfo> getInputAttributeInfo();
	public List<AttributeInfo> getOutputAttributeInfo();
	
	public void setTransformation(ReversibleTransformation transformation);
}
