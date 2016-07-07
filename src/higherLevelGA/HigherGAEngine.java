//
//package higherLevelGA;
//
//
//import evolutionGaTools.GeneticAlgorithm;
//import evolutionGaTools.Population;
//import interpreter.Functions;
//import lowerLevelGA.BlackBoxTree;
//
//import java.util.List;
//
///**
// * this class represents the object that manage the lower level GA
// * this object will be built within the higher level chromosome SymRegSolverChromosome by the GA params it withholds
// */
//public class HigherGAEngine {
//
//											//	private ParamGA paramGA;
//											//	Effort effort;
//
//	private GeneticAlgorithm<SymRegSolverChromosome, Double> environment;
//	private List<BlackBoxTree> listOfBlackboxes;
//	private List<Functions> baseFunctions;
//
//											//private Context context; //this context holds the variables
//
//											//private ComparableDataSet comparableDataSet;//data set should be created from the black box in SymRegSolverChromosome
//
//	/**
//	 * constructor
//	 * @param comparableDataSet
//	 * @param baseFunctions
//	 * @param paramGA
//     */
//	public HigherGAEngine(List<BlackBoxTree> listOfBlackboxes, List<Functions> baseFunctions) {
//		//TODO constuctor for a higher engine
//		this.listOfBlackboxes = listOfBlackboxes;
//		this.baseFunctions = listOfBlackboxes;
//																//		this.context = new Context(baseFunctions, paramGA.getVariables());
//																//		this.comparableDataSet = comparableDataSet;
//																//		this.paramGA = paramGA;
//																//		DistanceMeasurer distanceMeasurer = new DistanceMeasurer(this.comparableDataSet);
//		Population<SymRegSolverChromosome,Double> population = this.createPopulation(this.context, distanceMeasurer, paramGA.getPopulationSize());
//		this.environment = new GeneticAlgorithm<SymRegSolverChromosome, Double>(population, distanceMeasurer, paramGA);
//	}
//
//
//	/**
//	 * creates a population in the constuctor and in every evolution iteration
//	 * @param context
//	 * @param fitnessFunction
//	 * @param populationSize
//     * @return
//     */
//	private Population<SymRegSolverChromosome, Double> createPopulation(Fitness<SymRegSolverChromosome, Double> fitnessFunction, int populationSize) {//TODO maybe give a fitness function to the SymRegSolverChromosome
//		//TODO createPopulation
//		Population<SymRegSolverChromosome, Double> population = new Population<SymRegSolverChromosome, Double>();
//		for (int i = 0; i < populationSize; i++) {
//			SymRegSolverChromosome chromosome =
//					new SymRegSolverChromosome(ParamGA.getRandomParamGA(), baseFunctions);
//			population.addChromosome(chromosome);
//		}
//		return population;
//	}
//
//	/**
//	 * iteration listener for prints
//	 * @param listener
//     */
//	public void addIterationListener(final HigherGAEngineIterationListener listener) {
//		//TODO fix listner
//		this.environment.addIterationListener(new IterartionListener<SymRegSolverChromosome, Double>() {
//			@Override
//			public void update(GeneticAlgorithm<SymRegSolverChromosome, Double> environment) {
//				listener.update(HigherGAEngine.this);
//			}
//		});
//	}
//
//	/**
//	 * starts the evolution
//	 * @param iterationsCount
//     */
//	public void evolve(int iterationsCount) {
//		this.environment.evolve(iterationsCount);
//	}
//
//	/**
//	 * return the context
//	 * @return
//     */
//	public Context getContext() {
//		return this.context;
//	}
//
//													//	/**    maybe turn to getBestParams
//													//	 * a getter after the evolution is done for the best tree
//													//	 * @return
//													//     */
//													//	public Expression getBestSyntaxTree() {
//													//		return this.environment.getBest().getSyntaxTree();
//													//	}
//
//																//	/**  DELETE
//																//	 * a getter for the effort level used to get the best tree
//																//	 * @return
//																//     */
//																//	public Effort getEffortLevelUsedToReachBest() {
//																//		return effort;
//																//	}
//
//
//															//	/**
//															//	 * this method only compure the disstance of the expression from the dataset held by SolverGaEngine
//															//	 * should not really be used
//															//	 * @param expression
//															//	 * @return
//															//     */
//															//	public double fitnessMeasureShouldNotBeUsed(Expression expression) {
//															//		return this.comparableDataSet.distanceFromExpression(expression, this.context);
//															//	}
//
//	public void terminate() {
//		this.environment.terminate();
//	}
//
//	public int getIteration() {
//		return this.environment.getIteration();
//	}
//
//}
