package higherLevelGA;


import interpreter.Expression;
import lowerLevelGA.BlackBoxTree;
import evolutionGaTools.Effort;
import p.PARAMs;

/**
 * this class represents the bestModelCandidate returned by a certain run of a SymRegSolverChromosome
 */
public class BestModelCandidate {

    private Expression bestSyntaxTree;
    private Effort effortElement;
    private double distanceFromBlackBox;
    protected double hFitnessElement;

    /**
     * simple constuctor... doesn't get the distance (will be calculated later) and can not calc hFitnessElement without the distance
     * @param bestSyntaxTree
     * @param effortElement
     */
    public BestModelCandidate(Expression bestSyntaxTree, Effort effortElement) {
        this.bestSyntaxTree = bestSyntaxTree;
        this.effortElement = effortElement;
    }


    /**
     * simple toString
     * @return
     */
    public String toString(){
        return "Function: " + bestSyntaxTree.print() + "\n" +"      distance: " + distanceFromBlackBox+ " Effort: <" + effortElement + "> higherLvlFitness: " + hFitnessElement;
    }


    /**
     * A bestModelCandidate is created by SymRegSolverChromosome and the hFitnessElement it holds represents the
     * @param blackBox
     * @return
     */
    public double higherFitnessElementCalculator(BlackBoxTree blackBox) {
        distanceFromBlackBox = blackBox.measureDistanceFromCandidate(bestSyntaxTree);
        double distanceWeight = distanceFromBlackBox* 100 / PARAMs.EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP;//should be in the order 1000
        double effortWeight = effortElement.calcTotalEffort();//should be in the order of 100
                            System.out.println("distanceWeight: "+distanceWeight+ ", effortWeight: "+ effortWeight);
        hFitnessElement = distanceWeight + effortWeight;// PARAM set how to calc the hFitnessElement of ParamGA

        return distanceFromBlackBox;
    }

    /**
     * simple getter
     * @return
     */
    public Effort getEffortElement() {
        return effortElement;
    }
    /**
     * simple getter
     * @return
     */
    public double getDistanceFromBlackBox() {
        return distanceFromBlackBox;
    }
    /**
     * simple getter
     * @return
     */
    public double getHFitnessElement() {
        return hFitnessElement;
    }


}

