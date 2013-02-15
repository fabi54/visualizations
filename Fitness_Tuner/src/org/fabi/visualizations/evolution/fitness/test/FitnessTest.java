/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness.test;

import java.util.List;

import org.fabi.visualizations.Global;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.fitness.VisualizationFitnessFunction;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.MockChromosomeProvider;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.Rule;

/**
 *
 * @author janf
 */
public class FitnessTest {
    
    public static void main(String[] args) {

		Global.getInstance().init();
    	
        List<Rule> rules = MockChromosomeProvider.getRules();
        FitnessFunction fitness = new VisualizationFitnessFunction();
        int satisfiedCnt = 0;
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Running tests...");
        System.out.println("--------------------------------------------------------------------------------");
        for (Rule rule : rules) {
            boolean satisfied = rule.isSatisfied(fitness);
            if (satisfied) {
                satisfiedCnt++;
            }
            System.out.println((satisfied ? "ok   " : "fail ") + "... \"" + rule.getName() + "\"");
            System.out.println("              [" + fitness.getFitness(rule.getBetter())
            		+ (satisfied ? (rule.isSharp() ? "  >  " : "  => ") : (rule.isSharp() ? "  !> " : " !=> ")) + fitness.getFitness(rule.getWorse()) + "]");
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Satisfied: " + (100 * satisfiedCnt / rules.size()) + "%.");
        System.out.println("--------------------------------------------------------------------------------");
    }
}
