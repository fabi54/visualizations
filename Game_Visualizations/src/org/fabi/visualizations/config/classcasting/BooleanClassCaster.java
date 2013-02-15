package org.fabi.visualizations.config.classcasting;

public class BooleanClassCaster extends ClassCaster<Boolean,String> {

	protected static BooleanClassCaster instance = null;
	
	public static BooleanClassCaster getInstance() {
		if (instance == null) {
			instance = new BooleanClassCaster();
		}
		return instance;
	}
	
	protected BooleanClassCaster() {
		super(Boolean.class, String.class);
	}
	
	@Override
	public String castForward(Boolean arg) throws ClassCastException {
		return arg.toString();
	}

	@Override
	public Boolean castBackward(String arg) throws ClassCastException {
		return Boolean.parseBoolean(arg);
	}
}
