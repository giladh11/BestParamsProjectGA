package interpreter;

/**
 * AbstractContext interface for two type of contexts:
 * a. Context for Expression
 * b. CustomContext for CustomExpression
 */
public interface AbstractContext {

    void setVariable(String variable, double value);

}
