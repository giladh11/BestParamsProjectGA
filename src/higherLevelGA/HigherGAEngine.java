
package higherLevelGA;


import evolutionGaTools.*;
import interpreter.Functions;
import lowerLevelGA.BlackBoxTree;

import java.util.List;

/**
 * this class represents the object that manage the lower level GA
 * this object will be built within the higher level chromosome SymRegSolverChromosome by the GA params it withholds
 */
public class HigherGAEngine {

											//	private ParamGA paramGA;
											//	Effort effort;

	private GeneticAlgorithmHigherLevel<SymRegSolverChromosome, Double> environment;
	private List<BlackBoxTree> listOfBlackboxes;
	private List<Functions> baseFunctions;
	private int populationSize;

											//private Context context; //this context holds the variables

											//private ComparableDataSet comparableDataSet;//data set should be created from the black box in SymRegSolverChromosome

	/**
	 * constructor
	 * @param listOfBlackboxes
	 * @param baseFunctions
	 * @param populationSize
     */
	public HigherGAEngine(List<BlackBoxTree> listOfBlackboxes, List<Functions> baseFunctions, int populationSize) {
		this.listOfBlackboxes = listOfBlackboxes;
		this.baseFunctions = baseFunctions;
		this.populationSize = populationSize;
		ParamMeasurer paramMeasurer = new ParamMeasurer(listOfBlackboxes);
		Population<SymRegSolverChromosome,Double> population = this.createPopulation(paramMeasurer, populationSize);
		this.environment = new GeneticAlgorithmHigherLevel<SymRegSolverChromosome, Double>(population, paramMeasurer);
	}


	/**
	 * creates a population in the constuctor and in every evolution iteration
	 * @param higherFitnessFunction
	 * @param populationSize
     * @return
     */
	private Population<SymRegSolverChromosome, Double> createPopulation(Fitness<SymRegSolverChromosome, Double> higherFitnessFunction, int populationSize) {
		Population<SymRegSolverChromosome, Double> population = new Population<SymRegSolverChromosome, Double>();
		for (int i = 0; i < populationSize; i++) {
			SymRegSolverChromosome chromosome =
					new SymRegSolverChromosome(ParamGA.getRandomParamGA(), baseFunctions);
			population.addChromosome(chromosome);
		}
		return population;
	}

	/**
	 * iteration listener for prints
	 * @param listener
     */
	public void addIterationListener(final HigherGAEngineIterationListener listener) {
		this.environment.addIterationListener(new IterartionListenerHigh<SymRegSolverChromosome, Double>() {
			@Override
			public void update(GeneticAlgorithmHigherLevel<SymRegSolverChromosome, Double> environment) {
				listener.update(HigherGAEngine.this);
			}
		});
	}

	/**
	 * starts the evolution
	 * @param iterationsCount
     */
	public SymRegSolverChromosome evolve(int iterationsCount) {
		return this.environment.evolve(iterationsCount);
	}



	public void terminate() {
		this.environment.terminate();
	}

	public int getIteration() {
		return this.environment.getIteration();
	}

	public SymRegSolverChromosome getBestSymRegSolver() {
		return this.environment.getBest();
	}
}
