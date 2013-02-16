package org.fabi.visualizations.scatter.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter.ScatterplotVisualization;

public class AxisRangeControlPanel extends JPanel {
	
	private static final long serialVersionUID = -1896945511327703222L;

	public AxisRangeControlPanel(final ScatterplotVisualization visualization) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		int gridy = 0;
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		add(new JLabel("X-Axis lower bound:"), c);
		c.gridx = 1;
		final JCheckBox xAxisLowerChB = new JCheckBox();
		add(xAxisLowerChB, c);
		c.gridx = 2;
		double[][] bounds = visualization.getActualAxesBounds();
		final JSpinner xAxisLower = new JSpinner(new SpinnerNumberModel(
				bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND],
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		add(xAxisLower, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		add(new JLabel("X-Axis range:"), c);
		c.gridx = 1;
		final JCheckBox xAxisRangeChB = new JCheckBox();
		add(xAxisRangeChB, c);
		c.gridx = 2;
		final JSpinner xAxisRange = new JSpinner(new SpinnerNumberModel(
				bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.UPPER_BOUND] - bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND],
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		add(xAxisRange, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		add(new JLabel("Y-Axis lower bound:"), c);
		c.gridx = 1;
		final JCheckBox yAxisLowerChB = new JCheckBox();
		add(yAxisLowerChB, c);
		c.gridx = 2;
		final JSpinner yAxisLower = new JSpinner(new SpinnerNumberModel(
				bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND],
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		add(yAxisLower, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		add(new JLabel("Y-Axis range:"), c);
		c.gridx = 1;
		final JCheckBox yAxisRangeChB = new JCheckBox();
		add(yAxisRangeChB, c);
		c.gridx = 2;
		final JSpinner yAxisRange = new JSpinner(new SpinnerNumberModel(
				bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.UPPER_BOUND] - bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND],
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY,
				Double.MIN_VALUE));
		add(yAxisRange, c);
		xAxisLowerChB.setSelected(!Double.isNaN(visualization.getxAxisRangeLower()));
		xAxisRangeChB.setSelected(!Double.isNaN(visualization.getxAxisRangeUpper()));
		yAxisLowerChB.setSelected(!Double.isNaN(visualization.getyAxisRangeLower()));
		yAxisRangeChB.setSelected(!Double.isNaN(visualization.getyAxisRangeUpper()));
		xAxisLower.setEnabled(!Double.isNaN(visualization.getxAxisRangeLower()));
		xAxisRange.setEnabled(!Double.isNaN(visualization.getxAxisRangeUpper()));
		yAxisLower.setEnabled(!Double.isNaN(visualization.getyAxisRangeLower()));
		yAxisRange.setEnabled(!Double.isNaN(visualization.getyAxisRangeUpper()));
		xAxisLower.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setxAxisRangeLower((Double) xAxisLower.getValue());
			}
		});
		xAxisRange.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setxAxisRangeUpper((Double) xAxisLower.getValue() + (Double) xAxisRange.getValue());
			}
		});
		yAxisLower.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setyAxisRangeLower((Double) yAxisLower.getValue());
			}
		});
		yAxisRange.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setyAxisRangeUpper((Double) yAxisLower.getValue() + (Double) yAxisRange.getValue());
			}
		});
		xAxisLowerChB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setxAxisRangeLower(!xAxisLowerChB.isSelected() ? Double.NaN : (Double) xAxisLower.getValue());
				xAxisLower.setEnabled(xAxisLowerChB.isSelected());
			}
		});
		xAxisRangeChB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setxAxisRangeUpper(!xAxisLowerChB.isSelected() ? Double.NaN : ((Double) xAxisLower.getValue() + (Double) xAxisRange.getValue()));
				xAxisRange.setEnabled(xAxisRangeChB.isSelected());
			}
		});
		yAxisLowerChB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setyAxisRangeLower(!yAxisLowerChB.isSelected() ? Double.NaN : (Double) yAxisLower.getValue());
				yAxisLower.setEnabled(yAxisLowerChB.isSelected());
			}
		});
		yAxisRangeChB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setyAxisRangeUpper(!yAxisLowerChB.isSelected() ? Double.NaN : ((Double) yAxisLower.getValue() + (Double) yAxisRange.getValue()));
				yAxisRange.setEnabled(yAxisRangeChB.isSelected());
			}
		});
	}

}
