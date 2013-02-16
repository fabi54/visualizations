package org.fabi.visualizations.scatter.color;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.tools.gui.ColorTools;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Rainbow color model")
public class RegressionRainbowColorModel extends ColorModelBase {

	@Property(name="Lower gradient bound")
	protected double min = 0.0;
	@Property(name="Gradient range")
	protected double range = 1.0;
	@Property(name="Non-periodic")
	protected boolean nonPeriodic = true;
	@Property(name="Data alpha")
	protected int dataAlpha = 255;
	@Property(name="Model alpha")
	protected int modelAlpha = 255;
	
	protected static ColorTools colorTools = new ColorTools();
	
	public RegressionRainbowColorModel() {}
	
	public RegressionRainbowColorModel(double min, double max) {
		this.min = min;
		this.range = max - min;
	}
	
	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data, int index, int[] inputIndices, int[] outputIndices) {
		if (outputs.length != 1) {
			return null;
		}
		if (outputIndices.length != 1) {
			return new Color(0.0f, 0.0f, 0.0f, 0.0f);
		}
		double point = (outputs[0] - min) / range;
		if (nonPeriodic) {
			point = Math.max(0.0, Math.min(1.0, point));
		}
		return colorTools.getPointColor(point, data ? dataAlpha : modelAlpha);
	}

	@Override
	public void init(ScatterplotSource source) {
	}

	@Override
	public String getName() {
		return "Regression rainbow gradient";
	}

	@Override
	public JComponent getControls() {
		JPanel res = new JPanel(new GridBagLayout());
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
		final JCheckBox periodicCheckBox = new JCheckBox("Repeat gradient outside bounds");
		final JSlider dataAlphaSlider = new JSlider(0, 255, dataAlpha);
		final JSlider modelAlphaSlider = new JSlider(0, 255, modelAlpha);
		JLabel l1 = new JLabel("Lower gradient bound:");
		JLabel l2 = new JLabel("Gradient range:");
		JLabel l3 = new JLabel("Data alpha:");
		JLabel l4 = new JLabel("Model alpha:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		res.add(l1, c);
		c.gridx = 1;
		res.add(lowerSpinner, c);
		c.gridx = 0;
		c.gridy = 1;
		res.add(l2, c);
		c.gridx = 1;
		res.add(rangeSpinner, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		res.add(periodicCheckBox, c);
		c.gridy = 3;
		c.gridwidth = 1;
		res.add(l3, c);
		c.gridx = 1;
		res.add(dataAlphaSlider, c);
		c.gridx = 0;
		c.gridy = 4;
		res.add(l4, c);
		c.gridx = 1;
		res.add(modelAlphaSlider, c);
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
		periodicCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nonPeriodic = !periodicCheckBox.isSelected();
			}
		});
		dataAlphaSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				dataAlpha = dataAlphaSlider.getValue();
			}
		});
		modelAlphaSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				modelAlpha = modelAlphaSlider.getValue();
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

	public boolean isNonPeriodic() {
		return nonPeriodic;
	}

	public void setNonPeriodic(boolean nonPeriodic) {
		this.nonPeriodic = nonPeriodic;
	}

}
