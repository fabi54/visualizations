package org.fabi.visualizations.scatter.dotsize;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Distance size model")
public class MinkowskiDistanceDotSizeModel extends DotSizeModelBase {
	
	public MinkowskiDistanceDotSizeModel() { }
	
	public MinkowskiDistanceDotSizeModel(double p, double minDist, double rangeDist, int minSize, int rangeSize) {
		this.minDist = minDist;
		this.rangeDist = rangeDist;
		this.minSize = minSize;
		this.rangeSize = rangeSize;
		this.p = p;
	}

	@Property(name="Distance lower bound")
	private double minDist = 0.0;
	@Property(name="Distance range")
	private double rangeDist = 1.0;
	@Property(name="Minimal size")
	private int minSize = 0;
	@Property(name="Size range")
	private int rangeSize = 5;
	@Property(name="Exponent")
	private double p = 2;
	
	protected double[] inputsSetting;
	
	@Override
	public int getSize(double[] inputs, double[] outputs, int index, int[] inputIndices, int[] outputIndices) {
		if (rangeSize == 0) {
			return minSize;
		}
		double distance = 0.0;
		int ai = 0;
		for (int i = 0; i < inputs.length; i++) { // TODO ManyToOne
			if (ai < inputIndices.length && ai == i) {
				ai++;
			} else {
				distance += Math.pow(Math.abs(inputs[i] - inputsSetting[i]), p);
			}
		}
		distance = Math.pow(distance, (1 / p));
		if (rangeDist == 0) {
			return (distance == minDist) ? (minSize + rangeSize) : minSize;
		}
		distance = (distance - minDist) / rangeDist;
		return (int) Math.min(rangeSize + minSize, Math.max(minSize, (minSize + ((1 - distance) * rangeSize))));
	}

	@Override
	public void init(final ScatterplotVisualization visualization) {
		inputsSetting = visualization.getInputsSetting();
		visualization.addPropertyChangeListener(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				inputsSetting = visualization.getInputsSetting();
			}
		});
	}

	@Override
	public String getName() {
		return "Minkowski Distance Dot Size Model";
	}	

	@Override
	public void init(ScatterplotSource source) { }

	public double getMinDist() {
		return minDist;
	}

	public void setMinDist(double minDist) {
		this.minDist = minDist;
	}

	public double getRangeDist() {
		return rangeDist;
	}

	public void setRangeDist(double rangeDist) {
		this.rangeDist = rangeDist;
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public int getRangeSize() {
		return rangeSize;
	}

	public void setRangeSize(int rangeSize) {
		this.rangeSize = rangeSize;
	}
	
	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}
	
}
