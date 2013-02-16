package org.modgen.rapidminer.data;

import game.data.AbstractGameData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class RapidGameData extends AbstractGameData {
	
	public RapidGameData(ExampleSet set) {
		Attributes attrs = set.getAttributes();
		List<Attribute> listAttr = new ArrayList<Attribute>();
		ExampleTable table = set.getExampleTable();
		int size = table.size();
		
		int iNumber = 0, oNumber = 0;
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
		 
		Iterator<Example> originalReader = set.iterator();
		List<double[]> iVectors = new ArrayList<double[]>(size);
		List<double[]> oVectors = new ArrayList<double[]>(size);

		while (originalReader.hasNext()) {
			Example example = originalReader.next();
			DataRow row = example.getDataRow();		

            double[] iVect = new double[iNumber];
			double[] oVect = new double[oNumber];
            
            for (int j = 0; j < iNumber; j++) {
            	iVect[j] = row.get(listAttr.get(j));
            }
            
            if (label != null) {
	            if (label instanceof NominalAttribute) { // NominalAttribute = {Polynominal|Binominal}Attribute
	            	int value = (int) row.get(label);
	            	oVect[value] = 1;
	            } else {
	            	oVect[0] = row.get(label);
	            }
            }
            iVectors.add(iVect);
            oVectors.add(oVect);
		} 
		
		initData(iVectors, oVectors);
	}
	

}