package org.fabi.visualizations.scatter_old;

import java.util.List;

import org.fabi.visualizations.config.VisualizationConfig;
//import org.fabi.visualizations.scatter.sources.DataSource;
//import org.fabi.visualizations.tools.transformation.LinearTransformation;
//import org.fabi.visualizations.tools.transformation.TransformationProvider;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.fabi.visualizations.tools.transformation.TransformationProvider;

public class ScatterplotVisualizationInitializerTransformation extends ScatterplotVisualizationInitializer {

	//protected LinearTransformation transformation;
	protected String transformation;
	
	public ScatterplotVisualizationInitializerTransformation(/*LinearTransformation*/ String transformation) {
		this.transformation = transformation;
	}
	
	public ScatterplotVisualizationInitializerTransformation(/*LinearTransformation*/ String transformation, VisualizationConfig defaultConfig) {
		super(defaultConfig);
		this.transformation = transformation;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public VisualizationConfig getConfig(DatasetGenerator source) {
		VisualizationConfig cfg = super.getConfig(source);
		if (transformation != null) {
			//String transformationName = TransformationProvider.getInstance().getString(transformation);
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_TRANSFORMATION, /*transformationName*/ transformation);
			//int[] usedAttributes = null; // list of used attribute indices
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX, 0);
			DataSource data = source.dataSource;
			ReversibleTransformation linearTransformation = TransformationProvider.getInstance().getTransformation(transformation, data.getInputDataVectors());
			double[][] transformedInputs = linearTransformation.transformForwards(data.getInputDataVectors());
			List l = getInputSettings(transformedInputs);
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, l);
			if (!isRegression(source.dataSource)) {
				//usedAttributes = new int[]{0};
			} else {
				//usedAttributes = new int[]{0, 1};
				cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, 1);
			}
			boolean yAxisInput = !isRegression(source.dataSource);
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, yAxisInput);
			
			//DataSource data = source.dataSource;
			//double[][] transformedVectors = transformation.transformForwards(data.getInputDataVectors());
			//cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_JITTER_AMOUNT, getJitterAmount(transformedVectors, usedAttributes));
		}
		if (defaultConfig != null) {
			cfg.antiMerge(defaultConfig);
		}
		return cfg;
	}
}
