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

import higherLevelGA.ParamGA;
import higherLevelGA.SymRegSolverChromosome;

import java.util.*;

/**
 * heart of the genetic process
 * each iteration of the process is manged by the evolve method in this class
 * @param <C>
 * @param <T>
 */
public class GeneticAlgorithmHigherLevel<C extends Chromosome<C, T>, T extends Comparable<T>> {

	private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;
	private static double HIGHER_CHROMOSOME_RATE;
	private static double HIGHER_MUTATUION_RATE;
	private SymRegSolverChromosome bestParamGaChromosomeFound;
																										//private Effort effort;

	private class ChromosomesComparator implements Comparator<C> {

		private final Map<C, T> cache = new WeakHashMap<C, T>();

		@Override
		public int compare(C chr1, C chr2) {
			T fit1 = this.fit(chr1);
			T fit2 = this.fit(chr2);
			int ret = fit1.compareTo(fit2);
			return ret;
		}

		public T fit(C chr) {
			T fit = this.cache.get(chr);
			if (fit == null) {
				fit =  chr.getFitness();//TODO makesure the fitness func works good with this
				if(fit==null) {
					fit = GeneticAlgorithmHigherLevel.this.fitnessFunc.calculate(chr);//in lower level get the functionTreeChromosome
					this.cache.put(chr, fit);
					chr.setFitness(fit);
				}
			}
			return fit;
		};

		public void clearCache() {
			this.cache.clear();
		}
	}

	private final ChromosomesComparator chromosomesComparator;

	private final Fitness<C, T> fitnessFunc;

	private Population<C, T> population;

	// listeners of genetic algorithm iterations (handle callback afterwards)
	private final List<IterartionListenerHigh<C, T>> iterationListeners = new LinkedList<IterartionListenerHigh<C, T>>();

	private boolean terminate = false;


	private int iteration = 0;

	public GeneticAlgorithmHigherLevel(Population<C, T> population, Fitness<C, T> fitnessFunc) {
		this.population = population;
		this.fitnessFunc = fitnessFunc;
		this.chromosomesComparator = new ChromosomesComparator();
		this.population.sortPopulationByFitness(this.chromosomesComparator);
	}

	public void evolve() {
		double x;
		int parentPopulationSize = this.population.getSize();
		C chromosome, mutated , perviuosChromo = null;


		Population<C, T> newPopulation = new Population<C, T>();


		this.population.shufflePopulation();

		for (int i = 0; i < parentPopulationSize; i++) {
			chromosome = this.population.getChromosomeByIndex(i);

			//adding all chromosomes to future generation
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i));

			if (i%2==1){
				//getting crossover into the next generation
				x = Math.random();
				if (x < HIGHER_CHROMOSOME_RATE) {
																								//effort.numOfCrossovers++;
					List<C> crossovered = chromosome.crossover(perviuosChromo);
					for (C c : crossovered) {
						newPopulation.addChromosome(c);
					}
				}
			}else
				perviuosChromo = chromosome;

			//getting mutattion in the next generation
			x = Math.random();
			if (x < HIGHER_MUTATUION_RATE) {
																									//effort.numOfMutations++;
				mutated = chromosome.mutate();
				newPopulation.addChromosome(mutated);
			}
		}

		newPopulation.sortPopulationByFitness(this.chromosomesComparator);
		newPopulation.trim(parentPopulationSize); //choosing the best parentPopulationSize chromosomes in the new generation
		this.population = newPopulation;
	}

	public C evolve(int count) {
		this.terminate = false;

																										//effort = new Effort(0,0,0,0);
		for (int i = 0; i < count; i++) {
			if (this.terminate) {
																													//effort.genNum = i;
				break;
			}
			this.evolve();
			this.iteration = i;
			for (IterartionListenerHigh<C, T> l : this.iterationListeners) {
				l.update(this);
			}
		}
		return this.population.getChromosomeByIndex(0);

																		//		if (effort.genNum ==0)
																		//			effort.genNum = count;
																		//
																		//		effort.numOfPointsEvaluated += paramGA.getPopulationSize()*paramGA.getDataSetSize();
																		//		effort.numOfPointsEvaluated += effort.numOfCrossovers*paramGA.getDataSetSize()*2;
																		//		effort.numOfPointsEvaluated += effort.numOfMutations*paramGA.getDataSetSize();

																		//return effort;
	}

	public int getIteration() {
		return this.iteration;
	}

	public void terminate() {
		this.terminate = true;
	}

	public Population<C, T> getPopulation() {
		return this.population;
	}

	public C getBest() {
		return this.population.getChromosomeByIndex(0);
	}

	public C getWorst() {
		return this.population.getChromosomeByIndex(this.population.getSize() - 1);
	}

	public void addIterationListener(IterartionListenerHigh<C, T> listener) {
		this.iterationListeners.add(listener);
	}

	public void removeIterationListener(IterartionListenerHigh<C, T> listener) {
		this.iterationListeners.remove(listener);
	}

	public T fitness(C chromosome) {
		return this.chromosomesComparator.fit(chromosome);
	}

	public void clearCache() {
		this.chromosomesComparator.clearCache();
	}

	/**
	 * simple param setter
	 * @param higherMutatuionRate
     */
	public static void setHigherMutatuionRate(double higherMutatuionRate) {
		HIGHER_MUTATUION_RATE = higherMutatuionRate;
	}

	/**
	 * simple param setter
	 * @param higherChromosomeRate
     */
	public static void setHigherChromosomeRate(double higherChromosomeRate) {
		HIGHER_CHROMOSOME_RATE = higherChromosomeRate;
	}
}