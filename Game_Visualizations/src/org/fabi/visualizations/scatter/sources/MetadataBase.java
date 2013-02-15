package org.fabi.visualizations.scatter.sources;

import java.util.List;

public abstract class MetadataBase implements Metadata {
	
	@Override
	public List<AttributeInfo> getAttributeInfo() {
		List<AttributeInfo> allAttributes = getInputAttributeInfo();
		allAttributes.addAll(getOutputAttributeInfo());
		return allAttributes;
	}
}
