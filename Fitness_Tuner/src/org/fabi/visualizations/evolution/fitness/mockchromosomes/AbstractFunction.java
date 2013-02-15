/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import org.fabi.visualizations.scatter.sources.ModelSource;

/**
 *
 * @author janf
 */
public interface AbstractFunction extends ModelSource {
    
    public double getValue(double x);
    public double[] getValue(double[] x);
}
