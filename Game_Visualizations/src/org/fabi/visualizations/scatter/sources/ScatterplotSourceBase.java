package org.fabi.visualizations.scatter.sources;

import java.util.Observable;
import java.util.Observer;


public class ScatterplotSourceBase extends Observable implements ScatterplotSource, Observer {

	public static final int UNDEFINED_ATTR_CNT = -1;
	
	protected DataSource[] data;
	protected ModelSource[] models;
	protected Metadata metadata;
	protected int inputsNr;
	protected int outputsNr;
	
	public ScatterplotSourceBase(DataSource[] sources) {
		this(sources, null, null);
	}
	
	public ScatterplotSourceBase(DataSource[] sources, Metadata metadata) {
		this(sources, null, metadata);
	}

	public ScatterplotSourceBase(ModelSource[] sources) {
		this(null, sources, null);
	}
	
	public ScatterplotSourceBase(ModelSource[] sources, Metadata metadata) {
		this(null, sources, metadata);
	}
	
	public ScatterplotSourceBase(DataSource[] data, ModelSource[] models) {
		this(data, models, null);
	}
	
	public ScatterplotSourceBase(DataSource[] data, ModelSource[] models, Metadata metadata) {
		if (data != null) {
			this.data = data;
		} else {
			this.data = new DataSource[0];
		}
		if (models != null) {
			this.models = models;
		} else {
			this.models = new ModelSource[0];
		}
		this.metadata = metadata;
		inputsNr = UNDEFINED_ATTR_CNT;
		outputsNr = UNDEFINED_ATTR_CNT;
		/* Inputs and outputs number check: */
		if (this.data.length > 0) {
			inputsNr = this.data[0].inputsNumber();
			outputsNr = this.data[0].outputsNumber();
			for (int i = 1; i < this.data.length; i++) {
				if (this.data[i].inputsNumber() != inputsNr) {
					inputsNr = UNDEFINED_ATTR_CNT;
				}
				if (this.data[i].outputsNumber() != outputsNr) {
					outputsNr = UNDEFINED_ATTR_CNT;
				}
			}
		}
		if (this.models.length > 0) {
			int i = 0;
			if (this.data.length == 0) {
				inputsNr = this.models[0].inputsNumber();
				outputsNr = this.models[0].outputsNumber();
				i++;
			}
			for (; i < this.models.length; i++) {
				if (this.models[i].inputsNumber() != inputsNr) {
					inputsNr = UNDEFINED_ATTR_CNT;
				}
				if (this.models[i].outputsNumber() != outputsNr) {
					outputsNr = UNDEFINED_ATTR_CNT;
				}
			}
		}
		for (int i = 0; i < this.data.length; i++) {
			if (this.data[i] instanceof Observable) {
				((Observable) this.data[i]).addObserver(this);
			}
		}
		for (int i = 0; i < this.models.length; i++) {
			if (this.models[i] instanceof Observable) {
				((Observable)this.models[i]).addObserver(this);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(arg);
	}
	
	@Override
	public int getDataSourceCount() {
		return data.length;
	}

	@Override
	public int getModelSourceCount() {
		return models.length;
	}

	@Override
	public int getInputsNumber() {
		return inputsNr;
	}

	@Override
	public int getOutputsNumber() {
		return outputsNr;
	}

	@Override
	public DataSource getDataSource(int index) {
		return data[index];
	}

	@Override
	public ModelSource getModelSource(int index) {
		return models[index];
	}

	@Override
	public Metadata getMetadata() {
		return metadata;
	}

}
