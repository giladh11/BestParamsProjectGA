package evolutionGaTools;

/**
 * this class represent all the data involving the Effort in a "trysolving" run of a symRegSolverChromosome
 */
public class Effort {
    protected int genNum = 0;
    protected int numOfCrossovers = 0;
    protected int numOfMutations = 0;
    protected int numOfPointsEvaluated = 0;
    protected int sumOfTreesSizesCreated = 0;


    /**
     * simple constructor
     */
    public Effort(int sizeOfAllTreesCreatedInInitialPopulation) {
        this.sumOfTreesSizesCreated =sizeOfAllTreesCreatedInInitialPopulation;
    }

    @Override
    public String toString() {
        return "genNum: " + genNum +",  numOfCrossovers: "+numOfCrossovers +" , numOfMutations: "+ numOfMutations + ", numOfPointsEvaluated: " + numOfPointsEvaluated +  " sumOfTreesSizesCreated: " + sumOfTreesSizesCreated;
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
        double effort =
                numOfCrossovers/100+  //order of 1...5
                //genNum+
                numOfMutations/100+ //order of 1...5
                numOfPointsEvaluated/10000 + //order of 10...20
                sumOfTreesSizesCreated /10000;//should be in the order of 10
        return effort;
    }
}
