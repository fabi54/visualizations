package org.fabi.visualizations.scatter2.sources;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ModelSource;

// TODO transformation
public class ExtendableScatterplotSource extends Observable implements ScatterplotSource, Observer {

	protected ScatterplotSource orig;
	
	protected List<DataSource> addedData;
	protected List<ModelSource> addedModels;
	
	protected static final int INPUT_SCRIPT = 0;
	protected static final int OUTPUT_SCRIPT = 1;
	
	public ExtendableScatterplotSource(ScatterplotSource orig) {
		this.orig = orig;
		if (orig instanceof Observable) {
			((Observable) orig).addObserver(this);
		}
		addedData = new ArrayList<DataSource>();
		addedModels = new ArrayList<ModelSource>();
	}
	
	public ScatterplotSource getOrigSource() {
		return orig;
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

/* MODIFYING SOURCES *********************************************************/ 

	public void addDataSource(DataSource source) {
		addedData.add(source);
		if (source instanceof Observable) {
			((Observable) source).addObserver(this);
		}
		setChanged();
		notifyObservers();
	}
	
	public void removeDataSource(int index) {
		addedData.remove(index);
		setChanged();
		notifyObservers();
	}

	public void addModelSource(ModelSource source) {
		addedModels.add(source);
		if (source instanceof Observable) {
			((Observable) source).addObserver(this);
		}
		setChanged();
		notifyObservers();
	}
	
	public void removeModelSource(int index) {
		addedModels.remove(index);
		setChanged();
		notifyObservers();
	}
	
/* BASIC ScatterplotSource METHODS *******************************************/ 
	
	@Override
	public int getDataSourceCount() {
		return orig.getDataSourceCount() + addedData.size(); 
	}
	
	@Override
	public int getModelSourceCount() {
		return orig.getModelSourceCount() + addedModels.size();
	}
	
	public int getEditableDataSourceCount() {
		return addedData.size();
	}
	
	public int getEditableModelSourceCount() {
		return addedModels.size();
	}
	
	@Override
	public int getInputsNumber() {
		return orig.getInputsNumber();
	}
	
	@Override
	public int getOutputsNumber() {
		return orig.getOutputsNumber();
	}
	
	@Override
	public DataSource getDataSource(int index) {
		int origCnt = orig.getDataSourceCount();
		if (index < origCnt) {
			return orig.getDataSource(index);
		} else {
			index -= origCnt;
			return addedData.get(index);
		}
	}
	
	@Override
	public ModelSource getModelSource(int index) {
		int origCnt = orig.getModelSourceCount();
		if (index < origCnt) {
			return orig.getModelSource(index);
		} else {
			index -= origCnt;
			return addedModels.get(index);
		}	
	}
	
	@Override
	public Metadata getMetadata() {
		return orig.getMetadata();
	}
}
