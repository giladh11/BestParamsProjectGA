package evolutionGaTools;

/**
 * only used for printing
 * @param <C>
 * @param <T>
 */
public interface IterartionListener<C extends Chromosome<C, T>, T extends Comparable<T>> {

    void update( GeneticAlgorithm<C, T> environment );
    
}
