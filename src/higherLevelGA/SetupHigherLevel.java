package higherLevelGA;

import interpreter.Functions;
import lowerLevelGA.BlackBoxTree;

import java.util.Collection;
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
     * constuctor of a set up, will calculate the best models according to the given params
     */
    public SetupHigherLevel(String name, List<BlackBoxTree> blackBoxesList, List<Functions> baseFunctions, ParamGA paramGA){
        this.name = name;
        this.blackBoxesList = blackBoxesList;
        this.baseFunctions = baseFunctions;
        this.bestParamGASolverFound = new SymRegSolverChromosome(paramGA, baseFunctions);
        calculateBestModelFoundList();
    }


    /**
     * this method will run an engine on the boxes
     */
    public void runHigherOnTheBlackBoxes(){
        System.out.println("  Running Setup "+ name);
        HigherGAEngine engine = new HigherGAEngine(blackBoxesList, baseFunctions, RunHigherLevel.HIGHER_POPULATION_SIZE);
        if(RunHigherLevel.PRINT_HIGHER_LEVEL_ITERATIONS)
            RunHigherLevel.addListener(engine);
        bestParamGASolverFound = engine.evolve(RunHigherLevel.NUM_GEN_HIGHER_LEVEL);
        calculateBestModelFoundList();

        //printSetup(); GILAD GOOVER
    }





    /**
     * basic to String that prints the setup toString (only basic data
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("     SetupHigherLevel: name: " + name + "\n");
        if (bestParamGASolverFound != null)
        {
            s.append("         ParamGA: " + bestParamGASolverFound.getParamGA() +"\n");
            s.append("          with hFitness: " + bestParamGASolverFound.getFitness() +"\n");
        }
        else{
            s.append("        setup hasn't been ran\n");
        }
        return s.toString();
    }

    /**
     * This method will print the current setup
     * with its results on the blackbox list
     */
    public void printSetupWithBlackBoxesAndBestModels(){
        if(bestParamGASolverFound != null && bestModelFoundList.size() == blackBoxesList.size()) {
            System.out.print(this);
            Iterator<BlackBoxTree> blackBoxTreeIterator = blackBoxesList.iterator();
            Iterator<BestModelCandidate> bestModelCandidateIterator = bestModelFoundList.iterator();
            while(blackBoxTreeIterator.hasNext() && bestModelCandidateIterator.hasNext()){
                System.out.println("\t         BlackBox: " + blackBoxTreeIterator.next());
                System.out.println("\t          BestModel: " + bestModelCandidateIterator.next());
            }
        }
        else if(bestParamGASolverFound == null)
            System.out.println("   setup hasn't been ran\n");
        else
            System.out.println(" setup has been ran, just black boxes and no best models\n");

    }

    /**
     * This method will print the current setup
     * just with black boxes
     */
    public void printSetupWithBlackBoxes(){
        if(blackBoxesList.size() != 0) {
            System.out.print(this);
            Iterator<BlackBoxTree> blackBoxTreeIterator = blackBoxesList.iterator();
            while(blackBoxTreeIterator.hasNext())
                System.out.println("	         BlackBox: " + blackBoxTreeIterator.next());
        }
        else
            System.out.println("   no black boxes\n");


    }

    /**
     * This method will print the current setup
     * just with black boxes
     */

    public void printSetupWithBestModels(){
        if(bestParamGASolverFound != null && bestModelFoundList.size() != 0) {
            System.out.print(this);
            Iterator<BestModelCandidate> bestModelCandidateIterator = bestModelFoundList.iterator();
            while(bestModelCandidateIterator.hasNext())
                System.out.println("\t         BestModel: " + bestModelCandidateIterator.next());

        }
        else if(bestParamGASolverFound == null)
            System.out.println("   setup hasn't been ran\n");
        else
            System.out.println(" setup hasn't been ran, no best models\n");

    }



    /**
     * this method will run the solver on the list so we can see how are the best parameters acting for the best ParamGA found.
     */
    public void calculateBestModelFoundList(){
        double hFitSum = 0;
        if(this.bestParamGASolverFound == null ){
            System.out.println("SetupHigherLevel: bestParamGASolverFound is null, run the setup before calculating");
            return;
        }
        else{
            bestModelFoundList = new LinkedList<>();
            BestModelCandidate bestModelCandidate;
            for(BlackBoxTree blackBox : this.blackBoxesList){
                bestModelCandidate = bestParamGASolverFound.trySolving(blackBox, false);
                hFitSum+=bestModelCandidate.getHFitnessElement();
                bestModelFoundList.add(bestModelCandidate);
            }
        }
        bestParamGASolverFound.setFitness(hFitSum/blackBoxesList.size());
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

    public ParamGA getParamGA() {
        ParamGA paramGA = null;
        if(bestParamGASolverFound != null)
            paramGA = bestParamGASolverFound.getParamGA();
        return paramGA;
    }

    public BestModelCandidate getBestModelByBlackBox(BlackBoxTree blackbox){
        Iterator<BlackBoxTree> iterBlack = this.blackBoxesList.iterator();
        Iterator<BestModelCandidate> iterBest = this.bestModelFoundList.iterator();
        BlackBoxTree blackBox;
        BestModelCandidate bestModel = null;
        while(iterBlack.hasNext()){
            blackBox = iterBlack.next();
            if(blackBox.toString().equals(blackBox.toString()))
                bestModel = iterBest.next();
            iterBest.next();
        }

        return bestModel;
    }

    public Collection<? extends BlackBoxTree> getBlackBoxList() {
        return this.blackBoxesList;
    }

    /**
     * simple getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * simple getter
     * @return
     */
    public List<BlackBoxTree> getBlackBoxesList() {
        return blackBoxesList;
    }
}