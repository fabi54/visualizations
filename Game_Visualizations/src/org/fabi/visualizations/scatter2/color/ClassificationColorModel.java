package org.fabi.visualizations.scatter2.color;

import java.awt.Color;

import javax.swing.JComponent;

import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.fabi.visualizations.tools.gui.ColorTools;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Classification color model")
public class ClassificationColorModel extends ColorModelBase {

	@Property(name="Hues")
	protected float[] hue;
	@Property(name="Saturations")
	protected float[] saturation;
	@Property(name="Brightnesses")
	protected float[] brightness;
	@Property(name="Alphas")
	protected float[] alpha;
	@Property(name="Alpha shading")
	protected boolean alphaShading = false;
	
	@Override
	public void init(ScatterplotSource source) {
		double outputsNr = (double) source.getOutputsNumber();
		@SuppressWarnings("unused") // TODO remove warning
		ColorTools colorTools = new ColorTools();
		for (int i = 0; i < outputsNr; i++) {
			//colorTools.getPointColor(i / outputsNr, 255).getColorComponents(compArray);
			
		}
	}

	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data,
			int index, int[] inputsIndices, int[] outputsIndices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Classification color model";
	}

	@Override
	public JComponent getControls() {
		// TODO Auto-generated method stub
		return null;
	}

}
