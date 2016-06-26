package higherLevelGA;

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


    public ParamGA(double pMutation, double pCrossover, int populationSize, int bloatPenaltyRate, int dataSetSize, int initialParentChromosomesSurviveCount, int maxInitialTreeDepth) {
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

    //TODO crossover

    //TODO mutate
}
