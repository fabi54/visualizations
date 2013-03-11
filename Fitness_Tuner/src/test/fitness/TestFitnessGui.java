package test.fitness;

import game.data.AbstractGameData;
import game.evolution.treeEvolution.evolutionControl.EvolutionUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.evolution.scatterplot.VisualizationEvolution;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.additional.AdditionalDrawer;
import org.fabi.visualizations.scatter.additional.HistogramAdditionalDrawer;
import org.fabi.visualizations.scatter.color.ColorModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

import test.artificialdata.onedimensional.ConstantLinearConstantData;

import configuration.CfgTemplate;
import configuration.models.single.PolynomialModelConfig;

public class TestFitnessGui extends JFrame {
	
	public ModelSource[] getModels(DataSource data) {
		CfgTemplate[] templates = new CfgTemplate[10];
		for (int i = 0; i < templates.length; i++) {
			PolynomialModelConfig cfg = new PolynomialModelConfig();
			cfg.setMaxDegree(i + 3);
			templates[i] = cfg;
		}
		ModelSource[] ms = new ModelSource[10];
		for (int i = 0; i < ms.length; i++) {
			ms[i] = ModGenTools.learnRegressionModel(templates[i], data);
		}
		return ms;
	}
	
	public static void main(String[] args) {
		File data = new File("C:\\Users\\janf\\Documents\\Skola\\Dip\\Project\\External\\data\\bosthouse.txt");
		final AbstractGameData d = EvolutionUtils.readDataFromFile(data.getAbsolutePath());

		DataSource ds = new DataSource() {
			
			@Override
			public int outputsNumber() {
				return d.getONumber();
			}
			
			@Override
			public int inputsNumber() {
				return d.getINumber();
			}
			
			@Override
			public double[][] getOutputDataVectors() {
				return d.getOutputAttrs();
			}
			
			@Override
			public String getName() {
				return "data";
			}
			
			double[][] iv;
			
			@Override
			public double[][] getInputDataVectors() {
				return d.getInputVectors();
//				if (iv == null) {
//					double[][] r = d.getInputVectors();
//					iv = new double[r.length][1];
//					for (int i = 0; i < iv.length; i++) {
//						iv[i][0] = r[i][0];
//					}
//				}
//				return iv;
			}
		};
		new TestFitnessGui(ds);
//		new TestFitnessGui(new ConstantLinearConstantData());
	}

	private static final long serialVersionUID = -5067283467357036835L;

	public TestFitnessGui(DataSource  data) {
//		DataSource data = new DataSource() {
//			
//			@Override
//			public int outputsNumber() {
//				return d.getONumber();
//			}
//			
//			@Override
//			public int inputsNumber() {
//				return d.getINumber();
//			}
//			
//			@Override
//			public double[][] getOutputDataVectors() {
//				return d.getOutputAttrs();
//			}
//			
//			@Override
//			public String getName() {
//				return "data";
//			}
//			
//			@Override
//			public double[][] getInputDataVectors() {
//				return d.getInputVectors();
//			}
//		};
		setLayout(new BorderLayout());
		setSize(new Dimension(1024, 768));
		VisualizationEvolution evolution = new VisualizationEvolution();
		VisualizationEvolution.POPULATION_SIZE = 100;
		VisualizationEvolution.STEPS = 1000;
		final ModelSource[] models = getModels(data);
		evolution.setModeller(new Modeller() {
			
			@Override
			public ModelSource[] getModels(DataSource data) {
				return models;
			}
		});
		final ScatterplotVisualization vis = evolution.evolve(data)[0]; // new ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{data}, models));
		vis.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		vis.setDotSizeModel(new MinkowskiDistanceDotSizeModel(2, 0.0, 100.0, 1, 3));
		vis.setColorModel(new ColorModel() {
			@Override public void init(ScatterplotSource source) { }
			@Override public void init(ScatterplotVisualization visualization) { }
			@Override public String getName() { return ""; }
			@Override public JComponent getControls() { return null; }
			
			@Override
			public Color getColor(double[] inputs, double[] outputs, boolean data,
					int index, int[] inputsIndices, int[] outputsIndices) {
				if (data) {
					return new Color(0.0f, 0.0f, 0.0f, 0.25f);
				} else {
					return new Color(1.0f, 0.0f, 0.0f, 1.0f);
				}
			}
		});
		
		double[][][] responses = LocalFitness.getResponses(vis, models);
		double[][] inputs = LocalFitness.getModelInputs(vis);
		
		double[][] histogram = new double[inputs.length - 1][2];
		for (int z = 0; z < histogram.length; z++) {
			histogram[z][0] = inputs[z + 1][vis.getxAxisAttributeIndex()];
			histogram[z][1] = org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.evaluateLocalAt(responses, z + 1, vis);
		}
		double w = inputs[1][vis.getxAxisAttributeIndex()] - inputs[0][vis.getxAxisAttributeIndex()];
		vis.setAdditionalDrawers(new AdditionalDrawer[]{new HistogramAdditionalDrawer(vis, "Fitness", new Color(1.0f, 0.0f, 0.0f, 1.0f), histogram, w, 0.1)});
		add(vis.getVisualizationAsComponent(), BorderLayout.CENTER);
		add(vis.getControls(), BorderLayout.WEST);
		final JLabel l = new JLabel();
		add(l, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction f = new org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction(models, data);
		vis.addPropertyChangeListener(new String[]{ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER,
			ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER, ScatterplotVisualization.PROPERTY_INPUTS_SETTING,
			ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX}, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println(evt.getPropertyName());
					double[][][] responses = org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.getResponses(vis, models);
					l.setText(f.getFitness(new ChromosomeBase() {
						
						@Override
						public int compareTo(Chromosome arg0) {	return 0;}
						
						@Override
						public void mutate(double probability) {						}
						
						@Override
						public Object getPhenotype() {
							return vis;
						}
						
						@Override
						public void cross(Chromosome other) {		}
						
						@Override
						public Chromosome copy() {return null;						}
					}) + "/ size: " +
						org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.evaluateGlobal(responses, vis)
						+ ", local sum: "+ org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.evaluateLocal(responses, vis));
				}
		});
		setVisible(true);
	}
}
