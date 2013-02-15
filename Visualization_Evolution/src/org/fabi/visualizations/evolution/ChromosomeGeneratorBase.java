package org.fabi.visualizations.evolution;

public abstract class ChromosomeGeneratorBase implements ChromosomeGenerator {

	@Override
	public Chromosome[] generate(int size) {
		Chromosome[] res = new Chromosome[size];
		for (int i = 0; i < res.length; i++) {
			res[i] = generate();
		}
		return res;
	}
}
