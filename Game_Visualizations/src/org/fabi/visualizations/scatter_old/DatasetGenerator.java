package org.fabi.visualizations.scatter_old;


import java.util.List;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.DefaultMetadata;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.MultiModelSource;
import org.fabi.visualizations.tools.math.JitterGenerator;
import org.fabi.visualizations.tools.math.Metrics;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.fabi.visualizations.tools.transformation.TransformationProvider;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;


public class DatasetGenerator {
	DataSource dataSource;
	ModelSource modelSource;
	ReversibleTransformation transformation;
	JitterGenerator jitterGenerator;
	Metadata metadata;
	
	protected double[][] preparedInputs;
	
	public static final String DATA_SERIES_KEY = "data";
	
	public int getInputsNumber() {
		if (dataSource != null) {
			return dataSource.inputsNumber();
		} else if (modelSource != null) {
			return modelSource.inputsNumber();
		} else {
			return 0;
		}
	}
	
	public int getOutputsNumber() {
		if (dataSource != null) {
			return dataSource.outputsNumber();
		} else if (modelSource != null) {
			return modelSource.outputsNumber();
		} else {
			return 0;
		}
	}
	
	public ReversibleTransformation getTransformationObject() {
		return transformation;
	}
	
	public DatasetGenerator(ModelSource modelSource, DataSource dataSource, Metadata metadata) {
		this.dataSource = dataSource;
		this.modelSource = modelSource;
		init();
		this.metadata = metadata;
		metadata.setTransformation(transformation);
	}
	
	public DatasetGenerator(ModelSource modelSource, DataSource dataSource) {
		this.dataSource = dataSource;
		this.modelSource = modelSource;
		init();
		metadata = new DefaultMetadata(this);
		metadata.setTransformation(transformation);
	}
	
	protected void init() {
		if (dataSource != null && modelSource != null && (dataSource.inputsNumber() != modelSource.inputsNumber()
			|| dataSource.outputsNumber() != modelSource.outputsNumber())) {
			throw new IllegalArgumentException("Illegal model and data source combination - different feature space.");
		}
		transformation = null;
		this.jitterGenerator = new JitterGenerator(0);
	}
	
	public String getTransformation() {
		return TransformationProvider.getInstance().getString(transformation);
	}
	
	public void setTransformation(String transformation) {
		preparedInputs = null;
		double[][] inputDataVectors;
		if (dataSource == null) {
			inputDataVectors = new double[0][0];
		} else {
			inputDataVectors = dataSource.getInputDataVectors();
		}
		this.transformation = TransformationProvider.getInstance().getTransformation(transformation, inputDataVectors);
		metadata.setTransformation(this.transformation);
	}
	
	public void setJitterAmount(int jitterAmount) {
		preparedInputs = null;
		jitterGenerator.setJitterAmount(jitterAmount);
	}
	
	public int getJitterAmount() {
		return jitterGenerator.getJitterAmount();
	}
	
	public double[][] getTransformedInputs() {
		if (preparedInputs == null) {
			prepareInputs();
		}
		return preparedInputs;
	}
	
	protected void prepareInputs() {
		if (dataSource == null) {
			preparedInputs = new double[0][0];
			return;
		}
		preparedInputs = dataSource.getInputDataVectors();
		if (transformation != null) {
			preparedInputs = transformation.transformForwards(preparedInputs);
		}
		preparedInputs = jitterGenerator.addJitter(preparedInputs);
	}
	
	/**
	 * Expects either regression data with 1 output attribute (double)
	 * or classification data with n output attributes (binary array {0,1}^n).
	 * If other data provided, may result in unexpected return values.
	 */
	public XYDataset getData(ScatterplotVisualization visualization, boolean countDistance) {
		if (dataSource == null) {
			return new DefaultXYZDataset();
		}
		
		int[] axes;
		if (visualization.isyAxisInput()) {
			axes = new int[]{visualization.getxAxisIndex(),
					visualization.getyAxisIndex()};
		} else {
			axes = new int[]{visualization.getxAxisIndex()};
		}
		int outputsNr = dataSource.outputsNumber();
		double[][] inputs = getTransformedInputs();
		double[][] outputs = dataSource.getOutputDataVectors();
		outputs = jitterGenerator.addJitter(outputs);
		List<Double> inputsSettingList = visualization.getInputsSetting();
		double[] inputsSetting = new double[inputsSettingList.size()];
		for (int i = 0; i < inputsSetting.length; i++) {
			inputsSetting[i] = inputsSettingList.get(i);
		}
		
		/* One series >> regression dataset expected. */
		if (outputsNr == 1) {
				double[][] result = new double[3][inputs.length];
				for (int i = 0; i < inputs.length; i++) {
					result[0][i] = inputs[i][axes[0]];
				}
				if (visualization.isyAxisInput()) {
					for (int i = 0; i < inputs.length; i++) {
						result[1][i] = inputs[i][axes[1]];
					}
				} else {
					for (int i = 0; i < inputs.length; i++) {
						result[1][i] = outputs[i][0];
					}
				}	
				if (countDistance) {
					for (int i = 0; i < inputs.length; i++) {
						result[2][i] = Metrics.euclideanDistance(inputs[i], inputsSetting, axes);
					}
				}
				DefaultXYZDataset dataset = new DefaultXYZDataset();
				dataset.addSeries(DATA_SERIES_KEY, result);
				return dataset;
				
		/* Multiple series >> classification dataset expected. */
		} else { // expects that output is a binary ({0,1}) field
			int[] seriesSize = new int[outputsNr];
			int[] seriesIndex = new int[outputs.length];
			
			/* search for different classes */
			for (int i = 0; i < outputs.length; i++) {
				seriesIndex[i] = -1; // data with unsupported output attributes format will be truncated
				for (int j = 0; j < outputsNr; j++) {
					if (outputs[i][j] == 1) {
						seriesSize[j]++;
						seriesIndex[i] = j;
						j = outputs[i].length;
					}
				}
			}
			double[][][] result = new double[outputsNr][][];
			for (int i = 0; i < outputsNr; i++) {
				result[i] = new double[3][seriesSize[i]];
			}
			
			/* split classes into series */
			// in the next code, seriesSize is used as an iterator set through series arrays
			for (int i = outputs.length - 1; i >= 0; i--) {
				if (seriesIndex[i] != -1) {
					int index = --seriesSize[seriesIndex[i]];
					result[seriesIndex[i]][0][index] = inputs[i][axes[0]];
					if (visualization.isyAxisInput()) {
						result[seriesIndex[i]][1][index] = inputs[i][axes[1]];
					} else {
						result[seriesIndex[i]][1][index] = outputs[i][seriesIndex[i]];
					}
					if (countDistance) {
						result[seriesIndex[i]][2][index]= Metrics.euclideanDistance(inputs[i], inputsSetting, axes);
					}
					//System.out.println(result[seriesIndex[i]][0][index] + " " + result[seriesIndex[i]][1][index] + " " + result[seriesIndex[i]][2][index]);
				}
			}	
			
			/* create the dataset */
			DefaultXYZDataset dataset = new DefaultXYZDataset();
			List<AttributeInfo> outputInfo = metadata.getOutputAttributeInfo();
			for (int i = 0; i < outputsNr; i++) {
				dataset.addSeries(outputInfo.get(i).getName(), result[i]);
			}
			return dataset;
		}
	}
	
	public XYDataset getModelResponse(ScatterplotVisualization visualization) {
		return getModelResponse(visualization, modelSource);
	}
	
	public XYDataset getModelResponse(ScatterplotVisualization visualization, int index) {
		if (modelSource instanceof MultiModelSource) {
			MultiModelSource s = (MultiModelSource) modelSource;
			if (index < s.getModelCount()) {
				return getModelResponse(visualization, ((MultiModelSource) modelSource).getModel(index));
			}
		}
		return null;
	}
	
	protected XYDataset getModelResponse(ScatterplotVisualization visualization, ModelSource modelSource) {
		if (modelSource == null) {
			return new DefaultXYZDataset();
		}
		
		double[][] inputs = ModelInputGenerator.generateData(visualization);
		double[][] transformedInputs = inputs;
		if (transformation != null) {
			transformedInputs = transformation.transformBackwards(inputs);
		}
		double[][] outputs = modelSource.getModelResponses(transformedInputs);
		int xIndex = visualization.getxAxisIndex();
		if (visualization.isyAxisInput()) {
			int yIndex = visualization.getyAxisIndex();
			DefaultXYZDataset dataset = new DefaultXYZDataset();
			List<AttributeInfo> outputInfo = metadata.getOutputAttributeInfo();
			for (int output = 0; output < modelSource.outputsNumber(); output++) {
				double[][] result = new double[3][outputs.length];
				for (int i = 0; i < outputs.length; i++) {
					result[0][i] = inputs[i][xIndex];
					result[1][i] = inputs[i][yIndex];
					result[2][i] = outputs[i][output];
				}
				dataset.addSeries(outputInfo.get(output).getName(), result);
			}
			return dataset;
		} else {
			DefaultXYDataset dataset = new DefaultXYDataset();
			List<AttributeInfo> outputInfo = metadata.getOutputAttributeInfo();
			for (int output = 0; output < modelSource.outputsNumber(); output++) {
				double[][] result = new double[2][outputs.length];
				for (int i = 0; i < outputs.length; i++) {
					result[0][i] = inputs[i][xIndex];
					result[1][i] = outputs[i][output];
				}
				dataset.addSeries(outputInfo.get(output).getName(), result);
			}
			return dataset;
		}
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
}
