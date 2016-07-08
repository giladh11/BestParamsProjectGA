package lowerLevelGA;

import interpreter.*;

/**
 * This class represents a black box which encapsulates some function
 *      using a syntax tree.
 * This function is used as the target function for the lower level GA evolution.
 * Each SymRegSolverChromosome can request the evaluation of the function on a given
 *      value of x.
 */
public class BlackBoxTree {

    private static int objectiveNumOfPointsForDistanceMeasurer;

    //private Expression function;
    private AbstractContext context;
    private static AbstractContext contextRegular = null;//TODO whats this??
    private DataSet generalDataSet = null;
    private AbstractExpression function;

    public BlackBoxTree(String expression){
        function = new CustomExpression(expression);
        context = new CustomContext();
    }
    /**
     * constructor
     * @param maxSizeOfRandomTree
     * @param context
     */
    public BlackBoxTree(int maxSizeOfRandomTree, Context context) {
        this.function =  SyntaxTreeUtils.createTree(maxSizeOfRandomTree, context);
        this.context = context;
    }

    /**
     * constructor
     * @param function
     * @param context
     */
    public BlackBoxTree(Expression function, Context context) {
        this.function = function;
        this.context = context;
    }

    /**
     * only supposed to be used once
     * @param contextRegular
     */
    public static void setContextRegular(AbstractContext contextRegular) {
        BlackBoxTree.contextRegular = contextRegular;
    }


    /**
     * simple eval function
     * @param x
     * @return
     */
    public double eval(double x){
        context.setVariable("x", x);
        return function.eval(context);
    }

    /**
     * this is used to determine the OBJECTIVE distance!!
     * this method will measure the distance from an expression using the already created general dataset.
     */
    public double measureDistanceFromCandidate(Expression bestModelExpression){
        if(this.generalDataSet == null) { //CHECK this function
            this.generalDataSet = new DataSet(this.function, objectiveNumOfPointsForDistanceMeasurer, this.context);
        }
        return this.generalDataSet.distanceFromExpression(bestModelExpression,contextRegular);
    }

//    public void print(){
//        System.out.println(function.print());
//    }

    /**
     * simple toString
     * @return
     */
    public String toString(){
        return function.toString();
    }

    /**
     * simple getter for the function
     * @return
     */
    public AbstractExpression getFunction() {
        return function;
    }

    /**
     * simple getter
     * @return
     */
    public AbstractContext getContext() {
        return context;
    }

    /**
     * public setter for num of points
     * @param objectiveNumOfPointsForDistanceMeasurer
     */
    public static void setObjectiveNumOfPointsForDistanceMeasurer(int objectiveNumOfPointsForDistanceMeasurer) {
        BlackBoxTree.objectiveNumOfPointsForDistanceMeasurer = objectiveNumOfPointsForDistanceMeasurer;
    }
}
