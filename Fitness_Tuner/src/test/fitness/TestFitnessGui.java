package test.fitness;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import game.data.AbstractGameData;
import game.evolution.treeEvolution.evolutionControl.EvolutionUtils;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeBase;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.VisualizationEvolution;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.ModGenTools;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

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
		new TestFitnessGui(data);
	}

	private static final long serialVersionUID = -5067283467357036835L;

	public TestFitnessGui(File src) {
		final AbstractGameData d = EvolutionUtils.readDataFromFile(src.getAbsolutePath());

		DataSource data = new DataSource() {
			
			@Override
			public int outputsNumber() {
				return d.getONumber();
			}
			
			@Override
			public int inputsNumber() {
				return 1;
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
				if (iv == null) {
					double[][] r = d.getInputVectors();
					iv = new double[r.length][1];
					for (int i = 0; i < iv.length; i++) {
						iv[i][0] = r[i][0];
					}
				}
				return iv;
			}
		};
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
		final ScatterplotVisualization vis = evolution.evolve(data)[0];
		vis.setSource(new ScatterplotSourceBase(new DataSource[]{data}, models));
		vis.setDotSizeModel(new MinkowskiDistanceDotSizeModel(2, 0.0, 100.0, 1, 3));
		add(vis.getVisualizationAsComponent(), BorderLayout.CENTER);
		add(vis.getControls(), BorderLayout.WEST);
		final JLabel l = new JLabel();
		add(l, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final ScatterplotChromosomeFitnessFunctionBoundSim f = new ScatterplotChromosomeFitnessFunctionBoundSim(models, data);
		vis.addPropertyChangeListener(new String[]{ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER,
			ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER, ScatterplotVisualization.PROPERTY_INPUTS_SETTING,
			ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX}, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println(evt.getPropertyName());
					l.setText(Double.toString(f.getFitness(new ChromosomeBase() {
						
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
					})));
				}
		});
		setVisible(true);
	}
}
