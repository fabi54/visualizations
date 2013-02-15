package org.fabi.visualizations.evolution;

public interface Chromosome extends Comparable<Chromosome> {
	void mutate(double probability);
	void cross(Chromosome other);
	public double getFitness();
	public Object getPhenotype();
	public Chromosome copy();
	
	public static class CrossoverException extends RuntimeException {

		private static final long serialVersionUID = 6601412861955605871L;
		
		public CrossoverException() { super(); }
		public CrossoverException(String message) { super(message); }
		
	}
}
