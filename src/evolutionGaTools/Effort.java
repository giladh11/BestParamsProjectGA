package evolutionGaTools;

/**
 * this class represent all the data involving the Effort in a "trysolving" run of a symRegSolverChromosome
 */
public class Effort {
    protected int genNum;
    protected int numOfCrossovers;
    protected int numOfMutations;
    protected int numOfPointsEvaluated;


    /**
     * simple constructor
     * @param genNum
     * @param numOfCrossovers
     * @param numOfMutations
     * @param numOfPointsEvaluated
     */
    public Effort(int genNum, int numOfCrossovers, int numOfMutations, int numOfPointsEvaluated) {
        this.genNum = genNum;
        this.numOfCrossovers = numOfCrossovers;
        this.numOfMutations = numOfMutations;
        this.numOfPointsEvaluated = numOfPointsEvaluated;
    }

    @Override
    public String toString() {
        return "genNum: " + genNum +",  numOfCrossovers: "+numOfCrossovers +" , numOfMutations: "+ numOfMutations + ", numOfPointsEvaluated: " + numOfPointsEvaluated;
    }

    /**
     * simple getter
     * @return
     */
    public int getGen() {
        return genNum;
    }
}
