package higherLevelGA;

import evolutionGaTools.Chromosome;
import interpreter.Context;
import interpreter.Expression;
import interpreter.Functions;
import lowerLevelGA.*;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the chromosome for the higher level GA.
 * Its characteristics are the parameters that the basic GA will use for its evolution.
 * Its main method is "trySolving" which gets a BlackBox, try trySolving it and returns the
 * BestModel found.
 */
public class SymRegSolverChromosome implements Chromosome<SymRegSolverChromosome, Double>
{
    private static int MAX_NUM_OF_ITERATIONS_LOWER_LEVEL;
    private static  double EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP;

    private ParamGA paramGA;
    //Inner parameters
    private SolverGAEngine engine;
    private static List<Functions> baseFunctions;
    private double fitness = -1;


    /**
     * simple constuctor
     * @param paramGA
     * @param baseFunctions
     */
    public SymRegSolverChromosome(ParamGA paramGA, List<Functions> baseFunctions) {
        this.paramGA = paramGA;
        this.baseFunctions = baseFunctions;
    }

    /**
     * Initialize HigherGAEngine(Engine for lower level GA) for the given dataSet.
     * Assuming fixed genotype for all higher lever GA chromosomes.
     * @param dataSet
     */
    private void setEngine(DataSet dataSet) {
        this.engine = 	new SolverGAEngine(
                dataSet,
                baseFunctions,
                paramGA);
    }



    /**
     * This method gets a BlackBox, creates a data set based on it,
     * and tries to find the best model for the given BlackBox.
     * @param blackBoxTree
     * @return bestModelCandidate
     */
    public BestModelCandidate trySolving (BlackBoxTree blackBoxTree, boolean printIterations){
        DataSet dataSet = new DataSet(blackBoxTree.getFunction(), paramGA.getDataSetSize(), blackBoxTree.getContext());
        setEngine(dataSet);
        if(printIterations)
            addListener(engine);
        engine.evolve(MAX_NUM_OF_ITERATIONS_LOWER_LEVEL);
        BestModelCandidate bestModelCandidate = engine.buildBestModelCandidate();
        bestModelCandidate.fitnessCalculator(blackBoxTree);
        return bestModelCandidate;
    }


    /**
     * Utility method
     */
    private static <T> List<T> list(T... items) {
        List<T> list = new LinkedList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    /**
     * does a crossover with another SymRegSolverChromosome
     * The crossover operation on SymRegSolverChromosome is a wrapper
     * for crossover operation on its ParamGA.
     * @param symRegSolverChromosome
     * @return List<SymRegSolverChromosome>
     **/
     public List<SymRegSolverChromosome> crossover(SymRegSolverChromosome symRegSolverChromosome) {
        List<ParamGA> crossoverParamGA = this.paramGA.crossover(symRegSolverChromosome.getParamGA());
        List<SymRegSolverChromosome> crossoverSolvers = new LinkedList<>();
        SymRegSolverChromosome crossoverSolver;

        for(ParamGA paramGA : crossoverParamGA){
            crossoverSolver = new SymRegSolverChromosome(paramGA, baseFunctions);
            crossoverSolvers.add(crossoverSolver);
        }
        return crossoverSolvers;
    }

    /**
     * The mutation operation on SymRegSolverChromosome is a wrapper
     * for mutation operation on its ParamGA.
     * @param
     * @return SymRegSolverChromosome
     */
    public SymRegSolverChromosome mutate() {
        ParamGA mutatedParamGA = this.paramGA.mutate();
        SymRegSolverChromosome mutatedSolver = new SymRegSolverChromosome(mutatedParamGA, baseFunctions);
        return mutatedSolver;
    }

    /**
     * simple fitness getter
     * @return
     */
    public Double getFitness() {
        if(fitness == -1)
            System.out.println("!!!fitness yet to be calculated!!!");
        return fitness;
    }

    /**
     * sets the fitness so it wont get calculated more than once during the evolution
     * @param fit
     */
    public void setFitness(Double fit) {

    }

    /**
     * simple context getter
     * @return
     */
    public Context getContext() {
        return engine.getContext();
    }


    private static void addListener(SolverGAEngine engine) {
        engine.addIterationListener(new SolverGAEngineIterationListener() {
            @Override
            public void update(SolverGAEngine engine) {

                Expression bestSyntaxTree = engine.getBestSyntaxTree();

                double currFitValue = engine.fitnessMeasureShouldNotBeUsed(bestSyntaxTree);

                // log to console
                System.out.println(
                        String.format("iter = %s \t fit = %s \t func = %s",
                                engine.getIteration(), currFitValue, bestSyntaxTree.print()));

                // halt condition
                if (currFitValue < EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP) {
                    engine.terminate();
                }
            }
        });
    }


    public ParamGA getParamGA() {
        return paramGA;
    }

    /**
     * simple static setter
     * @param epsilonDistanceForLowerEvolutionToStop
     */
    public static void setEpsilonDistanceForLowerEvolutionToStop(double epsilonDistanceForLowerEvolutionToStop) {
        EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP = epsilonDistanceForLowerEvolutionToStop;
    }

    /**
     * simple static setter
     * @param maxNumOfIterationsLowerLevel
     */
    public static void setMaxNumOfIterationsLowerLevel(int maxNumOfIterationsLowerLevel) {
        MAX_NUM_OF_ITERATIONS_LOWER_LEVEL = maxNumOfIterationsLowerLevel;
    }
}
