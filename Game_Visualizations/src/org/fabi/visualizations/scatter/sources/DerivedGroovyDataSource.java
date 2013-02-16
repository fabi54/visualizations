package org.fabi.visualizations.scatter.sources;

import java.util.Observable;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Derived Groovy data source")
public class DerivedGroovyDataSource extends Observable implements DataSource {
	
	@Property(name="Inputs script")
	protected String inputScript = "return new double[0][];";
	@Property(name="Outputs script")
	protected String outputScript = "return new double[0][];";
	@Property(name="Source name")
	protected String name = "Derived1";
	
	protected ScatterplotSource orig;
	protected GroovyShell shell;
	
	double[][] inputs;
	double[][] outputs;
	
	public DerivedGroovyDataSource(ScatterplotSource orig) {
		this.orig = orig;
		shell = new GroovyShell(ClassLoader.getSystemClassLoader());
		shell.setVariable("source", orig);
		update();
	}
	
	protected void update() {
		Script parsedInputScript = shell.parse(inputScript.toString());
		double[][] inputs = (double[][]) parsedInputScript.run();
		if (inputs.length > 0 && inputs[0].length != inputsNumber()) {
			throw new RuntimeException("Inputs number must be equal to original.");
		}
		this.inputs = inputs;
		Script parsedOutputScript = shell.parse(outputScript.toString());
		double[][] outputs = (double[][]) parsedOutputScript.run();
		if (outputs.length > 0 && outputs[0].length != outputsNumber()) {
			throw new RuntimeException("Outputs number must be equal to original.");
		}
		this.outputs = outputs;
		setChanged();
		notifyObservers();
	}
	
	@Override
	public double[][] getInputDataVectors() {
		return inputs;
	}

	@Override
	public double[][] getOutputDataVectors() {
		return outputs;
	}

	@Override
	public int inputsNumber() {
		return orig.getInputsNumber();
	}

	@Override
	public int outputsNumber() {
		return orig.getOutputsNumber();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers();
	}

	public String getInputScript() {
		return inputScript;
	}

	public void setInputScript(String inputScript) {
		this.inputScript = inputScript;
		update();
	}

	public String getOutputScript() {
		return outputScript;
	}

	public void setOutputScript(String outputScript) {
		this.outputScript = outputScript;
		update();
	}
	
}
