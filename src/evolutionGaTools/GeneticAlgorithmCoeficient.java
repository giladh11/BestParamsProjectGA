package evolutionGaTools;

import higherLevelGA.ParamGA;

import java.util.*;

/**
 * CLASS is only used so we could GeneticAlgorithm easily
 * heart of the genetic process
 * each iteration of the process is manged by the evolve method in this class
 * @param <C>
 * @param <T>
 */
public class GeneticAlgorithmCoeficient<C extends Chromosome<C, T>, T extends Comparable<T>> {

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
				fit = GeneticAlgorithmCoeficient.this.fitnessFunc.calculate(chr);
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

	private Population<C, T> population;

	private ParamGA paramGA;

	// listeners of genetic algorithm iterations (handle callback afterwards)
	private final List<IterartionListener<C, T>> iterationListeners = new LinkedList<IterartionListener<C, T>>();

	private boolean terminate = false;

	private int iteration = 0;

	public GeneticAlgorithmCoeficient(Population<C,T> population, Fitness<C, T> fitnessFunc, ParamGA paramGA) {
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
		C chromosome, mutated , perviuosChromo = null;


		Population<C,T> newPopulation = new Population<C,T>();


		this.population.shufflePopulation();

		for (int i = 0; i < parentPopulationSize; i++) {
			chromosome = this.population.getChromosomeByIndex(i);

			//adding all chromosomes to future generation
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i));

			if (i%2==1){
				//getting crossover into the next generation
				x = Math.random();
				if (x < paramGA.getCrossoverRate()) {
					List<C> crossovered = chromosome.crossover(perviuosChromo);
					for (C c : crossovered) {
						newPopulation.addChromosome(c);
					}
				}
			}else
				perviuosChromo = chromosome;

			//getting mutattion in the next generation
			x = Math.random();
			if (x < paramGA.getpMutationRate()) {
				mutated = chromosome.mutate();
				newPopulation.addChromosome(mutated);
			}
		}

		effortInThisGeneration = newPopulation.getSize()-parentPopulationSize;
		newPopulation.sortPopulationByFitness(this.chromosomesComparator);
		newPopulation.trim(parentPopulationSize); //choosing the best parentPopulationSize chromosomes in the new generation
		this.population = newPopulation;
		return effortInThisGeneration;
	}

	public double evolve(int count) {
		this.terminate = false;
		double totalEngineEffort = this.population.getSize();
		for (int i = 0; i < count; i++) {
			if (this.terminate) {
				break;
			}
			totalEngineEffort += this.evolve();
			this.iteration = i;
//			for (IterartionListener<C, T> l : this.iterationListeners) {
//				l.update(this);
//			}
		}
		return totalEngineEffort;
	}

	public int getIteration() {
		return this.iteration;
	}

	public void terminate() {
		this.terminate = true;
	}

	public Population<C,T> getPopulation() {
		return this.population;
	}

	public C getBest() {
		return this.population.getChromosomeByIndex(0);
	}

	public C getWorst() {
		return this.population.getChromosomeByIndex(this.population.getSize() - 1);
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
