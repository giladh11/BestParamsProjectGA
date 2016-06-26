package higherLevelGA;

import evolutionGaTools.Chromosome;
import higherLevelGA.ParamGA;
import interpreter.Context;
import interpreter.Expression;
import interpreter.Functions;
import lowerLevelGA.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the chromosome for the higher level GA.
 * Its characteristics are the parameters that the basic GA will use for its evolution.
 * Its main method is "trySolving" which gets a BlackBox, try trySolving it and returns the
 * BestModel found.
 */
public class SymRegSolverChromosome implements Chromosome<SymRegSolverChromosome>
{

    private ParamGA paramGA;
    private int generations = 200;//PARAM
    //Inner parameters
    private SolverGAEngine engine;
    private static  double epsilon = 0.001;
    Random rand = new Random();//DELETE
    private static List<Functions> baseFunctions;
    Random rand = new Random();

    public SymRegSolverChromosome(ParamGA paramGA, List<Functions> baseFunctions) {
        this.paramGA = paramGA;
        this.baseFunctions = baseFunctions;
    }

    /**
     * Initialize SolverGAEngine for the given dataSet.
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
     * This method is used by trySolving when setting the engine to some dataset.
     * @param blackBoxTree
     * @return DateSet of size this.dataSetSize which contains randomly chosen points
     *  IMPROVE
     *     For now, the points are uniformly chosen within a fixed range.
     *     Better idea is to choose points that best characterize the given black box.
     */
    private DataSet createDataSet(BlackBoxTree blackBoxTree){
        DataSet dataSet = new DataSet();
        int numOfPoints = 0;
        double x, fx;

        while(numOfPoints < paramGA.getDataSetSize()){
            x = getRandomValueInRange();
            fx = blackBoxTree.eval(x);
            dataSet.addTarget(new Point().when("x", x).setYval(fx));
            numOfPoints++;
        }

        return dataSet;

    }

    /**
     * See createDataSet
     * @return
     */
    private double getRandomValueInRange() {
        int minimum = -50, maximum = 50;//PARAM
        double randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
        return randomNum;
    }


    /**
     * This method gets a BlackBox, creates a data set based on it,
     * and tries to find the best model for the given BlackBox.
     * @param blackBoxTree
     * @return bestModelCandidate
     */
    public BestModelCandidate trySolving(BlackBoxTree blackBoxTree){
        DataSet dataSet = createDataSet(blackBoxTree);
        setEngine(dataSet);
        addListener(engine);
        engine.evolve(generations);
        BestModelCandidate bestModelCandidate = engine.buildBestModelCandidate();
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
     * The crossover operation on SymRegSolverChromosome is a wrapper
     * for crossover operation on its ParamGA.
     * @param symRegSolverChromosome
     * @return List<SymRegSolverChromosome>
     */

    @Override
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
    @Override
    public SymRegSolverChromosome mutate() {
        ParamGA mutatedParamGA = this.paramGA.mutate();
        SymRegSolverChromosome mutatedSolver = new SymRegSolverChromosome(mutatedParamGA, baseFunctions);
        return mutatedSolver;
    }

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
                if (currFitValue < epsilon) {
                    engine.terminate();
                }
            }
        });
    }


    public ParamGA getParamGA() {
        return paramGA;
    }
}
