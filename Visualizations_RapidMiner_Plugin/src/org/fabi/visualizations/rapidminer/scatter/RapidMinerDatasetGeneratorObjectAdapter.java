package org.fabi.visualizations.rapidminer.scatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fabi.visualizations.rapidminer.renderers.ReportableContainer;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;
import com.rapidminer.operator.learner.PredictionModel;

public class RapidMinerDatasetGeneratorObjectAdapter extends ResultObjectAdapter implements ReportableContainer {
	private static final long serialVersionUID = 8242951136357182454L;
	
	protected ScatterplotSource source;
	protected PredictionModel model;
	protected ExampleSet data;
	
	public RapidMinerDatasetGeneratorObjectAdapter(PredictionModel model, ExampleSet data) {
		this.source = new ScatterplotSourceBase(new DataSource[]{new RapidMinerDataSource(data)},
				new ModelSource[]{new RapidMinerModelSource(model, data)},
				new RapidMinerMetadata(data));
		this.model = model;
		this.data = data;
	}
	
	public ScatterplotSource getScatterplotSource() {
		return source;
	}

	@Override
	public Collection<IOObject> getOtherReportables() {
		List<IOObject> reportables = new ArrayList<IOObject>(2);
		reportables.add(model);
		reportables.add(data);
		return reportables;
	}
}
