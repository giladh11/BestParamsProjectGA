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
	private static ParamGA currentParamGA;
	static int populationSize; static double pCrossover; static double pMutation; static int dataSetSize; static int maxInitialTreeDepth; static double bloatPenaltyRate;



	private static List<Functions> baseFunctions = list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.POW, Functions.VARIABLE, Functions.CONSTANT);
	private static Context sharedContext = new Context(baseFunctions, list("x"));

	private static Setup currentSetup = null;
	private static BlackBoxTree currentBlackBoxTree = null;
	private static BestModelCandidate tempBestModelFound = null;
	private static List<Setup> setUpsList = null;
	private static int currentSetupIndex = -1;

	private static SymRegSolverChromosome symRegSolverChromosome;



	//params chosen PARAM in lower level Tester
		private static int OBJECTIVE_NUM_OF_POINTS_FOR_BLACKBOX_DISTANCE_MEASURER = 100;
		private static int SIZE_OF_RANDOM_BLACKBOXTREE = 4;
		private static int MAX_NUM_OF_ITERATIONS_LOWER_LEVEL = 200;
		private static  double EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP = 0.001;

		private static int MAX_POINT_IN_RANGE = 50; //for DATASET creator
		private static int MIN_POINT_IN_RANGE = -50;



	public static void main(String[] args) {
		//params setters
			BlackBoxTree.setObjectiveNumOfPointsForDistanceMeasurer(OBJECTIVE_NUM_OF_POINTS_FOR_BLACKBOX_DISTANCE_MEASURER);
			SymRegSolverChromosome.setMaxNumOfIterationsLowerLevel(MAX_NUM_OF_ITERATIONS_LOWER_LEVEL);
			SymRegSolverChromosome.setEpsilonDistanceForLowerEvolutionToStop(EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP);
			DataSet.setMaxPointInRange(MAX_POINT_IN_RANGE);
			DataSet.setMinPointInRange(MIN_POINT_IN_RANGE);

		String s = null;
		String arrS[];
		boolean exit = false;

		Scanner in;
		in = new Scanner(System.in);
		StringBuilder sbuild;

				//setting curent population size
		populationSize=10; pCrossover=1; pMutation=1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
		setParamGA();


		setTheDefaultSetups();
		//*****************************
		printHelp();
		System.out.println("paramGA is set to "+currentParamGA);


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
					if (currentSetup==null)
						System.out.println("   no currentSetup chosen");
					else
						System.out.println(currentSetup);
					break;
				case "printSetups":
					printSetups();
					break;
				case "chooseSetup":
					chooseSetup(Integer.parseInt(arrS[1]));
					break;
				case "currentParamGA":
					System.out.println(currentParamGA);
					break;
				case "setParamGA":
					parseAndSetParamGa(arrS[1]);
					System.out.println("   paramGA was set to "+currentParamGA);
					break;
				case "currentSetup":
					if (currentSetup==null)
						System.out.println("   no currentSetup chosen");
					else {
						sbuild = new StringBuilder();
						sbuild.append(currentSetupIndex);
						currentSetup.appendSetupData(sbuild);
						sbuild.append("\n");
						System.out.println(sbuild.toString());
					}
					break;
				case "removeCurrentSetup":
					if (currentSetup==null)
						System.out.println("   no currentSetup chosen");
					else {
						setUpsList.remove(currentSetupIndex);
						currentSetupIndex = -1;
						currentSetup = null;
						currentBlackBoxTree = null;
						System.out.println("   setup and blackBox have been reset");
					}
					break;
				case "resetBlackBox":
					currentBlackBoxTree = null;
					System.out.println("   blackBox has been reset");
					break;
				case "help":
					printHelp();
					break;
				case "quit":
					System.out.println("   Goodbye:)");
					exit = true;
					break;
				default:
					System.out.println("   please enter a valid command, use 'Help' for list of commmands");
			}
		}

	}

	//TODO GUI make every set print the number of models it calculated
	//TODO add "randomFromFamily" that gets a number and chooses a random function from tal's string generator family
			//GILAD you can use the following functions:
			//1. createPolynomial(int ndegree)
			//2. createTrigonometricFunction(int nlength)
			//3. createExpFunction(int nlength)

	//TODO GILAD use the function getTestFunction() in order to get a list of 10 test functions.
	//TODO add runAll x option - if gets 0 make sure all the setups in memory had the same number of runs, if get higher number adds the the current most ran setup, x runs




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
		System.out.println("currentParamGA - will print the current ParamGA used");
		System.out.println("setParamGA  - to choose 6 new params for currentParamGA");
		System.out.println("currentSetup - will printthe current setup index, the blackbox and its averages");
		System.out.println("removeCurrentSetup - will remove the current setup from the memory(and reset the box");
		System.out.println("resetBlackBox - will reset the currentBox");
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
		//createNewFuncFromString("2*x");
		//createNewSetupFromCurrentBlackBox
	}



	/**
	 * runs a new random black box
	 */
	private static void createNewRandomBlackBox() {
		currentBlackBoxTree = new BlackBoxTree(SIZE_OF_RANDOM_BLACKBOXTREE, sharedContext);
		System.out.println(" Random function is " + currentBlackBoxTree);
		currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
		currentSetupIndex = -1;
	}

	/**
	 * creates a tree based on the string
	 */
	private static void createNewFuncFromString(String funcString) {
		currentBlackBoxTree = new BlackBoxTree(funcString);
		System.out.println(" chosenFunc function is " + currentBlackBoxTree);
		currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
		currentSetupIndex = -1;
	}

	/**
	 * this method reruns the genetic algorithem on the last BlackBox n times
	 * @param n
     */
	private static void runCurrentBox(int n) {
		boolean printEvoIterations = false;
		if(currentSetupIndex== -1){//adding setup to list
			setUpsList.add(currentSetup);
			currentSetupIndex = setUpsList.size()-1;
		}
		if (currentBlackBoxTree==null) {
			System.out.println("   currentBlackBoxTree is still null, please choose function");
			return;
		}
		if (n<0){
			n = -n;
			printEvoIterations=true;
		}

		for (int i = 0; i < n; i++) {
			tempBestModelFound = symRegSolverChromosome.trySolving(currentBlackBoxTree, printEvoIterations);//will not print the evolutions iterations
			currentSetup.addBestModel(tempBestModelFound);
			System.out.println("***\n" + tempBestModelFound);
		}

		System.out.println("***\n"+"***\n"+"***");
		System.out.println("currentBlackBoxTree was: "+ currentSetup.getBlackBoxTree());
		currentSetup.printAverages();
	}

	/**
	 * will print all the setups in memory
	 */
	private static void printSetups(){
		if(setUpsList.size()==0){
			System.out.println("   no setups stored on memory");
			return;
		}
		StringBuilder s = new StringBuilder();
		Iterator<Setup> iter =  setUpsList.listIterator();
		int i = 0;
		Setup set = null;

		while (iter.hasNext()){
			set = iter.next();
			s.append(i);
			set.appendSetupData(s);
			i++;
		}
		System.out.println(s.toString());
	}


	/**
	 * switches to the requested setup
	 * @param n
	 */
	private static void chooseSetup(int n){
		if (n>=setUpsList.size() || n<0){
			System.out.println("   "+n+" is not a valid setup index");
			return;
		}
		currentSetup = setUpsList.get(n);
		currentParamGA = currentSetup.getParamGA();
		currentBlackBoxTree = currentSetup.getBlackBoxTree();
		tempBestModelFound = null;
		currentSetupIndex = n;

		System.out.println("   switched to setup "+n+"!\n"+currentSetup);

	}

	/**
	 * this method will set the paramGa according to the string
	 * @param s
	 */
	public static void parseAndSetParamGa(String s){
		String arrS[] = s.split(" ", 6);
		populationSize=Integer.parseInt(arrS[0]);
		pCrossover=Double.parseDouble(arrS[1]);
		pMutation=Double.parseDouble(arrS[2]);
		dataSetSize=Integer.parseInt(arrS[3]);
		maxInitialTreeDepth=Integer.parseInt(arrS[4]);
		bloatPenaltyRate=Double.parseDouble(arrS[5]);
		setParamGA();
	}

	/**
	 * this method will set the paramGa according to the cuurent values of  populationSize=10; pCrossover=1; pMutation=1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
	 */
	public static void setParamGA(){
		currentParamGA = new ParamGA(populationSize, pCrossover, pMutation, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);
		BlackBoxTree.setContextRegular(new Context(baseFunctions, currentParamGA.getVariables()));
		symRegSolverChromosome = new SymRegSolverChromosome(currentParamGA, baseFunctions);
		if(currentBlackBoxTree!=null)
			currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
		else
			currentSetup = null;
		currentSetupIndex = -1;

	}




	/**
	 * Track each iteration
	 */
	private static void addListener(SolverGAEngine engine) {
		engine.addIterationListener(new SolverGAEngineIterationListener() {
			@Override
			public void update(SolverGAEngine engine) {

				Expression bestSyntaxTree = engine.getBestSyntaxTree();

				double currFitValue = engine.fitnessMeasureShouldNotBeUsed(bestSyntaxTree);//TODO GILAD check whats up with this?

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


