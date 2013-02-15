package org.fabi.visualizations.scatter_old;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

public class DatasetHolder {
	protected List<XYDataset> datasets;
	protected List<XYItemRenderer> renderers;
	protected List<Boolean> visible;
	
	protected XYPlot plot;
	
	public DatasetHolder(XYPlot plot) {
		this.plot = plot;
		datasets = new ArrayList<XYDataset>(2);
		renderers = new ArrayList<XYItemRenderer>(2);
		visible = new ArrayList<Boolean>(2);
	}
	
	public void registerDataset(XYDataset dataset, XYItemRenderer renderer) {
		datasets.add(dataset);
		renderers.add(renderer);
		visible.add(false);
	}
	
	public boolean isDatasetVisible(int index) {
		return visible.get(index);
	}
	
	public void setDatasetVisible(int index, boolean visible) {
		if (index < 0 || index >= this.visible.size()) {
			throw new ArrayIndexOutOfBoundsException("No such dataset.");
		} else {
			this.visible.set(index, visible);
			updatePlot();
		}
	}
	
	protected void updatePlot() {
		for (int i = 0; i < plot.getDatasetCount(); i++) {
			plot.setDataset(i, null);
			plot.setRenderer(i, null);
		}
		int index = 0;
		for (int i = 0; i < visible.size(); i++) {
			if (visible.get(i)) {
				plot.setDataset(index, datasets.get(i));
				plot.setRenderer(index, renderers.get(i));
				index++;
			}
		}
	}
}
