package org.fabi.visualizations.evolution;

public class Random {
	static java.util.Random rnd = null;
	
	public static java.util.Random getInstance() {
		if (rnd == null) {
			rnd = new java.util.Random(System.currentTimeMillis());
		}
		return rnd;
	}
}
