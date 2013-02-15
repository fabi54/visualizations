package org.fabi.visualizations.scatter_old;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.MultiModelSource;
import org.fabi.visualizations.tools.jfree.Classifier2DRenderer;
import org.fabi.visualizations.tools.jfree.CustomBubbleRenderer;
import org.fabi.visualizations.tools.jfree.CustomBubbleRendererConfiguration;
import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.transformation.TransformationProvider;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

// TODO implement org.ytoh.configurations
@Component(name="Scatterplot")
public class ScatterplotVisualization extends Visualization<DatasetGenerator> {

	public ScatterplotVisualization(DatasetGenerator generator) {
		super(generator);
		this.origConfig = getDefaultConfig();
	}
	
	public ScatterplotVisualization(DatasetGenerator generator, VisualizationConfig cfg) {
		super(generator, cfg);
		this.origConfig = cfg;
	}
	
	@Override
	public String toString() {
		return "Scatterplot";
	}
	
	DatasetGenerator getSource() {
		return source;
	}

	
/* *********************************************************************************************/
	
	protected ChartPanel chartPanel = null;
	protected List<Refreshable> controls = new ArrayList<Refreshable>();
	protected VisualizationConfig origConfig;
	
	@Override
	public void setProperty(String key, Object value) {
		VisualizationConfig cfg = getConfig();
		cfg.setTypedProperty(key, value);
		List<String> changedList = new ArrayList<String>(1);
		changedList.add(key);
		setConfiguration(cfg, changedList);
	}
	
	@Override
	public JComponent getControls() {
		XAxisAttributeSelectorComboBox xAxisAttributeIndexComboBox = new XAxisAttributeSelectorComboBox(this);
		YAxisAttributeSelectorComboBox yAxisAttributeIndexComboBox = new YAxisAttributeSelectorComboBox(this);
		TransformationSelectorComboBox transformationComboBox = new TransformationSelectorComboBox(this);
		InputsSettingPanel inputsPanel = new InputsSettingPanel(this);
		VisibleModelList visibleModelList = null;
		DisplayMultipleCheckBox displayMultipleCheckBox = null;
		//VisibleOutputsList visibleOutputsList = null;
		if (source.modelSource instanceof MultiModelSource) {
			displayMultipleCheckBox = new DisplayMultipleCheckBox(this);
			visibleModelList = new VisibleModelList(this);
			controls.add(visibleModelList);
			controls.add(displayMultipleCheckBox);
		}
		/*if (source.getOutputsNumber() > 1) {
			visibleOutputsList = new VisibleOutputsList(this);
			controls.add(visibleOutputsList);
		}*/
		controls.add(xAxisAttributeIndexComboBox);
		controls.add(yAxisAttributeIndexComboBox);
		controls.add(transformationComboBox);
		controls.add(inputsPanel);
		
		JButton restore = new JButton("Restore defaults");
		restore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setConfig(origConfig);
			}
		});
		
		JPanel result = new JPanel(new GridBagLayout());
		
		int gridy = 0;
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = gridy;
		JLabel l1 = new JLabel("Transformation:");
		l1.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		result.add(l1, c);
		c.gridx = 1;
		result.add(transformationComboBox, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		JLabel l2 = new JLabel("X-Axis:");
		l2.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		result.add(l2, c);
		c.gridx = 1;
		result.add(xAxisAttributeIndexComboBox, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		JLabel l3 = new JLabel("Y-Axis:");
		l3.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		result.add(l3, c);
		c.gridx = 1;
		result.add(yAxisAttributeIndexComboBox, c);
		c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 2;
		result.add(inputsPanel, c);
		/*c.gridx = 0;
		c.gridy = ++gridy;
		c.gridwidth = 1;
		JLabel l4 = new JLabel("Jitter:");
		l4.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		result.add(l4, c);
		c.gridx = 1;
		result.add(new JitterSlider(this), c);*/
		if (source.modelSource instanceof MultiModelSource) {
			c.gridx = 0;
			c.gridy = ++gridy;
			c.gridwidth = 2;
			JLabel l4 = new JLabel("Visible models:");
			l4.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			result.add(l4, c);
			c.gridx = 0;
			c.gridy = ++gridy;
			c.gridwidth = 2;
			result.add(visibleModelList, c);
			c.gridx = 0;
			c.gridy = ++gridy;
			c.gridwidth = 2;
			result.add(displayMultipleCheckBox, c);
		}
		/*if (source.getOutputsNumber() > 1) {
			c.gridx = 0;
			c.gridy = ++gridy;
			c.gridwidth = 2;
			JLabel l4 = new JLabel("Visible outputs:");
			l4.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			result.add(l4, c);
			c.gridx = 0;
			c.gridy = ++gridy;
			c.gridwidth = 2;
			result.add(visibleOutputsList, c);
		}*/
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = ++gridy;
		result.add(restore, c);
		JPanel parent = new JPanel();
		parent.add(result);
		return parent;
	}

	@Override
	public JComponent getVisualizationAsComponent() {
		chartPanel = new ChartPanel(getChart());
		return chartPanel;
	}

	@Override
	public BufferedImage getVisualizationAsImage(int width, int height) {
		JFreeChart chart = getChart();
		chart.setBackgroundPaint(new Color(0, 0, 0, 0));
		return chart.createBufferedImage(width, height);
	}

	@Override
	public BufferedImage getVisualizationAsImage() {
		return getVisualizationAsImage(preferredImageWidth, preferredImageHeight);
	}

	
	protected static final double RANGE_SCALE_RATIO = 1.1;
	
	public void updateActualAxesRanges() {
		if (!Double.isNaN(xAxisRangeLower) && !Double.isNaN(xAxisRangeUpper)) {
			actualXAxisRange = new Range(xAxisRangeLower, xAxisRangeUpper);
		} else {
			actualXAxisRange = Arrays.getBounds(source.getTransformedInputs(), xAxisIndex);
		}
		if (!Double.isNaN(yAxisRangeLower) && !Double.isNaN(yAxisRangeUpper)) {
			actualYAxisRange = new Range(yAxisRangeLower, yAxisRangeUpper);
		} else { // TODO if dataSource == null
			if (yAxisInput) {
				actualYAxisRange = Arrays.getBounds(source.getTransformedInputs(), yAxisIndex);
			} else {
				actualYAxisRange = Arrays.getBounds(source.dataSource.getOutputDataVectors());
			}
		}
		if (actualXAxisRange == null) {
			actualXAxisRange = new Range(0.0, 1.0);
		}
		if (actualYAxisRange == null) {
			actualYAxisRange = new Range(0.0, 1.0);
		}
		actualXAxisRange = scaleSymetrical(actualXAxisRange, RANGE_SCALE_RATIO);
		actualYAxisRange = scaleSymetrical(actualYAxisRange, RANGE_SCALE_RATIO);
	}
	
	protected void addDataDataset(XYPlot plot, int datasetIndex) {
		XYDataset data = source.getData(this, true);
		CustomBubbleRenderer dataRenderer = new CustomBubbleRenderer(data.getSeriesCount());
        double minz = Double.MAX_VALUE;
        double maxz = - Double.MAX_VALUE;
        if (data instanceof XYZDataset) {
            XYZDataset dataset = (XYZDataset) data;
            for (int series = 0; series < dataset.getSeriesCount(); series++) {
                for (int item = 0; item < dataset.getItemCount(series); item++) {
                    double z = dataset.getZValue(series, item);
                    minz = Math.min(minz, z);
                    maxz = Math.max(maxz, z);
                }
            }
        }
        dataRenderer.setConfiguration(new CustomBubbleRendererConfiguration(minz, maxz, 10, 2));
		plot.setDataset(datasetIndex, data);
		plot.setRenderer(datasetIndex, dataRenderer);
	}
	
	protected void addModelDataset(XYPlot plot, XYDataset model, int datasetIndex) {
		plot.setDataset(datasetIndex, model);
		if (yAxisInput) {
			Classifier2DRenderer modelRenderer = new Classifier2DRenderer(model.getSeriesCount());
			double xStep = actualXAxisRange.getLength() / outputPrecision;
			double yStep = actualYAxisRange.getLength() / outputPrecision;
			modelRenderer.init(xStep, yStep);
			modelRenderer.setShowConfidencies(showConfidences);
			modelRenderer.setLowerLimit(confidencesLower);
			modelRenderer.setUpperLimit(confidencesUpper);
			plot.setRenderer(datasetIndex, modelRenderer);
		} else {
			XYSplineRenderer modelRenderer = new XYSplineRenderer();
            modelRenderer.setBaseLinesVisible(true);
            modelRenderer.setBaseShapesVisible(false);
            plot.setRenderer(datasetIndex, modelRenderer);
		}
	}
	
	protected JFreeChart getChart() {
		XYPlot plot = new XYPlot();
		ValueAxis domainAxis = new NumberAxis();
		ValueAxis rangeAxis = new NumberAxis();
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(rangeAxis);
		updateActualAxesRanges();
		domainAxis.setRange(actualXAxisRange);
		rangeAxis.setRange(actualYAxisRange);
		JFreeChart chart = new JFreeChart("", plot);
		
		int datasetIndex = 0;

		if (yAxisInput && dataVisible) {
			addDataDataset(plot, datasetIndex++);
		}
		if (displayMultiple && source.modelSource instanceof MultiModelSource) {
			MultiModelSource s = (MultiModelSource) source.modelSource;
			for (int i = 0; i < s.getModelCount(); i++) {
				if (modelVisible.get(i)) {
					XYDataset model = source.getModelResponse(this, i);
					addModelDataset(plot, model, datasetIndex++);
				}
			}
		} else if (modelVisible.get(0)) {
			XYDataset model = source.getModelResponse(this);
			addModelDataset(plot, model, datasetIndex++);
		}
		if (!yAxisInput && dataVisible) {
			addDataDataset(plot, datasetIndex++);
		}
		
		if (!axesTickUnitsVisible) {
			domainAxis.setTickLabelsVisible(false);
			rangeAxis.setTickLabelsVisible(false);
			/*TickUnits domainTickUnits = new TickUnits();
			domainTickUnits.add(new NumberTickUnit(xAxisRange.getLowerBound() - 1.0));
			domainTickUnits.add(new NumberTickUnit(xAxisRange.getUpperBound() + 1.0));
			domainAxis.setStandardTickUnits(domainTickUnits);
			TickUnits rangeTickUnits = new TickUnits();
			rangeTickUnits.add(new NumberTickUnit(yAxisRange.getLowerBound() - 1.0));
			rangeTickUnits.add(new NumberTickUnit(yAxisRange.getUpperBound() + 1.0));
			rangeAxis.setStandardTickUnits(rangeTickUnits);*/
		} else {
			domainAxis.setLabel(source.getMetadata().getAttributeInfo().get(xAxisIndex).getName());
			if (yAxisInput) {
				rangeAxis.setLabel(source.getMetadata().getAttributeInfo().get(yAxisIndex).getName());
			} else {
				List<AttributeInfo> attributes = source.getMetadata().getOutputAttributeInfo();
				if (attributes.size() == 1) {
					rangeAxis.setLabel(attributes.get(0).getName());
				} else {
					rangeAxis.setLabel("<output>");
				}
			}
			/*domainAxis.setAutoTickUnitSelection(true);
			rangeAxis.setAutoTickUnitSelection(true);*/
		}
		if (!legendVisible) {
			chart.removeLegend();
		}
		//chart.addChangeListener(new RangeChartChangeListener(plot));
		return chart;
	}
	
	protected static Range scaleSymetrical(Range orig, double ratio) {
		double delta = ((RANGE_SCALE_RATIO - 1.0) * orig.getLength()) / 2.0;
		return new Range(orig.getLowerBound() - delta, orig.getUpperBound() + delta);
	}
	
//	@Override
	protected void update() {
		if (chartPanel != null) {
			chartPanel.setChart(getChart());
			for (Refreshable r : controls) {
				r.refresh();
			}
		}
	}
	
/* configurations ******************************************************************************/
	
	
	public static final String PROPERTY_X_AXIS_ATTRIBUTE_INDEX = "x_axis_attribute_index";
	public static final String PROPERTY_Y_AXIS_ATTRIBUTE_INDEX = "y_axis_attribute_index";
	public static final String PROPERTY_X_AXIS_RANGE_LOWER = "x_axis_range_lower";
	public static final String PROPERTY_X_AXIS_RANGE_UPPER = "x_axis_range_upper";
	public static final String PROPERTY_Y_AXIS_RANGE_LOWER = "y_axis_range_lower";
	public static final String PROPERTY_Y_AXIS_RANGE_UPPER = "y_axis_range_upper";
	public static final String PROPERTY_PREFERRED_IMAGE_WIDTH = "preferred_image_width";
	public static final String PROPERTY_PREFERRED_IMAGE_HEIGHT = "preferred_image_height";
	public static final String PROPERTY_TRANSFORMATION = "transformation";
	public static final String PROPERTY_Y_AXIS_INPUT = "y_axis_input";
	public static final String PROPERTY_INPUTS_SETTING = "inputs_setting";
	public static final String PROPERTY_OUTPUT_PRECISION = "output_precision";
	public static final String PROPERTY_JITTER_AMOUNT = "jitter_amount";
	public static final String PROPERTY_DATA_VISIBLE = "data_visible";
	public static final String PROPERTY_MODELS_VISIBLE = "model_visible";
	public static final String PROPERTY_DISPLAY_MULTIPLE_MODELS = "multiple_modeles";
	public static final String PROPERTY_OUTPUTS_VISIBLE = "outputs_visible";
	public static final String PROPERTY_LEGEND_VISIBLE = "legend_visible";
	public static final String PROPERTY_AXES_TICK_UNITS_VISIBLE = "axes_tick_units_visible";
	public static final String PROPERTY_SHOW_CONFIDENCES = "show_confidences";
	public static final String PROPERTY_CONFIDENCES_LOWER = "confidences_range_lower";
	public static final String PROPERTY_CONFIDENCES_UPPER = "confidences_range_upper";
		
	static {
		VisualizationConfig.addTypeCast(PROPERTY_X_AXIS_ATTRIBUTE_INDEX, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_X_AXIS_RANGE_LOWER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_X_AXIS_RANGE_UPPER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_Y_AXIS_RANGE_LOWER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_Y_AXIS_RANGE_UPPER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_TRANSFORMATION, String.class);
		VisualizationConfig.addTypeCast(PROPERTY_Y_AXIS_INPUT, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_OUTPUT_PRECISION, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_JITTER_AMOUNT, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_DATA_VISIBLE, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_DISPLAY_MULTIPLE_MODELS, Boolean.class);
		VisualizationConfig.addTypeCastList(PROPERTY_MODELS_VISIBLE, Boolean.class);
		VisualizationConfig.addTypeCastList(PROPERTY_OUTPUTS_VISIBLE, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_LEGEND_VISIBLE, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_AXES_TICK_UNITS_VISIBLE, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_SHOW_CONFIDENCES, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_CONFIDENCES_LOWER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_CONFIDENCES_UPPER, Double.class);
		VisualizationConfig.addTypeCast(PROPERTY_PREFERRED_IMAGE_WIDTH, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_PREFERRED_IMAGE_HEIGHT, Integer.class);
		VisualizationConfig.addTypeCastList(PROPERTY_INPUTS_SETTING, Double.class);
	}

	@Override
	public VisualizationConfig getDefaultConfig() {
		VisualizationConfig defaultConfig = new VisualizationConfig(this.getClass());
		defaultConfig.setTypedProperty(PROPERTY_X_AXIS_ATTRIBUTE_INDEX, 0);
		defaultConfig.setTypedProperty(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, 1);
		defaultConfig.setTypedProperty(PROPERTY_X_AXIS_RANGE_LOWER, new Double(Double.NaN));
		defaultConfig.setTypedProperty(PROPERTY_X_AXIS_RANGE_UPPER, new Double(Double.NaN));
		defaultConfig.setTypedProperty(PROPERTY_Y_AXIS_RANGE_LOWER, new Double(Double.NaN));
		defaultConfig.setTypedProperty(PROPERTY_Y_AXIS_RANGE_UPPER, new Double(Double.NaN));
		defaultConfig.setTypedProperty(PROPERTY_Y_AXIS_INPUT, true);
		defaultConfig.setTypedProperty(PROPERTY_DISPLAY_MULTIPLE_MODELS, true);
		defaultConfig.setTypedProperty(PROPERTY_OUTPUT_PRECISION, 20);
		defaultConfig.setTypedProperty(PROPERTY_TRANSFORMATION, TransformationProvider.NO_TRANSFORMATION);
		defaultConfig.setTypedProperty(PROPERTY_INPUTS_SETTING, new ArrayList<Double>());
		defaultConfig.setTypedProperty(PROPERTY_JITTER_AMOUNT, 0);
		defaultConfig.setTypedProperty(PROPERTY_DATA_VISIBLE, true);
		List<Boolean> modelVisible = new ArrayList<Boolean>(1);
		int modelCnt = 1;
		if (source.modelSource != null && source.modelSource instanceof MultiModelSource) {
			modelCnt = ((MultiModelSource) source.modelSource).getModelCount();
			for (int i = 0; i < modelCnt; i++) {
				modelVisible.add(true);
			}
		} else {
			modelVisible.add(true);
		}
		defaultConfig.setTypedProperty(PROPERTY_MODELS_VISIBLE, modelVisible);
		int outputsNr = source.getOutputsNumber();
		List<Boolean> outputsVisible = new ArrayList<Boolean>(outputsNr);
		for (int i = 0; i < outputsNr; i++) {
			outputsVisible.add(true);
		}
		defaultConfig.setTypedProperty(PROPERTY_OUTPUTS_VISIBLE, outputsVisible);
		defaultConfig.setTypedProperty(PROPERTY_LEGEND_VISIBLE, true);
		defaultConfig.setTypedProperty(PROPERTY_AXES_TICK_UNITS_VISIBLE, true);
		defaultConfig.setTypedProperty(PROPERTY_SHOW_CONFIDENCES, true);
		defaultConfig.setTypedProperty(PROPERTY_PREFERRED_IMAGE_WIDTH, 500);
		defaultConfig.setTypedProperty(PROPERTY_PREFERRED_IMAGE_HEIGHT, 400);
		defaultConfig.setTypedProperty(PROPERTY_CONFIDENCES_LOWER, 0.0);
		defaultConfig.setTypedProperty(PROPERTY_CONFIDENCES_UPPER, 1.0);
		return defaultConfig;
	}

	@Property(name=PROPERTY_X_AXIS_ATTRIBUTE_INDEX)
	@org.ytoh.configurations.annotations.Range(from=0, to=Integer.MAX_VALUE)
	private int xAxisIndex;
	
	@Property(name=PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)
	@org.ytoh.configurations.annotations.Range(from=0, to=Integer.MAX_VALUE)
	private int yAxisIndex;

	@Property(name=(PROPERTY_X_AXIS_RANGE_LOWER))
	private double xAxisRangeLower;
	@Property(name=(PROPERTY_X_AXIS_RANGE_UPPER))
	private double xAxisRangeUpper;
	private Range actualXAxisRange;
	
	@Property(name=PROPERTY_Y_AXIS_RANGE_LOWER)
	private double yAxisRangeLower;
	@Property(name=PROPERTY_Y_AXIS_RANGE_UPPER)
	private double yAxisRangeUpper;
	private Range actualYAxisRange;
	
	@Property(name=PROPERTY_CONFIDENCES_LOWER)
	private double confidencesLower;
	@Property(name=PROPERTY_CONFIDENCES_UPPER)
	private double confidencesUpper;
	
	@Property(name=PROPERTY_Y_AXIS_INPUT)
	private boolean yAxisInput;

	@Property(name=PROPERTY_OUTPUT_PRECISION)
	@org.ytoh.configurations.annotations.Range(from=0,to=10000)
	private int outputPrecision;

	@Property(name=PROPERTY_INPUTS_SETTING)
	private List<Double> inputsSetting;

	@Property(name=PROPERTY_DATA_VISIBLE)
	private boolean dataVisible;

	@Property(name=PROPERTY_DISPLAY_MULTIPLE_MODELS)
	private boolean displayMultiple;

	@Property(name=PROPERTY_MODELS_VISIBLE)
	private List<Boolean> modelVisible;

	@Property(name=PROPERTY_OUTPUTS_VISIBLE)
	private List<Boolean> outputsVisible;

	@Property(name=PROPERTY_LEGEND_VISIBLE)
	private boolean legendVisible;

	@Property(name=PROPERTY_AXES_TICK_UNITS_VISIBLE)
	private boolean axesTickUnitsVisible;

	@Property(name=PROPERTY_SHOW_CONFIDENCES)
	private boolean showConfidences;

	@Property(name=PROPERTY_PREFERRED_IMAGE_WIDTH)
	private int preferredImageWidth;

	@Property(name=PROPERTY_PREFERRED_IMAGE_HEIGHT)
	private int preferredImageHeight;

	@SuppressWarnings("unchecked")
	@Override
	public VisualizationConfig getConfig() {
		VisualizationConfig cfg = new VisualizationConfig(this.getClass());
		cfg.setTypedProperty(PROPERTY_X_AXIS_ATTRIBUTE_INDEX, xAxisIndex);
		cfg.setTypedProperty(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, yAxisIndex);
		cfg.setTypedProperty(PROPERTY_X_AXIS_RANGE_LOWER, xAxisRangeLower);
		cfg.setTypedProperty(PROPERTY_X_AXIS_RANGE_UPPER, xAxisRangeUpper);
		cfg.setTypedProperty(PROPERTY_Y_AXIS_RANGE_LOWER, yAxisRangeLower);
		cfg.setTypedProperty(PROPERTY_Y_AXIS_RANGE_UPPER, yAxisRangeUpper);
		cfg.setTypedProperty(PROPERTY_TRANSFORMATION, source.getTransformation());
		cfg.setTypedProperty(PROPERTY_OUTPUT_PRECISION, outputPrecision);
		@SuppressWarnings("rawtypes")
		List inputs = new ArrayList();
		for (Double d : inputsSetting) {
			inputs.add(d);
		}
		cfg.setTypedProperty(PROPERTY_INPUTS_SETTING, inputs);
		cfg.setTypedProperty(PROPERTY_Y_AXIS_INPUT, yAxisInput);
		cfg.setTypedProperty(PROPERTY_JITTER_AMOUNT, source.getJitterAmount());
		cfg.setTypedProperty(PROPERTY_DATA_VISIBLE, dataVisible);
		cfg.setTypedProperty(PROPERTY_DISPLAY_MULTIPLE_MODELS, displayMultiple);
		cfg.setTypedProperty(PROPERTY_MODELS_VISIBLE, modelVisible);
		cfg.setTypedProperty(PROPERTY_OUTPUTS_VISIBLE, outputsVisible);
		cfg.setTypedProperty(PROPERTY_LEGEND_VISIBLE, legendVisible);
		cfg.setTypedProperty(PROPERTY_AXES_TICK_UNITS_VISIBLE, axesTickUnitsVisible);
		cfg.setTypedProperty(PROPERTY_SHOW_CONFIDENCES, showConfidences);
		cfg.setTypedProperty(PROPERTY_CONFIDENCES_LOWER, confidencesLower);
		cfg.setTypedProperty(PROPERTY_CONFIDENCES_UPPER, confidencesUpper);
		cfg.setTypedProperty(PROPERTY_PREFERRED_IMAGE_WIDTH, preferredImageWidth);
		cfg.setTypedProperty(PROPERTY_PREFERRED_IMAGE_HEIGHT, preferredImageHeight);
		return cfg;
	}

	@Override
	protected void setConfiguration(VisualizationConfig cfg, Collection<String> changedProperties) {
		xAxisIndex = cfg.<Integer>getTypedProperty(PROPERTY_X_AXIS_ATTRIBUTE_INDEX);
		yAxisIndex = cfg.<Integer>getTypedProperty(PROPERTY_Y_AXIS_ATTRIBUTE_INDEX);
		xAxisRangeLower = cfg.<Double>getTypedProperty(PROPERTY_X_AXIS_RANGE_LOWER);
		xAxisRangeUpper = cfg.<Double>getTypedProperty(PROPERTY_X_AXIS_RANGE_UPPER);
		yAxisRangeLower = cfg.<Double>getTypedProperty(PROPERTY_Y_AXIS_RANGE_LOWER);
		yAxisRangeUpper = cfg.<Double>getTypedProperty(PROPERTY_Y_AXIS_RANGE_UPPER);
		source.setTransformation(cfg.<String>getTypedProperty(PROPERTY_TRANSFORMATION));
		outputPrecision = cfg.<Integer>getTypedProperty(PROPERTY_OUTPUT_PRECISION);
		@SuppressWarnings("rawtypes")
		List inputs = cfg.<List>getTypedProperty(PROPERTY_INPUTS_SETTING);
		List<Double> inputsSettingList = new ArrayList<Double>(source.getInputsNumber());
		if (inputs.size() != source.getInputsNumber()) {
			for (int i = 0; i < source.getInputsNumber(); i++) {
				inputsSettingList.add(new Double(0));
			}
			setInputsSetting(inputsSettingList);
		} else {
			for (int i = 0; i < source.getInputsNumber(); i++) {
				// supposing that all elements of inputsSetting are of type double
				inputsSettingList.add((Double) inputs.get(i));
			}
			inputsSetting = inputsSettingList;
		}
		yAxisInput = cfg.<Boolean>getTypedProperty(PROPERTY_Y_AXIS_INPUT);
		int jitterAmount = cfg.<Integer>getTypedProperty(PROPERTY_JITTER_AMOUNT);
		source.setJitterAmount(jitterAmount);
		dataVisible = cfg.<Boolean>getTypedProperty(PROPERTY_DATA_VISIBLE);
		@SuppressWarnings("rawtypes")
		List models = cfg.<List>getTypedProperty(PROPERTY_MODELS_VISIBLE);
		modelVisible = new ArrayList<Boolean>(models.size());
		for (int i = 0; i < models.size(); i++) {
			modelVisible.add((Boolean) models.get(i));
		}
		@SuppressWarnings("rawtypes")
		List outputs = cfg.<List>getTypedProperty(PROPERTY_OUTPUTS_VISIBLE);
		outputsVisible = new ArrayList<Boolean>(source.getOutputsNumber());
		for (int i = 0; i < source.getOutputsNumber(); i++) {
			outputsVisible.add((Boolean) outputs.get(i));
		}
		legendVisible = cfg.<Boolean>getTypedProperty(PROPERTY_LEGEND_VISIBLE);
		displayMultiple = cfg.<Boolean>getTypedProperty(PROPERTY_DISPLAY_MULTIPLE_MODELS);
		axesTickUnitsVisible = cfg.<Boolean>getTypedProperty(PROPERTY_AXES_TICK_UNITS_VISIBLE);
		showConfidences = cfg.<Boolean>getTypedProperty(PROPERTY_SHOW_CONFIDENCES);
		confidencesLower = cfg.<Double>getTypedProperty(PROPERTY_CONFIDENCES_LOWER);
		confidencesUpper = cfg.<Double>getTypedProperty(PROPERTY_CONFIDENCES_UPPER);
		preferredImageWidth = cfg.<Integer>getTypedProperty(PROPERTY_PREFERRED_IMAGE_WIDTH);
		preferredImageHeight = cfg.<Integer>getTypedProperty(PROPERTY_PREFERRED_IMAGE_HEIGHT);
		update();
	}
	
/* getters & setters ***************************************************************************/	

	public void setInputsSetting(List<Double> inputsSetting) {
		this.inputsSetting = inputsSetting;
		update();
	}
	
	public List<String> getModelNames() {
		List<String> list = null;
		if (source.modelSource instanceof MultiModelSource) {
			list = new ArrayList<String>(1);
			int size = ((MultiModelSource) source.modelSource).getModelCount();
			for (int i = 0; i < size; i++) {
			list.add("Model_" + i);
			}
			return list;
		} else {
			list = new ArrayList<String>(1);
			list.add("Model");
			return list;
		}
	}
	
	public List<String> getOutputNames() {
		List<AttributeInfo> attributes = source.metadata.getOutputAttributeInfo();
		List<String> res = new ArrayList<String>(attributes.size());
		for (AttributeInfo info : attributes) {
			res.add(info.getName());
		}
		return res;
	}
	
	public Range getActualXAxisRange() {
		return actualXAxisRange;
	}
	
	public Range getActualYAxisRange() {
		return actualYAxisRange;
	}
	
/* getters & setters (2) ********************************************************************/	

	public int getxAxisIndex() {
		return xAxisIndex;
	}

	public void setxAxisIndex(int xAxisIndex) {
		this.xAxisIndex = xAxisIndex;
	}

	public int getyAxisIndex() {
		return yAxisIndex;
	}

	public void setyAxisIndex(int yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}

	public double getxAxisRangeLower() {
		return xAxisRangeLower;
	}

	public void setxAxisRangeLower(double xAxisRangeLower) {
		this.xAxisRangeLower = xAxisRangeLower;
	}

	public double getxAxisRangeUpper() {
		return xAxisRangeUpper;
	}

	public void setxAxisRangeUpper(double xAxisRangeUpper) {
		this.xAxisRangeUpper = xAxisRangeUpper;
	}

	public double getyAxisRangeLower() {
		return yAxisRangeLower;
	}

	public void setyAxisRangeLower(double yAxisRangeLower) {
		this.yAxisRangeLower = yAxisRangeLower;
	}

	public double getyAxisRangeUpper() {
		return yAxisRangeUpper;
	}

	public void setyAxisRangeUpper(double yAxisRangeUpper) {
		this.yAxisRangeUpper = yAxisRangeUpper;
	}

	public double getConfidencesLower() {
		return confidencesLower;
	}

	public void setConfidencesLower(double confidencesLower) {
		this.confidencesLower = confidencesLower;
	}

	public double getConfidencesUpper() {
		return confidencesUpper;
	}

	public void setConfidencesUpper(double confidencesUpper) {
		this.confidencesUpper = confidencesUpper;
	}

	public boolean isyAxisInput() {
		return yAxisInput;
	}

	public void setyAxisInput(boolean yAxisInput) {
		this.yAxisInput = yAxisInput;
	}

	public int getOutputPrecision() {
		return outputPrecision;
	}

	public void setOutputPrecision(int outputPrecision) {
		this.outputPrecision = outputPrecision;
	}

	public boolean isDataVisible() {
		return dataVisible;
	}

	public void setDataVisible(boolean dataVisible) {
		this.dataVisible = dataVisible;
	}

	public boolean isDisplayMultiple() {
		return displayMultiple;
	}

	public void setDisplayMultiple(boolean displayMultiple) {
		this.displayMultiple = displayMultiple;
	}

	public List<Boolean> getModelVisible() {
		return modelVisible;
	}

	public void setModelVisible(List<Boolean> modelVisible) {
		this.modelVisible = modelVisible;
	}

	public List<Boolean> getOutputsVisible() {
		return outputsVisible;
	}

	public void setOutputsVisible(List<Boolean> outputsVisible) {
		this.outputsVisible = outputsVisible;
	}

	public boolean isLegendVisible() {
		return legendVisible;
	}

	public void setLegendVisible(boolean legendVisible) {
		this.legendVisible = legendVisible;
	}

	public boolean isAxesTickUnitsVisible() {
		return axesTickUnitsVisible;
	}

	public void setAxesTickUnitsVisible(boolean axesTickUnitsVisible) {
		this.axesTickUnitsVisible = axesTickUnitsVisible;
	}

	public boolean isShowConfidences() {
		return showConfidences;
	}

	public void setShowConfidences(boolean showConfidences) {
		this.showConfidences = showConfidences;
	}

	public int getPreferredImageWidth() {
		return preferredImageWidth;
	}

	public void setPreferredImageWidth(int preferredImageWidth) {
		this.preferredImageWidth = preferredImageWidth;
	}

	public int getPreferredImageHeight() {
		return preferredImageHeight;
	}

	public void setPreferredImageHeight(int preferredImageHeight) {
		this.preferredImageHeight = preferredImageHeight;
	}

	public List<Double> getInputsSetting() {
		return inputsSetting;
	}
	
}
