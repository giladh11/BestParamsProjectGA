package evolutionGaTools;

/**
 * only used for printing
 * @param <C>
 * @param <T>
 */
public interface IterartionListenerHigh<C extends Chromosome<C, T>, T extends Comparable<T>> {

    void update(GeneticAlgorithmHigherLevel<C, T> environment);
    
}
