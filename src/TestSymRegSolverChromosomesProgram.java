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

	private static Setup tempSetup;
	private static SymRegSolverChromosome symRegSolverChromosome;
	private static BlackBoxTree blackBoxTree;
	private static BestModelCandidate bestModelFound;
	private static List<BestModelCandidate> bestModelFoundList;

	private static List<BlackBoxTree> blackBoxTreeList;

	private static List<Setup> setUpsList;

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

		//setting curent population size
		populationSize=10; pParentSurviveRate=0.5; pCrossover=0.4; pMutation=0.1; dataSetSize=10; maxInitialTreeDepth=1; bloatPenaltyRate=0;
		paramGA = new ParamGA(populationSize, pParentSurviveRate, pCrossover, pMutation, dataSetSize, maxInitialTreeDepth, bloatPenaltyRate);//(int populationSize, int pParentSurviveRate, double pMutation, double pCrossover, int dataSetSize, int maxInitialTreeDepth, int bloatPenaltyRate)

		setBlackBoxTreeList();
		//*****************************
		printHelp();

		while(!exit){
			System.out.print("$"); s = in.nextLine(); arrS = s.split(" ");

			switch(arrS[0]){
				case "random":
					createNewRandomBlackBox();
					runCurrentBox(1);
					break;
				case "rerun":
					if (arrS.length==1)
						runCurrentBox(1);
					else
						runCurrentBox(Integer.parseInt(arrS[1]));
					break;
				case "paramGA":
					System.out.println(paramGA);
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
	 * this method sets the BlackBoxTreeList with method we want to run and test
	 */
	private static void setBlackBoxTreeList() {
		blackBoxTreeList = new LinkedList<BlackBoxTree>();
		//TODO TAL create a few easy trees
		//x^3+4
		//x^5 -3x^3 + x^2 + 13
	}


	/**
	 * prints the help menu
	 */
	private static void printHelp(){
		System.out.println("    ------- Test SymRegSolverChromosomes program  -------\n");

		System.out.println("Options:");
		System.out.println("random - create a random black box and run on it");
		System.out.println("rerun x - will rerun the last blackBox x times. when finished will print the results of the pervious runs and the averages");
		System.out.println("paramGA - will print the current ParamGA used");
		//System.out.println("setParamGA  - to choose new params for paramGA");//TODO maybe
		System.out.println("help - will print this option menu again");
		System.out.println("quit - will exit the program");
		System.out.println("");
	}

	/**
	 * runs a new random black box
	 */
	private static void createNewRandomBlackBox() {
		symRegSolverChromosome = new SymRegSolverChromosome(paramGA, baseFunctions);//TODO think where this should be
		blackBoxTree = new BlackBoxTree(4, sharedContext);//PARAM set how big will be the blackBoxTree
		System.out.println(" Random function is " + blackBoxTree);
		tempSetup = new Setup(blackBoxTree);
	}

	/**
	 * this method reruns the genetic algorithem on the last BlackBox n times
	 * @param i
     */
	private static void runCurrentBox(int n) {
		for(int i = 0; i< n; i++){
			System.out.println("run last box");
			bestModelFound = symRegSolverChromosome.trySolving(blackBoxTree);
			tempSetup.addBestModel(bestModelFound);
		}
		System.out.println(tempSetup);
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

/**
 * represents a blackBox tree and all the bestsolutions we got in different runs
 */
class Setup{
	BlackBoxTree blackBoxTree;
	private static List<BestModelCandidate> bestModelFoundList;

	private double minEffort; private double sumEffort; private double maxEffort;

	private double minDistanceFromBlackBox; private double sumDistanceFromBlackBox; private double maxDistanceFromBlackBox;

	private double minFitness; private double sumFitness; private double maxFitness;

	/**
	 * constuctor of a set up
	 * @param bt
     */
	public Setup(BlackBoxTree bt){
		this.blackBoxTree = bt;
		bestModelFoundList = new LinkedList<BestModelCandidate>();
		minEffort = -1 ; sumEffort = 0; maxEffort = 0;
		minDistanceFromBlackBox = -1 ; sumDistanceFromBlackBox = 0 ; maxDistanceFromBlackBox = 0;
		minFitness = -1; sumFitness = 0; maxFitness = 0;
	}

	/**
	 * returns the black box this setup represents
	 * @return
     */
	public BlackBoxTree getBlackBoxTree() {
		return blackBoxTree;
	}

	/**
	 * adds a new model the this set up
	 * new model came from running the genetic algorithem on the black list.
	 * @param best
     */
	public void addBestModel(BestModelCandidate best){
		sumEffort +=  best.getEffort();
		sumDistanceFromBlackBox +=  best.getDistanceFromBlackBox();
		sumFitness+= best.getFitness();

		if (minEffort==-1 || best.getEffort() < minEffort)
			minEffort = best.getEffort();
		if (minDistanceFromBlackBox==-1 || best.getDistanceFromBlackBox() < minDistanceFromBlackBox)
			minDistanceFromBlackBox = best.getDistanceFromBlackBox();
		if (minFitness==-1 || best.getFitness() < minFitness)
			minFitness = best.getFitness();

		if (best.getEffort() > maxEffort)
			maxEffort = best.getEffort();
		if (best.getDistanceFromBlackBox() > maxDistanceFromBlackBox)
			maxDistanceFromBlackBox = best.getDistanceFromBlackBox();
		if (best.getFitness() > maxFitness)
			maxFitness = best.getFitness();

		bestModelFoundList.add(best);//adds to the end of the list
	}

	/**
	 * iterating toString
	 * @return
     */
	public String toString(){
		StringBuilder s = new StringBuilder();
		Iterator<BestModelCandidate> iter =  bestModelFoundList.listIterator();
		int i = 0;
		BestModelCandidate best = null;
		while (iter.hasNext()){
			best = iter.next();
			s.append(i +". " + best.toString()+"\n");
			i++;
		}
		s.append("  minEffort = "+ minEffort + ", avgEffort = "+sumEffort/bestModelFoundList.size() + ", maxEffort = " + maxEffort + "\n");
		s.append("  minDistanceFromBlackBox = " + minDistanceFromBlackBox + ", avgDistanceFromBlackBox = "+sumDistanceFromBlackBox/bestModelFoundList.size() + ", maxDistanceFromBlackBox = " + maxDistanceFromBlackBox + "\n");
		s.append("  minFitness = " + minFitness + ", avgFitness = "+sumFitness/bestModelFoundList.size() + ", maxFitness = " + maxFitness +"\n");
		return s.toString();
	}


}
