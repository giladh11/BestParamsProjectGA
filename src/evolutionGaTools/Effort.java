package evolutionGaTools;

/**
 * this class represent all the data involving the Effort in a "trysolving" run of a symRegSolverChromosome
 */
public class Effort {
    protected int genNum = 0;
    protected int numOfCrossovers = 0;
    protected int numOfMutations = 0;
    protected int numOfPointsEvaluated = 0;
    protected int sizeOfAllTreesCreated = 0;


    /**
     * simple constructor
     */
    public Effort(int sizeOfAllTreesCreatedInInitialPopulation) {
        this.sizeOfAllTreesCreated=sizeOfAllTreesCreatedInInitialPopulation;
    }

    @Override
    public String toString() {
        return "genNum: " + genNum +",  numOfCrossovers: "+numOfCrossovers +" , numOfMutations: "+ numOfMutations + ", numOfPointsEvaluated: " + numOfPointsEvaluated +  " sizeOfAllTreesCreated: " + sizeOfAllTreesCreated;
    }

    /**
     * simple getter
     * @return
     */
    public int getGen() {
        return genNum;
    }

    /**
     * this method calcs the overAll effort, meaning our way to determine the running complexity
     * PARAM how to calculate the running complexity
     * @return
     */
    public double calcTotalEffort() {
        //TODO determine how to calculate the running complexity
//        protected int genNum = 0;   maybe a fine if it gets to the maximus nuber allowed - not sure because than it already have crappy diatnce
//        protected int numOfCrossovers = 0;     //we should decided how heavy is a crossover compared to mutation
//        protected int numOfMutations = 0;
//        protected int numOfPointsEvaluated = 0;   //the bigger the trees, the heavier the calculation of each point
//        protected int sizeOfAllTreesCreated = 0;
        return 0;
    }
}
