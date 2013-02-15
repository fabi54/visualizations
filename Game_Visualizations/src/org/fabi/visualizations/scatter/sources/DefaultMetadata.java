package org.fabi.visualizations.scatter.sources;

import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

public class DefaultMetadata extends MetadataBase {
	
	protected DatasetGenerator generator;
	protected ReversibleTransformation transformation;
	
	public DefaultMetadata(DatasetGenerator generator) {
		this.generator = generator;
		this.transformation = null;
	}

	@Override
	public List<AttributeInfo> getInputAttributeInfo() {
		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>(generator.getInputsNumber());
		for (int i = 0; i < generator.getInputsNumber(); i++) {
			attributes.add(new AttributeInfoBase(Integer.toString(i + 1), AttributeInfo.AttributeRole.STANDARD_INPUT));
		}
		if (transformation == null) {
			return attributes;
		} else {
			return transformation.getAttributeMetadata(attributes);
			// TODO resolve
		}
	}

	@Override
	public List<AttributeInfo> getOutputAttributeInfo() {
		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>(generator.getOutputsNumber());
		int offset = generator.getInputsNumber() + 1;
		for (int i = 0; i < generator.getOutputsNumber(); i++) {
			attributes.add(new AttributeInfoBase(Integer.toString(i + offset), AttributeInfo.AttributeRole.STANDARD_OUTPUT));
		}
		return attributes;
	}

	@Override
	public void setTransformation(ReversibleTransformation transformation) {
		this.transformation = transformation;
	}
}
