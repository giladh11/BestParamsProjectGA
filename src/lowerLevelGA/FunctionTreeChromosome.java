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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import evolutionGaTools.*;
import higherLevelGA.ParamGA;
import interpreter.Context;
import interpreter.Expression;
import interpreter.Function;
import interpreter.SyntaxTreeUtils;

/**
 * this class represents a possible solution for a certain blackBox -
 * 			meaning if every generation of the lower GA there are a lot of these
 */
class FunctionTreeChromosome implements Chromosome<FunctionTreeChromosome, Double> {

	private Expression syntaxTree;

	private Context context;

	private Fitness<FunctionTreeChromosome, Double> fitnessFunction;//can calculate

	private Random random = new Random();

	Double fitness = null;

	/**
	 * constructor
	 * @param context
	 * @param fitnessFunction
	 * @param syntaxTree
     */
	public FunctionTreeChromosome(Context context, Fitness<FunctionTreeChromosome, Double> fitnessFunction, Expression syntaxTree) {
		this.context = context;
		this.fitnessFunction = fitnessFunction;
		this.syntaxTree = syntaxTree;
	}

	/**
	 *	returns the fitness if its allocated
	 */
	public Double getFitness(){
		return fitness;//stop the fitness from being calculated twice
	}

	/**
	 *	sets the fitness
	 */
	public void setFitness(Double n){
		this.fitness = n;//stop the fitness from being calculated twice
	}

	@Override
	public int getSize() {
		return syntaxTree.getNumberOfNodes();
	}

	/**
	 * this method does a cross over with another FunctionTreeChromosome
	 * @param anotherChromosome
	 * @return
     */
	public List<FunctionTreeChromosome> crossover(FunctionTreeChromosome anotherChromosome) {
		List<FunctionTreeChromosome> ret = new ArrayList<FunctionTreeChromosome>(2);

		FunctionTreeChromosome thisClone = new FunctionTreeChromosome(this.context, this.fitnessFunction, this.syntaxTree.clone());
		FunctionTreeChromosome anotherClone = new FunctionTreeChromosome(this.context, this.fitnessFunction, anotherChromosome.syntaxTree.clone());

		Expression thisRandomNode = this.getRandomNode(thisClone.syntaxTree);
		Expression anotherRandomNode = this.getRandomNode(anotherClone.syntaxTree);

		Expression thisRandomSubTreeClone = thisRandomNode.clone();
		Expression anotherRandomSubTreeClone = anotherRandomNode.clone();

		this.swapNode(thisRandomNode, anotherRandomSubTreeClone);
		this.swapNode(anotherRandomNode, thisRandomSubTreeClone);

		ret.add(thisClone);
		ret.add(anotherClone);

		thisClone.optimizeTree();
		anotherClone.optimizeTree();

		return ret;
	}

	/**
	 * the upper level mutate function, uses 7 different mutate operators
	 * @return
     */
	public FunctionTreeChromosome mutate() {
		FunctionTreeChromosome ret = new FunctionTreeChromosome(this.context, this.fitnessFunction, this.syntaxTree.clone());


		int type = this.random.nextInt(7);
		switch (type) {
			case 0:
				ret.mutateByRandomChangeOfFunction();
				break;
			case 1:
				ret.mutateByRandomChangeOfChild();
				break;
			case 2:
				ret.mutateByRandomChangeOfNodeToChild();
				break;
			case 3:
				ret.mutateByReverseOfChildsList();
				break;
			case 4:
				ret.mutateByRootGrowth();
				break;
			case 5:
				ret.syntaxTree = SyntaxTreeUtils.createTree(2, this.context);
				break;
			case 6:
				ret.mutateByReplaceEntireTreeWithAnySubTree();
				break;
		}

		ret.optimizeTree();
		return ret;
	}

	/**
	 * random node used for the cross over
	 * @param tree
	 * @return
     */
	private Expression getRandomNode(Expression tree) {
		List<Expression> allNodesOfTree = tree.getAllNodesAsList();
		int allNodesOfTreeCount = allNodesOfTree.size();
		int indx = this.random.nextInt(allNodesOfTreeCount);
		return allNodesOfTree.get(indx);
	}

	/**
	 * swap one node with a different one - for mutattion
	 * @param oldNode
	 * @param newNode
     */
	private void swapNode(Expression oldNode, Expression newNode) {
		oldNode.setChildren(newNode.getChildren());
		oldNode.setFunction(newNode.getFunction());
		oldNode.setCoefficientsOfNode(newNode.getCoefficientsOfNode());
		oldNode.setVariable(newNode.getVariable());
	}

	/**
	 * creates the process of moving the function on the graph using evolution the shift coefficient
	 */
	public void optimizeTree() {
		this.optimizeTree(70);
	}

	/**
	 * creates the process of moving the function on the graph using evolution the shift coefficient
	 * @param iterations
     */
	public void optimizeTree(int iterations) {

		SyntaxTreeUtils.cutTree(this.syntaxTree, this.context, 6);
		SyntaxTreeUtils.simplifyTree(this.syntaxTree, this.context);

		List<Double> coefficientsOfTree = this.syntaxTree.getCoefficientsOfTree();

		if (coefficientsOfTree.size() > 0) {
			CoefficientsChromosome initialChromosome = new CoefficientsChromosome(coefficientsOfTree, 0.6, 0.8);
			Population<CoefficientsChromosome,Double> population = new Population<CoefficientsChromosome, Double>();
			for (int i = 0; i < 5; i++) {
				population.addChromosome(initialChromosome.mutate());
			}
			population.addChromosome(initialChromosome);

			Fitness<CoefficientsChromosome, Double> fit = new CoefficientsFitness();
			ParamGA tempParam = new ParamGA(0, 1, 1, 0, 0, 0); //optimize tree will use the original setup
			GeneticAlgorithmCoeficient<CoefficientsChromosome, Double> env = new GeneticAlgorithmCoeficient<CoefficientsChromosome, Double>(population, fit, tempParam );

			env.evolve(iterations);

			List<Double> optimizedCoefficients = env.getBest().getCoefficients();

			this.syntaxTree.setCoefficientsOfTree(optimizedCoefficients);
		}
	}

	/**
	 * getter for context
	 * @return
     */
	public Context getContext() {
		return this.context;
	}

	/**
	 * setter for context
	 * @param context
     */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * getter for the inner syntax tree
	 * @return
     */
	public Expression getSyntaxTree() {
		return this.syntaxTree;
	}

	/**
	 * inner class.
	 * their is an inner GA process happening whole evaluating the FunctionTreeChromosome
	 * this inner GA is used to find different coefficient
	 */
	private class CoefficientsChromosome implements Chromosome<CoefficientsChromosome, Double>, Cloneable {

		private double pMutation;

		private double pCrossover;

		private List<Double> coefficients;

		public CoefficientsChromosome(List<Double> coefficients, double pMutation, double pCrossover) {
			this.coefficients = coefficients;
			this.pMutation = pMutation;
			this.pCrossover = pCrossover;
		}

		/**
		 * only because i changed the interface of chromosome
		 * @return
         */
		public Double getFitness(){
			return new Double(-1);
		}

		/**
		 * only because i changed the interface of chromosome
		 * @return
		 */
		public void setFitness(Double n){
			return;
		}

		@Override
		public int getSize() {
			//should never be called
			return 1/0;
		}


		/**
		 * cross over for the inner coefficient chromosome
		 * @param anotherChromosome
         * @return
         */
		public List<CoefficientsChromosome> crossover(CoefficientsChromosome anotherChromosome) {
			List<CoefficientsChromosome> ret = new ArrayList<FunctionTreeChromosome.CoefficientsChromosome>(2);

			CoefficientsChromosome thisClone = this.clone();
			CoefficientsChromosome anotherClone = anotherChromosome.clone();

			for (int i = 0; i < thisClone.coefficients.size(); i++) {
				if (FunctionTreeChromosome.this.random.nextDouble() > this.pCrossover) {
					thisClone.coefficients.set(i, anotherChromosome.coefficients.get(i));
					anotherClone.coefficients.set(i, this.coefficients.get(i));
				}
			}
			ret.add(thisClone);
			ret.add(anotherClone);

			return ret;
		}

		/**
		 * mutation for the inner coefficient chromosome
		 * @return
         */
		public CoefficientsChromosome mutate() {
			CoefficientsChromosome ret = this.clone();
			for (int i = 0; i < ret.coefficients.size(); i++) {
				if (FunctionTreeChromosome.this.random.nextDouble() > this.pMutation) {
					double coeff = ret.coefficients.get(i);
					coeff += FunctionTreeChromosome.this.context.getRandomMutationValue();
					ret.coefficients.set(i, coeff);
				}
			}
			return ret;
		}

		/**
		 * clone something
		 * @return
         */
		protected CoefficientsChromosome clone() {
			List<Double> ret = new ArrayList<Double>(this.coefficients.size());
			for (double d : this.coefficients) {
				ret.add(d);
			}
			return new CoefficientsChromosome(ret, this.pMutation, this.pCrossover);
		}

		public List<Double> getCoefficients() {
			return this.coefficients;
		}

	}

	private class CoefficientsFitness implements Fitness<CoefficientsChromosome, Double> {

		/**
		 * calculate for the inner coefficient chromosome
		 * @param chromosome
         * @return
         */
		public Double calculate(CoefficientsChromosome chromosome) {
			FunctionTreeChromosome.this.syntaxTree.setCoefficientsOfTree(chromosome.getCoefficients());
			return FunctionTreeChromosome.this.fitnessFunction.calculate(FunctionTreeChromosome.this);
		}

	}



	// ****************************these are are specific 7 inner mutating functions            *****************************
	/**
	 * creates a completly new tree as a mutation
	 */
	private void mutateByReplaceEntireTreeWithAnySubTree() {
		this.syntaxTree = this.getRandomNode(this.syntaxTree);
	}

	/**
	 * mutate by adding an upper root to the tree
	 */
	private void mutateByRootGrowth() {
		Function function = this.context.getRandomNonTerminalFunction();
		Expression newRoot = new Expression(function);
		newRoot.addChild(this.syntaxTree);
		for (int i = 1; i < function.argumentsCount(); i++) {
			newRoot.addChild(SyntaxTreeUtils.createTree(0, this.context));
		}
		for (int i = 0; i < function.argumentsCount(); i++) {
			newRoot.addCoefficient(this.context.getRandomValue());
		}
		this.syntaxTree = newRoot;
	}

	private void mutateByRandomChangeOfFunction() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		Function oldFunction = mutatingNode.getFunction();
		Function newFunction = null;

		// trying to avoid case, when newFunction == oldFunction
		// hope, that in one of 3 iterations - we'll get
		// newFunction which != oldFunction
		for (int i = 0; i < 3; i++) {
			if (this.random.nextDouble() > 0.5) {
				newFunction = this.context.getRandomNonTerminalFunction();
			} else {
				newFunction = this.context.getRandomTerminalFunction();
			}

			if (newFunction != oldFunction) {
				break;
			}
		}

		mutatingNode.setFunction(newFunction);

		if (newFunction.isVariable()) {
			mutatingNode.setVariable(this.context.getRandomVariableName());
		}

		int functionArgumentsCount = newFunction.argumentsCount();
		int mutatingNodeChildsCount = mutatingNode.getChildren().size();

		if (functionArgumentsCount > mutatingNodeChildsCount) {
			for (int i = 0; i < ((functionArgumentsCount - mutatingNodeChildsCount) + 1); i++) {
				mutatingNode.getChildren().add(SyntaxTreeUtils.createTree(1, this.context));
			}
		} else if (functionArgumentsCount < mutatingNodeChildsCount) {
			List<Expression> subList = new ArrayList<Expression>(functionArgumentsCount);
			for (int i = 0; i < functionArgumentsCount; i++) {
				subList.add(mutatingNode.getChildren().get(i));
			}
			mutatingNode.setChildren(subList);
		}

		int functionCoefficientsCount = newFunction.coefficientsCount();
		int mutatingNodeCoefficientsCount = mutatingNode.getCoefficientsOfNode().size();
		if (functionCoefficientsCount > mutatingNodeCoefficientsCount) {
			for (int i = 0; i < ((functionCoefficientsCount - mutatingNodeCoefficientsCount) + 1); i++) {
				mutatingNode.addCoefficient(this.context.getRandomValue());
			}
		} else if (functionCoefficientsCount < mutatingNodeCoefficientsCount) {
			List<Double> subList = new ArrayList<Double>(functionCoefficientsCount);
			for (int i = 0; i < functionCoefficientsCount; i++) {
				subList.add(mutatingNode.getCoefficientsOfNode().get(i));
			}
			mutatingNode.setCoefficientsOfNode(subList);
		}
	}

	private void mutateByReverseOfChildsList() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);
		Function mutatingNodeFunction = mutatingNode.getFunction();

		if ((mutatingNode.getChildren().size() > 1)
				&& (!mutatingNodeFunction.isCommutative())) {

			Collections.reverse(mutatingNode.getChildren());

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfChild() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		if (!mutatingNode.getChildren().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChildren().size());

			mutatingNode.getChildren().set(indx, SyntaxTreeUtils.createTree(1, this.context));

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

	private void mutateByRandomChangeOfNodeToChild() {
		Expression mutatingNode = this.getRandomNode(this.syntaxTree);

		if (!mutatingNode.getChildren().isEmpty()) {

			int indx = this.random.nextInt(mutatingNode.getChildren().size());

			Expression child = mutatingNode.getChildren().get(indx);

			this.swapNode(mutatingNode, child.clone());

		} else {
			this.mutateByRandomChangeOfFunction();
		}
	}

}
