package org.fabi.visualizations.scatter.color;

import java.awt.Color;

import javax.swing.JComponent;

import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.tools.gui.ColorTools;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Classification color model")
public class ClassificationColorModel extends ColorModelBase {

	@Property(name="HSBA")
	protected float[][] compArray;
	@Property(name="Alpha shading")
	protected boolean alphaShading = false;
	
	@Override
	public void init(ScatterplotSource source) {
		int outputsNr = source.getOutputsNumber();
		compArray = new float[outputsNr][4];
		ColorTools colorTools = new ColorTools();
		double oNr = (double) outputsNr;
		for (int i = 0; i < outputsNr; i++) {
			compArray[i] = colorTools.getPointColor(i / oNr, 255).getColorComponents(compArray[i]);
		}
	}

	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data,
			int index, int[] inputsIndices, int[] outputsIndices) {
//		assert(outputs.length == compArray.length);
//		float[] components = new float[4];
//		for (int i = 0; i < outputs.length; i++) {
//			for (int j = 0; j < components.length; j++) {
//				if (alphaShading && j = 4) {
//				components[j] += outputs[i] * compArray[i][j];
//			}
//		}
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

	public float[][] getCompArray() {
		return compArray;
	}

	public void setCompArray(float[][] compArray) {
		this.compArray = compArray;
	}

	public boolean isAlphaShading() {
		return alphaShading;
	}

	public void setAlphaShading(boolean alphaShading) {
		this.alphaShading = alphaShading;
	}
	
	

}
