package org.fabi.visualizations.rapidminer.scatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fabi.visualizations.rapidminer.renderers.ReportableContainer;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.modgen.rapidminer.modelling.ConfidenceModel;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;
import com.rapidminer.operator.learner.PredictionModel;

public class RapidMinerDatasetGeneratorObjectAdapter extends ResultObjectAdapter implements ReportableContainer {
	private static final long serialVersionUID = 8242951136357182454L;
	
	protected DatasetGenerator generator;
	protected PredictionModel model;
	protected ExampleSet data;
	
	public RapidMinerDatasetGeneratorObjectAdapter(PredictionModel model, ExampleSet data) {
		this.generator = new DatasetGenerator(
				model instanceof ConfidenceModel ?
						new ConfidenceModelMultiModelSource((ConfidenceModel) model) :
						new RapidMinerModelSource(model, data), new RapidMinerDataSource(data), new RapidMinerMetadata(data));
		this.model = model;
		this.data = data;
	}
	
	public DatasetGenerator getDatasetGenerator() {
		return generator;
	}

	@Override
	public Collection<IOObject> getOtherReportables() {
		List<IOObject> reportables = new ArrayList<IOObject>(2);
		reportables.add(model);
		reportables.add(data);
		return reportables;
	}
}
