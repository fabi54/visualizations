package org.fabi.visualizations.scatter.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
//import org.fabi.visualizations.scatter.color.CustomGroovyColorModel;
import org.fabi.visualizations.scatter.color.GradientColorModel;
import org.fabi.visualizations.scatter.color.RegressionMonochromaticGradientColorModel;
import org.fabi.visualizations.scatter.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter.color.SeriesDependentColorModel;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public class ColorModelComboBox extends JComboBox implements Observer {

	private static final long serialVersionUID = -8317232169586685652L;
	
	protected static Class<?>[] modelList = new Class<?>[] {
			ConstantColorModel.class,
			GradientColorModel.class,
			RegressionRainbowColorModel.class,
			RegressionMonochromaticGradientColorModel.class,
			SeriesDependentColorModel.class//,
			//CustomGroovyColorModel.class
	};
	
	public ColorModelComboBox() {
		init(null, null);
	}
	
	public ColorModelComboBox(ScatterplotVisualization visualization) {
		init(visualization.getSource(), visualization.getColorModel());
		visualization.addObserver(this);
	}
	
	public ColorModelComboBox(ScatterplotSource source) {
		init(source, null);
	}

	public ColorModelComboBox(ColorModel actual) {
		init(null, actual);
	}
	
	public ColorModelComboBox(ScatterplotVisualization visualization, ColorModel actual) {
		init(visualization.getSource(), actual);
		visualization.addObserver(this);
	}
	
	public void init(ScatterplotSource source, ColorModel actual) {
		if (getItemCount() > 0) {
			removeAllItems();
		}
		for (Class<?> c : modelList) {
			if (actual != null && c.equals(actual.getClass())) {
				addItem(actual);
				setSelectedItem(actual);
			} else {
				try {
					Object model = c.getConstructor().newInstance();
					if (source != null) { ((ColorModel) model).init(source); }
					addItem(model);
				} catch (SecurityException e) {
					throw new RuntimeException(e.fillInStackTrace());
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.fillInStackTrace());
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.fillInStackTrace());
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.fillInStackTrace());
				} catch (InstantiationException e) {
					throw new RuntimeException(e.fillInStackTrace());
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ScatterplotVisualization) {
			ScatterplotVisualization visualization = ((ScatterplotVisualization) o);
			for (int i = 0; i < getItemCount(); i++) {
				Object item = getItemAt(i);
				if (item instanceof ColorModel) {
					((ColorModel) item).init(visualization.getSource());
				}
			}
		}
	}

}
