package org.fabi.visualizations.scatter2.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter2.sources.ExtendableScatterplotSource;

public class ScatterplotVisualizationControlPanel extends JPanel {

	private static final long serialVersionUID = 4450545891183048296L;
	
	protected ScatterplotVisualization visualization;
	
	public ScatterplotVisualizationControlPanel(ScatterplotVisualization visualization) {
		super(new GridBagLayout());
		this.visualization = visualization;
		propertyQueue = new HashMap<String, Object>();
		addComponents();
	}
	
	// TODO refactor
	protected void addComponents() {
		int gridy = 0;
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		// Extendable source dialog
		if (visualization.getSource() instanceof ExtendableScatterplotSource) {
			c.gridx = 1;
			c.gridy = gridy;
			JButton seriesButton = new JButton("Configure series...");
			add(seriesButton, c);
			final JDialog sourceDialog = new ExtendableSourceControlDialog((ExtendableScatterplotSource) visualization.getSource());
			sourceDialog.setVisible(false);
			seriesButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					sourceDialog.setVisible(true);
				}
				
			});
		}
		
		c.gridx = 0;
		c.gridy = ++gridy;
		JLabel l2 = new JLabel("X-Axis:");
		l2.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l2, c);
		c.gridx = 1;
		add(new AxisAttributeSelectorComboBox(visualization, this, ScatterplotVisualization.X_AXIS), c);
		c.gridx = 0;
		c.gridy = ++gridy;
		JLabel l3 = new JLabel("Y-Axis:");
		l3.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l3, c);
		c.gridx = 1;
		add(new AxisAttributeSelectorComboBox(visualization, this, ScatterplotVisualization.Y_AXIS), c);
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 2;
		add(new InputsSettingPanel(visualization, this, 2), c);
		
		// Visible series/outputs
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		JLabel l6 = new JLabel("Visible series:");
		l6.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l6, c);
		JButton visibleButton = new JButton("Settings...");
		c.gridx = 1;
		final JDialog visibilityControlDialog = new VisibilityControlDialog(visualization, this);
		visibilityControlDialog.setVisible(false);
		add(visibleButton, c);
		visibleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visibilityControlDialog.setVisible(true);
			}
		});
		
		// Color model
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		JLabel l4 = new JLabel("Color model:");
		l4.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l4, c);
		JButton colorModelButton = new JButton("Settings...");
		c.gridx = 1;
		final JDialog colorModelDialog = new ColorModelControlDialog(visualization, this);
		colorModelDialog.setVisible(false);
		add(colorModelButton, c);
		
		colorModelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				colorModelDialog.setVisible(true);
			}
			
		});
		
		// Dot size model
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		JLabel l5 = new JLabel("Dot size:");
		l5.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l5, c);
		final JDialog dotSizeModelDialog = new DotSizeModelControlDialog(visualization, this);
		dotSizeModelDialog.setVisible(false);
		JButton dotSizeModelButton = new JButton("Settings...");
		c.gridx = 1;
		add(dotSizeModelButton, c);
		dotSizeModelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dotSizeModelDialog.setVisible(true);
				
			}
		});
		
		// Visualization foreground/background
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		JLabel l7 = new JLabel("Background:");
		l7.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		add(l7, c);
		final JDialog colorDialog = new BackgroundControlDialog(visualization, this);
		colorDialog.setVisible(false);
		JButton colorButton = new JButton("Settings...");
		c.gridx = 1;
		add(colorButton, c);
		
		colorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				colorDialog.setVisible(true);
			}
			
		});
		// axes ranges
		c.gridy = ++gridy;
		c.gridx = 0;
		c.gridwidth = 2;
		add(new AxisRangeControlPanel(visualization), c);
		
		// lazy property controls
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 2;
		final JCheckBox lazyChb = new JCheckBox("Apply immidiately");
		add(lazyChb, c);
		c.gridy = ++gridy;
		final JButton lazyButton = new JButton("Apply");
		add(lazyButton, c);
		lazyChb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				applySetting();
				lazySetting = !(lazyChb.isSelected());
				lazyButton.setEnabled(lazySetting);
			}
		});
		lazyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				applySetting();
			}
		});
		lazySetting = false;
		lazyChb.setSelected(true);
		lazyButton.setEnabled(false);
	}
	
/* Lazy property setting */
	
	// TODO fire property change (e.g. disabled inputs setting sliders)
	
	protected boolean lazySetting;
	protected Map<String, Object> propertyQueue;
	
	public void setProperty(String propertyName, Object propertyValue) {
		if (lazySetting) {
			propertyQueue.put(propertyName, propertyValue);
		} else {
			visualization.setProperty(propertyName, propertyValue);
		}
	}
	
	protected void applySetting() {
		if (lazySetting) {
			for (Map.Entry<String, Object> entry : propertyQueue.entrySet()) {
				visualization.setProperty(entry.getKey(), entry.getValue());
			}
			propertyQueue.clear();
		}
	}

}
