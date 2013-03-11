package org.fabi.visualizations.scatter;

import java.awt.Color;
import java.util.Arrays;

import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.color.ConstantColorModel;
import org.fabi.visualizations.scatter.dotsize.DotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.math.plot.plots.ScatterPlot;
import org.math.plot.render.AbstractDrawer;

public class CustomScatterPlot extends ScatterPlot {
	
	protected ScatterplotVisualization visualization;
	
	public CustomScatterPlot(ScatterplotVisualization visualization) {
		super("", Color.WHITE, new double[][]{{}});
		this.visualization = visualization;
	}
	
	/**
	 * If the Y-axis of the visualization corresponds to output,
	 * each data instance is plotted multiple times, once for each active (visible) output,
	 * so is the model curve for each model.
	 * 
	 * If both axes correspond to input attributes,
	 * each data instance is plotted only once,
	 * so is the model.
	 */
	@Override
    public void plot(AbstractDrawer draw, Color c) {
        if (!visible) {
            return;
        }
        if (visualization.getyAxisAttributeIndex() == ScatterplotVisualization.OUTPUT_AXIS) {
        	plotDataIO(draw);
        	plotModelCurve(draw);
        } else {
        	plotModelHeatMap(draw);
            plotDataII(draw);
        }
        AdditionalDrawer[] drawers = visualization.getAdditionalDrawers();
        if (drawers != null) {
        	for (AdditionalDrawer d : drawers) {
        		d.draw(draw);
        	}
        }
    }
	
	protected void plotModelHeatMap(AbstractDrawer draw) {
        ColorModel colorModel = getColorModel();
        int xIndex = visualization.getxAxisAttributeIndex();
        int yIndex = visualization.getyAxisAttributeIndex();
        int outputPrecision = visualization.getOutputPrecision();
        
        // get model inputs and outputs
        double[][] modelInputs = visualization.getModelInputs();
        double[][][] modelOutputs = visualization.getModelOutputs();
        
        // ensemble and draw
        double[][] bounds = visualization.actualAxesBounds;
        double[] steps = new double[2];
        steps[ScatterplotVisualization.X_AXIS] = (bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.UPPER_BOUND]
        		- bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND]) / (double) outputPrecision;
        steps[ScatterplotVisualization.Y_AXIS] = (bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.UPPER_BOUND]
        		- bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND]) / (double) outputPrecision;
        for (int i = 0; i < steps.length; i++) {
    		steps[i] /= 2.0;
    	}
		int[] inputsIndices = new int[]{visualization.getxAxisAttributeIndex(), visualization.getyAxisAttributeIndex()};
		Arrays.sort(inputsIndices);
		boolean[][] visibleOutputs = visualization.getModelsVisible();
        for (int i = 0; i < modelOutputs.length; i++) {
        	// TODO whole model not visible
        	
        	// get visible outputs
        	int cnt = 0;
        	for (int j = 0; j < visibleOutputs[i].length; j++) {
        		if (visibleOutputs[i][j]) {
        			cnt++;
        		}
        	}
    		int[] outputsIndices = new int[cnt];
    		cnt = 0;
    		for (int j = 0; j < visibleOutputs[i].length; j++) {
    			if (visibleOutputs[i][j]) {
    				outputsIndices[cnt++] = j;
    			}
    		}
    			
        	// plot
        	for (int j = 0; j < modelOutputs[i].length; j++) {
	    		draw.setColor(colorModel.getColor(modelInputs[j], modelOutputs[i][j], ColorModel.MODEL, i, inputsIndices, outputsIndices));
				draw.fillPolygon(1.0f,
						new double[]{modelInputs[j][xIndex] - steps[0], modelInputs[j][yIndex] - steps[1]},
						new double[]{modelInputs[j][xIndex] + steps[0], modelInputs[j][yIndex] - steps[1]},
						new double[]{modelInputs[j][xIndex] + steps[0], modelInputs[j][yIndex] + steps[1]},
						new double[]{modelInputs[j][xIndex] - steps[0], modelInputs[j][yIndex] + steps[1]});
        		}
        }
	}
	
	protected void plotModelCurve(AbstractDrawer draw) {
		ScatterplotSource source = visualization.getSource();
        ColorModel colorModel = getColorModel();
        int xIndex = visualization.getxAxisAttributeIndex();
        boolean[][] visibleOutputs = visualization.getModelsVisible();
        
        // get model inputs and outputs
        double[][] modelInputs = visualization.getModelInputs();
        double[][][] modelOutputs = visualization.getModelOutputs();
        
        double[] inputs = visualization.getInputsSetting();
        double[] outputs = new double[visualization.getSource().getOutputsNumber()];
        
        // draw
        for (int i = 0; i < modelOutputs.length; i++) {
        	// TODO whole model not visible
        	for (int j = 0; j < source.getOutputsNumber(); j++) {
        		if (visibleOutputs[i][j]) {
	            	int[] inputsIndices = new int[]{visualization.getxAxisAttributeIndex()};
	            	int[] outputsIndices = new int[]{j};
	        		for (int k = 1; k < modelOutputs[i].length; k++) {
	        			inputs[xIndex] = (modelInputs[k - 1][xIndex] + modelInputs[k][xIndex]) / 2;
	        			for (int l = 0; l < outputs.length; l++) {
	        				outputs[l] = (modelOutputs[i][k - 1][l] + modelOutputs[i][k][l]) / 2;
	        			}
	        			draw.setColor(colorModel.getColor(modelInputs[i], outputs, ColorModel.MODEL, i, inputsIndices, outputsIndices));
	        			double[][] points = getActualCurvePoints(modelInputs[k - 1][xIndex], modelInputs[k][xIndex],
	        					modelOutputs[i][k - 1][j], modelOutputs[i][k][j]);
	        			if (points != null) {
	        				draw.drawLine(points[0], points[1]);
						}
	        		}
        		}
        	}
        }
	}
	
	protected double[][] getActualCurvePoints(double i1, double i2, double o1, double o2) {
		double[][] bounds = visualization.actualAxesBounds;
		if ((o1 > bounds[1][1] || o1 < bounds[1][0]) && (o2 > bounds[1][1] || o2 < bounds[1][0])) {
			return null;
		}
		double[][] res = new double[][]{{i1, o1}, {i2, o2}};
		if (o1 > bounds[1][1]) {
			res[0][0] = i2 - (i2 - i1) * (bounds[1][1] - o2) / (o1 - o2);
			res[0][1] = bounds[1][1];
		}
		if (o1 < bounds[1][0]) {
			res[0][0] = i2 - (i2 - i1) * (o2 - bounds[1][0]) / (o2 - o1);
			res[0][1] = bounds[1][0];
		}
		if (o2 > bounds[1][1]) {
			res[1][0] = i1 + (i2 - i1) * (bounds[1][1] - o1) / (o2 - o1);
			res[1][1] = bounds[1][1];
		}
		if (o2 < bounds[1][0]) {
			res[1][0] = i1 + (i2 - i1) * (o1 - bounds[1][0]) / (o1 - o2);
			res[1][1] = bounds[1][0];
		}
		return res;
	}
	
	protected void plotDataII(AbstractDrawer draw) {
        ScatterplotSource source = visualization.getSource();
        ColorModel colorModel = getColorModel();
        DotSizeModel dotSizeModel = visualization.getDotSizeModel();
		int[] inputsIndices = new int[]{visualization.getxAxisAttributeIndex(), visualization.getyAxisAttributeIndex()};
		Arrays.sort(inputsIndices);
		boolean[][] visibleOutputs = visualization.getDataVisible();
        double[][][] data = visualization.getTransformedDataInputs();
        for (int i = 0; i < source.getDataSourceCount(); i++) {
        	// get visible outputs
        	// TODO whole model not visible
        	int cnt = 0;
        	for (int j = 0; j < visibleOutputs[i].length; j++) {
        		if (visibleOutputs[i][j]) {
        			cnt++;
        		}
        	}
    		int[] outputsIndices = new int[cnt];
    		cnt = 0;
    		for (int j = 0; j < visibleOutputs[i].length; j++) {
    			if (visibleOutputs[i][j]) {
    				outputsIndices[cnt++] = j;
    			}
    		}
        	
        	// plot
        	DataSource ds = source.getDataSource(i);
        	double[][] outputs = ds.getOutputDataVectors();
        	double[][] bounds = visualization.getActualAxesBounds();
        	for (int j = 0; j < data[i].length; j++) {
        		double[] point = {data[i][j][visualization.getxAxisAttributeIndex()],
    							  data[i][j][visualization.getyAxisAttributeIndex()]};
        		if (isInside(point, bounds)) {
        			draw.setColor(colorModel.getColor(data[i][j], outputs[j], ColorModel.DATA, i, inputsIndices, outputsIndices));
        			if (dotSizeModel != null) {
        				draw.setDotRadius(dotSizeModel.getSize(data[i][j], outputs[j], i, inputsIndices, outputsIndices));
        			}
        			draw.drawDot(point);
        		}
        	}
        }
	}
	
	protected void plotDataIO(AbstractDrawer draw) {
        ScatterplotSource source = visualization.getSource();
        ColorModel colorModel = getColorModel();
        DotSizeModel dotSizeModel = visualization.getDotSizeModel();
        double[][][] data = visualization.getTransformedDataInputs();
        boolean[][] visibleOutputs = visualization.getDataVisible();
        for (int i = 0; i < source.getDataSourceCount(); i++) {
        	// TODO whole model not visible
        	DataSource ds = source.getDataSource(i);
        	double[][] outputs = ds.getOutputDataVectors();
        	double[][] bounds = visualization.getActualAxesBounds();
    		for (int k = 0; k < ds.outputsNumber(); k++) {
    			if (visibleOutputs[i][k]) {
	    			int[] inputsIndices = new int[]{visualization.getxAxisAttributeIndex()};
	    			int[] outputsIndices = new int[]{k};
	    			for (int j = 0; j < data[i].length; j++) {
		        		double[] point = {data[i][j][visualization.getxAxisAttributeIndex()],
		    							  outputs[j][k]};
		        		if (isInside(point, bounds)) {
		        			draw.setColor(colorModel.getColor(data[i][j], outputs[j], ColorModel.DATA, i, inputsIndices, outputsIndices));
		        			if (dotSizeModel != null) {
		        				draw.setDotRadius(dotSizeModel.getSize(data[i][j], outputs[j], i, inputsIndices, outputsIndices));
		        			}
		        			draw.drawDot(point);
		        		}
	        		}
    			}
        	}
        }
	}
	
	protected ColorModel getColorModel() {
		ColorModel res = visualization.getColorModel();
		if (res == null) {
			res = new ConstantColorModel();
		}
		return res;
	}
	
	protected boolean isInside(double[] point, double[][] bounds) {
		boolean res = true;
		for (int i = 0; i < point.length; i++) {
			res &= point[i] >= bounds[i][0] && point[i] <= bounds[i][1];
		}
		return res;
	}
}
