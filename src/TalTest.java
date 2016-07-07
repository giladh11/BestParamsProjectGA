import higherLevelGA.ParamGA;
import higherLevelGA.SymRegSolverChromosome;
import lowerLevelGA.BlackBoxTree;

/**
 * Created by talwanich on 05/07/2016.
 */
public class TalTest {

    public static void main(String[] args) {

        int populationSize; double pCrossover; double pMutation; int dataSetSize; int maxInitialTreeDepth; int bloatPenaltyRate;
        populationSize=10; pCrossover=0.4; pMutation=0.1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
        ParamGA paramGA = new ParamGA(populationSize, pCrossover, pMutation, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);//(int populationSize, double pMutation, double pCrossover, int dataSetSize, int maxInitialTreeDepth, int bloatPenaltyRate)

        BlackBoxTree tree = new BlackBoxTree("x^3 + 1");
        for (int i = 0; i < 20; i++){
            System.out.println("f(" + i + ") = " + tree.eval(i));
        }


    }
}
