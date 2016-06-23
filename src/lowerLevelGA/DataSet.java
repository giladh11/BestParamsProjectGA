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

import interpreter.Context;
import interpreter.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


/**
 * this class represents a DATASET of points and their values.
 * will usually be created by SymRegSolverChromosome before hading it out to the SolverGAEngine
 */
public class DataSet implements ComparableDataSet {

	private List<Point> points = new LinkedList<Point>();

	public DataSet(Point... points) {
		for (Point point : points) {
			this.points.add(point);
		}
	}



	public DataSet(List<Point> points) {
		this.points.addAll(points);
	}

	public void addTarget(Point point){
		this.points.add(point);
	}


	@Override
	public double distanceFromExpression(Expression expression, Context context) {
		double diff = 0;

		for (Point point : this.points) {
			for (Entry<String, Double> e : point.getContextState().entrySet()) {
				String variableName = e.getKey();
				Double variableValue = e.getValue();
				context.setVariable(variableName, variableValue);
			}
			double targetValue = point.getYval();
			double calculatedValue = expression.eval(context);
			diff += this.sqr(targetValue - calculatedValue);
		}

		return diff;
	}

	private double sqr(double x) {
		return x * x;
	}

}
