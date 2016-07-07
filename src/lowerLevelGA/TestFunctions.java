package lowerLevelGA;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class contains some of the functions on which
 * we tested our SymRegSolver.
 */
public class TestFunctions {


    private static Random rand = new Random();
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 50;
    private static final String VARIABLE = "x";
    private static final String ADD = "+";
    private static final String SUB = "-";
    private static final String MUL = "*";
    private static final String DIV = "/";
    private static final String POW = "^";
    private static final String SQRT = "sqrt";
    private static final String EXP = "exp";
    private static final String SPACE = " ";
    private static final String SIN = "sin";
    private static final String COS = "cos";
    private static final String TAN = "tan";
    private static final String ATAN = "atan";
    private static final List<String> trigonometricFunctions = Arrays.asList(SIN, COS, TAN, ATAN);
    private static final List<String> arithmeticOperations = Arrays.asList(ADD, SUB, MUL, DIV);




    /**
     * Utility function.
     * return a random integer in range(minimum, maximum)
     * @param minimum
     * @param maximum
     * @return
     */
    private static int getRandomIntegerInRange(int minimum, int maximum) {
        int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
        return randomNum;
    }
    /**
     * Utility function.
     * return a random double in range(minimum, maximum)
     * @param minimum
     * @param maximum
     * @return
     */
    private static double getRandomDoubleInRange(double minimum, double maximum) {
        double randomNum = rand.nextDouble();
        double result = minimum + (randomNum*(maximum - minimum));
        return result;
    }


    /**
     * Utility function.
     * Wrap the given string x with brackets.
     * @param x
     * @return
     */
    public static String brackets(String x){
        return "(" + x + ")";
    }

    /**
     * Utility function.
     * Imitating a flip of a coin.
     * @return
     */
    public static int coin(){
        return getRandomIntegerInRange(0, 1);
    }

    /**
     * Utility function.
     * return uniformly chosen add or sub.
     * @return
     */
    public static String randomSubAdd(){
        if(coin() == 1)
            return ADD;
        return SUB;
    }
    /**
     * Utility function.
     * return uniformly chosen sin or cos.
     * @return
     */
    public static String randomSinCos(){
        if(coin() == 1)
            return SIN;
        return COS;
    }

    /**
     * Utility function.
     * return uniformly chosen sin, cos, tan and arctan.
     * @return
     */

    public static String randomBasicTrigonometricFunction(){
        return trigonometricFunctions.get(rand.nextInt(trigonometricFunctions.size()));
    }

    /**
     * Utility function.
     * return uniformly chosen add, sub, mul and div.
     * @return
     */
    public static String randomArithmeticFunction(){
        return arithmeticOperations.get(rand.nextInt(arithmeticOperations.size()));
    }

    /**
     * Create a random polynomial of degree <= ndegree.
     * @param ndegree
     * @return
     */
    public static String createPolynomial(int ndegree){
        StringBuilder polynomial = new StringBuilder();

        int first = 0;
        String action, kx;

        while(ndegree > 0){
            if(coin() == 1) {
                if(first != 0) {
                    action = randomSubAdd();
                    polynomial.append(SPACE + action + SPACE);
                }

                kx = randomKx();
                polynomial.append(kx + POW + ndegree);

                first = 1;
            }

            ndegree--;
        }

        if(coin() == 1) {
            action = randomSubAdd();
            kx = randomK();
            polynomial.append(SPACE + action + SPACE + kx);
        }

        return polynomial.toString();
    }


    /**
     * Create a random trigonometric function of length nlength.
     * @param nlength
     * @return
     */
    public static String createTrigonometricFunction(int nlength){
        StringBuilder trigoFunc = new StringBuilder();

        int first = 0;
        String trigo;
        String action;

        while(nlength > 0){
            if(coin() == 1) {
                if(first != 0) {
                    action = randomSubAdd();
                    trigoFunc.append(SPACE + action + SPACE);
                }

                trigo = randomTrigonometricFunction();
                trigoFunc.append(trigo);
                first = 1;
            }

            nlength--;
        }

        return trigoFunc.toString();

    }

    /**
     * Create an atomic random trigonometric function of
     * the form k*func(c*x), where k,c are constant
     * and func = randomBasicTrigonometricFunction().
     * @return
     */
    public static String randomTrigonometricFunction(){
        StringBuilder trigoFunc = new StringBuilder();
        int angleCoefficient = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
        int coefficient = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
        String trigo = randomBasicTrigonometricFunction();
        trigoFunc.append(coefficient + MUL + trigo + brackets(angleCoefficient + MUL + VARIABLE));

        return trigoFunc.toString();

    }

    /**
     * Create an atomic random exponent function
     * of the form k*exp(c*x), where k,c are constant.
     * @return
     */
    public static String randomExp(){
        StringBuilder expFunc = new StringBuilder();
        int coefficient = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
        int expCoefficient = getRandomIntegerInRange(MIN_VALUE, 5);
        expFunc.append(coefficient + MUL + EXP + brackets(expCoefficient + MUL + VARIABLE));

        return expFunc.toString();
    }

    /**
     * Utility function.
     * return a random expression
     * from one of the following forms:
     * kx
     * k
     * k*exp(c*x)
     * @return
     */
    public static String randomExpKxK(){
        if(coin() == 1)
            return randomKx();
        else if(coin() == 1)
            return randomK();
        return randomExp();
    }

    /**
     * Create an atomic random expression of
     * the form k*x, where k is constant
     * @return
     */
    public static String randomKx(){
        StringBuilder kx = new StringBuilder();
        int coefficient = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
        kx.append(coefficient + MUL + VARIABLE);
        return kx.toString();
    }

    /**
     * Create an atomic random expression
     * of the form k, where k is constant
     * @return
     */
    public static String randomK(){
        StringBuilder k = new StringBuilder();
        int coefficient = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
        k.append(coefficient);
        return k.toString();
    }

    /**
     * Create an exponent function of length <= nlength.
     * @param nlength
     * @return
     */

    public static String createExpFunction(int nlength){
        StringBuilder expFunc = new StringBuilder();
        String element;
        String action;

        element = randomExp();
        expFunc.append(element);

        while(nlength > 1){
            if(coin() == 1) {
                action = randomSubAdd();
                expFunc.append(SPACE + action + SPACE);
                element = randomExpKxK();
                expFunc.append(element);
            }

            nlength--;
        }

        return expFunc.toString();

    }

    /**
     * Return a list of size count of random polynomial of <= degree.
     * @param count
     * @param length
     * @return
     */
    public static List<String> exponents(int count, int length){
        List<String> functions = new LinkedList<>();
        String linearFunction;
        while(count > 0){
            linearFunction = createExpFunction(length);
            if(!linearFunction.isEmpty()) {
                functions.add(linearFunction);
                count--;
            }

        }

        return functions;
    }

    /**
     * Return a list of size count of random polynomial of <= degree.
     * @param count
     * @param degree
     * @return
     */
    public static List<String> polynomials(int count, int degree){
        List<String> functions = new LinkedList<>();
        String linearFunction;
        while(count > 0){
            linearFunction = createPolynomial(degree);
            if(!linearFunction.isEmpty())
                functions.add(linearFunction);
            else
                count++;
            count--;
        }

        return functions;
    }

    /**
     * Return a list of size count of random trigonometric functions of <= length.
     * @param count
     * @param length
     * @return
     */
    public static List<String> trigonometricFunctions(int count, int length){
        List<String> functions = new LinkedList<>();
        String trigonometricFunction;
        while(count > 0){
            trigonometricFunction = createTrigonometricFunction(length);
            if(!trigonometricFunction.isEmpty())
                functions.add(trigonometricFunction);
            else
                count++;
            count--;
        }

        return functions;
    }


    /**
     * Return a list of black boxes that
     * corresponds to the given list of functions.
     * @param functions
     * @return
     */
    public static List<BlackBoxTree> buildBlackBoxList(List<String> functions){
        List<BlackBoxTree> blackBoxTreeList = new LinkedList<>();
        for(String function : functions)
            blackBoxTreeList.add(new BlackBoxTree(function));

        return blackBoxTreeList;
    }


    public static List<String> getTestFunctions(){
        List<String> customSet = new LinkedList<>();
        customSet.add("(x-1) - (x^2 - 2*x + 1)/(x - 1)");
        customSet.add("sin(x)^2 - cos(x)^2 + 1");
        customSet.add("1/(1 + x^2)");
        customSet.add("1/x^2");
        customSet.add(createPolynomial(3));
        customSet.add(createPolynomial(4));
        customSet.add(createExpFunction(3));
        customSet.add(createExpFunction(3));
        customSet.add(createTrigonometricFunction(3));
        customSet.add(createTrigonometricFunction(5));
        return customSet;
    }


    public static void main(String[] args) {

        List<String> polynomials = polynomials(10, 5);

        System.out.println("Polynomials");
        for (String poly: polynomials)
            System.out.println(poly);

        System.out.println();
        List<String> trigonometric = trigonometricFunctions(10, 5);

        System.out.println("Trigonometric");
        for (String trig: trigonometric)
            System.out.println(trig);

        System.out.println();
        List<String> exponents = exponents(10, 5);

        System.out.println("Exponents");
        for (String exp: exponents)
            System.out.println(exp);



        List<String> testFunctions = getTestFunctions();
        System.out.println("TestFunctions");

        for (String test: testFunctions)
            System.out.println(test);


        System.out.println();
        System.out.println("BlackBoxes");
        List<BlackBoxTree> blackBoxTreeList = buildBlackBoxList(testFunctions);
        for (BlackBoxTree blackBoxTree: blackBoxTreeList) {
            System.out.println("f(x) = " + blackBoxTree);
            double x = getRandomIntegerInRange(MIN_VALUE, MAX_VALUE);
            System.out.println("f("+x+") = " + blackBoxTree.eval(x));
        }

    }



}
