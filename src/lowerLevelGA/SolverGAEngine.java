package lowerLevelGA;

import java.util.List;

import evolutionGaTools.*;
import higherLevelGA.BestModelCandidate;
import higherLevelGA.ParamGA;
import interpreter.*;

/**
 * this class represents the object that manage the lower level GA
 * this object will be built within the higher level chromosome SymRegSolverChromosome by the GA params it withholds
 */
public class SolverGAEngine {

	private ParamGA paramGA;
	Effort effort;

	private GeneticAlgorithm<FunctionTreeChromosome, Double> environment;

	private Context context; //this context holds the variables

	private ComparableDataSet comparableDataSet;//data set should be created from the black box in SymRegSolverChromosome
	//IMPROVE we can pass to the solver the black box itself so it could change a bit the data set in every iteration

	/**
	 * constructor
	 * @param comparableDataSet
	 * @param baseFunctions
	 * @param paramGA
     */
	public SolverGAEngine(ComparableDataSet comparableDataSet, List<Functions> baseFunctions, ParamGA paramGA) {
		this.context = new Context(baseFunctions, paramGA.getVariables());
		this.comparableDataSet = comparableDataSet;
		this.paramGA = paramGA;
		DistanceMeasurer distanceMeasurer = new DistanceMeasurer(this.comparableDataSet);
		Population<FunctionTreeChromosome,Double> population = this.createPopulation(this.context, distanceMeasurer, paramGA.getPopulationSize());
		this.environment = new GeneticAlgorithm<FunctionTreeChromosome, Double>(population, distanceMeasurer, paramGA);
	}


	/**
	 * creates a population in the constuctor and in every evolution iteration
	 * @param context
	 * @param lowerFitnessFunction
	 * @param populationSize
     * @return
     */
	private Population<FunctionTreeChromosome, Double> createPopulation(Context context, Fitness<FunctionTreeChromosome, Double> lowerFitnessFunction, int populationSize) {
		Population<FunctionTreeChromosome, Double> population = new Population<FunctionTreeChromosome, Double>();
		for (int i = 0; i < populationSize; i++) {
			FunctionTreeChromosome chromosome =
					new FunctionTreeChromosome(context, lowerFitnessFunction, SyntaxTreeUtils.createTree(paramGA.getMaxInitialTreeDepth(), context));
			population.addChromosome(chromosome);
		}
		return population;
	}

	/**
	 * iteration listener for prints
	 * @param listener
     */
	public void addIterationListener(final SolverGAEngineIterationListener listener) {
		this.environment.addIterationListener(new IterartionListener<FunctionTreeChromosome, Double>() {
			@Override
			public void update(GeneticAlgorithm<FunctionTreeChromosome, Double> environment) {
				listener.update(SolverGAEngine.this);
			}
		});
	}

	/**
	 * starts the evolution
	 * @param iterationsCount
     */
	public void evolve(int iterationsCount) {
		effort = this.environment.evolve(iterationsCount);
	}

	/**
	 * return the context
	 * @return
     */
	public Context getContext() {
		return this.context;
	}

	/**
	 * a getter after the evolution is done for the best tree
	 * @return
     */
	public Expression getBestSyntaxTree() {
		return this.environment.getBest().getSyntaxTree();
	}

	/**
	 * a getter for the effort level used to get the best tree
	 * @return
     */
	public Effort getEffortLevelUsedToReachBest() {
		return effort;
	}

	/**
	 * this method only compure the disstance of the expression from the dataset held by SolverGaEngine
	 * should not really be used
	 * @param expression
	 * @return
     */
	public double lFitnessMeasureForEachIteration(Expression expression) {
		return this.comparableDataSet.distanceFromExpression(expression, this.context);
	}

	public void terminate() {
		this.environment.terminate();
	}

	public int getIteration() {
		return this.environment.getIteration();
	}


	/**
	 * This method is called by SymRegSolverChromosome.evolve
	 * after finishing one evolution process. Should return
	 * the best model found and the effort level of it's evolving.
	 *
	 * @return
     */
	public BestModelCandidate buildBestModelCandidate() {

		return new BestModelCandidate(getBestSyntaxTree(), getEffortLevelUsedToReachBest());
	}

}
