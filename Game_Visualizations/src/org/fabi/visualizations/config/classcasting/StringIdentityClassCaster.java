package org.fabi.visualizations.config.classcasting;

public class StringIdentityClassCaster extends ClassCaster<String, String> {

	protected static StringIdentityClassCaster instance = null;
	
	public static StringIdentityClassCaster getInstance() {
		if (instance == null) {
			instance = new StringIdentityClassCaster();
		}
		return instance;
	}
	
	protected StringIdentityClassCaster() {
		super(String.class, String.class);
	}
	
	@Override
	public String castForward(String arg)
			throws ClassCastException {
		return arg;
	}

	@Override
	public String castBackward(String arg)
			throws ClassCastException {
		return arg;
	}

}
