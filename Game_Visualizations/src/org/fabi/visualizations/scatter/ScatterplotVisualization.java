package org.fabi.visualizations.scatter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.dotsize.DotSizeModel;
import org.fabi.visualizations.scatter.gui.ScatterplotVisualizationControlPanel;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.math.plot.canvas.Plot2DCanvas;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.annotations.Range;

@Component(name="Scatterplot")
public class ScatterplotVisualization extends Visualization<ScatterplotSource> {
// TODO "DotShiftModel" - moving data instances after transformation (e.g. adding jitter)
	
	public ScatterplotVisualization() {
		super();
	}
	
	public ScatterplotVisualization(ScatterplotSource source) {
		this();
		setSource(source);
	}
	
	public void setSource(ScatterplotSource source) {
		this.source = source;
		if (source instanceof Observable) {
			((Observable) source).addObserver(this);
		}
		if (inputsSetting == null || inputsSetting.length != source.getInputsNumber()) {
			inputsSetting = new double[source.getInputsNumber()];
		}
		if (dataVisible == null || dataVisible.length != source.getDataSourceCount()) {
			dataVisible = new boolean[source.getDataSourceCount()][source.getOutputsNumber()];
			for (int i = 0; i < dataVisible.length; i++) {
				for (int j = 0; j < dataVisible[i].length; j++) {
					dataVisible[i][j] = true;
				}
			}
		}
		if (modelsVisible == null || modelsVisible.length != source.getModelSourceCount()) {
			modelsVisible = new boolean[source.getModelSourceCount()][source.getOutputsNumber()];
			for (int i = 0; i < modelsVisible.length; i++) {
				for (int j = 0; j < modelsVisible[i].length; j++) {
					modelsVisible[i][j] = true;
				}
			}
		}
		if (lowerInputs == null || lowerInputs.length != source.getInputsNumber() + 1
				|| upperInputs == null || upperInputs.length != source.getInputsNumber() + 1) {
			lowerInputs = new double[source.getInputsNumber() + 1];	
			upperInputs = new double[source.getInputsNumber() + 1];
			for (int i = 0; i < lowerInputs.length; i++) {
				lowerInputs[i] = Double.NaN;
				upperInputs[i] = Double.NaN;
			}
		}
		if (actualAxesBounds == null) {
			actualAxesBounds = new double[2][2];
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					actualAxesBounds[i][j] = Double.NaN;
				}
			}
		}
	}
	
	public ScatterplotSource getSource() {
		return source;
	}

/* property labels *****************************************************************************/
	
	public static final String PROPERTY_X_AXIS_ATTRIBUTE_INDEX = "x_axis_attribute_index";
	public static final String PROPERTY_Y_AXIS_ATTRIBUTE_INDEX = "y_axis_attribute_index";
	public static final String PROPERTY_X_AXIS_RANGE_LOWER = "x_axis_range_lower";
	public static final String PROPERTY_X_AXIS_RANGE_UPPER = "x_axis_range_upper";
	public static final String PROPERTY_Y_AXIS_RANGE_LOWER = "y_axis_range_lower";
	public static final String PROPERTY_Y_AXIS_RANGE_UPPER = "y_axis_range_upper";
	public static final String PROPERTY_TRANSFORMATION = "transformation";
	public static final String PROPERTY_INPUTS_SETTING = "inputs_setting";
	public static final String PROPERTY_OUTPUT_PRECISION = "output_precision";
	public static final String PROPERTY_DATA_VISIBLE = "data_visible";
	public static final String PROPERTY_MODELS_VISIBLE = "model_visible";
	public static final String PROPERTY_MODEL_RENDERER = "model_renderer";
	public static final String PROPERTY_LEGEND_VISIBLE = "legend_visible";
	public static final String PROPERTY_AXES_TICK_UNITS_VISIBLE = "axes_tick_units_visible";
	public static final String PROPERTY_COLOR_MODEL = "color_model";
	public static final String PROPERTY_DOT_SIZE_MODEL = "dot_size_model";
	public static final String PROPERTY_BACKGROUND_COLOR = "background_color";
	public static final String PROPERTY_ADDITIONAL_DRAWERS = "additional_drawers";
	public static final String PROPERTY_GRID_VISIBLE = "show_grid";

/* properties **********************************************************************************/
	
	public static final double AUTO_BOUND = Double.NaN;
	public static final int OUTPUT_AXIS = -1;
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int LOWER_BOUND = 0;
	public static final int UPPER_BOUND = 1;
	
	@Property(name=PROPERTY_X_AXIS_ATTRIBUTE_INDEX)
	@Range(from=0, to=Integer.MAX_VALUE)            private int xAxisAttributeIndex = 0;
	@Property(name=PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)
	@Range(from=-1, to=Integer.MAX_VALUE)           private int yAxisAttributeIndex = OUTPUT_AXIS;
	@Property(name=PROPERTY_X_AXIS_RANGE_LOWER)		private double xAxisRangeLower = AUTO_BOUND;
	@Property(name=PROPERTY_X_AXIS_RANGE_UPPER)		private double xAxisRangeUpper = AUTO_BOUND;
	@Property(name=PROPERTY_Y_AXIS_RANGE_LOWER)		private double yAxisRangeLower = AUTO_BOUND;
	@Property(name=PROPERTY_Y_AXIS_RANGE_UPPER)		private double yAxisRangeUpper = AUTO_BOUND;
	@Property(name=PROPERTY_TRANSFORMATION)			private ReversibleTransformation transformation = null;
	@Property(name=PROPERTY_INPUTS_SETTING)			private double[] inputsSetting;
	@Property(name=PROPERTY_OUTPUT_PRECISION)		private int outputPrecision = 50;
	@Property(name=PROPERTY_DATA_VISIBLE)			private boolean[][] dataVisible;
	@Property(name=PROPERTY_MODELS_VISIBLE)			private boolean[][] modelsVisible;
	@Property(name=PROPERTY_COLOR_MODEL)			private ColorModel colorModel = null;
	@Property(name=PROPERTY_DOT_SIZE_MODEL)			private DotSizeModel dotSizeModel = null;
	@Property(name=PROPERTY_LEGEND_VISIBLE)			private boolean legendVisible;
	@Property(name=PROPERTY_AXES_TICK_UNITS_VISIBLE)private boolean axesTickUnitsVisible;
	@Property(name=PROPERTY_BACKGROUND_COLOR)		private Color background = Color.WHITE;
	@Property(name=PROPERTY_ADDITIONAL_DRAWERS)		private AdditionalDrawer[] additionalDrawers = new AdditionalDrawer[0];
	@Property(name=PROPERTY_GRID_VISIBLE)			private boolean gridVisible = true;
	

/* cache ***************************************************************************************/
	
	protected double[] lowerInputs;
	protected double[] upperInputs;
	
	protected double[][] actualAxesBounds;
	
	protected double[][][] transformedDataInputs;
	protected double[][] modelInputs;
	protected double[][] transformedModelInputs;
	protected double[][][] modelOutputs;

/* *********************************************************************************************/
	
	List<Plot2DCanvas> components;
	
	@Override
	public JComponent getControls() {
		return new ScatterplotVisualizationControlPanel(this);
	}

	@Override
	public JComponent getVisualizationAsComponent() {
		if (components == null) {
			components = new ArrayList<Plot2DCanvas>(1);
		}
		Plot2DCanvas result = new Plot2DCanvas();
		result.addPlot(new CustomScatterPlot(this));
		update(result);
		components.add(result);
		return result;
	}

	@Override
	public BufferedImage getVisualizationAsImage() {
		return getVisualizationAsImage(640, 480);
	}

	@Override
	public BufferedImage getVisualizationAsImage(int width, int height) {
		Plot2DCanvas result = new Plot2DCanvas();
		result.addPlot(new CustomScatterPlot(this));
		result.setSize(width, height);
		JPanel container = new JPanel();
		container.setSize(width, height);
		update(result);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		SwingUtilities.paintComponent(img.createGraphics(), result, container, 0, 0, width, height);
		return img;
	}

	@Override
	public String toString() {
		return "Scatterplot";
	}
	
	@Override
	public void update(Observable o, Object arg) { // TODO? recompute only necessary series
		// For ScatterplotSource changes
		modelOutputs = null;
		modelInputs = null;
		transformedModelInputs = null;
		transformedDataInputs = null;
		update();
	}
	
	protected void update() {
		if (components != null) {
			for (Plot2DCanvas c : components) {
				Cursor cursorBck = c.getCursor();
				c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				update(c);
				c.repaint();
				c.setCursor(cursorBck);
			}
		}
	}

	protected void update(String s) {
		if (components != null) {
			for (Plot2DCanvas c : components) {
				Cursor cursorBck = c.getCursor();
				c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				update(s, c);
				c.setCursor(cursorBck);
			}
		}
	}
	
	protected void update(Plot2DCanvas c) {
		Cursor cursorBck = c.getCursor();
		c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (properties == null) {
			properties = propertyExtractor.propertiesFor(this);
		}
		for (@SuppressWarnings("rawtypes") org.ytoh.configurations.Property p : properties) {
			update(p.getName(), c, false);
		}
		c.repaint();
		c.setCursor(cursorBck);
	}
	
	protected static final double AUTO_BOUND_OFFSET = 0.05;

	protected static double getAutoBoundOffset(double min, double max) {
		return (max - min) * AUTO_BOUND_OFFSET;
	}
	
	protected void update(String s, Plot2DCanvas c) {
		Cursor cursorBck = c.getCursor();
		c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		update(s, c, true);
		c.setCursor(cursorBck);
	}
	
	protected void update(String s, Plot2DCanvas c, boolean repaint) {
		Cursor cursorBck = c.getCursor();
		c.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		// x axis index
		if (s.equals(PROPERTY_X_AXIS_ATTRIBUTE_INDEX)) {
			Metadata metadata = source.getMetadata();
			if (metadata != null) {
				List<AttributeInfo> inputs = metadata.getInputAttributeInfo();
				// depends on jmathplot version:
				c.setAxisLabel(X_AXIS, inputs.get(xAxisAttributeIndex).getName());
//				c.setAxeLabel(X_AXIS, inputs.get(xAxisAttributeIndex).getName());
			} else {
				// depends on jmathplot version:
				 c.setAxisLabel(X_AXIS, "attr" + xAxisAttributeIndex);
//				c.setAxeLabel(X_AXIS, "attr" + xAxisAttributeIndex);
			}
		}
		
		// y axis index
		if (s.equals(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)) {
			Metadata metadata = source.getMetadata();
			if (metadata != null) {
				List<AttributeInfo> inputs = metadata.getInputAttributeInfo();
				if (yAxisAttributeIndex == OUTPUT_AXIS) {
					// depends on jmathplot version:
					 c.setAxisLabel(Y_AXIS, "output");
//					c.setAxeLabel(Y_AXIS, "output");
				} else {
					// depends on jmathplot version:
					 c.setAxisLabel(Y_AXIS, inputs.get(yAxisAttributeIndex).getName());
//					c.setAxeLabel(Y_AXIS, inputs.get(yAxisAttributeIndex).getName());
				}
			} else {
				if (yAxisAttributeIndex == OUTPUT_AXIS) {
					// depends on jmathplot version:
					 c.setAxisLabel(Y_AXIS, "output");
//					c.setAxeLabel(Y_AXIS, "output");
				} else {
					// depends on jmathplot version:
					 c.setAxisLabel(Y_AXIS, "attr" + yAxisAttributeIndex);
//					c.setAxeLabel(Y_AXIS, "attr" + yAxisAttributeIndex);
				}
			}
		}
		
		// x axis range
		if (s.equals(PROPERTY_X_AXIS_RANGE_LOWER)
			|| s.equals(PROPERTY_X_AXIS_RANGE_UPPER)
			|| s.equals(PROPERTY_X_AXIS_ATTRIBUTE_INDEX)) {
			double min = xAxisRangeLower;
			double max = xAxisRangeUpper;
			if (Double.isNaN(min)) {
				min = getLowerInput(xAxisAttributeIndex);
			}
			if (Double.isNaN(max)) {
				max = getUpperInput(xAxisAttributeIndex);
			}
			if (Double.isNaN(xAxisRangeLower)) {
				min -= getAutoBoundOffset(min, max);
			}
			if (Double.isNaN(xAxisRangeUpper)) {
				max += getAutoBoundOffset(min, max);
			}
			actualAxesBounds[X_AXIS][LOWER_BOUND] = min;
			actualAxesBounds[X_AXIS][UPPER_BOUND] = max;
			c.setFixedBounds(0, min, max);
		}
		// y axis range
		else if (s.equals(PROPERTY_Y_AXIS_RANGE_LOWER)
			|| s.equals(PROPERTY_Y_AXIS_RANGE_UPPER)
			|| s.equals(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)) {
			double min = yAxisRangeLower;
			double max = yAxisRangeUpper;
			if (Double.isNaN(min)) {
				if (yAxisAttributeIndex == OUTPUT_AXIS) {
					min = getLowerOutput();
				} else {
					min = getLowerInput(yAxisAttributeIndex);
				}
			}
			if (Double.isNaN(max)) {
				if (yAxisAttributeIndex == OUTPUT_AXIS) {
					max = getUpperOutput();
				} else {
					max = getUpperInput(yAxisAttributeIndex);
				}
			}
			if (Double.isNaN(yAxisRangeLower)) {
				min -= getAutoBoundOffset(min, max);
			}
			if (Double.isNaN(yAxisRangeUpper)) {
				max += getAutoBoundOffset(min, max);
			}
			actualAxesBounds[Y_AXIS][LOWER_BOUND] = min;
			actualAxesBounds[Y_AXIS][UPPER_BOUND] = max;
			c.setFixedBounds(1, min, max);
		}
		
		if (s.equals(PROPERTY_TRANSFORMATION)) {
			transformedDataInputs = null;
			transformedModelInputs = null;
		}
		if (s.equals(PROPERTY_X_AXIS_RANGE_LOWER)
			|| s.equals(PROPERTY_X_AXIS_RANGE_UPPER)
			|| s.equals(PROPERTY_Y_AXIS_RANGE_LOWER)
			|| s.equals(PROPERTY_Y_AXIS_RANGE_UPPER)
			|| s.equals(PROPERTY_X_AXIS_ATTRIBUTE_INDEX)
			|| s.equals(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)) {
			modelOutputs = null;
			modelInputs = null;
			transformedModelInputs = null;
		}
		if (s.equals(PROPERTY_INPUTS_SETTING)
			|| s.equals(PROPERTY_OUTPUT_PRECISION)) {
			modelInputs = null;
			modelOutputs = null;
		}
		
		// Background
		if (s.equals(PROPERTY_BACKGROUND_COLOR)) {
			c.setBackground(background);
		}
		
		// Grid
		if (s.equals(PROPERTY_GRID_VISIBLE)) {
			c.getGrid().setGridVisible(0, gridVisible);
			c.getGrid().setGridVisible(1, gridVisible);
		}
		
		// New data source
		if (dataVisible.length != source.getDataSourceCount()) {
			boolean[][] old = dataVisible;
			dataVisible = new boolean[source.getDataSourceCount()][source.getOutputsNumber()];
			int iLen = Math.min(dataVisible.length, old.length);
			for (int i = 0; i < iLen; i++) { // TODO? if data source removed, copying old values will produce inconsistencies
				int jLen = Math.min(dataVisible[i].length, old[i].length);
				for (int j = 0; j < jLen; j++) {
					dataVisible[i][j] = old[i][j];
				}
			}
		}
		
		// New model source
		if (modelsVisible.length != source.getModelSourceCount()) {
			boolean[][] old = modelsVisible;
			modelsVisible = new boolean[source.getModelSourceCount()][source.getOutputsNumber()];
			int iLen = Math.min(modelsVisible.length, old.length);
			for (int i = 0; i < iLen; i++) { // TODO? if model source removed, copying old values will produce inconsistencies
				int jLen = Math.min(modelsVisible[i].length, old[i].length);
				for (int j = 0; j < jLen; j++) {
					modelsVisible[i][j] = old[i][j];
				}
			}
		}
		
		if (repaint) {
			c.repaint();
		}
		
		c.setCursor(cursorBck);
	}
	
	protected double getLowerInput(int index) {
		if (Double.isNaN(lowerInputs[index])) {
			lowerInputs[index] = Double.POSITIVE_INFINITY;
			upperInputs[index] = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < source.getDataSourceCount(); i++) {
				org.jfree.data.Range rng = Arrays.getBounds(source.getDataSource(i).getInputDataVectors(), index);
				lowerInputs[index] = Math.min(lowerInputs[index], rng.getLowerBound());
				upperInputs[index] = Math.max(upperInputs[index], rng.getUpperBound());
			}
		}
		return lowerInputs[index];
	}
	
	protected double getLowerOutput() {
		if (Double.isNaN(lowerInputs[lowerInputs.length - 1])) {
			lowerInputs[lowerInputs.length - 1] = Double.POSITIVE_INFINITY;
			upperInputs[lowerInputs.length - 1] = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < source.getDataSourceCount(); i++) {
				for (int j = 0; j < source.getOutputsNumber(); j++) {
					org.jfree.data.Range rng = Arrays.getBounds(source.getDataSource(i).getOutputDataVectors(), j);
					lowerInputs[lowerInputs.length - 1] = Math.min(lowerInputs[lowerInputs.length - 1], rng.getLowerBound());
					upperInputs[lowerInputs.length - 1] = Math.max(upperInputs[lowerInputs.length - 1], rng.getUpperBound());
				}
			}
		}
		return lowerInputs[lowerInputs.length - 1];
	}
	
	protected double getUpperInput(int index) {
		getLowerInput(index);
		return upperInputs[index];
	}
	
	protected double getUpperOutput() {
		getLowerOutput();
		return upperInputs[upperInputs.length - 1];
	}
	
	protected double[][][] getTransformedDataInputs() {
		if (transformedDataInputs == null) {
			int dataCnt = source.getDataSourceCount();
			transformedDataInputs = new double[dataCnt][][];
			for (int i = 0; i < dataCnt; i++) {
				transformedDataInputs[i] = source.getDataSource(i).getInputDataVectors();
				if (transformation != null) {
					transformedDataInputs[i] = transformation.transformForwards(transformedDataInputs[i]);
				}
			}
		}
		return transformedDataInputs;
	}
	
	protected double[][] getModelInputs() {
		if (modelInputs == null) {
			if (yAxisAttributeIndex == OUTPUT_AXIS) {
				getCurveInputs();
			} else {
				getHeatMapInputs();
			}
		}
		if (transformedModelInputs == null) {
			if (transformation == null) {
				transformedModelInputs = modelInputs;
			} else {
				transformedModelInputs = transformation.transformBackwards(modelInputs);
			}
		}
		return modelInputs;
	}
	
	protected void getHeatMapInputs() {
		modelInputs = new double[outputPrecision * outputPrecision][source.getInputsNumber()];
        double[][] bounds = actualAxesBounds;
        double[] steps = new double[2];
        steps[ScatterplotVisualization.X_AXIS] = (bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.UPPER_BOUND]
        		- bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND]) / (double) outputPrecision;
        steps[ScatterplotVisualization.Y_AXIS] = (bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.UPPER_BOUND]
        		- bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND]) / (double) outputPrecision;
        int index = 0;
        for (int i = 0; i < outputPrecision; i++) {
        	double x = bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND]
        			+ (0.5 + i) * steps[ScatterplotVisualization.X_AXIS];
            for (int j = 0; j < outputPrecision; j++) {
            	double y = bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND]
            			+ (0.5 + j) * steps[ScatterplotVisualization.Y_AXIS];
            	System.arraycopy(inputsSetting, 0, modelInputs[index], 0, inputsSetting.length);
            	modelInputs[index][xAxisAttributeIndex] = x;
            	modelInputs[index][yAxisAttributeIndex] = y;
            	index++;
            }
        }
	}
	
	protected void getCurveInputs() {
		modelInputs = new double[outputPrecision][source.getInputsNumber()];
        double[][] bounds = actualAxesBounds;
        double step = (bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.UPPER_BOUND]
        		- bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND]) / (double) (outputPrecision - 1);
        for (int i = 0; i < outputPrecision; i++) {
        	double x = bounds[ScatterplotVisualization.X_AXIS][ScatterplotVisualization.LOWER_BOUND]
        			+ i * step;
            System.arraycopy(inputsSetting, 0, modelInputs[i], 0, inputsSetting.length);
            	modelInputs[i][xAxisAttributeIndex] = x;
        }
	}
	
	protected double[][][] getModelOutputs() {
		if (modelOutputs == null) {
			modelOutputs = new double[source.getModelSourceCount()][][];
			double[][] modelInputs = getModelInputs();
	    	for (int i = 0; i < source.getModelSourceCount(); i++) {
	        	modelOutputs[i] = source.getModelSource(i).getModelResponses(modelInputs);
	        }
		}
		return modelOutputs;
	}

	@Override
	@Deprecated
	public VisualizationConfig getConfig() {
		return new VisualizationConfig(this.getClass());
	}

	@Override
	@Deprecated
	protected void setConfiguration(VisualizationConfig cfg, Collection<String> changedProperties) { }

	@Override
	@Deprecated
	public VisualizationConfig getDefaultConfig() {
		return new VisualizationConfig(this.getClass());
	}
	
	public void setAxisAttributeIndex(int index, int axis) {
		switch (axis) {
			case X_AXIS:
				setProperty(PROPERTY_X_AXIS_ATTRIBUTE_INDEX, index);
				break;
			case Y_AXIS:
				setProperty(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, index);
				break;
			default:
				throw new IllegalArgumentException("Illegal axis.");	
		}
	}
	
	public int getAxisAttributeIndex(int axis) {
		switch (axis) {
			case X_AXIS:
				return getxAxisAttributeIndex();
			case Y_AXIS:
				return getyAxisAttributeIndex();
			default:
				throw new IllegalArgumentException("Illegal axis.");	
		}
	}
	
/* getters and setters *************************************************************************/
	
	public int getxAxisAttributeIndex() {
		return xAxisAttributeIndex;
	}

	public void setxAxisAttributeIndex(int xAxisAttributeIndex) {
		this.xAxisAttributeIndex = xAxisAttributeIndex;
		update(PROPERTY_X_AXIS_ATTRIBUTE_INDEX);
	}

	public int getyAxisAttributeIndex() {
		return yAxisAttributeIndex;
	}

	public void setyAxisAttributeIndex(int yAxisAttributeIndex) {
		this.yAxisAttributeIndex = yAxisAttributeIndex;
		update(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX);
	}
	
	public double getxAxisRangeLower() {
		return xAxisRangeLower;
	}

	public void setxAxisRangeLower(double xAxisRangeLower) {
		this.xAxisRangeLower = xAxisRangeLower;
		update(PROPERTY_X_AXIS_RANGE_LOWER);
	}

	public double getxAxisRangeUpper() {
		return xAxisRangeUpper;
	}

	public void setxAxisRangeUpper(double xAxisRangeUpper) {
		this.xAxisRangeUpper = xAxisRangeUpper;
		update(PROPERTY_X_AXIS_RANGE_UPPER);
	}

	public double getyAxisRangeLower() {
		return yAxisRangeLower;
	}

	public void setyAxisRangeLower(double yAxisRangeLower) {
		this.yAxisRangeLower = yAxisRangeLower;
		update(PROPERTY_Y_AXIS_RANGE_LOWER);
	}

	public double getyAxisRangeUpper() {
		return yAxisRangeUpper;
	}

	public void setyAxisRangeUpper(double yAxisRangeUpper) {
		this.yAxisRangeUpper = yAxisRangeUpper;
		update(PROPERTY_X_AXIS_RANGE_UPPER);
	}

	public ReversibleTransformation getTransformation() {
		return transformation;
	}

	public void setTransformation(ReversibleTransformation transformation) {
		this.transformation = transformation;
		update(PROPERTY_TRANSFORMATION);
	}

	public double[] getInputsSetting() {
		return inputsSetting;
	}

	public void setInputsSetting(double[] inputsSetting) {
		this.inputsSetting = inputsSetting;
		update(PROPERTY_INPUTS_SETTING);
	}

	public int getOutputPrecision() {
		return outputPrecision;
	}

	public void setOutputPrecision(int outputPrecision) {
		this.outputPrecision = outputPrecision;
		update(PROPERTY_OUTPUT_PRECISION);
	}

	public boolean[][] getDataVisible() {
		return dataVisible;
	}

	public void setDataVisible(boolean[][] dataVisible) {
		this.dataVisible = dataVisible;
		update(PROPERTY_DATA_VISIBLE);
	}

	public boolean[][] getModelsVisible() {
		return modelsVisible;
	}

	public void setModelsVisible(boolean[][] modelsVisible) {
		this.modelsVisible = modelsVisible;
		update(PROPERTY_MODELS_VISIBLE);
	}

	public boolean isLegendVisible() {
		return legendVisible;
	}

	public void setLegendVisible(boolean legendVisible) {
		this.legendVisible = legendVisible;
		update(PROPERTY_LEGEND_VISIBLE);
	}

	public boolean isAxesTickUnitsVisible() {
		return axesTickUnitsVisible;
	}

	public void setAxesTickUnitsVisible(boolean axesTickUnitsVisible) {
		this.axesTickUnitsVisible = axesTickUnitsVisible;
		update(PROPERTY_AXES_TICK_UNITS_VISIBLE);
	}
	
	public double[][] getActualAxesBounds() {
		return actualAxesBounds;
	}

	public ColorModel getColorModel() {
		return colorModel;
	}

	public void setColorModel(ColorModel colorModel) {
		if (colorModel != null) {
			colorModel.init(this);
		}

//		java.util.logging.Logger.global.log(java.util.logging.Level.INFO, "Color Model Changed: " + this.colorModel
//				+ " -> " + colorModel);
		
		this.colorModel = colorModel;
		update(PROPERTY_COLOR_MODEL);
	}

	public DotSizeModel getDotSizeModel() {
		return dotSizeModel;
	}

	public void setDotSizeModel(DotSizeModel dotSizeModel) {
		if (dotSizeModel != null) {
			dotSizeModel.init(this);
		}
		this.dotSizeModel = dotSizeModel;
		update(PROPERTY_DOT_SIZE_MODEL);
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
		update(PROPERTY_BACKGROUND_COLOR);
	}

	public AdditionalDrawer[] getAdditionalDrawers() {
		return additionalDrawers;
	}

	public void setAdditionalDrawers(AdditionalDrawer[] drawers) {
		this.additionalDrawers = drawers;
	}

	public boolean isGridVisible() {
		return gridVisible;
	}

	public void setGridVisible(boolean gridVisible) {
		this.gridVisible = gridVisible;
	}
}
