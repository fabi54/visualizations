package org.fabi.visualizations.rapidminer.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import game.data.GameData;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.SymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosome;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeGenerator;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.rapidminer.MultipleVisualizationContainer;
import org.fabi.visualizations.rapidminer.scatter.ConfidenceModelMultiModelSource;
import org.fabi.visualizations.rapidminer.scatter.RapidMinerDataSource;
import org.fabi.visualizations.rapidminer.scatter.RapidMinerMetadata;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;
import org.modgen.rapidminer.data.RapidGameData;
import org.modgen.rapidminer.modelling.ConfidenceModel;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.UndefinedParameterError;

public class EvolveScatterplotOperator extends Operator {
	
	protected InputPort modelInput = getInputPorts().createPort("model");
	protected InputPort dataInput = getInputPorts().createPort("data");
	
	protected OutputPort chartOutput = getOutputPorts().createPort("visualization"); 
	
	public EvolveScatterplotOperator(OperatorDescription description) {
		super(description);
		modelInput.addPrecondition(new SimplePrecondition(modelInput,
				new MetaData(ConfidenceModel.class), true));
		dataInput.addPrecondition(new SimplePrecondition(dataInput,
				new MetaData(ExampleSet.class), true));
		getTransformer().addGenerationRule(chartOutput, MultipleVisualizationContainer.class);
	}
	
    @Override
    public List<ParameterType> getParameterTypes() {
	    List<ParameterType> types = super.getParameterTypes();
	      types.add(new ParameterTypeInt("population size", "", 10, 500, 30, true));
	      types.add(new ParameterTypeInt("steps", "", 1, 1000, 10, true));
	      types.add(new ParameterTypeDouble("selection pressure", "", 0.0, 1.0, 0.5, true));
	      types.add(new ParameterTypeDouble("mutation probability", "", 0.0, 1.0, 0.05, true));
	      types.add(new ParameterTypeDouble("elitism rate", "", 0.0, 1.0, 0.0, true));
	      types.add(new ParameterTypeInt("model eval. precision", "", 5, 100, 20, true));
	      types.add(new ParameterTypeInt("visualizations number", "", 1, 20, 1, true));
	      types.add(new ParameterTypeDouble("visualizations distance", "", 0.0, 1000.0, 2.0, true));
	      types.add(new ParameterTypeDouble("similarity significance", "", 0.0, 1000.0, 1.0, true));
	      types.add(new ParameterTypeDouble("similarity significance local", "", 0.0, 1000.0, 2.0, true));
	      types.add(new ParameterTypeDouble("size significance", "", 0.0, 1000.0, 1.0, true));
	      types.add(new ParameterTypeDouble("variance significance", "", 0.0, 1000.0, 1.0, true));
	      return types;
    }
	
    @Override
    public void doWork() {
    	try {
    		MIN_WEIGHTED_DISTANCE = getParameterAsDouble("visualizations distance");
    		ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE = getParameterAsDouble("similarity significance");
    		ScatterplotChromosomeFitnessFunction.SIMILARITY_SIGNIFICANCE2 = getParameterAsDouble("similarity significance local");
    		ScatterplotChromosomeFitnessFunction.SIZE_SIGNIFICANCE = getParameterAsDouble("size significance");
    		ScatterplotChromosomeFitnessFunction.VARIANCE_SIGNIFICANCE = getParameterAsDouble("variance significance");
    		ScatterplotChromosomeFitnessFunction.setModelOutputPrecision(getParameterAsInt("model eval. precision"));
    		System.out.println("EVOLUTION");
    		System.out.println("  pop s: " + getParameterAsInt("population size")
    				+ ", steps: " + getParameterAsInt("steps")
    				+ ", sel pr: " + getParameterAsDouble("selection pressure")
    				+ ", mut prob: " + getParameterAsDouble("mutation probability")
    				+ ", elitism: " + getParameterAsDouble("elitism rate"));
    		ConfidenceModelMultiModelSource model = new ConfidenceModelMultiModelSource(modelInput.getData(ConfidenceModel.class));
    		ExampleSet es = dataInput.getData(ExampleSet.class);
    		GameData data = new RapidGameData(es);
    		RapidMinerDataSource dataSource = new RapidMinerDataSource(es);
    		List<Chromosome> c = null;
    		FitnessFunction fitness = new ScatterplotChromosomeFitnessFunction(model, dataSource);
    		if (data.getONumber() == 1) {
    			c = evolveRegression(dataSource, fitness);
    		} else {
    			c = evolveClassification(dataSource, fitness);
    		}
    		List<Visualization<?>> visualizations = new ArrayList<Visualization<?>>(c.size());
			DatasetGenerator generator = new DatasetGenerator(model,
					dataSource,
					new RapidMinerMetadata(es));
			System.out.println("BEST:");
    		for (int i = 0; i < c.size(); i++) {
    			VisualizationConfig cfg = (VisualizationConfig) c.get(i).getPhenotype();
    			ScatterplotVisualization visualization = new ScatterplotVisualization(generator, cfg);
    			System.out.println("  {" + c.get(i).getFitness() + "} " + visualization.hashCode());
    			visualizations.add(visualization);
    		}
			chartOutput.deliver(new MultipleVisualizationContainer(visualizations));
    	} catch (OperatorException ex) {
    		ex.printStackTrace();
    	}
    }
    
    protected double MIN_WEIGHTED_DISTANCE = 0.25;
    
    protected List<Chromosome> evolveRegression(DataSource data, FitnessFunction fitness) throws UndefinedParameterError {
    	List<Chromosome> best = new LinkedList<Chromosome>();
    	int max = getParameterAsInt("visualizations number");
		for (int j = 0; j < data.inputsNumber(); j++) {
	    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
	    			data.getInputDataVectors(),
	    			true,
	    			new int[]{j});
	    	Population population = new PopulationBase(generator, getParameterAsInt("population size"));
	    	Population p = evolve(population);
			//if (best.size() < max) {
	    	int cnt = 0;
				for (int k = 0; k < p.size() && /*best.size()*/ cnt++ < max; k++) {
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
    
    protected List<Chromosome> evolveClassification(DataSource data, FitnessFunction fitness) throws UndefinedParameterError {
    	List<Chromosome> best = new LinkedList<Chromosome>();
    	int max = getParameterAsInt("visualizations number");
    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
    			data.getInputDataVectors(),
    			false,
    			null);
    	Population population = new PopulationBase(generator, getParameterAsInt("population size"));
		Population p = evolve(population);
		if (best.size() < max) {
			for (int k = 0; k < p.size() && best.size() < max; k++) {
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
					while (best.size() > max) {
						best.remove(best.size() - 1);
					}
				}
	    	}
		}
		return best;
    }
    
    protected Population evolve(Population population) throws UndefinedParameterError {
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		System.out.println("SCATTERPLOT_EVOLUTION: Initializing evolution.");
		algorithm.init(population, new SymetricCrossoverEvolutionStrategy(getParameterAsDouble("elitism rate"),
				getParameterAsDouble("mutation probability"), getParameterAsDouble("selection pressure")));
//		System.out.println(algorithm.getPopulation());
		Chromosome b = algorithm.getBest();
		System.out.println("[" + b.getFitness() + "] " + b);
		System.out.flush();
		for (int i = 0; i < getParameterAsInt("steps"); i++) {
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
