package higherLevelGA;

import evolutionGaTools.Chromosome;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * this class represents a GA parameters that will be used by SymRegSolverChromosome and
 * that will develop over time
 */
public class ParamGA implements Chromosome<ParamGA> {
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



    //private List<Functions> baseFunctions;
    //Note: this property is static for all instances since only one-variables models are tested.
    private static final List<String> VARIABLES = list("x");


    public ParamGA(int populationSize, int initialParentChromosomesSurviveCount, double pMutation, double pCrossover, int dataSetSize, int maxInitialTreeDepth, int bloatPenaltyRate) {
        this.pMutation = pMutation;
        this.pCrossover = pCrossover;
        this.populationSize = populationSize;
        this.bloatPenaltyRate = bloatPenaltyRate;
        this.dataSetSize = dataSetSize;
        this.initialParentChromosomesSurviveCount = initialParentChromosomesSurviveCount;
        this.maxInitialTreeDepth = maxInitialTreeDepth;
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

    @Override
    public List<ParamGA> crossover(ParamGA anotherChromosome) {
        return null;
    }

    @Override
    public ParamGA mutate() {
        return null;
    }

    //TODO crossover

    //TODO mutate
}
