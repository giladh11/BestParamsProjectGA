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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * a main function that allows to check the implementation of the lower level
 */
 public final class TestSymRegSolverChromosomesProgram {

	private static double epsilon = 0.001;
	private static ParamGA paramGA;



	private static List<Functions> baseFunctions = list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.VARIABLE, Functions.CONSTANT);
	private static Context sharedContext = new Context(baseFunctions, list("x"));

	private static Setup currentSetup;
	private static BlackBoxTree currentBlackBoxTree;
	private static BestModelCandidate tempBestModelFound;
	private static List<Setup> setUpsList;
	private static int currentSetupIndex;

	private static SymRegSolverChromosome symRegSolverChromosome;



	public static void main(String[] args) {
		int populationSize; double pParentSurviveRate; double pCrossover; double pMutation; int dataSetSize; int maxInitialTreeDepth; int bloatPenaltyRate;



		String s = null;
		String arrS[];
		boolean exit = false;
//		Console c = System.console();
//		if (c == null) {
//			System.err.println("No console.");
//			System.exit(1);
//		}
		Scanner in;
		in = new Scanner(System.in);
		StringBuilder sbuild;

				//setting curent population size
		populationSize=10; pParentSurviveRate=0.5; pCrossover=0.4; pMutation=0.1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
		paramGA = new ParamGA(populationSize, pParentSurviveRate, pCrossover, pMutation, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);
		symRegSolverChromosome = new SymRegSolverChromosome(paramGA, baseFunctions);//TODO think where this should be


		setTheDefaultSetups();
		//*****************************
		printHelp();

		while(!exit){
			System.out.print("$"); s = in.nextLine(); arrS = s.split(" ", 2);

			switch(arrS[0]){
				case "random":
					createNewRandomBlackBox();
					//runCurrentBox(1);
					break;
				case "setNewFunc":
					createNewFuncFromString(arrS[1]);
					break;
				case "run":
					if (arrS.length==1)
						runCurrentBox(1);
					else
						runCurrentBox(Integer.parseInt(arrS[1]));
					break;
				case "printModels":
					System.out.println(currentSetup);
					break;
				case "printSetups":
					printAllSetups();
					break;
				case "chooseSetup":
					changeToSetup(Integer.parseInt(arrS[1]));
					break;
				case "paramGA":
					System.out.println(paramGA);
					break;
				case "currentSetup":
					sbuild = new StringBuilder();
					sbuild.append(currentSetupIndex+ ".   !!!   " + "blackBox is : " +currentBlackBoxTree+"\n");
					currentSetup.appendAverages(sbuild);
					sbuild.append("\n");
					System.out.println(sbuild.toString());
					break;
				case "help":
					printHelp();
					break;
				case "quit":
					System.out.println("Goodbye:)");
					exit = true;
					break;
				default:
					System.out.println("please enter a valid command, use 'Help' for list of commmands");
			}
		}

	}




	/**
	 * prints the help menu
	 */
	private static void printHelp(){
		System.out.println("    ------- Test SymRegSolverChromosomes program  -------\n");

		System.out.println("Options:");
		System.out.println("random - create a random black box and run on it");
		System.out.println("setNewFunc 'FUNCTION_STRING' - will create a new BlackBox according to the requested string");
		System.out.println("run x - will rerun the last blackBox x times. when finished will print the results of the pervious runs and the averages");
		System.out.println("printModels - will print all the models found by the runs on the currentBlackBox");
		System.out.println("printSetups - will print all the setups currently on memory");
		System.out.println("chooseSetup x - will change to the requested setup index");
		System.out.println("paramGA - will print the current ParamGA used");
		//System.out.println("setParamGA  - to choose new params for paramGA");//TODO maybe
		System.out.println("currentSetup - will printthe current setup index, the blackbox and its averages");
		System.out.println("help - will print this option menu again");
		System.out.println("quit - will exit the program");
		System.out.println("");
	}

	/**
	 * this method sets the BlackBoxTreeList with method we want to run and test
	 */
	private static void setTheDefaultSetups() {
		setUpsList = new LinkedList<Setup>();
		currentSetupIndex = -1;
		//TODO TAL create a few easy trees
		//x^3+4
		//x^5 -3x^3 + x^2 + 13
	}

	/**
	 * runs a new random black box
	 */
	private static void createNewRandomBlackBox() {
		currentBlackBoxTree = new BlackBoxTree(4, sharedContext);//PARAM set how big will be the currentBlackBoxTree
		System.out.println(" Random function is " + currentBlackBoxTree);
		currentSetup = new Setup(currentBlackBoxTree);
		setUpsList.add(currentSetup);
		currentSetupIndex = setUpsList.size();
	}

	/**
	 * creates a tree based on the string
	 */
	private static void createNewFuncFromString(String funcString) {
		//currentBlackBoxTree = From TAL TODO
		System.out.println(" chosenFunc function is " + currentBlackBoxTree);
		currentSetup = new Setup(currentBlackBoxTree);
		setUpsList.add(currentSetup);
		currentSetupIndex = setUpsList.size();
	}

	/**
	 * this method reruns the genetic algorithem on the last BlackBox n times
	 * @param n
     */
	private static void runCurrentBox(int n) {
		if (n==1){
			tempBestModelFound = symRegSolverChromosome.trySolving(currentBlackBoxTree, true);//will print the iterations
			currentSetup.addBestModel(tempBestModelFound);
			System.out.println("***\n" + tempBestModelFound);
		}
		else {
			for (int i = 0; i < n; i++) {
				tempBestModelFound = symRegSolverChromosome.trySolving(currentBlackBoxTree, false);//will not print the evolutions iterations
				currentSetup.addBestModel(tempBestModelFound);
				System.out.println("***\n" + tempBestModelFound);
			}
		}
		System.out.println("***\n"+"***\n"+"***");
		System.out.println("currentBlackBoxTree was: "+ currentSetup.getBlackBoxTree());
		currentSetup.printAverages();
	}

	/**
	 * will print all the setups in memory
	 */
	private static void printAllSetups(){
		StringBuilder s = new StringBuilder();
		Iterator<Setup> iter =  setUpsList.listIterator();
		int i = 0;
		Setup set = null;

		while (iter.hasNext()){
			set = iter.next();
			s.append(i+ ".   !!!   " + "blackBox is : " + set.getBlackBoxTree()+"\n");
			set.appendAverages(s);
			s.append("\n");
			i++;
		}
		System.out.println(s.toString());
	}

	/**
	 * switches to the requested setup
	 * @param n
     */
	private static void changeToSetup(int n){
		if (n>=setUpsList.size() || n<0){
			System.out.println("!!! "+n+" is not a valid setup index!!!");
			return;
		}
		currentSetup = setUpsList.get(n);
		currentBlackBoxTree = currentSetup.getBlackBoxTree();
		tempBestModelFound = null;
		currentSetupIndex = n;

		System.out.println("switched to setup "+n+"!");
		currentSetup.printAverages();
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


