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

import java.util.*;

/**
 * heart of the genetic process
 * each iteration of the process is manged by the evolve method in this class
 * @param <C>
 * @param <T>
 */
public class GeneticAlgorithm<C extends Chromosome<C>, T extends Comparable<T>> {

	private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;

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
				fit = GeneticAlgorithm.this.fitnessFunc.calculate(chr);
				this.cache.put(chr, fit);
			}
			return fit;
		};

		public void clearCache() {
			this.cache.clear();
		}
	}

	private final ChromosomesComparator chromosomesComparator;

	private final Fitness<C, T> fitnessFunc;

	private Population<C> population;

	private ParamGA paramGA;

	// listeners of genetic algorithm iterations (handle callback afterwards)
	private final List<IterartionListener<C, T>> iterationListeners = new LinkedList<IterartionListener<C, T>>();

	private boolean terminate = false;

	// Percentage of parental chromosomes, which survive (and move to new
	// population)
	private double parentSurviveRate = 1;

	private int iteration = 0;

	public GeneticAlgorithm(Population<C> population, Fitness<C, T> fitnessFunc, ParamGA paramGA) {
		this.population = population;
		this.fitnessFunc = fitnessFunc;
		this.chromosomesComparator = new ChromosomesComparator();
		this.population.sortPopulationByFitness(this.chromosomesComparator);
		this.paramGA = paramGA;
	}

	public double evolve() {
		double x;
		double effortInThisGeneration = 0;
		int parentPopulationSize = this.population.getSize();

		Population<C> newPopulation = new Population<C>();

		//GILAD GOOVER
		//
		int parentSurviveCount = (int) Math.floor(this.parentSurviveRate*population.getSize());

		//going for next generation
		for (int i = 0; (i < parentPopulationSize) && (i < parentSurviveCount); i++) {
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i));
		}

		for (int i = 0; i < parentPopulationSize; i++) {
			C chromosome = this.population.getChromosomeByIndex(i);

			//getting mutattion in the next generation
			x = Math.random();
			if (x < paramGA.getpMutationRate()) {
				C mutated = chromosome.mutate();
				newPopulation.addChromosome(mutated);
			}

			//getting crossover into the next generation
			x = Math.random();
			if (x < paramGA.getCrossoverRate()) {
				C otherChromosome = this.population.getRandomChromosome();
				List<C> crossovered = chromosome.crossover(otherChromosome);
				for (C c : crossovered) {
					newPopulation.addChromosome(c);
				}
			}
		}

		//if next generation is too small adds the best ones from previous gen that warnt chosen before
		for(int i = 0; newPopulation.getSize() < parentPopulationSize ; i++){
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i + parentSurviveCount));
			System.out.println("adding old one from parent population");
		}

		effortInThisGeneration = newPopulation.getSize();//PARAM EFFORT   IMPROVE handle treesizes as well
		newPopulation.sortPopulationByFitness(this.chromosomesComparator);
		newPopulation.trim(parentPopulationSize); //choosing the best parentPopulationSize chromosomes in the new generation
		this.population = newPopulation;
		return effortInThisGeneration;
	}

	public double evolve(int count) {
		this.terminate = false;
		double totalEngineEffort = this.population.getSize();//PARAM EFFORT set the whight for the population in the first generation
		// IMPROVE GILAD TODO handle tree sizes as well
		//IMPROVE TODO handle data set size as well
		for (int i = 0; i < count; i++) {
			if (this.terminate) {
				break;
			}
			totalEngineEffort += this.evolve();//PARAM EFFORT can set an heavier wieght  for each generation
			this.iteration = i;
			for (IterartionListener<C, T> l : this.iterationListeners) {
				l.update(this);
			}
		}
		return totalEngineEffort;
	}

	public int getIteration() {
		return this.iteration;
	}

	public void terminate() {
		this.terminate = true;
	}

	public Population<C> getPopulation() {
		return this.population;
	}

	public C getBest() {
		return this.population.getChromosomeByIndex(0);
	}

	public C getWorst() {
		return this.population.getChromosomeByIndex(this.population.getSize() - 1);
	}

	public void setParentSurviveRate(double parentChromosomesCount) {
		this.parentSurviveRate = parentChromosomesCount;
	}

	public double getParentSurviveRate() {
		return this.parentSurviveRate;
	}

	public void addIterationListener(IterartionListener<C, T> listener) {
		this.iterationListeners.add(listener);
	}

	public void removeIterationListener(IterartionListener<C, T> listener) {
		this.iterationListeners.remove(listener);
	}

	public T fitness(C chromosome) {
		return this.chromosomesComparator.fit(chromosome);
	}

	public void clearCache() {
		this.chromosomesComparator.clearCache();
	}
}
