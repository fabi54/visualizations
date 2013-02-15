package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.MetadataBase;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter2.color.ColorModel;
import org.fabi.visualizations.scatter2.color.GradientColorModel;
import org.fabi.visualizations.scatter2.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.math.array.StatisticSample;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.ui.PropertyTable;


@Component(name="Test")
public class TestVisualization /* extends Visualization<String>*/ {
	
	/*public TestVisualization(String source, VisualizationConfig cfg) {
		super(source, cfg);
	}*/
	
	public static final String NAME = "A";
	
	@Property(name=NAME)
	private int a = 0;
	
	@Property(name="AAA")
	private String[] val = {"pvf", "Asw", "kgw"};
	
	@Property(name="tc")
	private boolean tc = true;
	
	public static void main(String[] args) {
		ScatterplotVisualization t = new ScatterplotVisualization(new ScatterplotSource() {

			double[][] out;
			double[][] in;
			
			@Override
			public int getDataSourceCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public int getModelSourceCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public int getInputsNumber() {
				// TODO Auto-generated method stub
				return 3;
			}

			@Override
			public int getOutputsNumber() {
				// TODO Auto-generated method stub
				return 2;
			}

			@Override
			public DataSource getDataSource(int index) {
				return new DataSource() {
					@Override public String getName() { return ""; }
					
					@Override
					public int outputsNumber() {
						// TODO Auto-generated method stub
						return 2;
					}
					
					@Override
					public int inputsNumber() {
						// TODO Auto-generated method stub
						return 3;
					}
					
					@Override
					public double[][] getOutputDataVectors() {
						if (out == null) {
//							double[] z = StatisticSample.randomNormal(1000, 0, 1);
//							out = new double[1000][1];
//							for (int i = 0; i < z.length; i++) {
//								out[i][0] = z[i];
//							}
							out = new double[1000][2];
							double[][] in = getInputDataVectors();
							for (int i = 0; i < in.length; i++) {
								out[i][0] = in[i][0];
								out[i][1] = in[i][1] - in[i][0];
								if (out[i][0] > out[i][1]) {
									out[i][1] = 0;
								} else {
									out[i][0] = 0;
								}
							}
//							double[] q = StatisticSample.randomUniform(30000, 0, 1);
//							for (int i = 0; i < q.length; i++) {
//								out[i][0] = q[i];
//							}
						}
						return out;
					}
					
					@Override
					public double[][] getInputDataVectors() {
						if (in == null) {
							double[] x = StatisticSample.randomNormal(1000, 0, 1);
							double[] y = StatisticSample.randomNormal(1000, 0, 1);
							double[] z = StatisticSample.randomNormal(1000, 0, 1);
							in = new double[1000][3];
							for (int i = 0; i < x.length; i++) {
								in[i][0] = x[i];
								in[i][1] = y[i];
								in[i][2] = z[i];
							}
						}
						return in;
					}
				};
			}

			@Override
			public ModelSource getModelSource(int index) {
				return new ModelSource() {
					@Override public String getName() { return ""; }
					
					@Override
					public int outputsNumber() {
						return 2;
					}
					
					@Override
					public int inputsNumber() {
						return 3;
					}
					
					@Override
					public double[][] getModelResponses(double[][] inputs) {
						double[][] res = new double[inputs.length][2];
						for (int i = 0; i < inputs.length; i++) {
							/*double r = 0.0;
							for (int j = 0; j < inputs[i].length; j++) {
								r += inputs[i][j] * inputs[i][j];
							}
							r = Math.sqrt(r);
							res[i][0] = r;*/
							res[i][0] = Math.sin(inputs[i][0] * inputs[i][1]) + inputs[i][2];
							res[i][1] = Math.sin(inputs[i][0] * inputs[i][1]) - inputs[i][2];
						}
						return res;
					}
				};
			}

			@Override
			public Metadata getMetadata() {
				return new MetadataBase() {
					
					@Override
					public void setTransformation(ReversibleTransformation transformation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public List<AttributeInfo> getOutputAttributeInfo() {
						List<AttributeInfo> res = new ArrayList<AttributeInfo>(1);
						res.add(new AttributeInfoBase("Out", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
						res.add(new AttributeInfoBase("OutXF", AttributeInfo.AttributeRole.STANDARD_OUTPUT));
						return res;
					}
						
					@Override
					public List<AttributeInfo> getInputAttributeInfo() {
						List<AttributeInfo> res = new ArrayList<AttributeInfo>(2);
						res.add(new AttributeInfoBase("First", AttributeInfo.AttributeRole.STANDARD_INPUT));
						res.add(new AttributeInfoBase("Second", AttributeInfo.AttributeRole.STANDARD_INPUT));
						res.add(new AttributeInfoBase("Third", AttributeInfo.AttributeRole.STANDARD_INPUT));
						return res;
					}
				};
			}
			
		});
//		t.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new RainbowColorModel(-2.0, 2.0));
		t.setProperty(ScatterplotVisualization.PROPERTY_OUTPUT_PRECISION, 100);
		t.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new GradientColorModel());
//		t.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new ColorModel() {
//			
//			@Override
//			public Color getColor(double[] inputs, double[] outputs, boolean data, int index,
//					int[] inputsIndices, int[] outputsIndices) {
//				if (data) {
//					Color c = new RainbowColorModel(0.0, 1.0).getColor(inputs, new double[]{outputs[0]}, data, index, inputsIndices, outputsIndices);
//					return new Color(c.getRed(), c.getBlue(), c.getGreen(), 50);
//				} else {
//					return new MonochromaticColorModel(new Color[]{Color.BLACK, Color.RED}, new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}).getColor(inputs, outputs, data, index, inputsIndices, outputsIndices);
//				}
//			}
//
//			@Override
//			public void init(ScatterplotSource source) {
//			}
//
//			@Override
//			public String getName() { return null;
//			}
//
//			@Override
//			public JComponent getControls() { return null;
//			}
//		});
//		t.setProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, ScatterplotVisualization.OUTPUT_AXIS);
		//t.setProperty(ScatterplotVisualization.PROPERTY_DOT_SIZE_MODEL, new DistanceDotSizeModel());
		PropertyTable tbl = new PropertyTable(t);
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(t.getVisualizationAsComponent(), BorderLayout.CENTER);
		f.add(tbl, BorderLayout.EAST);
		f.add(t.getControls(), BorderLayout.WEST);
        f.setSize(1024, 768);
        f.setVisible(true);
	}
	
	
}
