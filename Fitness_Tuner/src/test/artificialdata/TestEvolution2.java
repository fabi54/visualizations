package test.artificialdata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.evolution.SymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeGenerator;
import org.fabi.visualizations.scatter2.AdditionalDrawer;
import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter2.color.ColorModel;
import org.fabi.visualizations.scatter2.dotsize.MinkowskiDistanceDotSizeModel;
import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.fabi.visualizations.scatter2.sources.ScatterplotSourceBase;
import org.math.plot.render.AbstractDrawer;

import configuration.CfgTemplate;

public class TestEvolution2 {

	protected static int POPULATION_SIZE = 100;
	protected static int STEPS = 100;
	protected static double SELECTION_PRESSURE = 0.5;
	protected static double MUTATION_PROBABILITY = 0.05;
	protected static int ELITISM = 1;
	protected static double SIMILARITY_SIGNIFICANCE = 1.0;
	protected static double SIMILARITY_SIGNIFICANCE2 = 2.0;
	protected static double VARIANCE_SIGNIFICANCE = 1.0;
	protected static double SIZE_SIGNIFICANCE = 1.0;
	protected static int MODEL_OUTPUT_PRECISION = 20;
	protected static double MIN_WEIGHTED_DISTANCE = 0.0;
	
	protected static int VISUALIZATIONS_NUMBER = 5;
	
	protected static String PATH = "D:\\Data\\Dokumenty\\Skola\\FIT-MI\\misc\\fakegame\\Results\\evolution2a\\data1\\";
	
	protected static ScatterplotVisualization[] evolver(ModelSource[] models, DataSource data) throws IOException {
//		ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = SIMILARITY_SIGNIFICANCE;
//		ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE2 = SIMILARITY_SIGNIFICANCE2;
//		ScatterplotChromosomeFitnessFunction.SIZE_SIGNIFICANCE = SIZE_SIGNIFICANCE;
//		ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = VARIANCE_SIGNIFICANCE;
//		ScatterplotChromosomeFitnessFunction.setModelOutputPrecision(MODEL_OUTPUT_PRECISION);
		System.out.println("EVOLUTION");
		System.out.println("  pop s: " + POPULATION_SIZE
				+ ", steps: " + STEPS
				+ ", sel pr: " + SELECTION_PRESSURE
				+ ", mut prob: " + MUTATION_PROBABILITY
				+ ", elitism: " + ELITISM);
		List<Chromosome> c = null;
		FitnessFunction fitness = new ScatterplotChromosomeFitnessFunction(models, data);
		if (data.outputsNumber() == 1) {
			c = evolveRegression(data, fitness);
		} else {
			c = evolveClassification(data, fitness);
		}
		ScatterplotSource source = new ScatterplotSourceBase(new DataSource[]{data}, models);
		System.out.println("BEST:");
		ScatterplotVisualization[] res = new ScatterplotVisualization[c.size()];
		for (int i = 0; i < c.size(); i++) {
			res[i] = (ScatterplotVisualization) c.get(i).getPhenotype();
			res[i].setSource(source);
			res[i].setBackground(Color.BLACK);
			res[i].setDotSizeModel(getDotSizeModel(res[i]));
			res[i].setColorModel(new ColorModel() {
				@Override public void init(ScatterplotSource source) { }
				@Override public void init(ScatterplotVisualization visualization) { }
				@Override public String getName() { return ""; }
				@Override public JComponent getControls() { return null; }
				
				@Override
				public Color getColor(double[] inputs, double[] outputs, boolean data,
						int index, int[] inputsIndices, int[] outputsIndices) {
					if (data) {
						return new Color(1.0f, 1.0f, 1.0f, 0.25f);
					} else {
						return new Color(0.0f, 1.0f, 1.0f, 1.0f);
					}
				}
			});
			System.out.println("  {" + c.get(i).getFitness() + "} " + res[i].hashCode());
		}
		return res;
	}
	
	// regression version
	protected static MinkowskiDistanceDotSizeModel getDotSizeModel(ScatterplotVisualization visualization) {
		MinkowskiDistanceDotSizeModel model = new MinkowskiDistanceDotSizeModel();
		model.setP(2);
		model.setMinDist(0);
		ScatterplotSource source = visualization.getSource();
		double[] inputSetting = visualization.getInputsSetting();
		int xIndex = visualization.getxAxisAttributeIndex();
		double maxDist = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			for (int j = 0; j < inputs.length; j++) {
				double res = 0;
				for (int k = 0; k < inputs[k].length; k++) {
					if (k != xIndex) {
						res += Math.pow(inputs[j][k] - inputSetting[k], 2);
					}
				}
				maxDist = Math.max(maxDist, Math.sqrt(res));
			}
		}
		model.setRangeDist(maxDist);
		return model;
	}
	
	public static void main(String[] args) throws IOException {
		DataSource data = new Data11D();
		CfgTemplate[][] templates = Tester.getTemplates();
		String[] names = Tester.getNames();
		for (int i = 0; i < templates.length; i++) {
			System.out.println("=== " + names[i] + " ===");
			ModelSource[] models = new ModelGroup(templates[i], data).getModels();
			ScatterplotSource source = new ScatterplotSourceBase(new DataSource[]{data}, models);
			org.fabi.visualizations.scatter2.ScatterplotVisualization tmp = new org.fabi.visualizations.scatter2.ScatterplotVisualization(source);
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.add(tmp.getVisualizationAsComponent(), BorderLayout.CENTER);
			frame.add(tmp.getControls(), BorderLayout.WEST);
			frame.setSize(800, 600);
			frame.setVisible(true);
			ScatterplotVisualization[] vis = evolver(models, data);
			File dir = new File(PATH + names[i] + "\\");
			if (!dir.exists()) {
				System.out.println("Creating dir \"" + dir + "\"");
				dir.mkdir();
			}
			for (int j = 0; j < vis.length; j++) {
				vis[j].getVisualizationAsComponent();
				double[] yBounds = getyBounds(vis[j]);
				vis[j].setAdditionalDrawers(new AdditionalDrawer[]{new AreaAdditionalDrawer(
						vis[j].getxAxisRangeLower(),
						vis[j].getxAxisRangeUpper(),
						yBounds[0],
						yBounds[1])});
				double[] xBounds = getxBounds(vis[j]);
				double offset = (xBounds[1] - xBounds[0]) * 0.1;
				vis[j].setxAxisRangeLower(xBounds[0] - offset);
				vis[j].setxAxisRangeUpper(xBounds[1] + offset);
				yBounds = getyBounds(vis[j]);
				offset = (yBounds[1] - yBounds[0]) * 0.1;
				vis[j].setyAxisRangeLower(yBounds[0] - offset);
				vis[j].setyAxisRangeUpper(yBounds[1] + offset);
				vis[j].setGridVisible(false);
				BufferedImage img = vis[j].getVisualizationAsImage(800, 600);
				String n = (j < 10) ? ("0" + Integer.toString(j)) : Integer.toString(j);
				ImageIO.write(img, "png",new File(PATH + names[i] + "\\" + n + ".png"));
			}
		}
	}
	
	// regression version
	protected static double[] getxBounds(ScatterplotVisualization vis) {
		ScatterplotSource source = vis.getSource();
		int xIndex = vis.getxAxisAttributeIndex();
		double[] bounds = new double[]{vis.getxAxisRangeLower(), vis.getxAxisRangeUpper()};
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			for (int j = 0; j < inputs.length; j++) {
				bounds[0] = Math.min(bounds[0], inputs[j][xIndex]);
				bounds[1] = Math.max(bounds[1], inputs[j][xIndex]);
			}
		}
		return bounds;
	}
	
	// regression version
	protected static double[] getyBounds(ScatterplotVisualization vis) {
		ScatterplotSource source = vis.getSource();
		int xIndex = vis.getxAxisAttributeIndex();
		double xLower = vis.getxAxisRangeLower();
		double xUpper = vis.getxAxisRangeUpper();
		double[] bounds = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
		for (int i = 0; i < source.getDataSourceCount(); i++) {
			double[][] inputs = source.getDataSource(i).getInputDataVectors();
			double[][] outputs = source.getDataSource(i).getOutputDataVectors();
			for (int j = 0; j < inputs.length; j++) {
				if (inputs[j][xIndex] > xLower && inputs[j][xIndex] < xUpper) {
					bounds[0] = Math.min(bounds[0], outputs[j][0]);
					bounds[1] = Math.max(bounds[1], outputs[j][0]);
				}
			}
		}
		double[][] inputs = new double[20][source.getInputsNumber()];
		for (int i = 0; i < inputs.length; i++) {
			System.arraycopy(vis.getInputsSetting(), 0, inputs[i], 0, inputs[i].length);
		}
		inputs[0][xIndex] = xLower;
		double step = (xUpper - xLower) / 20;
		for (int i = 1; i < inputs.length; i++) {
			inputs[i][xIndex] = inputs[i - 1][xIndex] + step;
		}
		for (int i = 0 ; i < source.getModelSourceCount(); i++) {
			double[][] responses = source.getModelSource(i).getModelResponses(inputs);
			for (int j = 0; j < responses.length; j++) {
				bounds[0] = Math.min(bounds[0], responses[j][0]);
				bounds[1] = Math.max(bounds[1], responses[j][0]);
			}
		}
		return bounds;
	}
	
	protected static class AreaAdditionalDrawer implements AdditionalDrawer {
		
		protected double xl; protected double xu; protected double yl; protected double yu;
		protected Color clr = Color.WHITE;
		
		public AreaAdditionalDrawer(double xl, double xu, double yl, double yu) {
			this.xl = xl; this.xu = xu; this.yl = yl; this.yu = yu;
		}
		
		public void setColor(Color clr) {
			this.clr = clr;
		}
		
		@Override
		public void draw(AbstractDrawer draw) {
			draw.setColor(clr);
			draw.drawPolygon(new double[]{xl, yl}, new double[]{xl, yu}, new double[]{xu, yu}, new double[]{xu, yl});
			System.out.println("drawing " + xl + "," + yl + "," + xu + "," + yu);
		}
	}
    
    protected static List<Chromosome> evolveRegression(DataSource data, FitnessFunction fitness) {
    	List<Chromosome> best = new LinkedList<Chromosome>();
		for (int j = 0; j < data.inputsNumber(); j++) {
	    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
	    			data.getInputDataVectors(),
	    			true,
	    			new int[]{j});
	    	Population population = new PopulationBase(generator, POPULATION_SIZE);
	    	Population p = evolve(population);
			//if (best.size() < max) {
	    	int cnt = 0;
				for (int k = 0; k < p.size() && /*best.size()*/ cnt++ < VISUALIZATIONS_NUMBER; k++) {
					boolean add = true;
					for (Chromosome b : best) {
//						System.out.println("dist " + ((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b)
//								+ "(" + p.getIth(k).getFitness() + "/" + p.getIth(k).hashCode()
//								+ ", " + b.getFitness() + "/" + b.hashCode() + ")");
						if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
							add = false;
							break;
						}
					}
					if (add) {
						best.add(p.getIth(k));
//						System.out.println("Adding " + p.getIth(k).getFitness() + "/" + p.getIth(k).hashCode());
					}
				}
				Collections.sort(best, new Comparator<Chromosome>() {

					@Override
					public int compare(Chromosome o1, Chromosome o2) {
						return -Double.compare(o1.getFitness(), o2.getFitness());
					}
				});
			/*} else {
				for (int k = 0; k < p.size() &&
	    			(p.getIth(k).getFitness() > ((best.size() > 0) ? best.get(best.size() - 1).getFitness() : Double.MIN_VALUE)); k++) {
					boolean add = true;
					for (Chromosome b : best) {
//						System.out.println("dist " + ((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b)
//								+ "(" + p.getIth(k).getFitness() + "/" + p.getIth(k).hashCode()
//								+ ", " + b.getFitness() + "/" + b.hashCode() + ")");
						if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
							add = false;
							break;
						}
					}
					if (add) {
						for (int i = 0; i < best.size(); i++) {
							if (p.getIth(k).getFitness() > best.get(i).getFitness()) {
								best.add(i, p.getIth(k));
//								System.out.println("Adding " + p.getIth(k).getFitness() + "/" + p.getIth(k).hashCode());
								break;
							}
						}
						while (best.size() > max) {
//							System.out.println("Removing " + best.get(best.size() - 1).getFitness() + "/" + best.get(best.size() - 1).hashCode());
							best.remove(best.size() - 1);
						}
					}
				}
	    	}*/
		}
		return best;
    }
    
    protected static List<Chromosome> evolveClassification(DataSource data, FitnessFunction fitness) {
    	List<Chromosome> best = new LinkedList<Chromosome>();
    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
    			data.getInputDataVectors(),
    			false,
    			null);
    	Population population = new PopulationBase(generator, POPULATION_SIZE);
		Population p = evolve(population);
		if (best.size() < VISUALIZATIONS_NUMBER) {
			for (int k = 0; k < p.size() && best.size() < VISUALIZATIONS_NUMBER; k++) {
				boolean add = true;
				for (Chromosome b : best) {
					if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
						add = false;
						break;
					}
				}
				if (add) {
					best.add(p.getIth(k));
				}
			}
			Collections.sort(best, new Comparator<Chromosome>() {

				@Override
				public int compare(Chromosome o1, Chromosome o2) {
					return -Double.compare(o1.getFitness(), o2.getFitness());
				}
			});
		} else {
			for (int k = 0; k < p.size() &&
	    			(p.getIth(k).getFitness() > ((best.size() > 0) ? best.get(best.size() - 1).getFitness() : Double.MIN_VALUE)); k++) {
				boolean add = true;
				for (Chromosome b : best) {
					if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
						add = false;
						break;
					}
				}
				if (add) {
					for (int i = 0; i < best.size(); i++) {
						if (p.getIth(k).getFitness() > best.get(i).getFitness()) {
							best.add(i, p.getIth(k));
							break;
						}
					}
					while (best.size() > VISUALIZATIONS_NUMBER) {
						best.remove(best.size() - 1);
					}
				}
	    	}
		}
		return best;
    }
    
    protected static Population evolve(Population population) {
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		System.out.println("SCATTERPLOT_EVOLUTION: Initializing evolution.");
		algorithm.init(population, new SymetricCrossoverEvolutionStrategy(ELITISM,
				MUTATION_PROBABILITY, SELECTION_PRESSURE));
//		System.out.println(algorithm.getPopulation());
		Chromosome b = algorithm.getBest();
		System.out.println("[" + b.getFitness() + "] " + b);
		System.out.flush();
		for (int i = 0; i < STEPS; i++) {
			algorithm.optimize();
//			System.out.println("SCATTERPLOT_EVOLUTION: Generation " + (i + 1));
//			System.out.println(algorithm.getPopulation());
			b = algorithm.getBest();
			System.out.println("[" + b.getFitness() + "] " + b);
			System.out.flush();
		}
		return algorithm.getPopulation();
    }
}
