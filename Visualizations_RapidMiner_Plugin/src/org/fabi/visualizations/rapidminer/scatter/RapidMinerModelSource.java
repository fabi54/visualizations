package org.fabi.visualizations.rapidminer.scatter;

import java.util.Iterator;

import org.fabi.visualizations.scatter.sources.ModelSource;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.NominalMapping;
import com.rapidminer.example.table.PolynominalAttribute;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;

public class RapidMinerModelSource implements ModelSource {
	
	protected PredictionModel model;
	protected MemoryExampleTable table;
	protected ExampleSet referenceSet;
	protected Attribute label;
	protected Double[] valueArray;
	protected int oNumber;
	protected int iNumber;
	protected String name;
	
	public RapidMinerModelSource(PredictionModel model) {
		this(model, model.getTrainingHeader());
	}
	
	public RapidMinerModelSource(PredictionModel model, ExampleSet set) {
		this.model = model;
		name = model.getName();
		Attributes attributes = set.getAttributes();
		Attribute[] regular = attributes.createRegularAttributeArray();
		table = new MemoryExampleTable(regular);
		referenceSet = table.createExampleSet();
		iNumber = regular.length;
		label = attributes.getLabel();
		if (label != null) {
			if (label instanceof PolynominalAttribute) {
				NominalMapping map = ((PolynominalAttribute) label).getMapping();
				oNumber = map.size();
			} else if (label.isNumerical()) {
				oNumber++;
			}
		} 
	}

	@Override
	public double[][] getModelResponses(double[][] inputs) {
		try {
			table.clear();
			for (int i = 0; i < inputs.length; i++) {
				DataRow actRow = new DoubleArrayDataRow(inputs[i]);
				table.addDataRow(actRow);
			}
			model.apply(referenceSet);
			Iterator<Example> originalReader = referenceSet.iterator();
			NominalMapping nominalMapping = null;
			if (label instanceof PolynominalAttribute) {
				nominalMapping = ((PolynominalAttribute) label).getMapping();
			}
			double[][] result = new double[inputs.length][oNumber];

			for (int i = 0; originalReader.hasNext(); i++) {
				Example example = originalReader.next();
				if (label instanceof PolynominalAttribute) {
					for (int j = 0; j < nominalMapping.size(); j++) {
						result[i][j] = example.getConfidence(nominalMapping.mapIndex(j));
					}
	            } else {
	            	result[i][0] = example.getPredictedLabel();
	            }
			} 
			return result;
		} catch (OperatorException e) {
			throw new RuntimeException("OperatorException: " + e.getMessage());
		}
		
	}

	@Override
	public int inputsNumber() {
		return iNumber;
	}

	@Override
	public int outputsNumber() {
		return oNumber;
	}

	@Override
	public String getName() {
		return name;
	}

}
