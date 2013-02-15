/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

/**
 *
 * @author janf
 */
public class ConstantFunction extends AbstractFunctionBase {

    protected double c;
    
    public ConstantFunction(double c) {
        this.c = c;
    }
    
    @Override
    public double getValue(double x) {
        return c;
    }

}
