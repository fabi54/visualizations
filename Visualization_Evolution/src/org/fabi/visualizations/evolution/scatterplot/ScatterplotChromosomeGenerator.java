package org.fabi.visualizations.evolution.scatterplot;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeGeneratorBase;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.Random;
//import org.fabi.visualizations.tools.math.Arrays;

public class ScatterplotChromosomeGenerator extends ChromosomeGeneratorBase {

	protected double[][] inputs;
	protected boolean singleInput;
	protected int[] indices;
	protected FitnessFunction fitness;
	protected ScatterplotChromosomeBoundsHolder bounds;
	
	public ScatterplotChromosomeGenerator(FitnessFunction fitness, double[][] inputs, boolean singleInput, int[] indices) {
		if (indices != null && indices.length != (singleInput ? 1 : 2)) {
			throw new IllegalArgumentException();
		}
		this.inputs = inputs;
		this.singleInput = singleInput;
		this.indices = indices;
		this.fitness = fitness;
		this.bounds = new ScatterplotChromosomeBoundsHolder(inputs);
	}
	
	@Override
	public Chromosome generate() {
		int index = Random.getInstance().nextInt(inputs.length);
		boolean fixedIndices = (indices != null);
		int[] cIndices = new int[singleInput ? 1 : 2];
		double[] starts = new double[singleInput ? 1 : 2];
		double[] lengths = new double[singleInput ? 1 : 2];
		double[] others = new double[inputs[index].length];
		if (fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
			cIndices[i] = indices[i];
			}
		} else {
			cIndices[0] = Random.getInstance().nextInt(inputs[index].length);
			if (!singleInput) {
				cIndices[1] = Random.getInstance().nextInt(inputs[index].length - 1);
				if (cIndices[1] >= cIndices[0]) {
					cIndices[1]++;
				}
			}
		}
		for (int i = 0; i < starts.length; i++) {
			starts[i] = Random.getInstance().nextDouble();
			lengths[i] = Random.getInstance().nextDouble() * (1 - starts[i]);
		}
		System.arraycopy(inputs[index], 0, others, 0, others.length);
		return new ScatterplotChromosome(fitness, cIndices, starts, lengths, others, fixedIndices, bounds);
	}

	@Override
	public Chromosome[] generate(int size) {
		Chromosome[] res = new Chromosome[size];
		res[0] = generateLarge();
		for (int i = 1; i < res.length; i++) {
			res[i] = generate();
		}
		return res;
	}
	
	private Chromosome generateLarge() {
		int index = Random.getInstance().nextInt(inputs.length);
		boolean fixedIndices = (indices != null);
		int[] cIndices = new int[singleInput ? 1 : 2];
		double[] starts = new double[singleInput ? 1 : 2];
		double[] lengths = new double[singleInput ? 1 : 2];
		double[] others = new double[inputs[index].length];
		if (fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
			cIndices[i] = indices[i];
			}
		} else {
			cIndices[0] = Random.getInstance().nextInt(inputs[index].length);
			if (!singleInput) {
				cIndices[1] = Random.getInstance().nextInt(inputs[index].length - 1);
				if (cIndices[1] >= cIndices[0]) {
					cIndices[1]++;
				}
			}
		}
		for (int i = 0; i < starts.length; i++) {
			lengths[i] = 1.0;
		}
		System.arraycopy(inputs[index], 0, others, 0, others.length);
		return new ScatterplotChromosome(fitness, cIndices, starts, lengths, others, fixedIndices, bounds);
	}
}
