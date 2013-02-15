package org.fabi.visualizations.evolution;

public class AsymetricCrossoverEvolutionStrategy implements EvolutionStrategy {

    public int elitismSize = 0;
    public double mutationProbability = 0.01;
    public double selectionPressure = 0.8;
	
    public AsymetricCrossoverEvolutionStrategy() {}
    
    public AsymetricCrossoverEvolutionStrategy(int elitismSize, double mutationProbability, double selectionPressure) {
    	this.elitismSize = elitismSize;
    	this.mutationProbability = mutationProbability;
    	this.selectionPressure = selectionPressure;
    }
    
	@Override
	public Population getNextGeneration(Population orig) {
        int size = orig.size();
        Chromosome[] chromosomes = new Chromosome[size];
        int i = 0;
        for (; i < elitismSize; i++) {
            chromosomes[i] = orig.getIth(i).copy();
        }
        for (; i < size; i++) {
            chromosomes[i] = orig.getIth(select(size)).copy();
        }
        for (i = 0; i < size - 1; i += 2) {
            chromosomes[i].cross(chromosomes[i + 1]);
        }
        for (i = 0; i < size; i++) {
            chromosomes[i].mutate(mutationProbability);
        }
        orig.setChromosomes(chromosomes);
        return orig;
	}
	
	protected int select(int max) {
		return (int) Math.floor(
			Math.log(
				Math.random() * (
					Math.pow(selectionPressure, max) - 1
				) + 1
			) / Math.log(selectionPressure)
		);
	}

}
