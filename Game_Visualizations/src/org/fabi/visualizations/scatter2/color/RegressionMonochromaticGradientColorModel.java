package org.fabi.visualizations.scatter2.color;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Regression monochromatic gradient color model")
public class RegressionMonochromaticGradientColorModel extends ColorModelBase {

	@Property(name="Lower gradient bound")
	protected double min = 0.0;
	@Property(name="Gradient range")
	protected double range = 1.0;
	@Property(name="Hue")
	protected float hue = 1.0f;
	@Property(name="Saturation")
	protected float saturation = 0.0f;
	@Property(name="Brightness")
	protected float brightness = 0.0f;
	@Property(name="Alpha")
	protected float alpha = 1.0f;
	@Property(name="Alpha shading")
	protected boolean alphaShading = false;

	public RegressionMonochromaticGradientColorModel() {}
	
	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data, int index, int[] inputIndices, int[] outputIndices) {
		if (outputs.length != 1) {
			return null;
		}
		float point = (float) Math.max(0.0, Math.min(1.0, (outputs[0] - min) / range));
		if (alphaShading) {
			return new Color(hue, saturation, brightness, point);
		} else {
			return new Color(hue, point, brightness, alpha);
		}
	}

	@Override
	public void init(ScatterplotSource source) {
	}

	@Override
	public String getName() {
		return "Regression monochromatic gradient color model";
	}

	@Override
	public JComponent getControls() {
		JPanel res = new JPanel(new GridBagLayout());
		final JColorChooser chooser = new JColorChooser(new Color(hue, saturation, brightness));
		final JSlider slider = new JSlider(0, 1000, (int) (alpha * 1000));
		final JCheckBox checkBox = new JCheckBox("Use alpha shading");
		final JSpinner lowerSpinner = new JSpinner(new SpinnerNumberModel(
				min,
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		final JSpinner rangeSpinner = new JSpinner(new SpinnerNumberModel(
				range,
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		checkBox.setSelected(alphaShading);
		slider.setEnabled(!alphaShading);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		res.add(chooser, c);
		c.gridwidth = 1;
		c.gridy = 1;
		res.add(new JLabel("Alpha:"), c);
		c.gridx = 1;
		res.add(slider, c);
		c.gridy = 2;
		res.add(checkBox, c);
		c.gridx = 0;
		c.gridy = 3;
		res.add(new JLabel("Lower gradient bound:"), c);
		c.gridx = 1;
		res.add(lowerSpinner, c);
		c.gridx = 0;
		c.gridy = 4;
		res.add(new JLabel("Graident range:"), c);
		c.gridx = 1;
		res.add(rangeSpinner, c);
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		chooser.setChooserPanels(new AbstractColorChooserPanel[]{panels[1]});
		lowerSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				min = (Double) lowerSpinner.getModel().getValue();
			}
		});
		rangeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				range = (Double) rangeSpinner.getModel().getValue();
			}
		});
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				float[] components = chooser.getColor().getColorComponents(null);
				hue = components[0];
				saturation = components[1];
				brightness = components[2];
			}
		});
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				alpha = slider.getValue() / 1000.0f;
			}
		});
		checkBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				alphaShading = checkBox.isSelected();
				slider.setEnabled(!alphaShading);
			}
		});
		return res;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public boolean isAlphaShading() {
		return alphaShading;
	}

	public void setAlphaShading(boolean alphaShading) {
		this.alphaShading = alphaShading;
	}

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
}
