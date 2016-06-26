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
import interpreter.*;
import lowerLevelGA.*;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class TestSymRegSolverChromosomesProgram {

	private static double epsilon = 0.001;
	private static ParamGA paramGA;


	public static void main(String[] args) {
		int populationSize; int initialParentChromosomesSurviveCount; double pMutation; double pCrossover; int dataSetSize; int maxInitialTreeDepth; int bloatPenaltyRate;
		List<Functions> baseFunctions = list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT);
		Context sharedContext = new Context(baseFunctions, list("x"));
		SymRegSolverChromosome symRegSolverChromosome;
		BlackBoxTree blackBoxTree;
		BestModelCandidate bestModelFound;
		String s = null;
		boolean exit = false;
//		Console c = System.console();
//		if (c == null) {
//			System.err.println("No console.");
//			System.exit(1);
//		}
		Scanner in;
		in = new Scanner(System.in);

		//setting curent population size
		populationSize=10; initialParentChromosomesSurviveCount=1; pMutation=1; pCrossover=1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
		paramGA = new ParamGA(populationSize, initialParentChromosomesSurviveCount, pMutation, pCrossover, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);//(int populationSize, int initialParentChromosomesSurviveCount, double pMutation, double pCrossover, int dataSetSize, int maxInitialTreeDepth, int bloatPenaltyRate)

		//TODO change idstance to be avarge distance that depends on the amount of points...
		//*****************************
		printHelp();

		while(!exit){
			System.out.print("$"); s = in.nextLine();
			switch(s){
				case "Random":
					symRegSolverChromosome = new SymRegSolverChromosome(paramGA, baseFunctions);
					blackBoxTree = new BlackBoxTree(4, sharedContext);

					System.out.println(" Random function is " + blackBoxTree);
					bestModelFound = symRegSolverChromosome.trySolving(blackBoxTree);
					System.out.println("Best Model is "+bestModelFound);
					break;
				case "Help":
					printHelp();
					break;
				case "Quit":
					System.out.println("Goodbye:)");
					exit = true;
					break;
				default:
					System.out.println("please enter a valid command, use 'Help' for list of commmands");
			}
		}

	}




	private static void printHelp(){
		System.out.println("    ------- Test SymRegSolverChromosomes program  -------\n");

		System.out.println("Options:");
		System.out.println("Random - create a random black box and run on it");
		//System.out.println("paramGA - will print the current ParamGA used");//TODO add the other functionality to the program
		//System.out.println("setParamGA  - to choose new params for paramGA");
		//System.out.println("Rerun - will rerun the last blackBox. when finished will print the results of the pervious runs and the averages");
		System.out.println("Help - will print this option menu again");
		System.out.println("Quit");
		System.out.println("");
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
