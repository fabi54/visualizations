package org.fabi.visualizations.config.classcasting;

@SuppressWarnings("rawtypes")
public class ClassClassCaster extends ClassCaster<Class, String> {

	protected static ClassClassCaster instance = null;
	
	public static ClassClassCaster getInstance() {
		if (instance == null) {
			instance = new ClassClassCaster();
		}
		return instance;
	}
	
	protected ClassClassCaster() {
		super(Class.class, String.class);
	}
	
	@Override
	public String castForward(Class arg)
			throws ClassCastException {
		if (arg == null) {
			return NULL_STRING;
		} else {
			return arg.getCanonicalName();
		}
	}

	@Override
	public Class<?> castBackward(String arg)
			throws ClassCastException {
		if (arg.equals(NULL_STRING)) {
			return null;
		}
		try {
			return (Class) Class.forName(arg);
		} catch (ClassNotFoundException e) {
			throw new ClassCastException("ClassNotFoundException: " + e.getMessage());
		}
	}

}
