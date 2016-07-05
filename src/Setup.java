import higherLevelGA.BestModelCandidate;
import lowerLevelGA.BlackBoxTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * represents a blackBox tree and all the bestsolutions we got in different runs
 */
class Setup{
    BlackBoxTree blackBoxTree;
    private static List<BestModelCandidate> bestModelFoundList;

    private double minEffort; private double sumEffort; private double maxEffort;

    private double minDistanceFromBlackBox; private double sumDistanceFromBlackBox; private double maxDistanceFromBlackBox;

    private double minFitness; private double sumFitness; private double maxFitness;

    /**
     * constuctor of a set up
     * @param bt
     */
    public Setup(BlackBoxTree bt){
        this.blackBoxTree = bt;
        bestModelFoundList = new LinkedList<BestModelCandidate>();
        minEffort = -1 ; sumEffort = 0; maxEffort = 0;
        minDistanceFromBlackBox = -1 ; sumDistanceFromBlackBox = 0 ; maxDistanceFromBlackBox = 0;
        minFitness = -1; sumFitness = 0; maxFitness = 0;
    }

    /**
     * returns the black box this setup represents
     * @return
     */
    public BlackBoxTree getBlackBoxTree() {
        return blackBoxTree;
    }

    /**
     * adds a new model the this set up
     * new model came from running the genetic algorithem on the black list.
     * @param best
     */
    public void addBestModel(BestModelCandidate best){
        sumEffort +=  best.getEffort();
        sumDistanceFromBlackBox +=  best.getDistanceFromBlackBox();
        sumFitness+= best.getFitness();

        if (minEffort==-1 || best.getEffort() < minEffort)
            minEffort = best.getEffort();
        if (minDistanceFromBlackBox==-1 || best.getDistanceFromBlackBox() < minDistanceFromBlackBox)
            minDistanceFromBlackBox = best.getDistanceFromBlackBox();
        if (minFitness==-1 || best.getFitness() < minFitness)
            minFitness = best.getFitness();

        if (best.getEffort() > maxEffort)
            maxEffort = best.getEffort();
        if (best.getDistanceFromBlackBox() > maxDistanceFromBlackBox)
            maxDistanceFromBlackBox = best.getDistanceFromBlackBox();
        if (best.getFitness() > maxFitness)
            maxFitness = best.getFitness();

        bestModelFoundList.add(best);//adds to the end of the list
    }



    /**
     * basic to String that prints the setup toString
     * @return
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        Iterator<BestModelCandidate> iter =  bestModelFoundList.listIterator();
        int i = 0;
        BestModelCandidate best = null;
        s.append("blackBox is : " + blackBoxTree+"\n     ***   \n");
        while (iter.hasNext()){
            best = iter.next();
            s.append(i +". " + best.toString()+"\n");
            i++;
        }
        s.append("***\n"+"***\n"+"***+\n");
        appendAverages(s);
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
    public void appendAverages(StringBuilder s){
        s.append("     minEffort = "+ minEffort + ", avgEffort = "+sumEffort/bestModelFoundList.size() + ", maxEffort = " + maxEffort + "\n");
        s.append("     minDistanceFromBlackBox = " + minDistanceFromBlackBox + ", avgDistanceFromBlackBox = "+sumDistanceFromBlackBox/bestModelFoundList.size() + ", maxDistanceFromBlackBox = " + maxDistanceFromBlackBox + "\n");
        s.append("     minFitness = " + minFitness + ", avgFitness = "+sumFitness/bestModelFoundList.size() + ", maxFitness = " + maxFitness +"\n");
    }


}