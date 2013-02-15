package org.fabi.visualizations.config;


//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.config.classcasting.ClassCaster;
import org.fabi.visualizations.config.classcasting.ClassCasterMap;
import org.fabi.visualizations.config.classcasting.ClassClassCaster;
import org.fabi.visualizations.config.classcasting.StringIdentityClassCaster;
import org.fabi.visualizations.config.classcasting.collection.ListClassCaster;

public class VisualizationConfig extends Properties {
	
	private static final long serialVersionUID = 6213955219054022723L;
	
	public static final String PROPERTY_CLASS_REF = "class_ref";
	
	private static Map<String, ClassCaster<?,String>> casterMap;
	
	static {
		casterMap = new HashMap<String,ClassCaster<?,String>>();
		casterMap.put(PROPERTY_CLASS_REF, ClassClassCaster.getInstance());
	}

	public static void addTypeCast(String key, Class<?> classRef) throws IllegalArgumentException {
		if (casterMap.containsKey(key)) {
			throw new IllegalArgumentException("Key already in use: " + key + ".");
		}
		ClassCaster<?,String> caster = ClassCasterMap.getInstance().get(classRef);
		if (caster == null) {
			throw new IllegalArgumentException("ClassCaster not available.");
		}
		casterMap.put(key, caster);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addTypeCastList(String key, Class<?> classRef) throws IllegalArgumentException {
		if (casterMap.containsKey(key)) {
			throw new IllegalArgumentException("Key already in use: " + key + ".");
		}
		casterMap.put(key, new ListClassCaster(classRef, String.class));
	}
	
	@Deprecated
	protected static void addTypeCaster(String key, ClassCaster<?,String> caster) throws IllegalArgumentException {
		if (casterMap.containsKey(key)) {
			throw new IllegalArgumentException("Key already in use: " + key + ".");
		}
		casterMap.put(key, caster);
	}
	
	private static ClassCaster<?,String> getClassCaster(String key) throws ClassCastException {
		ClassCaster<?, String> caster = casterMap.get(key);
		if (caster == null) {
			return StringIdentityClassCaster.getInstance();
		}
		return casterMap.get(key);
	}
	
	public VisualizationConfig(@SuppressWarnings("rawtypes") Class referenceClass) {
		this.setTypedProperty(PROPERTY_CLASS_REF, referenceClass);
	}

	@SuppressWarnings("unchecked")
	public final <T> void setTypedProperty(String key, T value) throws ClassCastException {
		ClassCaster<?,String> classCaster = getClassCaster(key);
		setProperty(key, ((ClassCaster<T,String>) classCaster).castForward(value));
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T getTypedProperty(String key) throws ClassCastException {
		ClassCaster<?,String> classCaster = getClassCaster(key);
		return (((ClassCaster<T,String>) classCaster).castBackward(getProperty(key)));
	}
	
	/**
	 * Will construct a union of this and the config in parameter.
	 * In case of conflict, values from this will be used.
	 * Resulted config stored in this.
	 */
	public void merge(VisualizationConfig defaults) {
		Set<Object> defaultKeys = defaults.keySet();
		for (Object key : defaultKeys) {
			if (!this.containsKey(key)) {
				this.put(key, defaults.get(key));
			}
		}
	}
	
	/**
	 * Will construct a union of this and the config in parameter.
	 * In case of conflict, values from defaults will be used.
	 * Resulted config stored in this.
	 */
	public void antiMerge(VisualizationConfig defaults) {
		Set<Object> defaultKeys = defaults.keySet();
		for (Object key : defaultKeys) {
			this.put(key, defaults.get(key));
		}
	}
	
	public Collection<String> changedEntries(VisualizationConfig other) {
		Set<String> result = new HashSet<String>();
		Set<Object> otherKeysView = other.keySet();
		Set<Object> otherKeys = new HashSet<Object>();
		otherKeys.addAll(otherKeysView);
		for (Object key : otherKeys) {
			if (!containsKey(key)) {
				result.add((String) key);
			} else if (!this.get(key).equals(other.get(key))) {
				result.add((String) key);
			}
		}
		return result;
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public<T> Visualization<T> createVisualization(T o) {
//		Class<?> clazz = getTypedProperty(PROPERTY_CLASS_REF);
//		for (Constructor<?> constructor : clazz.getConstructors()) {
//			if (constructor.getParameterTypes().length == 1) {
//				try {
//					return (Visualization) constructor.newInstance(o);
//				} catch (InvocationTargetException ex) {
//					return null;
//				} catch (IllegalAccessException ex) {
//					return null;
//				} catch (InstantiationException ex) {
//					return null;
//				}
//			}
//		}
//		return null;
//	}
}
