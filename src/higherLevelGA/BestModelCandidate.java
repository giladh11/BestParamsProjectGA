package higherLevelGA;

/**
 * gilad is checking
 *  this Class represents a candidate for a best model for a certain black box
 *  will usually be returned by SymRegSolverChromosome
 *  Created by Gilad on 21/06/2016.
 */


import interpreter.Expression;

/**
 *
 */
public class BestModelCandidate {

    private Expression bestSyntaxTree;
    private double effort;

    public BestModelCandidate(Expression bestSyntaxTree, double effort) {
        this.bestSyntaxTree = bestSyntaxTree;
        this.effort = effort;
    }

    //calcs the overall fitness with the effort level

    public String toString(){
        return "Function: " + bestSyntaxTree.print() + "\n" + "Effort: " + effort;
    }



    //TODO fitness calculator method
    // - get the black box - checks the distance from it
    // according to many points and
    //ADD context to use eval

}
