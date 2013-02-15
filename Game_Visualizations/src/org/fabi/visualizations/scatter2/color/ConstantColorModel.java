package org.fabi.visualizations.scatter2.color;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Constant color model")
public class ConstantColorModel extends ColorModelBase {

	@Property(name="Color")
	private Color color = Color.BLACK;
	
	@Override
	public void init(ScatterplotSource source) { }

	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data,
			int index, int[] inputsIndices, int[] outputsIndices) {
		return color;
	}

	@Override
	public String getName() {
		return "Constant color model";
	}

	@Override
	public JComponent getControls() {
		JPanel res = new JPanel(new GridBagLayout());
		final JColorChooser chooser = new JColorChooser(color);
		final JSlider slider = new JSlider(0, 255, color.getAlpha());
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
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		chooser.setChooserPanels(new AbstractColorChooserPanel[]{panels[1]});
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Color res = chooser.getColor();
				color = new Color(res.getRed(), res.getBlue(), res.getGreen(), color.getAlpha());
			}
		});
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				color = new Color(color.getRed(), color.getBlue(), color.getGreen(), slider.getValue());
			}
		});
		return res;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}	

}
