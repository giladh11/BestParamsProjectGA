package higherLevelGA;

import interpreter.Functions;
import lowerLevelGA.BlackBoxTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * represents a blackBox tree and all the bestsolutions we got in different runs
 */
class SetupHigherLevel {
    private String name;
    private List<BlackBoxTree> blackBoxesList;
    private List<Functions> baseFunctions;
    private SymRegSolverChromosome bestParamGASolverFound = null;
    private List<BestModelCandidate> bestModelFoundList = null;//will hold all the models the best solver found



    /**
     * constuctor of a set up
     */
    public SetupHigherLevel(String name, List<BlackBoxTree> blackBoxesList, List<Functions> baseFunctions){
        this.name = name;
        this.blackBoxesList = blackBoxesList;
        this.baseFunctions = baseFunctions;
    }


    /**
     * this method will run an engine on the boxes
     */
    public void runHigherOnTheBlackBoxes(){
        System.out.println("Running Setup "+ name);
        HigherGAEngine engine = new HigherGAEngine(blackBoxesList, baseFunctions, RunHigherLevel.HIGHER_POPULATION_SIZE);
        if(RunHigherLevel.PRINT_HIGHER_LEVEL_ITERATIONS)
            RunHigherLevel.addListener(engine);
        bestParamGASolverFound = engine.evolve(RunHigherLevel.NUM_GEN_HIGHER_LEVEL);
        calculateBestModelFoundList();

        printSetup();
    }





    /**
     * basic to String that prints the setup toString (only basic data
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("SetupHigherLevel: name: " + name + "\n");
        if (bestParamGASolverFound != null)
        {
            s.append("   ParamGA: " + bestParamGASolverFound.getParamGA() +"\n");
            s.append("   with fitness: " + bestParamGASolverFound.getFitness() +"\n");
        }
        else{
            s.append("   setup hasn't been ran\n");
        }
        return s.toString();
    }

    /**
     * This method will print the current setup
     * with its results on the blackbox list
     */
    public void printSetup(){
        if(bestParamGASolverFound != null && bestModelFoundList.size() == blackBoxesList.size()) {
            System.out.print(this);
            Iterator<BlackBoxTree> blackBoxTreeIterator = blackBoxesList.iterator();
            Iterator<BestModelCandidate> bestModelCandidateIterator = bestModelFoundList.iterator();
            while(blackBoxTreeIterator.hasNext() && bestModelCandidateIterator.hasNext()){
                System.out.println("\tBlackBox: " + blackBoxTreeIterator.next());
                System.out.println("\tBestModel: " + bestModelCandidateIterator.next());
            }
        }

    }

    /**
     * this method will run the solver on the list so we can see how are the best parameters acting for the best ParamGA found.
     */
    public void calculateBestModelFoundList(){
        if(this.bestParamGASolverFound == null ){
            System.out.println("SetupHigherLevel: bestParamGASolverFound is null, run the setup before calculating");
            return;
        }
        else{
            bestModelFoundList = new LinkedList<BestModelCandidate>();
            BestModelCandidate bestModelCandidate;
            for(BlackBoxTree blackBox : this.blackBoxesList){
                bestModelCandidate = bestParamGASolverFound.trySolving(blackBox, false);
                bestModelFoundList.add(bestModelCandidate);
            }
        }

    }

    /**
     * returns true if the setup has already been ran
     * @return
     */
    public boolean hasBeenRan() {
        if(bestParamGASolverFound==null)
            return false;
        else
            return true;
    }
}