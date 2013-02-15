package org.fabi.visualizations.config.classcasting;

public abstract class ClassCaster<S,T> {
	
	protected Class<S> sourceClass;
	protected Class<T> targetClass;
	protected static final String NULL_STRING = "null";
	
	public ClassCaster(Class<S> sourceClass, Class<T> targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}
	
	public abstract T castForward(S arg) throws ClassCastException;
	public abstract S castBackward(T arg) throws ClassCastException;
}
