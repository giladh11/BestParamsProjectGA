package evolutionGaTools;

import java.util.List;

/**
 * simple interface just to make sure operators exists
 * @param <C>
 */
public interface Chromosome<C extends Chromosome<C, T>, T extends Comparable<T>> {
	
	List<C> crossover( C anotherChromosome );
	
	C mutate();

	T getFitness();

	void setFitness(T fit);

	int getSize();

//	double getCrossoverRate();
//
//	double getMutateRate();
//
//	double getBloatPenaltyRate();




}
