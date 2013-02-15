package org.fabi.visualizations.scatter.sources;

public class AttributeInfoBase implements AttributeInfo {

	protected String name;
	protected AttributeRole role;
	
	public AttributeInfoBase(String name, AttributeRole role) {
		this.name = name;
		this.role = role;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public AttributeRole getRole() {
		return role;
	}

}
