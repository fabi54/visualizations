package org.fabi.visualizations.rapidminer.scatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fabi.visualizations.scatter.sources.DataSource;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.BinominalAttribute;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.ExampleTable;
import com.rapidminer.example.table.NominalAttribute;
import com.rapidminer.example.table.NominalMapping;
import com.rapidminer.example.table.PolynominalAttribute;

public class RapidMinerDataSource implements DataSource {

	double[][] inputs;
	double[][] outputs;
	int iNumber;
	int oNumber;
	String name;
	
	public RapidMinerDataSource(ExampleSet data) {
		name = data.getName();
		Attributes attrs = data.getAttributes();
		List<Attribute> listAttr = new ArrayList<Attribute>();
		ExampleTable table = data.getExampleTable();
		int size = table.size();
		
		Attribute[] attributes = attrs.createRegularAttributeArray();
		iNumber = attributes.length;
		for (Attribute a : attributes) {
			listAttr.add(a);
		}
		
		Attribute label = attrs.getLabel();
		if (label != null) {
			if (label instanceof PolynominalAttribute) {
				NominalMapping map = ((PolynominalAttribute) label).getMapping();
				oNumber = map.size();
			} else if (label instanceof BinominalAttribute) {
				NominalMapping map = ((BinominalAttribute) label).getMapping();
				oNumber = map.size();
			} else /*if (label.isNumerical())*/ {
				oNumber++;
			}
		}	 
		 
		Iterator<Example> originalReader = data.iterator();
		inputs = new double[size][iNumber];
		outputs = new double[size][oNumber];

		for (int i = 0; originalReader.hasNext(); i++) {
			Example example = originalReader.next();
			DataRow row = example.getDataRow();
            
            for (int j = 0; j < iNumber; j++) {
            	inputs[i][j] = row.get(listAttr.get(j));
            }
            
            if (label instanceof NominalAttribute) { // NominalAttribute = {Polynominal|Binominal}Attribute
            	int value = (int) row.get(label);
            	outputs[i][value] = 1;
            } else {
            	outputs[i][0] = row.get(label);
            }
		} 
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
