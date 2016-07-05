package interpreter;

/**
 * An interface for two types of expressions:
 * a. Expression
 * b. CustomExpression
 */
public interface AbstractExpression {

    double eval(AbstractContext context);
}
