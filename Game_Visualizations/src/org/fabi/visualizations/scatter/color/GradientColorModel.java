package org.fabi.visualizations.scatter.color;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.tools.gui.ColorTools;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Gradient color model")
public class GradientColorModel extends ColorModelBase {

	protected static final int LOW = 0;
	protected static final int HIGH = 1;
	
	protected static final int RED = 0;
	protected static final int GREEN = 1;
	protected static final int BLUE = 2;
	protected static final int ALPHA = 3;
	
	@Property(name="components")
	protected int[][][] components;
	
	@Property(name="gradient lower bounds")
	protected double[] min;
	@Property(name="gradient ranges")
	protected double[] range;
	
	protected String[] names;
	
	public GradientColorModel() {}
	
	/**
	 * Resulting color is mixed from colors for all outputs.
	 */
	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data, int index, int[] inputIndices, int[] outputIndices) {
		List<Color> colors = new ArrayList<Color>(outputs.length);
		int outputIndex = 0;
		for (int i = 0; i < outputs.length; i++) {
			if (outputIndex < outputIndices.length && outputIndices[outputIndex] == i) {
				colors.add(getColorForOutput(outputs[i], i));
				outputIndex++;
			}
		}
		Color[] clrArray = new Color[colors.size()];
		clrArray = colors.toArray(clrArray);
		return mixColors(clrArray);
	}
	
	protected Color getColorForOutput(double outputVal, int outputIndex) {
		double b = (outputVal - min[outputIndex]) / range[outputIndex];
		b = Math.min(1.0, Math.max(0.0, b));
		int[] res = new int[4];
		res[RED] = getWeightedAverage(components[outputIndex][LOW][RED], components[outputIndex][HIGH][RED], b);
		res[GREEN] = getWeightedAverage(components[outputIndex][LOW][GREEN], components[outputIndex][HIGH][GREEN], b);
		res[BLUE] = getWeightedAverage(components[outputIndex][LOW][BLUE], components[outputIndex][HIGH][BLUE], b);
		res[ALPHA] = getWeightedAverage(components[outputIndex][LOW][ALPHA], components[outputIndex][HIGH][ALPHA], b);
		return new Color(res[RED], res[GREEN], res[BLUE], res[ALPHA]);
	}
	
	protected static int getWeightedAverage(int lower, int upper, double disbalance) {
		return (int) (lower + disbalance * (upper - lower));
	}
	
    protected static Color mixColors(Color[] colors) {
        double[] res = new double[4];
        for (Color clr : colors) {
        	res[RED] += clr.getRed() * clr.getAlpha();
        	res[GREEN] += clr.getGreen() * clr.getAlpha();
        	res[BLUE] += clr.getBlue() * clr.getAlpha();
        	res[ALPHA] += clr.getAlpha();
        }
        for (int i = 0; i < 3; i++) {
        	res[i] /= res[ALPHA];
        }
        for (int i = 0; i < res.length; i++) {
        	res[i] /= colors.length;
        }
        return new Color((int) res[RED], (int) res[GREEN], (int) res[BLUE], (int) res[ALPHA]);
    }

	@Override
	public void init(ScatterplotSource source) {
		int len = source.getOutputsNumber();
		boolean initColors = false;
		if (components == null) { components = new int[len][2][4]; initColors = true; }
		if (min == null) { min = new double[len]; }
		if (range == null) { range = new double[len]; for (int i = 0; i < range.length; i++) { range[i] = 1.0; }}
		
		if (initColors) {
			ColorTools colorTools = new ColorTools();
			for (int i = 0; i < len; i++) {
				Color clr = colorTools.getPointColor(((double) i) / ((double) len - 1), 127);
				components[i][HIGH][RED] = clr.getRed();
				components[i][HIGH][GREEN] = clr.getGreen();
				components[i][HIGH][BLUE] = clr.getBlue();
				components[i][HIGH][ALPHA] = 255;
				components[i][LOW][RED] = clr.getRed();
				components[i][LOW][GREEN] = clr.getGreen();
				components[i][LOW][BLUE] = clr.getBlue();
				components[i][LOW][ALPHA] = 0;
			}
		}
		
		// attribute names
		names = new String[len];
		Metadata metadata = source.getMetadata();
		if (metadata != null) {
			List<AttributeInfo> outputs = metadata.getOutputAttributeInfo();
			for (int i = 0; i < outputs.size(); i++) {
				names[i] = outputs.get(i).getName();
			}
		} else {
			for (int i = 0; i < len; i++) {
				names[i] = "output " + i;
			}
		}
	}

	@Override
	public String getName() {
		return "Gradients for outputs";
	}

	@Override
	public JComponent getControls() {
		JTabbedPane tabs = new JTabbedPane();
		for (int i = 0; i < names.length; i++) {
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			panel.add(getControlsForOutput(i, LOW), c);
			c.gridx = 1;
			panel.add(getControlsForOutput(i, HIGH), c);
			tabs.add(panel, names[i]);
		}
		return tabs;
	}
	
	protected JComponent getControlsForOutput(final int outputIndex, final int bound) {
		JPanel res = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		final JColorChooser chooser = new JColorChooser(new Color(components[outputIndex][bound][RED],
				components[outputIndex][bound][GREEN], components[outputIndex][bound][BLUE]));
		res.add(chooser, c);
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		chooser.setChooserPanels(new AbstractColorChooserPanel[]{panels[1]});
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Color clr = chooser.getColor();
				components[outputIndex][bound][RED] = clr.getRed();
				components[outputIndex][bound][GREEN] = clr.getGreen();
				components[outputIndex][bound][BLUE] = clr.getBlue();
			}
		});
		c.gridwidth = 1;
		c.gridy = 1;
		res.add(new JLabel("Alpha:"), c);
		c.gridx = 1;
		final JSlider slider = new JSlider(0, 255, components[outputIndex][bound][ALPHA]);
		res.add(slider, c);
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				components[outputIndex][bound][ALPHA] = slider.getValue();
			}
		});
		return res;
	}

	public int[][][] getComponents() {
		return components;
	}

	public void setComponents(int[][][] components) {
		this.components = components;
	}

	public double[] getMin() {
		return min;
	}

	public void setMin(double[] min) {
		this.min = min;
	}

	public double[] getRange() {
		return range;
	}

	public void setRange(double[] range) {
		this.range = range;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}
	
	
	
}
