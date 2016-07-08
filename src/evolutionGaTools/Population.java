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
package evolutionGaTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Population<C extends Chromosome<C,T> , T extends Comparable<T>> implements Iterable<C> {

	private static final int DEFAULT_NUMBER_OF_CHROMOSOMES = 32;

	private List<C> chromosomes = new ArrayList<C>(DEFAULT_NUMBER_OF_CHROMOSOMES);

	private final Random random = new Random();

	public void addChromosome(C chromosome) {
		this.chromosomes.add(chromosome);
	}

	public int getSize() {
		return this.chromosomes.size();
	}

	/**
	 * returns a random chromosome for population,
	 * used by the cross over operator
	 * @return
     */
	public C getRandomChromosome() {
		int numOfChromosomes = this.chromosomes.size();
		// IMPROVE random generator
		// maybe use pattern strategy ?
		int indx = this.random.nextInt(numOfChromosomes);
		return this.chromosomes.get(indx);
	}

	/**
	 * simple getter
	 * @param indx
	 * @return
     */
	public C getChromosomeByIndex(int indx) {
		return this.chromosomes.get(indx);
	}

	/**
	 * sorting the collection of chromosomes, used to choose the best candidates for the next generation
	 * @param chromosomesComparator
     */
	public void sortPopulationByFitness(Comparator<C> chromosomesComparator) {
		Collections.shuffle(this.chromosomes);
		Collections.sort(this.chromosomes, chromosomesComparator);
	}

	/**
	 * shuffles the population
	 */
	public void shufflePopulation() {
		Collections.shuffle(this.chromosomes);
	}

	/**
	 * shortening population till specific number
	 */
	public void trim(int len) {
		this.chromosomes = this.chromosomes.subList(0, len);
	}

	/**
	 * toString
	 * @return
     */
	public String toString() {
//		StringBuilder s = new StringBuilder();
//		for (int i = 0; i<)
		return chromosomes.toString();
	}

	@Override
	public Iterator<C> iterator() {
		return this.chromosomes.iterator();
	}


	/**
	 *
	 * @return
     */
	public int getSumOfTreeSizes() {
		Iterator<C> iter = chromosomes.iterator();
		int sumOfTrees = 0;
		C chromosome;
		while(iter.hasNext()){
			chromosome = iter.next();
			sumOfTrees+=chromosome.getSize();
		}
		return sumOfTrees;
	}
}
