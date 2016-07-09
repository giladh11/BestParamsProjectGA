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
        if (bestParamGASolverFound != null)
        {
            ParamGA paramGA = bestParamGASolverFound.getParamGA();
            s.append("SetupHigherLevel: name: " + name + "\n");
            s.append("\tParamGA: " + paramGA +"\n");

        }
        return s.toString();
    }

    /**
     * This method will print the current setup
     * with its results on the blackbox list
     */
    public void printSetup(){
        if(bestParamGASolverFound != null && bestModelFoundList.size() == blackBoxesList.size()) {
            System.out.println("SetupHigherLevel: " + this.toString());
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
            System.out.println("SetupHigherLevel: bestParamGASolverFound is null");
            return;
        }
        else{
            bestModelFoundList = new LinkedList<>();
            BestModelCandidate bestModelCandidate;
            for(BlackBoxTree blackBox : this.blackBoxesList){
                bestModelCandidate = bestParamGASolverFound.trySolving(blackBox, false);
                bestModelFoundList.add(bestModelCandidate);
            }
        }

    }

}