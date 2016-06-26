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

import java.util.Collection;
import java.util.List;

import evolutionGaTools.GeneticAlgorithm;
import evolutionGaTools.Fitness;
import evolutionGaTools.IterartionListener;
import evolutionGaTools.Population;
import higherLevelGA.BestModelCandidate;
import higherLevelGA.ParamGA;
import interpreter.Context;
import interpreter.Expression;
import interpreter.Function;
import interpreter.SyntaxTreeUtils;

/**
 * this class represents the object that manage the lower level GA
 * this object will be built within the higher level chromosome SymRegSolverChromosome by the GA params it withholds
 */
public class SolverGAEngine {

	private ParamGA paramGA;

	private GeneticAlgorithm<FunctionTreeChromosome, Double> environment;

	private Context context; //this context holds the variables

	private ComparableDataSet comparableDataSet;//data set should be created from the black box in SymRegSolverChromosome
	//IMPROVE we can pass to the solver the black box itself so it could change a bit the data set in every iteration

	/**
	 * constructor
	 * @param comparableDataSet
	 * @param variables
     */
	public SolverGAEngine(ComparableDataSet comparableDataSet, ParamGA paramGA) {
		this.context = new Context(paramGA.getBaseFunctions(), paramGA.getVariables());
		this.comparableDataSet = comparableDataSet;
		this.paramGA = paramGA;
		DistanceMeasurer distanceMeasurer = new DistanceMeasurer(this.comparableDataSet);
		Population<FunctionTreeChromosome> population = this.createPopulation(this.context, distanceMeasurer, paramGA.getPopulationSize());
		this.environment = new GeneticAlgorithm<FunctionTreeChromosome, Double>(population, distanceMeasurer, paramGA);
		this.environment.setParentChromosomesSurviveCount(paramGA.getInitialParentChromosomesSurviveCount());
	}

	// DELETE lowerLevelGA constructor for problems with example code
//	public SolverGAEngine(ComparableDataSet comparableDataSet, Collection<String> variables, List<? extends Function> baseFunctions) {
//		this.context = new Context(baseFunctions, variables);
//		this.comparableDataSet = comparableDataSet;
//		this.paramGA = null;
//		DistanceMeasurer distanceMeasurer = new DistanceMeasurer(this.comparableDataSet);
//		Population<FunctionTreeChromosome> population = this.createPopulation(this.context, distanceMeasurer, paramGA.getPopulationSize());
//		this.environment = new GeneticAlgorithm<FunctionTreeChromosome, Double>(population, distanceMeasurer, paramGA);
//		this.environment.setParentChromosomesSurviveCount(paramGA.getInitialParentChromosomesSurviveCount());
//	}


	/**
	 * creates a population in the constuctor and in every evolution iteration
	 * @param context
	 * @param fitnessFunction
	 * @param populationSize
     * @return
     */
	private Population<FunctionTreeChromosome> createPopulation(Context context, Fitness<FunctionTreeChromosome, Double> fitnessFunction, int populationSize) {
		Population<FunctionTreeChromosome> population = new Population<FunctionTreeChromosome>();
		for (int i = 0; i < populationSize; i++) {
			FunctionTreeChromosome chromosome =
					new FunctionTreeChromosome(context, fitnessFunction, SyntaxTreeUtils.createTree(paramGA.getMaxInitialTreeDepth(), context));
			population.addChromosome(chromosome);
		}
		return population;
	}

	/**
	 * iteration listener for prints
	 * @param listener
     */
	public void addIterationListener(final SolverGAEngineIterationListener listener) {
		this.environment.addIterationListener(new IterartionListener<FunctionTreeChromosome, Double>() {
			@Override
			public void update(GeneticAlgorithm<FunctionTreeChromosome, Double> environment) {
				listener.update(SolverGAEngine.this);
			}
		});
	}

	/**
	 * starts the evolution
	 * @param iterationsCount
     */
	public void evolve(int iterationsCount) {
		this.environment.evolve(iterationsCount);
	}

	/**
	 * return the context
	 * @return
     */
	public Context getContext() {
		return this.context;
	}

	/**
	 * a getter after the evolution is done for the best tree
	 * @return
     */
	public Expression getBestSyntaxTree() {
		return this.environment.getBest().getSyntaxTree();
	}

	/**
	 * a getter for the effort level used to get the best tree
	 * @return
     */
	public double getEffortLevelUsedToReachBest() {
		//TODO getAffortLevelUsedToReachBest
		return 0;
	}

	/**
	 * this method only compure the disstance of the expression from the dataset held by SolverGaEngine
	 * should not really be used
	 * @param expression
	 * @return
     */
	public double fitnessMeasureShouldNotBeUsed(Expression expression) {
		return this.comparableDataSet.distanceFromExpression(expression, this.context);
	}

	public void terminate() {
		this.environment.terminate();
	}

	public int getIteration() {
		return this.environment.getIteration();
	}

	public void setParentsSurviveCount(int n) {
		this.environment.setParentChromosomesSurviveCount(n);
	}


	/**
	 * This method is called by SymRegSolverChromosome.evolve
	 * after finishing one evolution process. Should return
	 * the best model found and the effort level of it's evolving.
	 *
	 * @return
     */
	public BestModelCandidate buildBestModelCandidate() {

		return new BestModelCandidate(getBestSyntaxTree(), getEffortLevelUsedToReachBest());
	}

}
