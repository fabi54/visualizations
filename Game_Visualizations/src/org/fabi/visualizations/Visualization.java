package org.fabi.visualizations;


import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.ytoh.configurations.AbstractProperty;
import org.ytoh.configurations.DefaultArrayProperty;
import org.ytoh.configurations.Property;
import org.ytoh.configurations.util.AnnotationPropertyExtractor;

import javax.swing.JComponent;

public abstract class Visualization<S> extends Observable implements Observer, Cloneable {
	
	protected S source;
	
	protected static AnnotationPropertyExtractor propertyExtractor = new AnnotationPropertyExtractor();
	
	protected PropertyChangeSupport propertyChangeSupport;
	
	@SuppressWarnings("rawtypes")
	protected List<Property> properties;
	
	public Visualization() {
	}
	
	public void setSource(S source) {
		this.source = source;
	}
	
	public Visualization<?> copy() {
		try {
			return (Visualization<?>) this.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException("Unexpected CloneNotSupportedException.");
		}
	}
	
	public abstract JComponent getControls();
	
	public abstract JComponent getVisualizationAsComponent();
	
	/** Returns image of size preferred by the visualization settings. */
	public abstract BufferedImage getVisualizationAsImage();
	
	public abstract BufferedImage getVisualizationAsImage(int width, int height);
	
	@Override
	public abstract String toString();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setProperty(String key, Object value) {
		if (properties == null) {
			properties = propertyExtractor.propertiesFor(this);
		}
		for (Property p : properties) {
			if (key.equals(p.getName())) {
				if (p instanceof DefaultArrayProperty) {
					try {
						Object v = p.getValue();
						int origLength = Array.getLength(v);
						int newLength = Array.getLength(value);
						if (origLength != newLength) {
							throw new IllegalArgumentException("Invalid property value for \"" + key + "\".");
						}
						for (int i = 0; i < origLength; i++) {
							Array.set(v, i, Array.get(value, i));
						}
					} catch (Exception ex) {
						throw new IllegalArgumentException("Invalid property value for \"" + key + "\".");
					}
					update();
				} else {
					p.setValue(value);
				}
				return;
			}
		}
//		VisualizationConfig cfg = getConfig();
//		cfg.setTypedProperty(key, value);
//		List<String> changedList = new ArrayList<String>(1);
//		changedList.add(key);
//		setConfiguration(cfg, changedList);
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (properties == null) {
			properties = propertyExtractor.propertiesFor(this);
		}
		for (Property<?> p : properties) {
			if (p.getName().equals(propertyName)) {
				((AbstractProperty<?>) p).addPropertyChangeListener(listener);
			}
		}
	}
	
	public void addPropertyChangeListener(String[] propertyNames, PropertyChangeListener listener) {
		for (String propertyName : propertyNames) {
			addPropertyChangeListener(propertyName, listener);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		update();
	}
	
	protected abstract void update();
}
