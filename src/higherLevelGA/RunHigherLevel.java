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

/**
 * a main function that allows to mange higher level runs
 */
 public final class RunHigherLevel {

	//*****************************************************************params chosen PARAM in higher level Tester**************************************************************************
		protected static int NUM_GEN_HIGHER_LEVEL = 3;
		private static int OBJECTIVE_NUM_OF_POINTS_FOR_BLACKBOX_DISTANCE_MEASURER = 100;
		private static int MAX_NUM_OF_ITERATIONS_LOWER_LEVEL = 200;
		private static  double EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP =0.5;
		protected static int HIGHER_POPULATION_SIZE = 2;
		private static double HIGHER_CHROMOSOME_RATE = 0.8;
		private static double HIGHER_MUTATUION_RATE = 0.25;

		private static int MAX_POINT_IN_RANGE = 50; //for DATASET creator
		private static int MIN_POINT_IN_RANGE = -50;
		protected static boolean PRINT_HIGHER_LEVEL_ITERATIONS = false;
		protected static boolean PRINT_LOWER_LEVEL_ITERATIONS = false;


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


															//setTheDefaultSetups();
		//*****************************
		printHelp();
																	//System.out.println("paramGA is set to "+currentParamGA);

//TODO choose the family of functions to create black box from (and the amount of functions)
		//TODO print all the black box list options - descrivbe the diffrent families
		//TODO add as setup a ParamGA and make it possible to run it on every function group
		//the idea is that it allows you to run different families on params we got in different runs

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
					currentSetup = new SetupHigherLevel("Test1", createBlackBoxesList(), baseFunctions);
					setupsLists.add(currentSetup);

					currentSetup = new SetupHigherLevel("Test2", createBlackBoxesList2(), baseFunctions);
					setupsLists.add(currentSetup);
					runAll();
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
						System.out.println("no currentSetup chosen and noCurrentParamGA");
					break;
				case "setParamGA":
					parseAndSetParamGa(arrS[1]);
					System.out.println("   paramGA was set to "+currentParamGA);
					currentSetup = null; //TODO GILAD make sure resets the currentSetup
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
						setupsLists.remove(currentSetupIndex);//TODO GILAD manage currentSetupIndex
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
//		System.out.println("setNewFunc 'FUNCTION_STRING' - will create a new BlackBox according to the requested string");

		System.out.println("runAll - will make sure all the current setups were run");
		System.out.println("printSetups - will print all the setups currently on memory");
		System.out.println("printSetupsModels - will print all the setups currently on memory, and the bestModels each one of them found");
		System.out.println("chooseSetup x - will change to the requested setup index");
		System.out.println("currentParamGA - will print the current ParamGA used");
		System.out.println("setParamGA  - to choose 6 new params for currentParamGA");
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
	private static List<BlackBoxTree> createBlackBoxesList() {
		List<BlackBoxTree> list = new LinkedList<BlackBoxTree>();
		list.add(new BlackBoxTree("2*x + 3"));
		list.add(new BlackBoxTree("3*x + 4"));

		return list;
	}

	/**
	 * this method will return a list with BlackBoxTree
	 * @return
	 */
	private static List<BlackBoxTree> createBlackBoxesList2() {
		List<BlackBoxTree> list = new LinkedList<BlackBoxTree>();
		list.add(new BlackBoxTree("5*x + 3"));
		list.add(new BlackBoxTree("4*x + 4"));

		return list;
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
	}

	/**
	 * will print all the setups (withOutinfo on each best model it found
	 * at the end cuurentSetup=null
	 */
	private static void printAllSetups(){
		//TODO TAL
	}

	/**
	 * will print the currentSetups info with info on model
	 */
	private static void printCurrentSetupWithInfo(){
		//TODO TAL
	}


	/**
	 * will print all the setups WithInfoOnBestModels
	 * at the end cuurentSetup=null
	 */
	private static void printAllSetupsWithInfoOnBestModels(){
		//TODO TAL
	}

	/**
	 * will switch to the chosen setup
	 * @param i
     */
	private static void chooseSetup(int i) {
		//TODO TAL find find the setup and put in the currentSetups, print unvalid index if un valid.
		// take example from the functions in TestSymRegSolvelProgram
		//TODO GILAD decide what else should be changed, ParamGA

	}















//	/**
//	 * this method sets the BlackBoxTreeList with method we want to run and test
//	 */
//	private static void setTheDefaultSetups() {
//		setUpsList = new LinkedList<Setup>();
//		currentSetupIndex = -1;
//		//createNewFuncFromString("2*x");
//		//createNewSetupFromCurrentBlackBox
//	}



//	/**
//	 * runs a new random black box
//	 */
//	private static void createNewRandomBlackBox() {
//		currentBlackBoxTree = new BlackBoxTree(4, sharedContext);
//		System.out.println(" Random function is " + currentBlackBoxTree);
//		currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
//		currentSetupIndex = -1;
//	}

//	/**
//	 * creates a tree based on the string
//	 */
//	private static void createNewFuncFromString(String funcString) {
//		currentBlackBoxTree = new BlackBoxTree(funcString);
//		System.out.println(" chosenFunc function is " + currentBlackBoxTree);
//		currentSetup = new Setup(currentBlackBoxTree, currentParamGA);
//		currentSetupIndex = -1;
//	}

//	/**
//	 * this method reruns the genetic algorithem on the last BlackBox n times
//	 * @param n
//     */
//	private static void runCurrentBox(int n) {
//		boolean printEvoIterations = false;
//		if(currentSetupIndex== -1){//adding setup to list
//			setUpsList.add(currentSetup);
//			currentSetupIndex = setUpsList.size()-1;
//		}
//		if (currentBlackBoxTree==null) {
//			System.out.println("   currentBlackBoxTree is still null, please choose function");
//			return;
//		}
//		if (n<0){
//			n = -n;
//			printEvoIterations=true;
//		}
//
//		for (int i = 0; i < n; i++) {
//			tempBestModelFound = symRegSolverChromosome.trySolving(currentBlackBoxTree, printEvoIterations);//will not print the evolutions iterations
//			currentSetup.addBestModel(tempBestModelFound);
//			System.out.println("***\n" + tempBestModelFound);
//		}
//
//		System.out.println("***\n"+"***\n"+"***");
//		System.out.println("currentBlackBoxTree was: "+ currentSetup.getBlackBoxTree());
//		currentSetup.printAverages();
//	}

//	/**
//	 * will print all the setups in memory
//	 */
//	private static void printSetups(){
//		if(setUpsList.size()==0){
//			System.out.println("   no setups stored on memory");
//			return;
//		}
//		StringBuilder s = new StringBuilder();
//		Iterator<Setup> iter =  setUpsList.listIterator();
//		int i = 0;
//		Setup set = null;
//
//		while (iter.hasNext()){
//			set = iter.next();
//			s.append(i);
//			set.appendSetupData(s);
//			i++;
//		}
//		System.out.println(s.toString());
//	}
//
//
//	/**
//	 * switches to the requested setup
//	 * @param n
//	 */
//	private static void chooseSetup(int n){
//		if (n>=setUpsList.size() || n<0){
//			System.out.println("   "+n+" is not a valid setup index");
//			return;
//		}
//		currentSetup = setUpsList.get(n);
//		currentParamGA = currentSetup.getParamGA();
//		currentBlackBoxTree = currentSetup.getBlackBoxTree();
//		tempBestModelFound = null;
//		currentSetupIndex = n;
//
//		System.out.println("   switched to setup "+n+"!\n"+currentSetup);
//
//	}
//
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


