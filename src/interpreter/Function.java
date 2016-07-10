package interpreter;

import java.util.List;

/**
 * dont really get this!!
 */
public interface Function {

	double eval(Expression expression, Context context);

	int argumentsCount();

	boolean isVariable();

	boolean isNumber();

	boolean isCommutative();

	String print(Expression expression);

	List<Double> getCoefficients(Expression expression);

	void setCoefficients(Expression expression, List<Double> coefficients, int startIndex);

	int coefficientsCount();

}
