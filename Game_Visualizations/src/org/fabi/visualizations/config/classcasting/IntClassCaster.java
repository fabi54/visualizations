package org.fabi.visualizations.config.classcasting;

public class IntClassCaster extends ClassCaster<Integer,String> {

	protected static IntClassCaster instance = null;
	
	public static IntClassCaster getInstance() {
		if (instance == null) {
			instance = new IntClassCaster();
		}
		return instance;
	}
	
	protected IntClassCaster() {
		super(Integer.class, String.class);
	}
	
	@Override
	public String castForward(Integer arg) throws ClassCastException {
		return arg.toString();
	}

	@Override
	public Integer castBackward(String arg) throws ClassCastException {
		return Integer.parseInt(arg);
		// throws NumberFormatException
	}

}
