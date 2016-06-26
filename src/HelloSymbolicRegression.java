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
import higherLevelGA.BestModelCandidate;
import higherLevelGA.ParamGA;
import higherLevelGA.SymRegSolverChromosome;
import interpreter.Context;
import interpreter.Expression;
import interpreter.Functions;
import interpreter.SyntaxTreeUtils;
import lowerLevelGA.*;

import java.util.LinkedList;
import java.util.List;


/**
 * f(x) - ? <br/>
 * 
 * f(0) = 0 <br/>
 * f(1) = 11 <br/>
 * f(2) = 24 <br/>
 * f(3) = 39 <br/>
 * f(4) = 56 <br/>
 * f(5) = 75 <br/>
 * f(6) = 96 <br/>
 * 
 * (target function is f(x) = x^2 + 10*x)
 */
public class HelloSymbolicRegression {

	private static double epsilon = 0.001;

	public static void main(String[] args) {
		DataSet dataSet;

		SolverGAEngine engine;

		ParamGA paramGA;

		//*********************************************************
		System.out.println("!   !!!          run 1              !!!");
		dataSet =
				new DataSet();

		paramGA = new ParamGA(1,1, 10, 0,10,1,1);//pMutation, pCrossover, populationSize, bloatPenaltyRate, dataSetSize, initialParentChromosomesSurviveCount, maxInitialTreeDepth

		for(int i = 0; i < 15; i++){
			dataSet.addTarget(new Point().when("x", i).setYval(i*i+10*i));
		}

		engine =
				new SolverGAEngine(
						dataSet,
						// define variables
						list("x"),
						// define base functions
						list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT), paramGA);

		//addListener(engine);

		// 200 iterations
		//engine.evolve(200);

		System.out.println("---- Test BlackBox ----");
		SymRegSolverChromosome symRegSolverChromosome = new SymRegSolverChromosome(paramGA);
		Context sharedContext = new Context(list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT), list("x"));
		Expression randomTree = SyntaxTreeUtils.createTree(4, sharedContext);
		BlackBoxTree blackBoxTree = new BlackBoxTree(randomTree, sharedContext);

		BestModelCandidate ans = symRegSolverChromosome.trySolving(blackBoxTree);
		System.out.println(" Random function is ");
		blackBoxTree.print();
		System.out.println(" Best Model ");
		System.out.println(ans);

//		System.out.println(" Distance: ");
//
//		for(int i = 0; i < 50; i++){
//			System.out.println(blackBoxTree.eval(i) - ans.eval(i));
//
//		}


//		//*********************************************************
//		System.out.println("!   !!!          run 2              !!!");
//		//dataSet =
//				new DataSet();
//
//		//paramGA = new ParamGA(0,0, 5, 0,0,1,1);
//
//		SymRegSolverChromosome symRegSolver2 = new SymRegSolverChromosome(paramGA);
	//	BlackBoxTree blackBoxTree = new BlackBoxTree();
//		symRegSolver2.trySolving(blackBoxTree);
//
////		//for(int i = 0; i < 15; i++){
////			dataSet.addTarget(new Point().when("x", i).setYval(i*i+10*i));
////		}
//
//
//
//		addListener(engine);
//
//		// 200 iterations
//		engine.evolve(200);


//		//*********************************************************
//		System.out.println("!   !!!          run 5              !!!");
//		dataSet =
//				new DataSet();
//
//		for(int i = 0; i < 15; i++){
//			dataSet.addTarget(new Point().when("x", i).setYval(i*i+10*i));
//		}
//
//		engine =
//				new SolverGAEngine(
//						dataSet,
//						// define variables
//						list("x"),
//						// define base functions
//						list(Functions.java.ADD, Functions.java.SUB, Functions.java.MUL, Functions.java.VARIABLE, Functions.java.CONSTANT));
//
//		addListener(engine);
//
//		// 200 iterations
//		engine.evolve(200);
//		//*********************************************************
//		System.out.println("!   !!!          run 6              !!!");
//		dataSet =
//				new DataSet();
//
//		for(int i = 0; i < 15; i++){
//			dataSet.addTarget(new Point().when("x", i).setYval(i*i*i+10*i + 5));
//		}
//
//		engine =
//				new SolverGAEngine(
//						dataSet,
//						// define variables
//						list("x"),
//						// define base functions
//						list(Functions.java.ADD, Functions.java.SUB, Functions.java.MUL, Functions.java.VARIABLE, Functions.java.CONSTANT));
//
//		addListener(engine);
//
//		// 200 iterations
//		engine.evolve(200);
//		//*********************************************************
//		System.out.println("!   !!!          run 7              !!!");
//		dataSet =
//				new DataSet();
//
//		for(int i = 0; i < 15; i++){
//			dataSet.addTarget(new Point().when("x", i).setYval(i*i*i*i - i*i + 23*i + 1));
//		}
//
//		engine =
//				new SolverGAEngine(
//						dataSet,
//						// define variables
//						list("x"),
//						// define base functions
//						list(Functions.java.ADD, Functions.java.SUB, Functions.java.MUL, Functions.java.VARIABLE, Functions.java.CONSTANT));
//
//		addListener(engine);
//
//		// 200 iterations
//		engine.evolve(200);
//		//*********************************************************
//		System.out.println("!   !!!          run 8              !!!");
//		dataSet =
//				new DataSet();
//
//		for(int i = 0; i < 15; i++){
//			dataSet.addTarget(new Point().when("x", i).setYval(3*i*i*i + 27*i - 1000));
//		}
//
//		engine =
//				new SolverGAEngine(
//						dataSet,
//						// define variables
//						list("x"),
//						// define base functions
//						list(Functions.java.ADD, Functions.java.SUB, Functions.java.MUL, Functions.java.VARIABLE, Functions.java.CONSTANT));
//
//		addListener(engine);
//
//		// 200 iterations
//		engine.evolve(200);
	}

	/**
	 * Track each iteration
	 */
	private static void addListener(SolverGAEngine engine) {
		engine.addIterationListener(new SolverGAEngineIterationListener() {
			@Override
			public void update(SolverGAEngine engine) {

				Expression bestSyntaxTree = engine.getBestSyntaxTree();

				double currFitValue = engine.fitnessMeasureShouldNotBeUsed(bestSyntaxTree);

				// log to console
				System.out.println(
						String.format("iter = %s \t fit = %s \t func = %s",
								engine.getIteration(), currFitValue, bestSyntaxTree.print()));

				// halt condition
				if (currFitValue < epsilon) {
					engine.terminate();
				}
			}
		});
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
}
