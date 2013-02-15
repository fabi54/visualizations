package org.fabi.visualizations;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;

public class Global {
	protected static Global instance = null;
	
	protected Global() { }
	
	public static Global getInstance() {
		if (instance == null) {
			instance = new Global();
		}
		return instance;
	}
	
	public void init() {
		// static initializers call
		try {
			new VisualizationConfig(Visualization.class);
		} catch (Exception ex) { }
		try {
			new ScatterplotVisualization(null);
		} catch (Exception ex) { }
		try {
			new InstantiableTreeVisualization(null);
		} catch (Exception ex) { }
	}
}
