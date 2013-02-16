package org.fabi.visualizations.rapidminer.renderers;

import java.awt.Component;
import java.util.Collection;
import java.util.logging.Level;

import javax.swing.JLabel;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.gui.renderer.Renderer;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.gui.tools.RadioCardPanel;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.report.Reportable;
import com.rapidminer.tools.LogService;

public class ReportableContainerRenderer extends AbstractRenderer {

	@Override
	public Reportable createReportable(Object arg0, IOContainer arg1, int arg2,
			int arg3) {
		return null;
	}

	@Override
	public Component getVisualizationComponent(Object renderable, IOContainer iocontainer) {
		if (!(renderable instanceof ReportableContainer)) {
			return null;
		}
		IOObject[] renderables = new IOObject[0];
		renderables = ((ReportableContainer) renderable).getOtherReportables().toArray(renderables);
		return createVisualizations(renderables);
	}

	/* Visualizations for all IOObjects.
	 * 
	 * Partially copied from com.rapidminer.gui.processeditor.results.ResultDisplayTools,
	 * but with RadioCardPanel additional buttons disabled. */
	protected Component createVisualizations(IOObject[] renderables) {
		RadioCardPanel visualizationComponent = new RadioCardPanel("", null, false, false);
		Component[] ioobjectComponents = new Component[renderables.length];
		for (int i = 0; i < renderables.length; i++) {
			ioobjectComponents[i] = createVisualization(renderables[i]);
		}
		for (int i = 0; i < renderables.length; i++) {
			visualizationComponent.addCard(RendererService.getName(renderables[i].getClass()), ioobjectComponents[i]);
		}
		return visualizationComponent;
	}
	
	/* Visualizations for one IOObject.
	 * 
	 * Copied from com.rapidminer.gui.processeditor.results.ResultDisplayTools,
	 * but with RadioCardPanel additional buttons disabled. */
	protected Component createVisualization(IOObject renderable) {
		String resultName = RendererService.getName(renderable.getClass());
		if (resultName == null) {
			resultName = "";
		}
		RadioCardPanel visualizationComponent = new RadioCardPanel(resultName, renderable, false, false);
		Collection<Renderer> renderers = RendererService.getRenderers(renderable);
	    for (Renderer renderer : renderers) {
	    	try {
	    		Component rendererComponent = renderer.getVisualizationComponent(renderable, null);
	    		if (rendererComponent != null) {
	    			visualizationComponent.addCard(renderer.getName(), rendererComponent);
	    		}
	    	} catch (Exception e) {
	    		LogService.getRoot().log(Level.WARNING, "Error creating renderer: " + e, e);
	    		visualizationComponent.addCard(renderer.getName(), new JLabel("Error creating renderer " + renderer.getName() + " (see log)."));
	    	}
	    }
	    return visualizationComponent;
	}

	@Override
	public String getName() {
		return "Members";
	}

}
