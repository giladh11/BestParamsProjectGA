package higherLevelGA;


import interpreter.Expression;
import lowerLevelGA.BlackBoxTree;
import evolutionGaTools.Effort;

/**
 * this class represents the bestModelCandidate returned by a certain run of a SymRegSolverChromosome
 */
public class BestModelCandidate {

    private Expression bestSyntaxTree;
    private Effort effort;
    private double distanceFromBlackBox;
    protected double fitness;

    /**
     * simple constuctor... doesn't get the distance (will be calculated later) and can not calc fitness without the distance
     * @param bestSyntaxTree
     * @param effort
     */
    public BestModelCandidate(Expression bestSyntaxTree, Effort effort) {
        this.bestSyntaxTree = bestSyntaxTree;
        this.effort = effort;
    }


    /**
     * simple toString
     * @return
     */
    public String toString(){
        return "Function: " + bestSyntaxTree.print() + "\n" +"      distance: " + distanceFromBlackBox+ " Effort: <" + effort + "> higherLvlFitness: " + fitness;
    }


    /**
     * this method will get a blackbox and check its distance from the bestSyntaxTree
     * the amount of points is a project parameter
     * @param blackBox
     * @return
     */
    public double fitnessCalculator(BlackBoxTree blackBox) {
        distanceFromBlackBox = blackBox.measureDistanceFromCandidate(bestSyntaxTree);
        fitness = 1/distanceFromBlackBox + effort.calcTotalEffort();//TODO decide how to calc fitness PARAM set how to calc the fitness of ParamGA
        return distanceFromBlackBox;
    }

    /**
     * simple getter
     * @return
     */
    public Effort getEffort() {
        return effort;
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
    public double getFitness() {
        return fitness;
    }


}

