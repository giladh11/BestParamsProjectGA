package interpreter;

import parsii.eval.Scope;
import parsii.eval.Variable;

/**
 * CustomContext is the equivalent Context class for CustomExpression.
 */


public class CustomContext implements AbstractContext {
    private Scope scope;


    public CustomContext(){
        scope = Scope.create();

    }
    @Override
    public void setVariable(String variable, double value) {
        Variable x = scope.getVariable(variable);
        x.setValue(value);
    }

    public Scope getScope() {
        return scope;
    }
}
