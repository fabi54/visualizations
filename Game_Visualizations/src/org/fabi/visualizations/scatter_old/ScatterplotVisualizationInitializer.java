package org.fabi.visualizations.scatter_old;

import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.VisualizationInitializer;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.math.Metrics;
//import org.jfree.data.Range;

public class ScatterplotVisualizationInitializer implements VisualizationInitializer<DatasetGenerator, ScatterplotVisualization> {
	
	protected VisualizationConfig defaultConfig;
	
	public ScatterplotVisualizationInitializer() {
		defaultConfig = null;
	}
	
	public ScatterplotVisualizationInitializer(VisualizationConfig defaultConfig) {
		setDefaultConfig(defaultConfig);
	}
	
	public void setDefaultConfig(VisualizationConfig defaultConfig) {
		if (defaultConfig == null) {
			this.defaultConfig = null;
		} else {
			Class<?> classRef = defaultConfig.getTypedProperty(VisualizationConfig.PROPERTY_CLASS_REF);
			if (!ScatterplotVisualization.class.equals(classRef)) {
				throw new IllegalArgumentException("Incompatible config");
			}
			this.defaultConfig = defaultConfig;
		}
	}
	
	protected boolean isRegression(DataSource data) {
		return (data.outputsNumber() == 1);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public VisualizationConfig getConfig(DatasetGenerator source) {
		VisualizationConfig cfg = new VisualizationConfig(ScatterplotVisualization.class);
		DataSource data = source.dataSource;
		boolean yAxisInput = !isRegression(source.dataSource);
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, yAxisInput);
		List inputSettings = getInputSettings(data.getInputDataVectors());
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, inputSettings);
		if (defaultConfig != null) {
			cfg.antiMerge(defaultConfig);
		}
		//cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_JITTER_AMOUNT, getJitterAmount(data.getInputDataVectors()));
		return cfg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List getInputSettings(double[][] inputs) {
		double[] inputSettings = Arrays.getMedians(inputs);
		double[] minVect = null;
		double minDist = Double.MAX_VALUE;
		for (int i = 0; i < inputs.length; i++) {
			double dist = Metrics.euclideanDistance(inputs[i], inputSettings);
			if (dist < minDist) {
				minDist = dist;
				minVect = inputs[i];
			}
		}
		List l = new ArrayList(minVect.length);
		for (int i = 0; i < minVect.length; i++) {
			l.add(minVect[i]);
		}		
		return l;
	}
	
	protected static final double NULL_JITTER_BOUNDARY = 2;
	protected static final double JITTER_CONSTANT = 0.5;
	protected static final double MAX_JITTER = 2;

	protected int getJitterAmount(double[][] inputs) {
		if (inputs.length < 1) {
			return 0;
		}
		int[] usedAttributes = new int[inputs[0].length];
		for (int i = 0; i < usedAttributes.length; i++) {
			usedAttributes[i] = i;
		}
		return getJitterAmount(inputs, usedAttributes);
	}
	
	/**
	 * based on distinct values present in used attributes related to number of instances
	 */
	protected int getJitterAmount(double[][] inputs, int[] usedAttributes) {
		if (inputs.length < 1) {
			return 0;
		}
		int attributesNr = usedAttributes.length;
		int distinctValuesProduct = 1;
		for (int i = 0; i < attributesNr; i++) {
			int distinctValuesAttribute = 0;
			double[] column = Arrays.getColumn(inputs, usedAttributes[i]);
			java.util.Arrays.sort(column);
			double previous = column[0];
			for (int j = 0; j < column.length; j++) {
				if (column[j] != previous) {
					distinctValuesAttribute++;
					previous = column[j];
				}
			}
			distinctValuesProduct *= distinctValuesAttribute;
		}
		distinctValuesProduct = inputs.length / distinctValuesProduct; // relative with respect to number of instances
		if (distinctValuesProduct < NULL_JITTER_BOUNDARY) {
			return 0;
		} else {
			return (int) Math.min((distinctValuesProduct - NULL_JITTER_BOUNDARY) * JITTER_CONSTANT, MAX_JITTER); 
		}
	}


	@Override
	public ScatterplotVisualization getVisualization(DatasetGenerator source) {
		return new ScatterplotVisualization(source, getConfig(source));
	}

}
