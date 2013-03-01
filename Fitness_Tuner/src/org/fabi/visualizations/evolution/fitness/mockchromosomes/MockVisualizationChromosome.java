/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.tools.math.ArithmeticAverage;
import org.fabi.visualizations.tools.math.ManyToOne;

/**
 *
 * @author janf
 */
public class MockVisualizationChromosome extends ChromosomeBase {

    protected double begin;
    protected double end;
    protected AbstractFunction[] models;

    protected ManyToOne mto;
    
    protected static int MODEL_PRECISION = 50;

    public ModelSource[] models() {
    	ModelSource[] res = new ModelSource[models.length];
    	for (int i = 0; i < res.length; i++) {
    		res[i] = models[i];
    	}
    	return res;
    }
    
    public MockVisualizationChromosome(double begin, double end, AbstractFunction[] models) {
        this.begin = begin;
        this.end = end;
        this.models = new AbstractFunction[models.length];
        System.arraycopy(models, 0, this.models, 0, models.length);
        this.mto = new ArithmeticAverage();
    }
    
    @Override public void mutate(double d) { throw new UnsupportedOperationException("Not supported.");}
    @Override public void cross(Chromosome chrmsm) { throw new UnsupportedOperationException("Not supported.");}
    @Override public Chromosome copy() { throw new UnsupportedOperationException("Not supported.");}
    @Override public int compareTo(Chromosome t) { throw new UnsupportedOperationException("Not supported.");}
    
    @Override
    public ScatterplotVisualization getPhenotype() {
    	ScatterplotVisualization vis = new ScatterplotVisualization();
        double[][] inputs = new double[MODEL_PRECISION][1];
        double d = begin;
        double step = (end - begin) / MODEL_PRECISION;
        for (int i = 0; i < inputs.length; i++) {
        	inputs[i][0] = d;
        	d += step;
        }
        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < models.length; i++) {
        	double[][] res = getModelResponses(i, inputs);
        	for (int j = 0; j < MODEL_PRECISION; j++) {
        		if (!Double.isNaN(res[j][0])) {
	        		min = Math.min(min, res[j][0]);
	        		max = Math.max(max, res[j][0]);
        		}
        	}
        }
        vis.setxAxisRangeLower(begin);
        vis.setxAxisRangeUpper(end);
        vis.setyAxisRangeLower(min);
        vis.setyAxisRangeUpper(max);
        vis.setInputsSetting(new double[]{0});
        vis.setOutputPrecision(MODEL_PRECISION);
        return vis;
    }
    
    public double[][] getModelResponses(int i, double[][] inputs) {
    	return models[i].getModelResponses(inputs);
    }

    
}
