package higherLevelGA;

import evolutionGaTools.Fitness;
import lowerLevelGA.BlackBoxTree;


import java.util.Iterator;
import java.util.List;

/**
 * this is a simple class that hold a Dataset and can calcute distance from a given chromosome;
 */
public class ParamMeasurer implements Fitness<SymRegSolverChromosome, Double> {

	private List<BlackBoxTree> listOfBlackboxes;

	public ParamMeasurer(List<BlackBoxTree> listOfBlackboxes) {
		this.listOfBlackboxes = listOfBlackboxes;
	}

	/**
	 * this is a method that calcute the hFitnessElement of this params on the list of black boxes it got
	 * meaning it will run the lower level ga with the params on each of the boxes and see the resulats
	 * @param symRegSolverChromosome
	 * @return
     */
	@Override
	public Double calculate(SymRegSolverChromosome symRegSolverChromosome) {
		Iterator<BlackBoxTree> iter = listOfBlackboxes.listIterator();
		BlackBoxTree currentBox;
		BestModelCandidate currentBestCandidate;
		double sumHFitnessElements = 0;
		if(RunHigherLevel.PRINT_EACH_HIGHER_LEVEL_CHROMOSOME_EVALUATION)
			System.out.println("Evaluating chromosome with ParamGA: "+ symRegSolverChromosome.getParamGA());
		while(iter.hasNext()){
			currentBox = iter.next();
			currentBestCandidate = symRegSolverChromosome.trySolving(currentBox, RunHigherLevel.PRINT_LOWER_LEVEL_ITERATIONS);
			sumHFitnessElements+=currentBestCandidate.getHFitnessElement();
		}
		if(RunHigherLevel.PRINT_EACH_HIGHER_LEVEL_CHROMOSOME_EVALUATION)
			System.out.println(" symRegSolverChromosome.hFitnessElement = "+ sumHFitnessElements);

		return sumHFitnessElements/listOfBlackboxes.size();//size of boxes should never be zero
	}

}
