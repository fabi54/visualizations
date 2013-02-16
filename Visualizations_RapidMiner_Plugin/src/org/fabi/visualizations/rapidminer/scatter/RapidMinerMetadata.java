package org.fabi.visualizations.rapidminer.scatter;

import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.scatter.sources.MetadataBase;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.BinominalAttribute;
import com.rapidminer.example.table.NominalMapping;
import com.rapidminer.example.table.PolynominalAttribute;

public class RapidMinerMetadata extends MetadataBase {

	protected ExampleSet data;
	protected ReversibleTransformation transformation;
	
	public RapidMinerMetadata(ExampleSet data) {
		this.data = data;
	}
	
	@Override
	public List<AttributeInfo> getInputAttributeInfo() {
		Attributes attrs = data.getAttributes();
		Attribute[] attributes = attrs.createRegularAttributeArray();
		List<AttributeInfo> attributeList = new ArrayList<AttributeInfo>(attributes.length);
		for (int i = 0; i < attributes.length; i++) {
			attributeList.add(new AttributeInfoBase(attributes[i].getName(), AttributeInfo.AttributeRole.STANDARD_INPUT));
		}
		if (transformation == null) {
			return attributeList;
		} else {
			return transformation.getAttributeMetadata(attributeList);
		}
	}

	@Override
	public List<AttributeInfo> getOutputAttributeInfo() {
		Attributes attrs = data.getAttributes();
		Attribute label = attrs.getLabel();
		NominalMapping map = null;
		if (label != null) {
			if (label instanceof PolynominalAttribute) {
				map = ((PolynominalAttribute) label).getMapping();
			} else if (label instanceof BinominalAttribute) {
				map = ((BinominalAttribute) label).getMapping();
			}
			
			if (map == null) /*if (label.isNumerical())*/ {
				List<AttributeInfo> attributes = new ArrayList<AttributeInfo>(1);
				attributes.add(new AttributeInfoBase(label.getName(), AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				return attributes;
			} else {
				List<AttributeInfo> attributes = new ArrayList<AttributeInfo>(map.size());
				for (String value : map.getValues()) {
					attributes.add(new AttributeInfoBase(value, AttributeInfo.AttributeRole.STANDARD_OUTPUT));
				}
				return attributes;
			}
		} else {
			return new ArrayList<AttributeInfo>(0);
		}
	}

	@Override
	public void setTransformation(ReversibleTransformation transformation) {
		this.transformation = transformation;
	}

}
