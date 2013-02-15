package org.fabi.visualizations.rapidminer;
/*
import java.awt.Component;

import javax.swing.JLabel;

import org.fabi.visualizations.game.GameClassifierSource;
import org.fabi.visualizations.game.GameModelLearnableSource;
import org.fabi.visualizations.scatter.DatasetGenerator;
import org.fabi.visualizations.scatter.ScatterplotVisualizationInitializerAttributeSelector;
import org.fabi.visualizations.scatter.VisualizationAttributeSelectorCorrelation;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.report.Reportable;

import game.classifier.GameClassifierContainer;
import game.classifiers.Classifier;
import game.model.GameModelContainer;
import game.models.ModelLearnable;*/

public class ScatterplotVisualizationRenderer { /*extends AbstractRenderer {

	@Override
	public Reportable createReportable(Object arg0, IOContainer arg1, int arg2, int arg3) {
		return null;
	}

	@Override
	public String getName() {
		return "Scatterplot";
	}

	@Override
	public Component getVisualizationComponent(	Object renderable, IOContainer ioContainer)  {
		DataSource dSource = null;
		ModelSource mSource = null;
		if (renderable instanceof GameModelContainer) {
			ModelLearnable model = (ModelLearnable) (((GameModelContainer) renderable).getModel().get(0).getModel());
			GameModelLearnableSource source = new GameModelLearnableSource(model);
			dSource = source;
			mSource = source;
		} else if (renderable instanceof GameClassifierContainer) {
			Classifier classifier = ((GameClassifierContainer) renderable).getConnectableClassifier();
			GameClassifierSource source = new GameClassifierSource(classifier);
			dSource = source;
			mSource = source;
		}
		if (dSource != null) {
			DatasetGenerator generator = new DatasetGenerator(mSource, dSource);
			ScatterplotVisualizationInitializerAttributeSelector initializer
				= new ScatterplotVisualizationInitializerAttributeSelector(VisualizationAttributeSelectorCorrelation.getInstance());
			return initializer.getVisualization(generator).getVisualizationAsComponent();
		} else {
			return new JLabel("Error creating renderer.");
		}
	}
*/
}
