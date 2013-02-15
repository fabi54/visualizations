package org.fabi.visualizations.scatter2.color;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Custom groovy color model")
public class CustomGroovyColorModel extends ColorModelBase {

	protected static final String FUNCTION_HEADER
		=	"Color getColor(double[] inputs, double[] outputs, boolean data,"
		+	"int index, int[] inputsIndices, int[] outputsIndices, ScatterplotSource source {";
	
	@Property(name="Script")
	private String script = "";
	
	protected ScatterplotSource source;
	
	@Override
	public void init(ScatterplotSource source) {
		this.source = source;
	}

	@Override
	public Color getColor(double[] inputs, double[] outputs, boolean data,
			int index, int[] inputsIndices, int[] outputsIndices) {
		try {
			GroovyShell shell = new GroovyShell(ClassLoader.getSystemClassLoader());
			shell.setVariable("inputs", inputs);
			shell.setVariable("outputs", outputs);
			shell.setVariable("data", data);
			shell.setVariable("index", index);
			shell.setVariable("inputsIndices", inputsIndices);
			shell.setVariable("outputsIndices", outputsIndices);
			shell.setVariable("source", source);
			StringBuffer resultScript = new StringBuffer();
			resultScript.append("import org.fabi.visualizations.scatter2.sources.ScatterplotSource;\n");
			resultScript.append("import java.awt.Color;\n");
			resultScript.append(script);
			Script parsedScript = shell.parse(resultScript.toString());
			return (Color) parsedScript.run();
		} catch (Exception ex) {
			System.out.println(ex);
			return Color.BLACK;
		}
	}

	@Override
	public String getName() {
		return "Custom Groovy color model";
	}

	@Override
	public JComponent getControls() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(FUNCTION_HEADER), BorderLayout.NORTH);
		final JTextField textField = new JTextField(script);
		textField.setAlignmentY(JComponent.TOP_ALIGNMENT);
		panel.add(textField, BorderLayout.CENTER);
		panel.add(new JLabel("}"), BorderLayout.SOUTH);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				script = textField.getText();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				script = textField.getText();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				script = textField.getText();
			}
		});
		return panel;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
