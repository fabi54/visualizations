package org.fabi.visualizations.tools.math;

import java.util.Random;

/**
 * A helper class used to add jitter in data.
 * 
 * @author janf
 *
 */
public class JitterGenerator {
	
	/**
	 * Jitter amount.
	 */
	protected int jitterAmount = 0;
	
	protected static final int RANDOM_SEED = 2001;
	
	protected static final double JITTER_RATIO = 200;
	
	public JitterGenerator(int jitterAmount) {
		this.jitterAmount = jitterAmount;
	}
	
	public void setJitterAmount(int amount) {
		jitterAmount = amount;
	}
	
	public int getJitterAmount() {
		return jitterAmount;
	}
	
	/**
	 * Destructive - overwrites sourceData.
	 * 
	 * @param sourceData rectangular bidimensional array
	 * @result sourceData with jitter
	 */
	public double[][] addJitter(double[][] sourceData) {
		if (jitterAmount == 0) {
			return sourceData;
		}
		if (sourceData.length < 1) {
			return new double[0][0];
		}
		int sampleCount = sourceData.length;
		int attributeCount = sourceData[0].length;
		double[] max = new double[attributeCount];
		double[] min = new double[attributeCount];
		for (int i = 0; i < attributeCount; i++) {
			max[i] = Double.NEGATIVE_INFINITY;
			min[i] = Double.POSITIVE_INFINITY;
		}
		for (int i = 0; i < sampleCount; i++) {
			for (int j = 0; j < attributeCount; j++) {
				max[j] = Math.max(sourceData[i][j], max[j]);
				min[j] = Math.min(sourceData[i][j], min[j]);
			}
		}
		Random jitterRandom = new Random(RANDOM_SEED);
		double[] oldRanges = new double[attributeCount];
		for (int i = 0; i < attributeCount; i++) {
			oldRanges[i] = max[i] - min[i];
			if (Double.isInfinite(oldRanges[i])) {
				oldRanges[i] = 0;
			}
		}
		for (int i = 0; i < sampleCount; i++) {
			for (int j = 0; j < attributeCount; j++) {
				double pertX = oldRanges[j] * (jitterAmount / JITTER_RATIO) * jitterRandom.nextGaussian();
				sourceData[i][j] += pertX;
			}
		}
		return sourceData;
	}
}
