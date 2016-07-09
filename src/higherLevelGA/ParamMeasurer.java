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
	 * this is a method that calcute the fitness of this params on the list of black boxes it got
	 * meaning it will run the lower level ga with the params on each of the boxes and see the resulats
	 * @param symRegSolverChromosome
	 * @return
     */
	@Override
	public Double calculate(SymRegSolverChromosome symRegSolverChromosome) {
		Iterator<BlackBoxTree> iter = listOfBlackboxes.listIterator();
		BlackBoxTree currentBox;
		BestModelCandidate currentBestCandidate;
		double fitness = 0;
		System.out.println("Evaluating chromosome with ParamGA: "+ symRegSolverChromosome.getParamGA());
		while(iter.hasNext()){
			currentBox = iter.next();
			currentBestCandidate = symRegSolverChromosome.trySolving(currentBox, RunHigherLevel.PRINT_LOWER_LEVEL_ITERATIONS);
			fitness+=currentBestCandidate.getFitness();
		}
		System.out.println(" symRegSolverChromosome.fitness = "+ fitness);
		fitness= fitness/listOfBlackboxes.size();//size of boxes should never be zero
		return fitness;
	}

}
