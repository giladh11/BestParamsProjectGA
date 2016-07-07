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

import interpreter.*;

import java.util.LinkedList;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;


/**
 * this class represents a DATASET of points and their values.
 * will usually be created by SymRegSolverChromosome before hading it out to the SolverGAEngine
 */
public class DataSet implements ComparableDataSet {
	private Random rand = new Random();
	private List<Point> points = new LinkedList<Point>();

//	public DataSet(AbstractExpression function, int i, Point... points) {
//		for (Point point : points) {
//			this.points.add(point);
//		}
//	}


	/**
	 * constuctor that gets a list of points
	 * @param points
	 */
	public DataSet(List<Point> points) {
		this.points.addAll(points);
	}

	/**
	 * constuctor that creates a random data set according to a function and a size
	 * @param function
	 * @param size
	 * @param context
	 */
	public DataSet(AbstractExpression function, int size, AbstractContext context) {
		int numOfPoints = 0;
		double x, fx;
		while(size>0) {
			x = getRandomValueInRange();
			context.setVariable("x", x);
			fx = function.eval(context);
			this.addTarget(new Point().when("x", x).setYval(fx));
			size--;
		}
	}



	/**
	 * See createDataSet
	 * @return
	 */
	public double getRandomValueInRange() {
		int minimum = -50, maximum = 50;//PARAM getRandomValueInRange() for the chosing of x values of the data sets
		double randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
		return randomNum;
	}

	/**
	 * adds a point to the dataset
	 * @param point
     */
	public void addTarget(Point point){
		this.points.add(point);
	}


	/**
	 * calculate the average distance from expression
	 * @param expression
	 * @param context
     * @return diff/num average distance
     */
	public double distanceFromExpression(AbstractExpression expression, AbstractContext context) {
		double diff = 0;
		int num = 0;

		//GILAD GOOVER
		//When the diff is too small,
		//the program refers to it as NaN or Infinity.
		//I suggest you to set some bound under which
		//the diff is essentially zero.
		
		for (Point point : this.points) {
			for (Entry<String, Double> e : point.getContextState().entrySet()) {
				String variableName = e.getKey();
				Double variableValue = e.getValue();
				context.setVariable(variableName, variableValue);
			}
			double targetValue = point.getYval();
			double calculatedValue = expression.eval(context);
			diff += Math.abs(targetValue - calculatedValue);
			num++;
		}

		return diff/num;//average distance
	}



//	private double sqr(double x) {
//		return x * x;
//	}

	//simple list data structure
	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}

}
