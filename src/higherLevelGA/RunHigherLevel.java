package higherLevelGA;


import evolutionGaTools.GeneticAlgorithmHigherLevel;
import interpreter.Context;
import interpreter.Functions;
import lowerLevelGA.BlackBoxTree;
import lowerLevelGA.DataSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static lowerLevelGA.TestFunctions.*;

/**
 * a main function that allows to mange higher level runs
 */
 public final class RunHigherLevel {

	//*****************************************************************params chosen PARAM in higher level Tester**************************************************************************
		protected static int NUM_GEN_HIGHER_LEVEL = 3;
		private static int OBJECTIVE_NUM_OF_POINTS_FOR_BLACKBOX_DISTANCE_MEASURER = 100;
		private static int MAX_NUM_OF_ITERATIONS_LOWER_LEVEL = 10;
		private static  double EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP =30;
		protected static int HIGHER_POPULATION_SIZE = 3;
		private static double HIGHER_CHROMOSOME_RATE = 0.8;
		private static double HIGHER_MUTATUION_RATE = 0.25;

		private static int MAX_POINT_IN_RANGE = 50; //for DATASET creator
		private static int MIN_POINT_IN_RANGE = -50;
		protected static boolean PRINT_HIGHER_LEVEL_ITERATIONS = true;
		protected static boolean PRINT_LOWER_LEVEL_ITERATIONS = true;


		private static int SIZE_OF_RANDOM_BLACKBOXTREE = 4;//not sure that will be used here
		//param for mutation inside of ParamGA
		private static List<Functions> baseFunctions = list(Functions.ADD, Functions.SUB, Functions.MUL, Functions.POW, Functions.VARIABLE, Functions.CONSTANT);
	//*********************************************************************end param chosen***************************************************************************************************
	//DEBUG prints
		protected static boolean PRINT_EACH_HIGHER_LEVEL_CHROMOSOME_EVALUATION = false;


	//********end debug prints




	private static Context sharedContext = new Context(baseFunctions, list("x"));
	static int populationSize; static double pCrossover; static double pMutation; static int dataSetSize; static int maxInitialTreeDepth; static double bloatPenaltyRate;
	private static List<BlackBoxTree> listOfBlackboxes;
	private static List<SetupHigherLevel> setupsLists = new LinkedList<SetupHigherLevel>();

	private  static SetupHigherLevel currentSetup = null;
	private static ParamGA currentParamGA = null;
	private static int currentSetupIndex = -1;



	//*******************MAIN**************************
	public static void main(String[] args) {
		//params setters
			BlackBoxTree.setObjectiveNumOfPointsForDistanceMeasurer(OBJECTIVE_NUM_OF_POINTS_FOR_BLACKBOX_DISTANCE_MEASURER);
			SymRegSolverChromosome.setMaxNumOfIterationsLowerLevel(MAX_NUM_OF_ITERATIONS_LOWER_LEVEL);
			SymRegSolverChromosome.setEpsilonDistanceForLowerEvolutionToStop(EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP);
			DataSet.setMaxPointInRange(MAX_POINT_IN_RANGE);
			DataSet.setMinPointInRange(MIN_POINT_IN_RANGE);
			GeneticAlgorithmHigherLevel.setHigherChromosomeRate(HIGHER_CHROMOSOME_RATE);
			GeneticAlgorithmHigherLevel.setHigherMutatuionRate(HIGHER_MUTATUION_RATE);
		//end param setters
		BlackBoxTree.setContextRegular(new Context(baseFunctions, list("x")));


		String s = null;
		String arrS[];
		boolean exit = false;

		Scanner in;
		in = new Scanner(System.in);
		StringBuilder sbuild;


		//*****************************
		printHelp();





		//TODO make it easy to see the ParamGA chromosomes of the first population
		//TODO choose a few hand-crafted ParamGA and add them to the setups
	/*ideas
		* check if running the highlevel on the same group of function return similar parameters in each run/
		*
	 */

		while(!exit){
			System.out.print("$"); s = in.nextLine(); arrS = s.split(" ", 2);

			switch(arrS[0]){
				case "default":
					//TODO write default actions to be made
						//					currentSetup = new SetupHigherLevel("Test1", createBlackBoxesList(), baseFunctions);
						//					setupsLists.add(currentSetup);
						//
						//					currentSetup = new SetupHigherLevel("Test2", createBlackBoxesList2(), baseFunctions);
						//					setupsLists.add(currentSetup);
					runAll();
					break;
				case "setTestFunctions":
					currentSetup = new SetupHigherLevel(arrS[1], addTheseFunctionsStringsToBlackBoxesList(getTestFunctions()), baseFunctions);
					setupsLists.add(currentSetup);
					currentSetupIndex=setupsLists.size()-1;
					break;
				case "setFamily":
					arrS = s.split(" ", 3);
					currentSetup = new SetupHigherLevel(arrS[1], createFamilyBlackBoxesList(arrS[2]), baseFunctions);
					setupsLists.add(currentSetup);
					currentSetupIndex=setupsLists.size()-1;
					break;
//				case "setNewFunc":
//					createNewFuncFromString(arrS[1]);
//					break;
				case "runAll":
					runAll();
					break;
//				case "printModels":
//					if (currentSetup==null)
//						System.out.println("   no currentSetup chosen");
//					else
//						System.out.println(currentSetup);
//					break;
				case "printSetups":
					printAllSetups();
					break;
				case "printSetupsModels":
					printAllSetupsWithInfoOnBestModels();
					break;
				case "chooseSetup":
					chooseSetup(Integer.parseInt(arrS[1]));
					break;
				case "currentParamGA":
					if (currentParamGA!=null)
						System.out.println(currentParamGA);
					else
						System.out.println("   no CurrentParamGA chosen");
					break;
				case "setParamGA":
					resetCurrentSetupAndParamGA();
					parseAndSetParamGa(arrS[1]);
					System.out.println("   paramGA was set to "+currentParamGA);
					break;
				case "setParamGaSetups":
					setParamGaSetups(arrS[1]);
					break;
				case "currentSetup":
					if (currentSetup==null)
						System.out.println("   no currentSetup chosen");
					else {
						printCurrentSetupWithInfo();
					}
					break;
				case "removeCurrentSetup":
					if (currentSetup==null)
						System.out.println("   no currentSetup chosen");
					else {
						setupsLists.remove(currentSetupIndex);
						currentSetupIndex = -1;
						currentSetup = null;
						currentParamGA = null;
						System.out.println("   setup and currentParamGA have been reset");
					}
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
		System.out.println("    ------- RunHigherLevel program  -------\n");

		System.out.println("Options:");
		System.out.println("default - will set all the default setups and run them");
		System.out.println("setTestFunctions chosenName  -  will add a list of 10 functions from the testfunctions, to a new setup chosenName in the list");
		System.out.println("setFamily chosenName poly/exp/trigo 'x' 'y' - will add a list of 'y' functions from the family with degree/length x, to a new setup chosenName in the list");
//		System.out.println("setNewFunc 'FUNCTION_STRING' - will create a new BlackBox according to the requested string");

		System.out.println("runAll - will make sure all the current setups were run");
		System.out.println("printSetups - will print all the setups currently on memory");
		System.out.println("printSetupsModels - will print all the setups currently on memory, and the bestModels each one of them found");
		System.out.println("chooseSetup x - will change to the requested setup index");
		System.out.println("currentParamGA - will print the current ParamGA used");
		System.out.println("setParamGA  - to choose 6 new params for currentParamGA");
		System.out.println("setParamGaSetups x y - will recreate the setups with indexes between x and y (including) as new ones with the current paramGA");
		System.out.println("          //the idea is that it allows you to run different families on params we got in different runs");
		System.out.println("currentSetup - will printthe current setup index, and all its models");
		System.out.println("removeCurrentSetup - will remove the current setup from the memory(and reset the box");
		System.out.println("help - will print this option menu again");
		System.out.println("quit - will exit the program");
		System.out.println("");
	}

	/**
	 * this method will return a list with BlackBoxTree
	 * @return
	 */
	private static List<BlackBoxTree> createFamilyBlackBoxesList(String s) {
		List<BlackBoxTree> blackBoxList = null;
		String arrS[] = s.split(" ", 3);
		int lengthOrDegree =  Integer.parseInt(arrS[1]);
		int count = Integer.parseInt(arrS[2]);
		switch(arrS[0]) {
			case "poly":
				blackBoxList = addTheseFunctionsStringsToBlackBoxesList(polynomials(lengthOrDegree, count));
				break;
			case "exp":
				blackBoxList = addTheseFunctionsStringsToBlackBoxesList(exponents(lengthOrDegree, count));
				break;
			case "trigo":
				blackBoxList = addTheseFunctionsStringsToBlackBoxesList(trigonometricFunctions(lengthOrDegree, count));
				break;
			default:
				System.out.println("choose poly/exp/trigo ");
				break;
		}

		return blackBoxList;
	}



	/**
	 * this method will add to the setups the list of string functions it got
	 */
	private static List<BlackBoxTree> addTheseFunctionsStringsToBlackBoxesList(List<String> stringFunctionsList) {
		List<BlackBoxTree> blackBoxList = new LinkedList<BlackBoxTree>();
		Iterator<String> iter = stringFunctionsList.iterator();
		String currentString;
		while(iter.hasNext()){
			currentString= iter.next();
			blackBoxList.add(new BlackBoxTree(currentString));
		}
		System.out.println(" created "+stringFunctionsList.size()+" new blackBoxes in a list");
		return blackBoxList;
	}


	/**
	 * this method will make sure all setups has been ran
	 */
	private static void runAll(){
		Iterator<SetupHigherLevel> iter = setupsLists.iterator();
		currentSetup = null;
		while(iter.hasNext()){
			currentSetup = iter.next();
			if(!currentSetup.hasBeenRan()){
				currentSetup.runHigherOnTheBlackBoxes();
			}
		}
		resetCurrentSetupAndParamGA();
	}

	/**
	 * resets the currentSetupAndParamGA
	 */
	private static void resetCurrentSetupAndParamGA() {
		currentSetup = null;
		currentSetupIndex = -1;
		currentParamGA = null;
	}

	/**
	 * will print all the setups (without info on each best model it found
	 * at the end cuurentSetup=null
	 */
	private static void printAllSetups(){

		if(setupsLists.size() != 0)
			for(SetupHigherLevel setupHigherLevel: setupsLists)
				System.out.println(setupHigherLevel);
		else
			System.out.println("   There are no setups");

		resetCurrentSetupAndParamGA();

	}


	/**
	 * will print the currentSetups info with info on model
	 */
	private static void printCurrentSetupWithInfo(){
		if(currentSetup != null)
			currentSetup.printSetup();
		else
			System.out.println("   There is no current setup");

	}



	/**
	 * will print all the setups WithInfoOnBestModels
	 * at the end currentSetup=null
	 */
	private static void printAllSetupsWithInfoOnBestModels(){
		int i = 0;
		if(setupsLists.size() != 0) {
			for (SetupHigherLevel setupHigherLevel : setupsLists) {
				System.out.println(i + ".");
				setupHigherLevel.printSetup();
				i++;
			}
		}
		else
			System.out.println("   There are no setups");

		resetCurrentSetupAndParamGA();
	}




	/**
	 * will recreate the setups with indexes between x and y (including) as new ones with the current paramGA
	 * @param s
	 */
	private static void setParamGaSetups(String s) {
		String arrS[] = s.split(" ", 2);
		int i =  Integer.parseInt(arrS[0]);
		int ibackup=i;
		int j = Integer.parseInt(arrS[1]);
		if (i<0 || i>=setupsLists.size() || i>j || j>=setupsLists.size()) {
			System.out.println(" indexes inserted are not legal!!");
			return;
		}
		Iterator<SetupHigherLevel> iter =  setupsLists.listIterator(i);
		SetupHigherLevel set = null;
		List<SetupHigherLevel> tempList = new LinkedList<SetupHigherLevel>();
		while (!(i>j)){
			set = iter.next();

			currentSetup = new SetupHigherLevel(set.getName()+".setParams",set.getBlackBoxesList(), baseFunctions ,currentParamGA);
			tempList.add(currentSetup);
			i++;
		}
		setupsLists.addAll(tempList);
	}

	/**
	 * will switch to the chosen setup
	 * @param index
     */
	private static void chooseSetup(int index) {
		if (index >= setupsLists.size() || index < 0){
			System.out.println("   index "+index+" is invalid");
			return;
		}
		switchToSetup(setupsLists.get(index), index);


		System.out.println("   switched to setup "+index+"!\n"+currentSetup);
	}



	/**
	 * switches to the requested setup
	 * @param setupHigherLevel
	 * @param index
	 */
	private static void switchToSetup(SetupHigherLevel setupHigherLevel, int index) {
		currentSetup = setupHigherLevel;
		currentParamGA = currentSetup.getParamGA();
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
	 * this method will set the paramGa according to the cuurent values
	 */
	public static void setParamGA(){
		currentParamGA = new ParamGA(populationSize, pCrossover, pMutation, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);
		currentSetupIndex = -1;

	}


	/**
	 * Track each iteration
	 */
	protected static void addListener(HigherGAEngine engine) {
		engine.addIterationListener(new HigherGAEngineIterationListener() {
			@Override
			public void update(HigherGAEngine engine) {
				SymRegSolverChromosome bestSymRegSolver = engine.getBestSymRegSolver();

				// log to console
				System.out.println(
						String.format("\niter = %s \t fit = %s \t param = %s \n      ***",
								engine.getIteration(), bestSymRegSolver.getFitness(), bestSymRegSolver.getParamGA()));
//
//				// halt condition
//				if (currFitValue < epsilon) {
//					engine.terminate();
//				}
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


