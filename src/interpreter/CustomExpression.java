package interpreter;


import interpreter.java.parsii.eval.Parser;
import interpreter.java.parsii.eval.StringBasedExpression;
import interpreter.java.parsii.tokenizer.ParseException;


/**
 * This class is used to create expression from a given string.
 */
public class CustomExpression implements AbstractExpression {

    String expression;


    public CustomExpression(String expression) {
        this.expression = expression;
    }

    @Override

    public double eval(AbstractContext context) {

        StringBasedExpression expr = null;
        CustomContext temp = (CustomContext) context;
        try {
            expr = Parser.parse(expression, temp.getScope());
        } catch (ParseException e) {
            System.out.println("CustomExpression error: unable to parse expression");
        }
        return expr.evaluate();
    }

    public String toString(){
        return expression;
    }
}
