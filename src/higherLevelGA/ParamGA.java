package higherLevelGA;

import evolutionGaTools.Chromosome;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * this class represents a GA parameters that will be used by SymRegSolverChromosome and
 * that will develop over time
 */
public class ParamGA implements Chromosome<ParamGA> {
    //These are the Chromosome's characteristics parameters
    private double pMutation;
    private double pCrossover;
    private int populationSize;


    //This parameter defines the measure of fitnessMeasureShouldNotBeUsed deduction
    //for too big trees
    private double bloatPenaltyRate;//IMPROVE too complicated now
    private int dataSetSize;

    private int initialParentChromosomesSurviveCount;//1 TODO maybe turn to precentage and join with pmutation and p cross over

    private int maxInitialTreeDepth;//1

    //Number of ParamGA that are evolved during higher lever GA.
    private static final int PARAM_GA_COUNT = 7;


    //PARAM
    private static final double MIN_MUTATION_PROB = 0;
    private static final double MAX_MUTATION_PROB = 1;

    //PARAM
    private static final double  MIN_CROSSOVER_PROB = 0;
    private static final double MAX_CROSSOVER_PROB = 1;

    //PARAM
    private static final int MIN_POPULATION_SIZE = 5;
    private static final int MAX_POPULATION_SIZE = 10;

    //PARAM
    private static final double MIN_BLOAT_PENALTY_RATE = 0;
    private static final double MAX_BLOAT_PENALTY_RATE = 2;

    //PARAM
    private static final int MIN_DATA_SET_SIZE = 5;
    private static final int MAX_DATA_SET_SIZE = 20;

    //PARAM
    private static final int MIN_ELITISM_SIZE = 0;
    private static final int MAX_ELITISM_SIZE = MAX_POPULATION_SIZE;

    //PARAM
    private static final int MIN_TREE_DEPTH = 1;
    private static final int MAX_TREE_DEPTH = 8;



    //Note: this property is static for all instances since only one-variables models are tested.
    private static final List<String> VARIABLES = list("x");

    private static Random rand = new Random();


    public ParamGA(int populationSize, int initialParentChromosomesSurviveCount, double pMutation, double pCrossover, int dataSetSize, int maxInitialTreeDepth, double bloatPenaltyRate) {
        this.pMutation = pMutation;
        this.pCrossover = pCrossover;
        this.populationSize = populationSize;
        this.bloatPenaltyRate = bloatPenaltyRate;
        this.dataSetSize = dataSetSize;
        this.initialParentChromosomesSurviveCount = initialParentChromosomesSurviveCount;
        this.maxInitialTreeDepth = maxInitialTreeDepth;
    }

    public ParamGA(ParamGA paramGA) {
        this.pMutation = paramGA.pMutation;
        this.pCrossover = paramGA.pCrossover;
        this.populationSize = paramGA.populationSize;
        this.bloatPenaltyRate = paramGA.bloatPenaltyRate;
        this.dataSetSize = paramGA.dataSetSize;
        this.initialParentChromosomesSurviveCount = paramGA.initialParentChromosomesSurviveCount;
        this.maxInitialTreeDepth = paramGA.maxInitialTreeDepth;

    }

    public ParamGA() {

    }


    public double getpMutationRate() {
        return pMutation;
    }

    public double getCrossoverRate() {
        return pCrossover;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxInitialTreeDepth() {
        return maxInitialTreeDepth;
    }

    public double getBloatPenaltyRate() {
        return bloatPenaltyRate;
    }


    public int getDataSetSize() {
        return dataSetSize;
    }

    public int getInitialParentChromosomesSurviveCount() {
        return initialParentChromosomesSurviveCount;
    }

    public Collection<String> getVariables() {
        return VARIABLES;
    }

    private static <T> List<T> list(T... items) {
        List<T> list = new LinkedList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    @Override
    public List<ParamGA> crossover(ParamGA anotherChromosome) {
        int crossoverPoint = getRandomIntegerInRange(1, PARAM_GA_COUNT);
        List<ParamGA> crossoverParamGAs = new LinkedList<>();

        ParamGA firstOffspring = null;
        ParamGA secondOffspring = null;

        try {
            System.out.println("crossover point: " + crossoverPoint);
            firstOffspring = createOffspring(this, anotherChromosome, crossoverPoint);
            secondOffspring = createOffspring(anotherChromosome, this, crossoverPoint);

        } catch (IllegalAccessException e) {
            System.out.println("crossover: error in creating offspring");
            e.printStackTrace();
        }

        crossoverParamGAs.add(firstOffspring);
        crossoverParamGAs.add(secondOffspring);
        return crossoverParamGAs;
    }

    /**
     * Create an offspring composed of the first fields of first till the crossoverPoint,
     * and the rest of the fields of the second.
     * Used by crossover method.
     * @param first
     * @param second
     * @param crossoverPoint
     * @return
     * @throws IllegalAccessException
     */
    private ParamGA createOffspring(ParamGA first, ParamGA second, int crossoverPoint) throws IllegalAccessException {
        ParamGA offspring = new ParamGA();
        int copiedFields = 1;

        for(Field field : first.getClass().getDeclaredFields()){
            if(copiedFields <= crossoverPoint){
                    field.set(offspring, field.get(first));
                copiedFields++;
            }
            else if(copiedFields <= PARAM_GA_COUNT){
                field.set(offspring, field.get(second));
                copiedFields++;
            }

            else
                break;
        }
        return offspring;
    }

    @Override
    public ParamGA mutate() {
        int mutatedParam =  getRandomIntegerInRange(1, PARAM_GA_COUNT);
        //Copy the original ParamGA before mutation
        ParamGA mutated = new ParamGA(this);
        switch (mutatedParam){
            //pMutation
            case 1:
                double pMutation = getRandomDoubleInRange(MIN_MUTATION_PROB, MAX_MUTATION_PROB);
                mutated.pMutation = pMutation;
                break;
            //pCrossover
            case 2:
                double pCrossover = getRandomDoubleInRange(MIN_CROSSOVER_PROB, MAX_CROSSOVER_PROB);
                mutated.pCrossover = pCrossover;
                break;
            //populationSize
            case 3:
                int populationSize = getRandomIntegerInRange(MIN_POPULATION_SIZE, MAX_POPULATION_SIZE);
                mutated.populationSize = populationSize;
                break;
            //bloatPenaltyRate
            case 4:
                double bloatPenaltyRate = getRandomDoubleInRange(MIN_BLOAT_PENALTY_RATE, MAX_BLOAT_PENALTY_RATE);
                mutated.bloatPenaltyRate = bloatPenaltyRate;
                break;
            //dataSetSize
            case 5:
                int dataSetSize = getRandomIntegerInRange(MIN_DATA_SET_SIZE, MAX_DATA_SET_SIZE);
                mutated.dataSetSize = dataSetSize;
                break;
            //initialParentChromosomesSurviveCount
            case 6:
                int elitism = getRandomIntegerInRange(MIN_ELITISM_SIZE, MAX_ELITISM_SIZE);
                mutated.initialParentChromosomesSurviveCount = elitism;
                break;
            //maxInitialTreeDepth
            case 7:
                int maxInitialTreeDepth = getRandomIntegerInRange(MIN_TREE_DEPTH, MAX_TREE_DEPTH);
                mutated.maxInitialTreeDepth = maxInitialTreeDepth;
                break;
        }
        return mutated;
    }


    private static int getRandomIntegerInRange(int minimum, int maximum) {
        int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
        return randomNum;
    }

    private static double getRandomDoubleInRange(double minimum, double maximum) {
        double randomNum = rand.nextDouble();
        double result = minimum + (randomNum*(maximum - minimum));
        return result;
    }


    public static ParamGA getRandomParamGA(){
        ParamGA randomParamGA = new ParamGA();

        double pMutation = getRandomDoubleInRange(MIN_MUTATION_PROB, MAX_MUTATION_PROB);
        randomParamGA.pMutation = pMutation;

        double pCrossover = getRandomDoubleInRange(MIN_CROSSOVER_PROB, MAX_CROSSOVER_PROB);
        randomParamGA.pCrossover = pCrossover;

        int populationSize = getRandomIntegerInRange(MIN_POPULATION_SIZE, MAX_POPULATION_SIZE);
        randomParamGA.populationSize = populationSize;

        double bloatPenaltyRate = getRandomDoubleInRange(MIN_BLOAT_PENALTY_RATE, MAX_BLOAT_PENALTY_RATE);
        randomParamGA.bloatPenaltyRate = bloatPenaltyRate;

        int dataSetSize = getRandomIntegerInRange(MIN_DATA_SET_SIZE, MAX_DATA_SET_SIZE);
        randomParamGA.dataSetSize = dataSetSize;

        int elitism = getRandomIntegerInRange(MIN_ELITISM_SIZE, MAX_ELITISM_SIZE);
        randomParamGA.initialParentChromosomesSurviveCount = elitism;

        int maxInitialTreeDepth = getRandomIntegerInRange(MIN_TREE_DEPTH, MAX_TREE_DEPTH);
        randomParamGA.maxInitialTreeDepth = maxInitialTreeDepth;

        return randomParamGA;

    }
    public String toString(){

        return "ParamGA - pMutation: " +  pMutation + ", "
                + "pCrossover: " + pCrossover + ", "
                + "populationSize: " + populationSize + ", "
                + "bloatPenaltyRate: " + bloatPenaltyRate + ", "
                + "dateSetSize: " + dataSetSize + ", "
                + "elitism: " + initialParentChromosomesSurviveCount + "%.";

    }



    public static void main(String[] args) {
//        ParamGA first = new ParamGA(MIN_POPULATION_SIZE, MIN_ELITISM_SIZE, MIN_MUTATION_PROB, MIN_CROSSOVER_PROB, MIN_DATA_SET_SIZE, MIN_TREE_DEPTH, MIN_BLOAT_PENALTY_RATE);
//        ParamGA second = new ParamGA(MAX_POPULATION_SIZE, MAX_ELITISM_SIZE, MAX_MUTATION_PROB, MAX_CROSSOVER_PROB, MAX_DATA_SET_SIZE, MAX_TREE_DEPTH, MAX_BLOAT_PENALTY_RATE);
        ParamGA first = getRandomParamGA();
        ParamGA second = getRandomParamGA();
        List<ParamGA> offspring = first.crossover(second);
        System.out.println("Parents: ");
        System.out.println("Parent1: " + first);
        System.out.println("Parent2: " + second);
        System.out.println("offspring:");
        for (ParamGA off: offspring)
            System.out.println(off);




    }

}
