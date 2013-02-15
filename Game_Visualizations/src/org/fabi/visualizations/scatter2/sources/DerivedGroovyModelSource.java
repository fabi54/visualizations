package org.fabi.visualizations.scatter2.sources;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.Observable;

import org.fabi.visualizations.scatter.sources.ModelSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Derived Groovy data source")
public class DerivedGroovyModelSource extends Observable implements ModelSource {

	@Property(name="Response script")
	protected String responseScript = "return new double[inputs.length][" + outputsNumber() + "];";
	@Property(name="Source name")
	protected String name = "Derived1";

	protected ScatterplotSource orig;
	protected GroovyShell shell;
	protected Script parsedResponseScript;
	
	public DerivedGroovyModelSource(ScatterplotSource orig) {
		this.orig = orig;
		shell = new GroovyShell(ClassLoader.getSystemClassLoader());
		shell.setVariable("source", orig);
		update();
	}
	
	protected void update() {
		parsedResponseScript = shell.parse(responseScript.toString());
		setChanged();
		notifyObservers();
	}
	
	@Override
	public double[][] getModelResponses(double[][] inputs) {
		shell.setVariable("inputs", inputs);
		return (double[][]) parsedResponseScript.run();
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

	public String getResponseScript() {
		return responseScript;
	}

	public void setResponseScript(String responseScript) {
		this.responseScript = responseScript;
		update();
	}

	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers();
	}
}
