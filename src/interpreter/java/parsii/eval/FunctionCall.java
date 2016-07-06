/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package interpreter.java.parsii.eval;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the invocation of a function.
 */
public class FunctionCall extends StringBasedExpression {

    private static final long serialVersionUID = 5758404497296893915L;
    private List<StringBasedExpression> parameters = new ArrayList<StringBasedExpression>();
    private Function function;

    @Override
    public double evaluate() {
        return function.eval(parameters);
    }

    @Override
    public StringBasedExpression simplify() {
        if (!function.isNaturalFunction()) {
            return this;
        }
        for (StringBasedExpression expr : parameters) {
            if (!expr.isConstant()) {
                return this;
            }
        }
        return new Constant(evaluate());
    }

    /**
     * Sets the function to evaluate.
     *
     * @param function the function to evaluate
     */
    public void setFunction(Function function) {
        this.function = function;
    }

    /**
     * Adds an expression as parameter.
     *
     * @param expression the parameter to add
     */
    public void addParameter(StringBasedExpression expression) {
        parameters.add(expression);
    }

    /**
     * Returns all parameters added so far.
     *
     * @return a list of parameters added to this call
     */
    public List<StringBasedExpression> getParameters() {
        return parameters;
    }

}
