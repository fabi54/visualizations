/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;
import org.fabi.visualizations.scatter_old.sources.MultiModelSource;
import org.fabi.visualizations.tools.math.ArithmeticAverage;
import org.fabi.visualizations.tools.math.ManyToOne;
import org.fabi.visualizations.tools.math.Maximum;
import org.fabi.visualizations.tools.math.Minimum;

/**
 *
 * @author janf
 */
public class MockVisualizationChromosome extends ChromosomeBase implements MultiModelSource {

    protected double begin;
    protected double end;
    protected AbstractFunction[] models;

    protected ManyToOne mto;
    
    protected static int MODEL_PRECISION = 50;

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
	@Override public String getName() { return ""; }
    
    @Override
    public Object getPhenotype() {
        VisualizationConfig cfg = new VisualizationConfig(ScatterplotVisualization.class);
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
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER, begin);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER, end);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER, min);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER, max);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_DATA_VISIBLE, false);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, false);
        //cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_LEGEND_VISIBLE, false);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_DISPLAY_MULTIPLE_MODELS, true);
        cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_OUTPUT_PRECISION, MODEL_PRECISION);
        return cfg;
    }

    @Override
    public void setCountMethod(ManyToOne mto) {
        this.mto = mto;
    }

    @Override
    public double[][] getModelResponses(int i, double[][] doubles) {
        return models[i].getModelResponses(doubles);
    }

    @Override
    public double[][] getMaxResponses(double[][] doubles) {
        ManyToOne bck = mto;
        setCountMethod(new Maximum());
        double[][] res = getModelResponses(doubles);
        mto = bck;
        return res;
    }

    @Override
    public double[][] getMinResponses(double[][] doubles) {
        ManyToOne bck = mto;
        setCountMethod(new Minimum());
        double[][] res = getModelResponses(doubles);
        mto = bck;
        return res;
    }

    @Override
    public int getModelCount() {
        return models.length;
    }

    @Override
    public ModelSource getModel(int i) {
        return models[i];
    }

    @Override
    public double[][] getModelResponses(double[][] doubles) {
        double[][][] responses = new double[models.length][][];
        for (int i = 0; i < models.length; i++) {
            responses[i] = models[i].getModelResponses(doubles);
        }
        double[][] res = new double[doubles.length][1];
        for (int i = 0; i < res.length; i++) {
            double[] tmp = new double[models.length];
            for (int j = 0; j < models.length; j++) {
                tmp[j] = responses[j][i][0];
            }
            res[i][0] = mto.getResult(tmp);
        }
        return res;
    }

    @Override
    public int inputsNumber() {
        return 1;
    }

    @Override
    public int outputsNumber() {
        return 1;
    }

	@Override
	public ModelSource[] getModels() {
		return models;
	}

    
}
