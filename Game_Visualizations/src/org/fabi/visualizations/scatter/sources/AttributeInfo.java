package org.fabi.visualizations.scatter.sources;

public interface AttributeInfo {
	public enum AttributeRole{STANDARD_INPUT,STANDARD_OUTPUT};
	
	public String getName();
	public AttributeRole getRole();
}
