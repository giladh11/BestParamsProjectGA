package evolutionGaTools;

import higherLevelGA.ParamGA;
import p.PARAMs;

import java.util.*;

/**
 * heart of the genetic process
 * each iteration of the process is manged by the evolve method in this class
 * @param <C>
 * @param <T>
 */
public class GeneticAlgorithm<C extends Chromosome<C, T>, T extends Comparable<T>> {

	private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;
	private Effort effort;

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
			T lFit = this.cache.get(chr);
			if (lFit == null) {
				lFit =  chr.getFitness();//LowerFitness
				if(lFit==null) {
					lFit = GeneticAlgorithm.this.lowerFitnessFunc.calculate(chr);//in lower level get the functionTreeChromosome
				}
				this.cache.put(chr, lFit);
				chr.setFitness(lFit);
			}
			return lFit;
		};

		public void clearCache() {
			this.cache.clear();
		}
	}

	private final ChromosomesComparator lowerChromosomesComparator;

	private final Fitness<C, T> lowerFitnessFunc;

	private Population<C, T> population;

	private ParamGA paramGA;

	// listeners of genetic algorithm iterations (handle callback afterwards)
	private final List<IterartionListener<C, T>> iterationListeners = new LinkedList<IterartionListener<C, T>>();

	private boolean terminate = false;


	private int iteration = 0;

	public GeneticAlgorithm(Population<C, T> population, Fitness<C, T> lowerFitnessFunc, ParamGA paramGA) {
		this.population = population;
		this.effort = new Effort(population.getSumOfTreeSizes());
		this.lowerFitnessFunc = lowerFitnessFunc;
		this.lowerChromosomesComparator = new ChromosomesComparator();
		this.population.sortPopulationByFitness(this.lowerChromosomesComparator);
		this.paramGA = paramGA;

	}

	public void evolve() {
		double x;
		int parentPopulationSize = this.population.getSize();
		C chromosome, mutated , previousChromo = null;


		Population<C, T> newPopulation = new Population<C, T>();


		this.population.shufflePopulation();

		for (int i = 0; i < parentPopulationSize; i++) {
			chromosome = this.population.getChromosomeByIndex(i);

			//adding all chromosomes to future generation
			newPopulation.addChromosome(this.population.getChromosomeByIndex(i));

			if (i%2 == 1){
				//getting crossover into the next generation
				x = Math.random();
				if (x < paramGA.getCrossoverRate()) {
					effort.numOfCrossovers++;
					List<C> crossovered = chromosome.crossover(previousChromo);
					for (C c : crossovered) {
						effort.sumOfTreesSizesCreated +=c.getSize();
						newPopulation.addChromosome(c);
					}
				}
			}else
				previousChromo = chromosome;

			//getting mutattion in the next generation
			x = Math.random();
			if (x < paramGA.getpMutationRate()) {
				effort.numOfMutations++;
				mutated = chromosome.mutate();
				newPopulation.addChromosome(mutated);
				effort.sumOfTreesSizesCreated +=mutated.getSize();
			}
		}

		newPopulation.sortPopulationByFitness(this.lowerChromosomesComparator);
		newPopulation.trim(parentPopulationSize); //choosing the best parentPopulationSize chromosomes in the new generation
		this.population = newPopulation;

	}

	public Effort evolve(int count) {
		this.terminate = false;
		for (int i = 0; i < count; i++) {
			if (this.terminate) {
				effort.genNum = i;
				break;
			}
			this.evolve();
			this.iteration = i;
			for (IterartionListener<C, T> l : this.iterationListeners) {
				l.update(this);
			}
			if( ((Double)getBest().getFitness()).doubleValue() <= PARAMs.EPSILON_DISTANCE_FOR_LOWER_EVOLUTION_TO_STOP)//lowerFitness
				this.terminate();
		}

		if (effort.genNum ==0)
			effort.genNum = count;

		effort.numOfPointsEvaluated += paramGA.getPopulationSize()*paramGA.getDataSetSize();
		effort.numOfPointsEvaluated += effort.numOfCrossovers*paramGA.getDataSetSize()*2;
		effort.numOfPointsEvaluated += effort.numOfMutations*paramGA.getDataSetSize();


		return effort;
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

	public void addIterationListener(IterartionListener<C, T> listener) {
		this.iterationListeners.add(listener);
	}

	public void removeIterationListener(IterartionListener<C, T> listener) {
		this.iterationListeners.remove(listener);
	}

	/**
	 * lower fitness
	 * @param chromosome
	 * @return
     */
	public T fitness(C chromosome) {
		return this.lowerChromosomesComparator.fit(chromosome);
	}

	public void clearCache() {
		this.lowerChromosomesComparator.clearCache();
	}
}
