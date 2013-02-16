package org.fabi.visualizations.scatter.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fabi.visualizations.scatter.sources.DerivedGroovyDataSource;

public class DerivedGroovyDataSourceControlPanel extends JPanel {

	private static final long serialVersionUID = -1147842558012943197L;
	
	public DerivedGroovyDataSourceControlPanel(final DerivedGroovyDataSource source) {
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(600, 600));
		GridBagConstraints c = new GridBagConstraints();
		final JTextField name = new JTextField(source.getName());
		final JTextArea inputScript = new JTextArea(source.getInputScript());
		final JTextArea outputScript = new JTextArea(source.getOutputScript());
		final JButton okButton = new JButton("Execute scripts");
		inputScript.setPreferredSize(new Dimension(600, 600));
		outputScript.setPreferredSize(new Dimension(600, 600));
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Name:"), c);
		c.gridx = 1;
		add(name, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		JLabel l1 = new JLabel("<html><b>Inputs</b> <span style=\"font-weight: normal;\"><font color=\"red\">function</font>(source : ScatterplotSource) : double[<i>#instances</i>][" + source.inputsNumber() + "]</span></html>");
		add(l1, c);
			// TODO StringBuilder...
		c.gridy = 2;
		add(inputScript, c);
		c.gridy = 3;
		JLabel l2 = new JLabel("<html><b>Outputs</b> <span style=\"font-weight: normal;\"><font color=\"red\">function</font>(source : ScatterplotSource) : double[<i>#instances</i>][" + source.outputsNumber() + "]</span></html>");
		add(l2, c);
		// TODO StringBuilder...
		c.gridy = 4;
		add(outputScript, c);
		c.gridy = 5;
		add(okButton, c);
		name.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override public void removeUpdate(DocumentEvent e) { updateName(); }
			@Override public void insertUpdate(DocumentEvent e) { updateName(); }
			@Override public void changedUpdate(DocumentEvent e) { updateName(); }
			
			protected void updateName() {
				source.setName(name.getText());
			}
		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					source.setInputScript(inputScript.getText());
					source.setOutputScript(outputScript.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(okButton, "Script execution failed: \"" + ex.getMessage() + "\".", "Groovy Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

}
