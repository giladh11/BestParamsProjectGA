package lowerLevelGA;

import interpreter.Context;
import interpreter.Expression;

/**
 * This class represents a black box which encapsulates some function
 * using syntax tree.
 * This function is used as the target function for the lower level GA evolution.
 * Each SymRegSolverChromosome can request the evaluation of the function on a given
 * value of x.
 */
public class BlackBoxTree {

    private Expression function;
    private Context context;

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
     * simple eval function
     * @param x
     * @return
     */
    public double eval(double x){
        context.setVariable("x", x);
        return function.eval(context);
    }

    public void print(){
        System.out.println(function.print());
    }
}
