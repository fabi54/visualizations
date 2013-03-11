/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.MockVisualizationChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;


/**
 * History:
 * 
 * I. return s0 = x_axis_range_length
 * 		-> 12% satisfied
 * II. return s1 = s0 * y_axis_range_length
 * 		-> 51% satisfied
 * III. return s2 = s1 * (sum_forall_modelevaluations(highest_prediction - lowest_prediction) / modelevaluations_cnt)
 * 		-> 67% satisfied
 * IV. return s3 = s1 * (sum_forall_modelevaluations(me_i) / modelevaluations_cnt)
 *     me_i = { begin = 0; end = modelevaluations_i.length;
 *     			while(begin != end) {
 *     				me_i += abs(/end/-highest_prediction - /begin/-highest_prediction);
 *     				in-/de-crement begin/end, where the prediction difference is lower
 *     			}
 *     		  }
 *     -> 74% satisfied
 * V. return s4 = s3 * (sum_forall_modelevaluations(abs(avg_evaluation_i - avg_evaluation_{i-1})) / modelevaluations_cnt)
 *	   -> 87% satisfied
 *
 * // NaN outliers added (2 rules)
 * 	   -> 81% satisfied
 *
 * VI. new interestingness computation model sum of (trend (+/-) * length)
 *     -> 84% satisfied (e.g. "shorter sine peak only better than longer partially sine peak, partially constant" solved)
 *     
 * VII. some rules set flat
 */
public class VisualizationFitnessFunction extends ScatterplotChromosomeFitnessFunction {
	
	public VisualizationFitnessFunction() {
		super(new ModelSource[0], new DataSource() {
			@Override public int outputsNumber() { return 0; }
			@Override public int inputsNumber() { return 0; }
			@Override public double[][] getOutputDataVectors() { return new double[0][0]; }
			@Override public double[][] getInputDataVectors() { return new double[0][0]; }
			@Override public String getName() { return ""; }
		});
	}

	
	protected ModelSource[] getSource(Chromosome chrmsm) {
		return ((MockVisualizationChromosome) chrmsm).models();
	}
}
