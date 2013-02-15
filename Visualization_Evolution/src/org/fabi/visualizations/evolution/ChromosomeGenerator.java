package org.fabi.visualizations.evolution;

public interface ChromosomeGenerator {
	Chromosome generate();
	Chromosome[] generate(int count);
}
