package org.fabi.visualizations.scatter.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.DerivedGroovyDataSource;

public class NewDatasetPanel extends JPanel {

	private static final long serialVersionUID = 4652071193406157243L;
	
	protected JComponent controls;
	protected DataSource dataSource;
	
	protected ExtendableSourceControlDialog panel;
	
	public NewDatasetPanel(final ExtendableSourceControlDialog panel) {
		this.panel = panel;
		setLayout(new BorderLayout());
		reset();
		JPanel south = new JPanel();
		JButton okButton = new JButton("OK");
		JButton resetButton = new JButton("Reset");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.source.addDataSource(dataSource);
				reset();
			}
		});
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		south.add(okButton);
		south.add(resetButton);
		add(south, BorderLayout.SOUTH);
	}
	
	protected void reset() {
		if (controls != null) {
			remove(controls);
		}
		dataSource = new DerivedGroovyDataSource(panel.source.getOrigSource());
		controls = panel.getComponentForSource(dataSource);
		add(controls, BorderLayout.CENTER);
	}
}
