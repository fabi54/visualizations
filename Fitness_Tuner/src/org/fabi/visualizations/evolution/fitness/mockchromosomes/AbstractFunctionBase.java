/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

/**
 *
 * @author janf
 */
public abstract class AbstractFunctionBase implements AbstractFunction {
    @Override
    public double[] getValue(double[] x) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = getValue(x[i]);
        }
        return res;
    }
    
    
    public double[][] getModelResponses(double[][] doubles) {
        if (doubles.length == 0) {
            return new double[0][2];
        }
        if (doubles[0].length != 1) {
            throw new IllegalArgumentException();
        }
        double[][] res = new double[doubles.length][1];
        for (int i = 0; i < doubles.length; i++) {
            res[i][0] = getValue(doubles[i][0]);
        }
        return res;
    }

    public int inputsNumber() {
        return 1;
    }

    public int outputsNumber() {
        return 1;
    }
    
	@Override public String getName() { return ""; }
}
