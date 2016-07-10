package lowerLevelGA;

import evolutionGaTools.Fitness;
import interpreter.Context;
import interpreter.Expression;

/**
 * this is a simple class that hold a Dataset and can calcute distance from a given chromosome;
 */
public class DistanceMeasurer implements Fitness<FunctionTreeChromosome, Double> {

	private ComparableDataSet comparableDataSet;

	public DistanceMeasurer(ComparableDataSet comparableDataSet) {
		this.comparableDataSet = comparableDataSet;
	}

	/**
	 * this is a method that calcute the distance from the given model the chromosome holds..
	 * this method implement an interface method of Fitness<>
	 * @param chromosome
	 * @return
     */
	@Override
	public Double calculate(FunctionTreeChromosome chromosome) {
		Expression expression = chromosome.getSyntaxTree();
		Context context = chromosome.getContext();
		return this.comparableDataSet.distanceFromExpression(expression, context);
	}

}
