package org.fabi.visualizations.evolution.scatterplot.modelling.evolution;

import java.util.Arrays;
import java.util.List;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.Random;
import org.fabi.visualizations.evolution.scatterplot.modelling.ModelPool;

import configuration.CfgTemplate;


public class ModelGroupChromosome extends ChromosomeBase {

	protected int[] indices;
	
	protected static CfgTemplate[] templates;
	
	static {
		List<CfgTemplate> t = ModelPool.getTemplates();
		templates = new CfgTemplate[t.size()];
		int i = 0;
		for (CfgTemplate cfg : t) {
			templates[i++] = cfg;
		}
	}
	
	public ModelGroupChromosome(int size, FitnessFunction fitness) {
		setFitnessFunction(fitness);
		indices = new int[size];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = Random.getInstance().nextInt(templates.length);
		}
	}
	
	@Override
	public void mutate(double probability) {
		for (int i = 0; i < indices.length; i++) {
			if (Random.getInstance().nextDouble() < probability) {
				indices[i] = Random.getInstance().nextInt(templates.length);
			}
		}
	}

	@Override
	public void cross(Chromosome other) {
		if (!(other instanceof ModelGroupChromosome)) {
			throw new CrossoverException();
		}
		ModelGroupChromosome c = (ModelGroupChromosome) other;
		if (c.indices.length != indices.length) {
			throw new CrossoverException();
		}
		int bck;
		for (int i = 0; i < indices.length; i++) {
			if (Random.getInstance().nextDouble() < 0.5) {
				bck = indices[i];
				indices[i] = c.indices[i];
				c.indices[i] = bck;
			}
		}
	}
	
	@Override
	public String toString() {
		return Arrays.toString(indices);
	}

	@Override
	public Object getPhenotype() {
		CfgTemplate[] res = new CfgTemplate[indices.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = templates[indices[i]];
		}
		return res;
	}

	@Override
	public Chromosome copy() {
		ModelGroupChromosome c = new ModelGroupChromosome(indices.length, fitnessFunction);
		System.arraycopy(indices, 0, c.indices, 0, indices.length);
		return c;
	}

	@Override
	public int compareTo(Chromosome arg0) {
		if (!(arg0 instanceof ModelGroupChromosome)) {
			return Integer.MAX_VALUE;
		}
		ModelGroupChromosome c = (ModelGroupChromosome) arg0;
		int res = 0;
		for (int i = 0; i < indices.length; i++) {
			res += Math.abs(indices[i] - c.indices[i]);
		}
		return res;
	}
	
}