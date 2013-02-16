package org.fabi.visualizations.scatter_old;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter.sources.DataSource;

@Deprecated
public class ScatterplotVisualizationInitializerAttributeSelector extends ScatterplotVisualizationInitializer {

	protected VisualizationAttributeSelector attributeSelector;
	
	public ScatterplotVisualizationInitializerAttributeSelector(VisualizationAttributeSelector attributeSelector) {
		this.attributeSelector = attributeSelector;
	}
	
	@Override
	public VisualizationConfig getConfig(DatasetGenerator source) {
		VisualizationConfig cfg = super.getConfig(source);
		if (attributeSelector != null) {
			int[] orderedAttributes = attributeSelector.getAttributesForVisualization(source);
			int[] usedAttributes = null; // list of used attribute indices
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX, orderedAttributes[0]);
			if (!isRegression(source.dataSource)) {
				usedAttributes = new int[]{orderedAttributes[0]};
			} else {
				usedAttributes = new int[]{orderedAttributes[0], orderedAttributes[1]};
				if (orderedAttributes != null && orderedAttributes.length > 1) {
					cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, orderedAttributes[1]);
				}
			}
			DataSource data = source.dataSource;
			cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_JITTER_AMOUNT, getJitterAmount(data.getInputDataVectors(), usedAttributes));
		}
		return cfg;
	}
}
