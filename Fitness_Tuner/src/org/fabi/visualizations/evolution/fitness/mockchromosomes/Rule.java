/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.mockchromosomes;

import org.fabi.visualizations.evolution.FitnessFunction;

/**
 *
 * @author janf
 */
public class Rule {
    protected MockVisualizationChromosome better;
    protected MockVisualizationChromosome worse;
    protected boolean sharp;
    protected String name;
    
    public Rule(MockVisualizationChromosome better, MockVisualizationChromosome worse, String name) {
        this(better, worse, name, true);
    }
    
    public Rule(MockVisualizationChromosome better, MockVisualizationChromosome worse, String name, boolean sharp) {
        this.worse = worse;
        this.better = better;
        this.name = name;
        this.sharp = sharp;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isSharp() {
    	return sharp;
    }
    
    public boolean isSatisfied(FitnessFunction function) {
        return sharp ? (function.getFitness(worse) < function.getFitness(better)) : (function.getFitness(worse) <= function.getFitness(better));
    }
    
    public MockVisualizationChromosome getBetter() {
    	return better;
    }
    
    public MockVisualizationChromosome getWorse() {
    	return worse;
    }
}
