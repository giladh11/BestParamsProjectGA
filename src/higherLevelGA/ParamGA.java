package higherLevelGA;

import interpreter.Functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * this class represents a GA parameters that will be used by SymRegSolverChromosome and
 * that will develop over time
 */
public class ParamGA {
    //These are the Chromosome's characteristics parameters
    private double pMutation;
    private double pCrossover;
    private int populationSize;


    //This parameter defines the measure of fitnessMeasureShouldNotBeUsed deduction
    //for too big trees
    private int bloatPenaltyRate;//IMPROVE too complicated now
    private int dataSetSize;

    private int initialParentChromosomesSurviveCount;//1

    private int maxInitialTreeDepth;//1



    private List<Functions> baseFunctions;
    //Note: this property is static for all instances since only one-variables models are tested.
    private static final List<String> VARIABLES = list("x");


    public ParamGA(double pMutation, double pCrossover, int populationSize, int bloatPenaltyRate, int dataSetSize, int initialParentChromosomesSurviveCount, int maxInitialTreeDepth, List<Functions> baseFunctions) {
        this.pMutation = pMutation;
        this.pCrossover = pCrossover;
        this.populationSize = populationSize;
        this.bloatPenaltyRate = bloatPenaltyRate;
        this.dataSetSize = dataSetSize;
        this.initialParentChromosomesSurviveCount = initialParentChromosomesSurviveCount;
        this.maxInitialTreeDepth = maxInitialTreeDepth;
        this.baseFunctions = baseFunctions;
    }

    /**
     * This constructor is used by FunctionTreeChromosome.optimizeTree().
     * No actual effect since the evolution done by optimizeTree dose not use paramGA.
     * @param pMutation
     * @param pCrossover
     * @param populationSize
     * @param bloatPenaltyRate
     * @param dataSetSize
     * @param initialParentChromosomesSurviveCount
     * @param maxInitialTreeDepth
     */

    public ParamGA(double pMutation, double pCrossover, int populationSize, int bloatPenaltyRate, int dataSetSize, int initialParentChromosomesSurviveCount, int maxInitialTreeDepth) {
        this.pMutation = pMutation;
        this.pCrossover = pCrossover;
        this.populationSize = populationSize;
        this.bloatPenaltyRate = bloatPenaltyRate;
        this.dataSetSize = dataSetSize;
        this.initialParentChromosomesSurviveCount = initialParentChromosomesSurviveCount;
        this.maxInitialTreeDepth = maxInitialTreeDepth;
        this.baseFunctions = null;
    }





    public double getpMutationRate() {
        return pMutation;
    }

    public double getCrossoverRate() {
        return pCrossover;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxInitialTreeDepth() {
        return maxInitialTreeDepth;
    }

    public int getBloatPenaltyRate() {
        return bloatPenaltyRate;
    }

    public List<Functions> getBaseFunctions() {
        return baseFunctions;
    }

    public int getDataSetSize() {
        return dataSetSize;
    }

    public int getInitialParentChromosomesSurviveCount() {
        return initialParentChromosomesSurviveCount;
    }

    public Collection<String> getVariables() {
        return VARIABLES;
    }

    private static <T> List<T> list(T... items) {
        List<T> list = new LinkedList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    //TODO crossover

    //TODO mutate
}
