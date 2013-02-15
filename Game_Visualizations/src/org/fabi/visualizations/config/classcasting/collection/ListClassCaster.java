package org.fabi.visualizations.config.classcasting.collection;

import java.util.LinkedList;
import java.util.List;

import org.fabi.visualizations.config.classcasting.ClassCaster;
import org.fabi.visualizations.config.classcasting.ClassCasterMap;


@SuppressWarnings("rawtypes")
public class ListClassCaster<T> extends ClassCaster<List,String> {

	protected ClassCaster<T,String> itemCaster;
	protected Class<T> itemClass;
	
	protected final static String SEPARATOR = "xxx";
	
	@SuppressWarnings("unchecked")
	public ListClassCaster(Class<T> itemClass, Class<String> targetClass) {
		super(List.class, targetClass);
		this.itemClass = itemClass;
		itemCaster = (ClassCaster<T,String>) (ClassCasterMap.getInstance().get(itemClass));
		if (itemCaster == null) {
			throw new IllegalArgumentException("Caster for parameter class not available");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String castForward(List arg) throws ClassCastException {
		if (arg == null) {
			return NULL_STRING;
		}
		StringBuffer result = new StringBuffer();
		for (Object item : arg) {
			if (result.length() > 0) {
				result.append(SEPARATOR);
			}
			if (!(itemClass.equals(item.getClass()))) {
				throw new ClassCastException("List may contain only objects of type " + itemClass.getName());
			}
			result.append(itemCaster.castForward((T) item));
		}
		return result.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List castBackward(String arg) throws ClassCastException {
		if (arg.equals(NULL_STRING)) {
			return null;
		}
		List result = new LinkedList();
		if (arg.length() == 0) {
			return result;
		}
		String[] items = arg.split(SEPARATOR);
		for (String s : items) {
			result.add(itemCaster.castBackward(s));
		}
		return result;
	}
	
}
