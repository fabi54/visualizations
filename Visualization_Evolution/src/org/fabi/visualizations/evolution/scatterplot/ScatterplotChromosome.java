package org.fabi.visualizations.evolution.scatterplot;

// TODO model preparation

import java.util.Arrays;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.Random;
import org.fabi.visualizations.scatter.ScatterplotVisualization;

public class ScatterplotChromosome extends ChromosomeBase {
	
	protected int[] indices;
	protected double[] starts;
	protected double[] lengths;
	protected double[] others;
	protected boolean fixedIndices;
	protected ScatterplotChromosomeBoundsHolder bounds;
		
	public ScatterplotChromosome(FitnessFunction fitness,
			int[] indices,
			double[] starts,
			double[] lengths,
			double[] others,
			boolean fixedIndices,
			ScatterplotChromosomeBoundsHolder bounds) {
		this.bounds = bounds;
		init(indices, starts, lengths, others, fixedIndices);
		setFitnessFunction(fitness);
	}
	
	protected ScatterplotChromosome(int[] indices,
			double[] starts,
			double[] lengths,
			double[] others,
			boolean fixedIndices,
			ScatterplotChromosomeBoundsHolder bounds) {
		this.bounds = bounds;
		init(indices, starts, lengths, others, fixedIndices);
	}
	
	protected void init(int[] indices,
			double[] starts,
			double[] lengths,
			double[] others,
			boolean fixedIndices) {
		this.indices = indices;
		this.starts = starts;
		this.lengths = lengths;
		this.others = others;
		this.fixedIndices = fixedIndices;;
	}
	
	@Override
	public int compareTo(Chromosome o) {
		if (!(o instanceof ScatterplotChromosome)) {
			return Integer.MIN_VALUE;
		}
		ScatterplotChromosome ch = (ScatterplotChromosome) o;
		if (ch.indices.length != indices.length || ch.others.length != others.length) {
			return Integer.MIN_VALUE;
		}
		int result = 0;
		int t = 1;
		if (!fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
				if (indices[i] > ch.indices[i]) {
					result += t;
				}
				t *= 2;
			}
		}
		for (int i = 0; i < starts.length; i++) {
			if (starts[i] > ch.starts[i]) {
				result += t;
			}
			t *= 2;
		}
		for (int i = 0; i < lengths.length; i++) {
			if (lengths[i] > ch.lengths[i]) {
				result += t;
			}
			t *= 2;
		}
		for (int i = 0; i < others.length; i++) {
			if (others[i] > ch.others[i]) {
				result += t;
			}
			t *= 2;
		}
		return result;
	}
	
	public double weightedDistanceTo(ScatterplotChromosome o) {
		if (o.indices.length != indices.length || o.others.length != others.length) {
			return Double.MAX_VALUE;
		}
		double result = 0.0;
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] != o.indices[i]) {
				result += 1.0;
			}
		}
		for (int i = 0; i < starts.length; i++) {
			result += dist(starts[i], o.starts[i]);
		}
		for (int i = 0; i < lengths.length; i++) {
			result += dist(lengths[i], o.lengths[i]);
		}
		for (int i = 0; i < others.length; i++) {
			boolean cmp = true;
			for (int j = 0; j < indices.length; j++) {
				cmp &= (indices[j] != i) && (o.indices[j] != i);
			}
			if (cmp) {
				result += dist(others[i], o.others[i]);
			}
		}
		return result;
	}
	
	protected double dist(double one, double two) {
		double res = one - two;
		if (res == 0) {
			return 0;
		}
		else {
			res = Math.abs(res / Math.max(Math.max(one, two), Math.abs(one - two)));
			assert(res >= 0 && res <= 1);
			if (res < 0 || res > 1) {
				System.out.println("!!! " + res + "= dist(" + one + "," + two + ")");
			}
			return res;
		}
	}

	@Override
	public void mutate(double probability) {
		boolean mutated = false;
		if (!fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
				if (Random.getInstance().nextDouble() < probability) {
					indices[i] = mutation(others.length - i);
					mutated = true;
				}
			}
		}
		for (int i = 0; i < starts.length; i++) {
			if (Random.getInstance().nextDouble() < probability) {
				starts[i] = mutation(starts[i]);
				//starts[i] = Random.getInstance().nextDouble(); 
				mutated = true;
			}
		}
		for (int i = 0; i < lengths.length; i++) {
			if (Random.getInstance().nextDouble() < probability) {
				lengths[i] = Math.abs(mutation(lengths[i]));
				//lengths[i] = Random.getInstance().nextDouble();
				mutated = true;
			}
		}
		for (int i = 0; i < others.length; i++) {
			if (Random.getInstance().nextDouble() < probability) {
				others[i] = mutation(others[i]);
				mutated = true;
			}
		}
		if (mutated) {
			resetFitness();
		}
	}
	
	// Creates two equal individuals (average crossover)
	@Override
	public void cross(Chromosome other) {
		if (!(other instanceof ScatterplotChromosome)) {
			throw new CrossoverException("Incompatible individuals.");
		}
		ScatterplotChromosome o = (ScatterplotChromosome) other;
		if (o.indices.length != indices.length || o.others.length != others.length) {
			throw new CrossoverException("Incompatible individuals.");
		}
		
		// uniform crossover
		/*if (!fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
				double sgn = Math.signum(Random.getInstance().nextGaussian());
				if (sgn > 0) {
					int tmp = indices[i];
					indices[i] = o.indices[i];
					o.indices[i] = tmp;
				}
			}
		}
		for (int i = 0; i < starts.length; i++) {
			double sgn = Math.signum(Random.getInstance().nextGaussian());
			if (sgn > 0) {
				double tmp = starts[i];
				starts[i] = o.starts[i];
				o.starts[i] = tmp;
			}
		}
		for (int i = 0; i < lengths.length; i++) {
			double sgn = Math.signum(Random.getInstance().nextGaussian());
			if (sgn > 0) {
				double tmp = lengths[i];
				lengths[i] = o.lengths[i];
				o.lengths[i] = tmp;
			}
		}

		for (int i = 0; i < others.length; i++) {
			double sgn = Math.signum(Random.getInstance().nextGaussian());
			if (sgn > 0) {
				double tmp = others[i];
				others[i] = o.others[i];
				o.others[i] = tmp;
			}
		}*/
		
		// average crossover
		if (!fixedIndices) {
			for (int i = 0; i < indices.length; i++) {
				indices[i] += o.indices[i];
				indices[i] /= 2;
				//o.indices[i] = indices[i];
			}
		}
		for (int i = 0; i < starts.length; i++) {
			starts[i] += o.starts[i];
			starts[i] /= 2;
			//o.starts[i] = starts[i];
		}
		for (int i = 0; i < lengths.length; i++) {
			lengths[i] += o.lengths[i];
			lengths[i] /= 2;
			//o.lengths[i] = lengths[i];
		}

		for (int i = 0; i < others.length; i++) {
			others[i] += o.others[i];
			others[i] /= 2;
			//o.others[i] = others[i];
		}
		resetFitness();
		o.resetFitness();
	}
	
	protected int mutation(int max) {
		return Random.getInstance().nextInt(max);
	}
	
	protected double mutation(double orig) {
		return orig * (Random.getInstance().nextGaussian() + 1) * Math.signum(Random.getInstance().nextGaussian())
				+ Random.getInstance().nextGaussian();
	}
	
	@Override
	public ScatterplotChromosome copy() {
		int[] nIndices = new int[indices.length];
		double[] nStarts = new double[starts.length];
		double[] nLengths = new double[lengths.length];
		double[] nOthers = new double[others.length];
		System.arraycopy(indices, 0, nIndices, 0, indices.length);
		System.arraycopy(starts, 0, nStarts, 0, starts.length);
		System.arraycopy(lengths, 0, nLengths, 0, lengths.length);
		System.arraycopy(others, 0, nOthers, 0, others.length);
		ScatterplotChromosome res = new ScatterplotChromosome(nIndices, nStarts, nLengths, nOthers, fixedIndices, bounds);
		res.fitness = this.fitness;
		res.fitnessFunction = this.fitnessFunction;
		return res;
	}

	@Override
	public Object getPhenotype() {
		ScatterplotVisualization visualization = new ScatterplotVisualization();
		double xLower = bounds.bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][indices[0]]
				+ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][indices[0]] * starts[0];
		boolean yAxisInput = indices.length == 2;
		visualization.setxAxisAttributeIndex(indices[0]);
		visualization.setyAxisAttributeIndex(yAxisInput ? indices[1] : ScatterplotVisualization.OUTPUT_AXIS);
		visualization.setxAxisRangeLower(xLower);
		visualization.setxAxisRangeUpper(xLower
				+ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][indices[0]] * lengths[0] * Math.abs(1 - starts[0]));
		if (yAxisInput) {
			double yLower = bounds.bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][indices[1]]
				+ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][indices[1]] * starts[1];
			visualization.setyAxisRangeLower(yLower);
			visualization.setyAxisRangeUpper(yLower
					+ bounds.bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][indices[1]] * lengths[1] * Math.abs(1 - starts[1]));
		}
		visualization.setInputsSetting(others);
		return visualization;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(indices)).append(Arrays.toString(starts))
				.append(Arrays.toString(lengths)).append(Arrays.toString(others));
		return sb.toString();
	}

}
