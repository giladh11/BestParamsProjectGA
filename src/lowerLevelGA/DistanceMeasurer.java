/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package lowerLevelGA;

import evolutionGaTools.Fitness;
import interpreter.Context;
import interpreter.Expression;

/**
 * this is a simple class that hold a Dataset and can calcute distance from a given chromosome;
 */
class DistanceMeasurer implements Fitness<FunctionTreeChromosome, Double> {

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
