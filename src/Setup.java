import higherLevelGA.BestModelCandidate;
import higherLevelGA.ParamGA;
import lowerLevelGA.BlackBoxTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * represents a blackBox tree and all the bestsolutions we got in different runs
 */
class Setup{
    private BlackBoxTree blackBoxTree;
    private ParamGA paramGA;
    private List<BestModelCandidate> bestModelFoundList;

    private int minGen; private int sumGen; private int maxGen;

    private double minDistanceFromBlackBox; private double sumDistanceFromBlackBox; private double maxDistanceFromBlackBox;

    private double minHFitness; private double sumHFitness; private double maxHFitness;

    /**
     * constuctor of a set up
     * @param bt
     */
    public Setup(BlackBoxTree bt, ParamGA paramGA){
        this.blackBoxTree = bt;
        this.paramGA = paramGA;
        bestModelFoundList = new LinkedList<BestModelCandidate>();
        minGen = -1 ; sumGen = 0; maxGen = 0;
        minDistanceFromBlackBox = -1 ; sumDistanceFromBlackBox = 0 ; maxDistanceFromBlackBox = 0;
        minHFitness = -1; sumHFitness = 0; maxHFitness = 0;
    }

    /**
     * returns the black box this setup represents
     * @return
     */
    public BlackBoxTree getBlackBoxTree() {
        return blackBoxTree;
    }


    /**
     * simple getter
     * @return
     */
    public ParamGA getParamGA() {
        return paramGA;
    }


    /**
     * adds a new model the this set up
     * new model came from running the genetic algorithem on the black list.
     * @param best
     */
    public void addBestModel(BestModelCandidate best){
        sumGen +=  best.getEffortElement().getGen();
        sumDistanceFromBlackBox +=  best.getDistanceFromBlackBox();
        sumHFitness += best.getHFitnessElement();

        if (minGen ==-1 || best.getEffortElement().getGen() < minGen)
            minGen = best.getEffortElement().getGen();
        if (minDistanceFromBlackBox==-1 || best.getDistanceFromBlackBox() < minDistanceFromBlackBox)
            minDistanceFromBlackBox = best.getDistanceFromBlackBox();
        if (minHFitness ==-1 || best.getHFitnessElement() < minHFitness)
            minHFitness = best.getHFitnessElement();

        if (best.getEffortElement().getGen() > maxGen)
            maxGen = best.getEffortElement().getGen();
        if (best.getDistanceFromBlackBox() > maxDistanceFromBlackBox)
            maxDistanceFromBlackBox = best.getDistanceFromBlackBox();
        if (best.getHFitnessElement() > maxHFitness)
            maxHFitness = best.getHFitnessElement();

        bestModelFoundList.add(best);//adds to the end of the list
    }



    /**
     * basic to String that prints the setup toString (only basic data
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        appendSetupData(s);
        return s.toString();
    }

    /**
     * this method prints the
     */
    public void printAverages(){
        StringBuilder s = new StringBuilder();
        appendAverages(s);
        System.out.println(s.toString());
    }



    /**
     * adds to the string builder all the averages
     * @param s
     */
    private void appendAverages(StringBuilder s){
        s.append("      num of best models found = "+bestModelFoundList.size() + "\n");
        s.append("      minGen = "+ minGen + ", avgGen = "+ sumGen /bestModelFoundList.size() + ", maxGen = " + maxGen + "\n");
        s.append("      minDistanceFromBlackBox = " + minDistanceFromBlackBox + ", avgDistanceFromBlackBox = "+sumDistanceFromBlackBox/bestModelFoundList.size() + ", maxDistanceFromBlackBox = " + maxDistanceFromBlackBox + "\n");
        s.append("      minHFitness = " + minHFitness + ", avgHFitness = "+ sumHFitness /bestModelFoundList.size() + ", maxHFitness = " + maxHFitness +"\n");
    }

    /**
     * adds to the string builder all the averages
     * @param s
     */
    public void appendSetupData(StringBuilder s){
        s.append(".   !!!   "+"ParamGA is : " + paramGA+"\n");
        s.append("     blackBox is : " + blackBoxTree+"\n       ***   \n");
        if (bestModelFoundList.size()!=0)
            appendAverages(s);
        else
            s.append("     no runs have been made");
        s.append("\n");
    }

    /**
     * prints the setups history
     */
    public void printSetupHistory(){
        StringBuilder s = new StringBuilder();
        Iterator<BestModelCandidate> iter =  bestModelFoundList.listIterator();
        int i = 0;
        BestModelCandidate best = null;
        s.append("ParamGA is : " + paramGA+"  \n");
        s.append("blackBox is : " + blackBoxTree+"\n     ***   \n");
        while (iter.hasNext()){
            best = iter.next();
            s.append(i +". " + best.toString()+"\n");
            i++;
        }
        s.append("               ***\n"+"               ***\n"+"               ***\n");
        appendAverages(s);
        System.out.println(s.toString());
    }

    /**
     * simple getter
     * @return
     */
    public int getNumOfModels() {
        return bestModelFoundList.size();
    }
}