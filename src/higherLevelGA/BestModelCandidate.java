package higherLevelGA;

/**
 * gilad is checking
 *  this Class represents a candidate for a best model for a certain black box
 *  will usually be returned by SymRegSolverChromosome
 *  Created by Gilad on 21/06/2016.
 */


import interpreter.Expression;
import lowerLevelGA.BlackBoxTree;

/**
 *
 */
public class BestModelCandidate {

    private Expression bestSyntaxTree;
    private double effort;
    private double distanceFromBlackBox = -1;
    private double fitness = -1;

    public BestModelCandidate(Expression bestSyntaxTree, double effort) {
        this.bestSyntaxTree = bestSyntaxTree;
        this.effort = effort;
    }

    //calcs the overall fitness with the effort level

    public String toString(){
        return "Function: " + bestSyntaxTree.print() + "\n" +"      distance: " + distanceFromBlackBox+ " Effort: " + effort + " fitness: " + fitness;
    }


    /**
     * this method will get a blackbox and check its distance from the bestSyntaxTree
     * PARAM the amount of points is a project parameter
     * @param blackBox
     * @return
     */
    public double fitnessCalculator(BlackBoxTree blackBox) {
        distanceFromBlackBox = blackBox.measureDistanceFromCandidate(bestSyntaxTree);
        return distanceFromBlackBox;
    }

    /**
     * simple getter
     * @return
     */
    public double getEffort() {
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
