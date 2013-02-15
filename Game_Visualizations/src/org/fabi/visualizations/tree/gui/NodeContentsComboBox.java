
package org.fabi.visualizations.tree.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.fabi.visualizations.tree.AbstractTreeVisualization;

public class NodeContentsComboBox extends JComboBox {

	private static final long serialVersionUID = -2305641562580331284L;
	
	public NodeContentsComboBox(final AbstractTreeVisualization<?> visualization) {
		addItem("None");
		for (String s : visualization.getNodeContentsLabels()) {
			addItem(s);
		}
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				visualization.setProperty(AbstractTreeVisualization.PROPERTY_NODE_CONTENTS, getSelectedIndex());
			}
		});
		// TODO setSelectedIndex(...);
	}

}