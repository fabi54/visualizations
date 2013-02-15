package test.artificialdata;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.fabi.visualizations.Global;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.evolution.SymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.scatterplot_old.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot_old.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot_old.ScatterplotChromosomeGenerator;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.MultiModelSourceBase;
import org.fabi.visualizations.scatter2.sources.ScatterplotSourceBase;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;
import org.fabi.visualizations.tools.math.ArithmeticAverage;

import configuration.CfgTemplate;

public class TestEvolution {

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
	
	protected static String PATH = "D:\\Data\\Dokumenty\\Skola\\FIT-MI\\misc\\fakegame\\Results\\evolution\\data3\\";
	
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
		MultiModelSourceBase model = new MultiModelSourceBase(models, new ArithmeticAverage());
		List<Chromosome> c = null;
		FitnessFunction fitness = new ScatterplotChromosomeFitnessFunction(model, data);
		if (data.outputsNumber() == 1) {
			c = evolveRegression(data, fitness);
		} else {
			c = evolveClassification(data, fitness);
		}
		DatasetGenerator generator = new DatasetGenerator(model, data);
		System.out.println("BEST:");
		ScatterplotVisualization[] res = new ScatterplotVisualization[c.size()];
		for (int i = 0; i < c.size(); i++) {
			VisualizationConfig cfg = (VisualizationConfig) c.get(i).getPhenotype();
			ScatterplotVisualization visualization = new ScatterplotVisualization(generator, cfg);
			System.out.println("  {" + c.get(i).getFitness() + "} " + visualization.hashCode());
			res[i] = visualization;
		}
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		Global.getInstance().init();
		DataSource data = new Data3();
		CfgTemplate[][] templates = Tester.getTemplates();
		String[] names = Tester.getNames();
		for (int i = 0; i < templates.length; i++) {
			System.out.println("=== " + names[i] + " ===");
			ModelSource[] models = new ModelGroup(templates[i], data).getModels();
			org.fabi.visualizations.scatter2.ScatterplotVisualization tmp = new org.fabi.visualizations.scatter2.ScatterplotVisualization(new ScatterplotSourceBase(new DataSource[]{data}, models));
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
				BufferedImage img = vis[j].getVisualizationAsImage(800, 600);
				ImageIO.write(img, "png",new File(PATH + names[i] + "\\" + j + ".png"));
			}
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
