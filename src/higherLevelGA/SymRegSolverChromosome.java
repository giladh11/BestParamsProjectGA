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
    Random rand = new Random();

    public SymRegSolverChromosome(ParamGA paramGA) {
        this.paramGA = paramGA;
    }

    /**
     * Initialize SolverGAEngine for the given dataSet.
     * Assuming fixed genotype for all higher lever GA chromosomes.
     * TODO make the basic functions a parameter for higher level GA evolution.
     * @param dataSet
     */
    private void setEngine(DataSet dataSet) {
        this.engine = 	new SolverGAEngine(
                dataSet,
                // define variables
                list("x"),
                // define base functions
                list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT), paramGA);
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

    @Override
    public List<SymRegSolverChromosome> crossover(SymRegSolverChromosome symRegSolverChromosome) {
        return null;
    }

    @Override
    public SymRegSolverChromosome mutate() {
        return null;
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


    //TODO add all the crossover and mutate methods - easy here, will just use the method written in ParamGA

}
