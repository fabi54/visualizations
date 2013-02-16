package org.fabi.visualizations.scatter.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;

public class MinkowskiDistanceDotSizeModelControlPanel extends JPanel {

	private static final long serialVersionUID = -6032056990757970938L;

	public MinkowskiDistanceDotSizeModelControlPanel (final MinkowskiDistanceDotSizeModel model) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		JLabel l1 = new JLabel("Exponent:");
		l1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		final JSpinner exponentSpinner = new JSpinner(new SpinnerNumberModel(
				model.getP(),
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		exponentSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setP((Double) exponentSpinner.getModel().getValue());
			}
		});
		add(l1, c);
		c.gridx = 1;
		add(exponentSpinner, c);
		c.gridy++;
		c.gridx = 0;
		JLabel l2 = new JLabel("Largest size:");
		l1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		final JSpinner minSizeSpinner = new JSpinner(new SpinnerNumberModel(
				model.getMinSize(), 0, 50, 1));
		minSizeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setMinSize((Integer) minSizeSpinner.getModel().getValue());
			}
		});
		add(l2, c);
		c.gridx = 1;
		add(minSizeSpinner, c);
		c.gridy++;
		c.gridx = 0;
		JLabel l3 = new JLabel("Largest size distance bound:");
		l1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		final JSpinner minDistSpinner = new JSpinner(new SpinnerNumberModel(
				model.getMinDist(),
				0,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		minDistSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setMinDist((Double) minDistSpinner.getModel().getValue());
			}
		});
		add(l3, c);
		c.gridx = 1;
		add(minDistSpinner, c);
		c.gridy++;
		c.gridx = 0;
		JLabel l4 = new JLabel("Size range:");
		l4.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		final JSpinner rangeSizeSpinner = new JSpinner(new SpinnerNumberModel(
				model.getRangeSize(), 0, 50, 1));
		rangeSizeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setRangeSize((Integer) rangeSizeSpinner.getModel().getValue());
			}
		});
		add(l4, c);
		c.gridx = 1;
		add(rangeSizeSpinner, c);
		c.gridy++;
		c.gridx = 0;
		JLabel l5 = new JLabel("Largest size distance range:");
		l5.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		final JSpinner rangeDistSpinner = new JSpinner(new SpinnerNumberModel(
				model.getRangeDist(),
				0,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		rangeDistSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setRangeDist((Double) rangeDistSpinner.getModel().getValue());
			}
		});
		add(l5, c);
		c.gridx = 1;
		add(rangeDistSpinner, c);
	}
}
