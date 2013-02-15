package org.fabi.visualizations.evolution;

public interface Population extends Iterable<Chromosome> {
	
	public int size();
	
	public Chromosome getIth(int index);
	
	public Chromosome getBest();
	
	public Chromosome[] toArray();
	
	public void setChromosomes(Chromosome[] chromosomes);
}
