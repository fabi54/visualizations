package org.fabi.visualizations.scatter.color;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
@Deprecated
public class GradientColorModel extends ColorModelBase {

	protected static final int LOW = 0;
	protected static final int HIGH = 1;
	
	protected static final int HUE = 0;
	protected static final int SATURATION = 1;
	protected static final int BRIGHTNESS = 2;
	protected static final int ALPHA = 3;
	
	@Property(name="components")
	protected float[][][] h;
	
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
		Color[] colors = new Color[outputs.length];
		for (int i = 0; i < outputs.length; i++) {
			colors[i] = getColorForOutput(outputs[i], i); 
		}
		return mixColors(colors);
	}
	
	protected Color getColorForOutput(double outputVal, int outputIndex) {
		float b = (float) ((outputVal - min[outputIndex]) / range[outputIndex]);
		b = Math.min(1.0f, Math.max(0.0f, b));
		float[] res = new float[4];
		for (int i = 0; i < 4; i++) {
			res[i] = h[HIGH][i][outputIndex] + b * (h[HIGH][i][outputIndex] - h[LOW][i][outputIndex]); 
		}
		Color clr = Color.getHSBColor(res[HUE], res[SATURATION], res[BRIGHTNESS]);
		float[] resClr = clr.getRGBComponents(null);
		return new Color(resClr[0], resClr[1], resClr[2], res[ALPHA]);
	}
	
    protected static Color mixColors(Color[] colors) {
        float[] avg = new float[4];
        float[] current = new float[4];
        for (Color c : colors) {
        	c.getRGBComponents(current);
            for (int i = 0; i < 3; i++) {
            	avg[i] += current[i] * current[ALPHA];
            }
            avg[ALPHA] += current[ALPHA];
        }
        if (avg[ALPHA] > 0.0f) {
            for (int i = 0; i < 3; i++) {
            	avg[i] /= avg[ALPHA];
            }
            avg[ALPHA] /= 4;
        }
        return new Color(avg[HUE], avg[SATURATION], avg[BRIGHTNESS], avg[ALPHA]);
    }

	@Override
	public void init(ScatterplotSource source) {
		int len = source.getOutputsNumber();
		boolean initColors = false;
		if (h == null) { h = new float[2][4][len]; initColors = true; }
		if (min == null) { min = new double[len]; }
		if (range == null) { range = new double[len]; for (int i = 0; i < range.length; i++) { range[i] = 1.0; }}
		
		if (initColors) {
			ColorTools colorTools = new ColorTools();
			for (int i = 0; i < len; i++) {
				Color clr = colorTools.getPointColor(((double) i) / ((double) len - 1), 127);
				float[] hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), null);
				for (int j : new int[]{HUE, BRIGHTNESS}) {
					h[HIGH][j][i] = hsb[j];
					h[LOW][j][i] = hsb[j];
				}
				h[HIGH][SATURATION][i] = 1.0f;
				h[LOW][SATURATION][i] = 0.0f;
				h[HIGH][ALPHA][i] = 1.0f;
				h[LOW][ALPHA][i] = 1.0f;
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
		final JColorChooser chooser = new JColorChooser(Color.getHSBColor(h[bound][HUE][outputIndex],
				h[bound][SATURATION][outputIndex], h[bound][BRIGHTNESS][outputIndex]));
		res.add(chooser, c);
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		chooser.setChooserPanels(new AbstractColorChooserPanel[]{panels[1]});
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Color clr = chooser.getColor();
				float[] hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), null);
				for (int i = 0; i < 3; i++) {
					h[bound][i][outputIndex] = hsb[i];
				}
			}
		});
		c.gridwidth = 1;
		c.gridy = 1;
		res.add(new JLabel("Alpha:"), c);
		c.gridx = 1;
		final JSlider slider = new JSlider(0, 1000, (int) h[bound][ALPHA][outputIndex] * 1000);
		res.add(slider, c);
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				h[bound][ALPHA][outputIndex] = slider.getValue() / 1000.0f;
			}
		});
		return res;
	}
	
}
