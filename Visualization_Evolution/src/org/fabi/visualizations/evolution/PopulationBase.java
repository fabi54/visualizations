package org.fabi.visualizations.evolution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PopulationBase implements Population {
	
	protected Chromosome[] chromosomes;
	
	/**
	 * May be destructive to "chromosomes".
	 */
	public PopulationBase(Chromosome[] chromosomes) {
		setChromosomes(chromosomes);
	}
	
	public PopulationBase(ChromosomeGenerator generator, int size) {
		setChromosomes(generator.generate(size));
	}
	
	@Override
	public Iterator<Chromosome> iterator() {
	    return new Iterator<Chromosome>() {
	        int idx = 0;

	        public boolean hasNext() {
	          return idx < chromosomes.length;
	        }

	        public Chromosome next() {
	          if (hasNext()) {
	            return chromosomes[idx++];
	          }
	          throw new NoSuchElementException();
	        }

	        public void remove() {
	          throw new UnsupportedOperationException();
	        }

	      };
	}
	
	public int size() {
		return chromosomes.length;
	}
	
	public Chromosome getBest() {
		return chromosomes[0];
	}

	@Override
	public Chromosome getIth(int index) {
		return chromosomes[index];
	}

	@Override
	public Chromosome[] toArray() {
		Chromosome[] array = new Chromosome[chromosomes.length];
		System.arraycopy(chromosomes, 0, array, 0, chromosomes.length);
		return array;
	}

	@Override
	public void setChromosomes(Chromosome[] chromosomes) {
		Arrays.sort(chromosomes, new Comparator<Chromosome>() {

			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return -Double.compare(o1.getFitness(), o2.getFitness());
			}
		});
		this.chromosomes = chromosomes;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("POPULATION of ").append(size()).append("\n");
		for (Chromosome c : chromosomes) {
			sb.append("  {").append(c.getFitness()).append("} ").append(c.toString()).append("\n");
		}
		return sb.toString();
	}
}
