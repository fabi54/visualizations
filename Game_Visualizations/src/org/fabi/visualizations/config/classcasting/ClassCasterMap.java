package org.fabi.visualizations.config.classcasting;


import java.util.HashMap;

public class ClassCasterMap extends HashMap<Class<?>, ClassCaster<?,String>> {
	
	private static final long serialVersionUID = 869699217850782921L;
	
	protected static ClassCasterMap instance;
	
	public static ClassCasterMap getInstance() {
		if (instance == null) {
			instance = new ClassCasterMap();
		}
		return instance;
	}
		
	protected ClassCasterMap() {
		super();
		put(Boolean.class, BooleanClassCaster.getInstance());
		put(Double.class, DoubleClassCaster.getInstance());
		put(Integer.class, IntClassCaster.getInstance());
		put(String.class, StringIdentityClassCaster.getInstance());
	}
}
