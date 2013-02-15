package org.fabi.visualizations.tree;

public class BentLineChildSupporter extends BentLineSupporter {
	public Object child;
	
	public BentLineChildSupporter(Object parent, Object child) {
		super(parent);
		this.child = child;
	}
}
