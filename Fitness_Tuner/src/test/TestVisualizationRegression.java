package test;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.AttributeInfoBase;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.MetadataBase;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter2.color.RegressionRainbowColorModel;
import org.fabi.visualizations.scatter2.sources.ExtendableScatterplotSource;
import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.math.array.StatisticSample;
import org.ytoh.configurations.ui.PropertyTable;

public class TestVisualizationRegression {
	
	public static void main(String[] args) {
		ScatterplotVisualization t = new ScatterplotVisualization(new ExtendableScatterplotSource(new ScatterplotSource() {

			double[][] out;
			double[][] in;
			
			@Override
			public int getDataSourceCount() {
				return 1;
			}

			@Override
			public int getModelSourceCount() {
				return 1;
			}

			@Override
			public int getInputsNumber() {
				return 3;
			}

			@Override
			public int getOutputsNumber() {
				return 1;
			}

			@Override
			public DataSource getDataSource(int index) {
				return new DataSource() {
					@Override public String getName() { return "Data"; }
					
					@Override
					public int outputsNumber() {
						// TODO Auto-generated method stub
						return 1;
					}
					
					@Override
					public int inputsNumber() {
						// TODO Auto-generated method stub
						return 3;
					}
					
					@Override
					public double[][] getOutputDataVectors() {
						if (out == null) {
							out = new double[30000][1];
							double[][] in = getInputDataVectors();
							for (int i = 0; i < in.length; i++) {
								out[i][0] = in[i][0];
							}
						}
						return out;
					}
					
					@Override
					public double[][] getInputDataVectors() {
						if (in == null) {
							double[] x = StatisticSample.randomNormal(30000, 0, 1);
							double[] y = StatisticSample.randomNormal(30000, 0, 1);
							double[] z = StatisticSample.randomNormal(30000, 0, 1);
							in = new double[30000][3];
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
					@Override public String getName() { return "Model"; }
					
					@Override
					public int outputsNumber() {
						return 1;
					}
					
					@Override
					public int inputsNumber() {
						return 3;
					}
					
					@Override
					public double[][] getModelResponses(double[][] inputs) {
						double[][] res = new double[inputs.length][1];
						for (int i = 0; i < inputs.length; i++) {
							res[i][0] = Math.sin(inputs[i][0] * inputs[i][1]) + inputs[i][2];
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
			
		}));
//		t.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new RainbowColorModel(-2.0, 2.0));
		t.setProperty(ScatterplotVisualization.PROPERTY_OUTPUT_PRECISION, 100);
		t.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, new RegressionRainbowColorModel());
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
//		t.setProperty(ScatterplotVisualization.PROPERTY_DOT_SIZE_MODEL, new org.fabi.visualizations.scatter2.DistanceDotSizeModel());
		PropertyTable tbl = new PropertyTable(t);
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.add(t.getVisualizationAsComponent(), BorderLayout.CENTER);
		f.add(tbl, BorderLayout.EAST);
		f.add(t.getControls(), BorderLayout.WEST);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024, 768);
        f.setVisible(true);
        
        try {
			ImageIO.write(t.getVisualizationAsImage(800, 600), "PNG", new File("D:\\Data\\Dokumenty\\Skola\\FIT-MI\\misc\\fakegame\\Results\\img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
