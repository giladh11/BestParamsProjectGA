package higherLevelGA;

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
    private SymRegSolverChromosome bestParamGASolverFound = null;
    private List<BestModelCandidate> bestModelFoundList = null;//will hold all the models the best solver found



    /**
     * constuctor of a set up
     */
    public SetupHigherLevel(String name, List<BlackBoxTree> blackBoxesList){
        this.name = name;
        this.blackBoxesList = blackBoxesList;
    }





    /**
     * basic to String that prints the setup toString (only basic data
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
       //TODO
        return s.toString();
    }

    /**
     * this method will run the solver on the list so we can see hows is the best parameters acting for the best ParamGA found.
     */
    public void calculateAndPrintBestModelFoundList(){
        //TODO TAL   use a a general method
    }

}