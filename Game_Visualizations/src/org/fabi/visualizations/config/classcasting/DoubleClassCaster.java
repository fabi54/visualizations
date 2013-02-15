package org.fabi.visualizations.config.classcasting;

public class DoubleClassCaster extends ClassCaster<Double,String> {

	protected static DoubleClassCaster instance = null;
	
	public static DoubleClassCaster getInstance() {
		if (instance == null) {
			instance = new DoubleClassCaster();
		}
		return instance;
	}
	
	protected DoubleClassCaster() {
		super(Double.class, String.class);
	}
	
	@Override
	public String castForward(Double arg) throws ClassCastException {
		return arg.toString();
	}

	@Override
	public Double castBackward(String arg) throws ClassCastException {
		return Double.parseDouble(arg);
		// throws NumberFormatException
	}
}
