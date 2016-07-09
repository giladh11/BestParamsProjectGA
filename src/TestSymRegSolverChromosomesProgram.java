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

import static lowerLevelGA.TestFunctions.*;

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
		private static  double EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP = 0.5;
		//param for mutation inside of ParamGA
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
				case "setTestFunctions":
					addTheseFunctionsStringsToSetups(getTestFunctions());
					break;
				case "setFamily":
					setFamily(arrS[1]);
					break;
				case "copySetups":
					copySetups(arrS[1]);
					break;
				case "run":
					if (arrS.length==1)
						runCurrentBox(1);
					else
						runCurrentBox(Integer.parseInt(arrS[1]));
					break;
				case "runAll":
					if (arrS.length==1)
						runAll(0);
					else
						runAll(Integer.parseInt(arrS[1]));
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
						resetCurrentSetup();
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




	/**
	 * prints the help menu
	 */
	private static void printHelp(){
		System.out.println("    ------- Test SymRegSolverChromosomes program  -------\n");

		System.out.println("Options:");
		System.out.println("random - create a random black box and run on it");
		System.out.println("setNewFunc 'FUNCTION_STRING' - will create a new BlackBox according to the requested string");
		System.out.println("setTestFunctions - will add the 10 diverse functions to the SetupList with the current ParamGA");
		System.out.println("setFamily poly/exp/trigo 'x' 'y' - will add 'y' functions from the family with degree/length x");
		System.out.println("copySetups x y - will copy all the setups with indexes between x and y (including) as new ones with the current paramGA");
		System.out.println("run x - will rerun the last blackBox x times. when finished will print the results of the pervious runs and the averages");
		System.out.println("runAll x - will make each memory setup run the same amount in total");
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
		//System.out.println(" chosenFunc function is " + currentBlackBoxTree);
		currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
		currentSetupIndex = -1;
	}


	/**
	 * this method will add to the setups the list of string functions it got
	 */
	private static void addTheseFunctionsStringsToSetups(List<String> stringFunctionsList) {
		Iterator<String> iter = stringFunctionsList.iterator();
		String currentString;
		while(iter.hasNext()){
			currentString= iter.next();
			createNewFuncFromString(currentString);
			setUpsList.add(currentSetup);
		}
		System.out.println(" created "+stringFunctionsList.size()+" new setups");
		resetCurrentSetup();
	}

	/**
	 * will add  count functions from the family with degree/length x  to the setups
	 * @param s
	 */
	private static void setFamily(String s) {
		String arrS[] = s.split(" ", 3);
		int lengthOrDegree =  Integer.parseInt(arrS[1]);
		int count = Integer.parseInt(arrS[2]);
		switch(arrS[0]) {
			case "poly":
				addTheseFunctionsStringsToSetups(polynomials(lengthOrDegree, count));
				break;
			case "exp":
				addTheseFunctionsStringsToSetups(exponents(lengthOrDegree, count));
				break;
			case "trigo":
				addTheseFunctionsStringsToSetups(trigonometricFunctions(lengthOrDegree, count));
				break;
		}
	}

	/**
	 * will copy all the setups with indexes between x and y (including) as new ones with the current paramGA
	 * @param s
	 */
	private static void copySetups(String s) {
		String arrS[] = s.split(" ", 2);
		int i =  Integer.parseInt(arrS[0]);
		int ibackup=i;
		int j = Integer.parseInt(arrS[1]);
		if (i<0 || i>=setUpsList.size() || i>j || j>=setUpsList.size()) {
			System.out.println(" indexes inserted are not legal!!");
			return;
		}
		Iterator<Setup> iter =  setUpsList.listIterator(i);
		Setup set = null;
		List<Setup> tempList = new LinkedList<Setup>();
		while (!(i>j)){
			set = iter.next();
			currentBlackBoxTree = set.getBlackBoxTree();//new BlackBoxTree(set.getBlackBoxTree());// make a copy constructor for BlackTree , or check if its a problem like this
			currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
			tempList.add(currentSetup);
			i++;
		}
		setUpsList.addAll(tempList);
		resetCurrentSetup();
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
	 *  if gets 0 make sure all the setups in memory had the same number of runs, if get higher number adds the the current most ran setup, x runs
	 * @param numsOfRunsToAdd
	 */
	private static void runAll(int numsOfRunsToAdd) {
		if(setUpsList.size()==0){
			System.out.println("   no setups stored on memory");
			return;
		}
		int maxNumOfModelsInASetup = 0;
		int numToRunSet;
		Iterator<Setup> iter =  setUpsList.listIterator();
		Setup set = null;
		while (iter.hasNext()){
			set = iter.next();
			if(maxNumOfModelsInASetup < set.getNumOfModels())
				maxNumOfModelsInASetup = set.getNumOfModels();
		}

		iter =  setUpsList.listIterator();
		set = null;
		while (iter.hasNext()){
			set = iter.next();
			numToRunSet = numsOfRunsToAdd + maxNumOfModelsInASetup - set.getNumOfModels();
			System.out.println("!!!running function: '"+set.getBlackBoxTree() +"' "+numToRunSet + " times, found models:");
			switchToSetup(set, -1);//current index doesnt really matter
			for (int i = 0; i < numToRunSet; i++) {
				tempBestModelFound = symRegSolverChromosome.trySolving(currentBlackBoxTree, false);//will not print the evolutions iterations
				currentSetup.addBestModel(tempBestModelFound);
				System.out.println("   " + tempBestModelFound);
			}
		}

		resetCurrentSetup();
		System.out.println("\n   all setups has been run "+ (maxNumOfModelsInASetup+numsOfRunsToAdd) + " times in total!");
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
	 * @param index
	 */
	private static void chooseSetup(int index){
		if (index>=setUpsList.size() || index<0){
			System.out.println("   "+index+" is not a valid setup index");
			return;
		}
		switchToSetup(setUpsList.get(index), index);


		System.out.println("   switched to setup "+index+"!\n"+currentSetup);

	}

	/**
	 * switches to the requested setup
	 * @param setup
	 * @param index
     */
	private static void switchToSetup(Setup setup, int index){
		currentSetup = setup;
		currentParamGA = currentSetup.getParamGA();
		currentBlackBoxTree = currentSetup.getBlackBoxTree();
		tempBestModelFound = null;
		currentSetupIndex = index;
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

				double currFitValue = engine.fitnessMeasureForEachIteration(bestSyntaxTree);

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

	/**
	 * will reset the current set up and make the user choose a setup next time he want to run
	 */
	private static void resetCurrentSetup(){
		currentSetupIndex = -1;
		currentSetup = null;
		currentBlackBoxTree = null;
	}

	private static <T> List<T> list(T... items) {
		List<T> list = new LinkedList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
}


