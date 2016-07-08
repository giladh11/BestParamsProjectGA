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
}
