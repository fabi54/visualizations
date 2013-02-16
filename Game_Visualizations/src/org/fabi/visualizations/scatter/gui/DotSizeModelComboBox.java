package org.fabi.visualizations.scatter.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.dotsize.DotSizeModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public class DotSizeModelComboBox extends JComboBox implements Observer {

	private static final long serialVersionUID = 2199650270581231189L;

	protected static Class<?>[] modelList = new Class<?>[] {
		MinkowskiDistanceDotSizeModel.class
	};
	
	public DotSizeModelComboBox() {
		init(null, null);
	}
	
	public DotSizeModelComboBox(ScatterplotVisualization visualization) {
		init(visualization.getSource(), visualization.getDotSizeModel());
		visualization.addObserver(this);
	}
	
	public DotSizeModelComboBox(ScatterplotSource source) {
		init(source, null);
	}

	public DotSizeModelComboBox(DotSizeModel actual) {
		init(null, actual);
	}
	
	public DotSizeModelComboBox(ScatterplotVisualization visualization, DotSizeModel actual) {
		init(visualization.getSource(), actual);
		visualization.addObserver(this);
	}
	
	public void init(ScatterplotSource source, DotSizeModel actual) {
		if (getItemCount() > 0) {
			removeAllItems();
		}
		for (Class<?> c : modelList) {
			addItem("<none>");
			if (actual != null && c.equals(actual.getClass())) {
				addItem(actual);
				setSelectedItem(actual);
			} else {
				try {
					Object model = c.getConstructor().newInstance();
					if (source != null) { ((DotSizeModel) model).init(source); }
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
				if (item instanceof DotSizeModel) {
					((DotSizeModel) item).init(visualization.getSource());
				}
			}
		}
	}
}
