package flowerpack;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
/*************************************************************
 * Flower Probability Calculator: Uses Iris Database to train
 * an algorithm to recognize flowers based on petal width and 
 * length.
 * Outputs results that show what the algorithm "guessed" and
 * if it was correct.
 * 
 * @author Mariah Bray
 * @version Oct 20th, 2021
 ************************************************************/

public class FlowerDataBase {
    
    //iris data set
    File myfile = new File("IrisFlowerLearner/src/flowerpack/data.txt");
    
    //an array list for each class contaning data to train
    List<Flower> setosaList = new ArrayList<>();
    List<Flower> versicolorList = new ArrayList<>();
    List<Flower> virginicaList = new ArrayList<>();
    
    // counts of the total amount of flowers in each list
    int setosaCount = 0;
    int versiCount = 0;
    int virginCount = 0;

    // an array list for each class contaning data to test
    List<Flower> setosaTestList = new ArrayList<>();
    List<Flower> versicolorTestList = new ArrayList<>();
    List<Flower> virginicaTestList = new ArrayList<>();

    // the calculated mean for each class
    double setosaWidthMean;
    double versiWidthMean;
    double virginWidthMean;
    double setosaLengthMean;
    double versiLengthMean;
    double virginLengthMean;

    // the calculated standard deviation for each class
    double setosaStnDivW;
    double versiStnDivW;
    double virginStnDivW;
    double setosaStnDivL;
    double versiStnDivL;
    double virginStnDivL;

    
    /*****************************************************************
     * This Method does a variety of actions. First, the Iris Data is
     * read in from a text file. Then 60% of the data is put in lists
     * for training and 40% for testing. Next, the mean, standard 
     * deviation, guasian probability, and naive bayes methods are 
     * called. 
     ****************************************************************/
    public void readFlowerData(){
        
        try{
            Scanner sc = new Scanner(myfile);  
            sc.useDelimiter(",");   //sets the delimiter pattern 

            do{
                sc.next();
                sc.next();
                float len = sc.nextFloat();
                float width = sc.nextFloat();
                String type = sc.next();
                Flower f1 = new Flower(width, len, type);

                if(f1.getFlowerType().equals("setosa")){
                    setosaList.add(f1);
                    ++setosaCount;
                }
                if(f1.getFlowerType().equals("versicolor")){
                    versicolorList.add(f1);
                    ++versiCount;
                }
                if(f1.getFlowerType().equals("virginica")){
                    virginicaList.add(f1);
                    ++virginCount;
                }
            }while (sc.hasNext());

        } catch(FileNotFoundException e){
            System.out.println("file not found");
        }   

        for(int i = 0; i < 20; ++i){
            setosaTestList.add(setosaList.get(i));
            setosaList.remove(i);
            --setosaCount;

            versicolorTestList.add(versicolorList.get(i));
            versicolorList.remove(i);
            --versiCount;

            virginicaTestList.add(virginicaList.get(i));
            virginicaList.remove(i);
            --virginCount;
        }
        setMeans(); 
        setStnDiv();
        calcGuasVals(setosaTestList);
        calcGuasVals(versicolorTestList);
        calcGuasVals(virginicaTestList);
        bayesAlgo();
        
        results(setosaTestList);
        System.out.print("\n");
        results(virginicaTestList);
        System.out.print("\n");
        results(versicolorTestList);
    }
    
    /*****************************************************************
     * This Method adds the petal width for each flower in the class 
     * of flower given.
     * 
     * @param l an Array list of Flower objects representing a class
     * of flower.
     * 
     * @return a array of doubles represnting the total of flower 
     * widths and lengths.
     ****************************************************************/
    public double[] calcMean(List<Flower> l){
        double totalwidth = 0;
        double totallength = 0;
        double[] stats = new double[2];
        for(Flower f : l){
            totalwidth = totalwidth + f.getWidth();
            totallength = totallength + f.getLength();
            stats[0] = totalwidth;
            stats[1] = totallength;
        }
        return stats;

    }

    /*****************************************************************
     * This Method calculates the mean for each of the 3 flower 
     * classes in the Iris dataset. 
     ****************************************************************/
    public void setMeans(){
        setosaWidthMean = calcMean(setosaList)[0]/setosaCount;
        versiWidthMean = calcMean(versicolorList)[0]/versiCount;
        virginWidthMean = calcMean(virginicaList)[0]/virginCount;
        setosaLengthMean = calcMean(setosaList)[1]/setosaCount;
        versiLengthMean = calcMean(versicolorList)[1]/versiCount;
        virginLengthMean = calcMean(virginicaList)[1]/virginCount;
    }

    /*****************************************************************
     * This Method calculates the variance for the flower class given.
     * 
     * @param l an Array list of Flower objects representing a class
     * of flower.
     * 
     * @param width a boolean representing if we are caluculating width variance.
     * 
     * @return a double represnting the variance.
     ****************************************************************/
    public double calcVariance(List<Flower> l, double mean, boolean width){
        double total = 0;
        for(Flower f : l){
            float flow;
            if(width){
                flow = f.getWidth();
            }
            else{
                flow = f.getLength();
            }
            total = total + ((flow - mean)*(flow - mean));
        }
        return total;
    }

    /*****************************************************************
     * This Method calculates the standard deviation for each of the 
     * 3 flower classes in the Iris dataset. 
     ****************************************************************/
    public void setStnDiv(){
        setosaStnDivW = Math.sqrt(calcVariance(setosaList,setosaWidthMean,true)/(setosaCount - 1));
        versiStnDivW = Math.sqrt(calcVariance(versicolorList,versiWidthMean,true)/(versiCount - 1));
        virginStnDivW= Math.sqrt(calcVariance(virginicaList,virginWidthMean,true)/(virginCount - 1)); 
        setosaStnDivL = Math.sqrt(calcVariance(setosaList,setosaLengthMean,false)/(setosaCount - 1));
        versiStnDivL = Math.sqrt(calcVariance(versicolorList,versiLengthMean,false)/(versiCount - 1));
        virginStnDivL= Math.sqrt(calcVariance(virginicaList,virginLengthMean,false)/(virginCount - 1));
    }

    /*****************************************************************
     * This Method calculates the probability that a certain flower
     * belongs to a certain class given the flowers width and the
     * class's mean and standard deviation.
     * 
     * @param w a double representing the flowers width.
     * 
     * @param mean a double representing the class's mean.
     * 
     * @param stdev a double representing the class's standard 
     * deviation.
     * 
     * @return a double represnting the probability.
     ****************************************************************/
    public double calcGuasProbab(double w, double mean,double stdev){
        double power = (-.5)*((w-mean)*(w-mean))/(stdev * stdev);
        double exp = Math.exp(power);
        return (1 / (Math.sqrt(2 * Math.PI) * stdev)) * exp;
    }
    
    /*****************************************************************
     * This Method assigns each flower in the testing portion of the
     * data a Guasian probability for each class.
     ****************************************************************/
    public void calcGuasVals(List<Flower> list){
        for(Flower f : list){
            double widthProb = calcGuasProbab(f.getWidth(), setosaWidthMean, setosaStnDivW);
            double lengthProb = calcGuasProbab(f.getLength(), setosaLengthMean, setosaStnDivW);
            f.setSetosaProb((.4*lengthProb) + (.6*widthProb));
        }
        for(Flower f : list){
            double widthProb = calcGuasProbab(f.getWidth(), versiWidthMean, versiStnDivW);
            double lengthProb = calcGuasProbab(f.getLength(), versiLengthMean, versiStnDivL);
            f.setVersiProb((.4*lengthProb) + (.6*widthProb));
        }
        for(Flower f : list){
            double widthProb = calcGuasProbab(f.getWidth(), virginWidthMean, virginStnDivW);
            double lengthProb = calcGuasProbab(f.getLength(), virginLengthMean, virginStnDivL);
            f.setVirginProb((.4*lengthProb) + (.6*widthProb));
        }
    }

    /*****************************************************************
     * This Method checks the guasian probability a flower has for 
     * each class and assigns it a "guess" flower type based on the
     * highest probability.
     ****************************************************************/
    public void bayesAlgo(){
        for(Flower f : setosaTestList){
            if (f.getSetosaProb() >= f.getVersiProb() && f.getSetosaProb() >= f.getVirginProb()){
                f.setGuess("setosa");
            }
            else if (f.getVersiProb() >= f.getSetosaProb() && f.getVersiProb() >= f.getVirginProb()){
                f.setGuess("versicolor");
            }
            else if ( f.getVirginProb() >= f.getVersiProb() && f.getVirginProb() >= f.getSetosaProb()){
                f.setGuess("virginica");
            }
            else{
                f.setGuess("cant guess");
            }
        }
        for(Flower f : versicolorTestList){
            if (f.getSetosaProb() >= f.getVersiProb() && f.getSetosaProb() >= f.getVirginProb()){
                f.setGuess("setosa");
            }
            else if (f.getVersiProb() >= f.getSetosaProb() && f.getVersiProb() >= f.getVirginProb()){
                f.setGuess("versicolor");
            }
            else if ( f.getVirginProb() >= f.getVersiProb() && f.getVirginProb() >= f.getSetosaProb()){
                f.setGuess("virginica");
            }
            else{
                f.setGuess("cant guess");
            }
        }
        for(Flower f : virginicaTestList){
            if (f.getSetosaProb() >= f.getVersiProb() && f.getSetosaProb() >= f.getVirginProb()){
                f.setGuess("setosa");
            }
            else if (f.getVersiProb() >= f.getSetosaProb() && f.getVersiProb() >= f.getVirginProb()){
                f.setGuess("versicolor");
            }
            else if ( f.getVirginProb() >= f.getVersiProb() && f.getVirginProb() >= f.getSetosaProb()){
                f.setGuess("virginica");
            }
            else{
                f.setGuess("cant guess");
            }
        }
    }

    /*****************************************************************
     * This determines if the Bayes guesses for each flower we correct
     * and outputs the results in a readable fashion.
     * 
     * @param l an array list of flowers of a certain class.
     ****************************************************************/
    public void results(List<Flower> l){
        String type = "";
        int countC = 0;
        int countI = 0;
        List<Flower> correct = new ArrayList<>();
        List<Flower> incorrect = new ArrayList<>();
        for(Flower f : l){
            if (f.getGuess().equals(f.getFlowerType())){
                correct.add(f);
                type = f.getFlowerType();
                ++countC;
            }
            else{
                incorrect.add(f);
                ++countI;
            }
        }

        System.out.println(""+ type + " results: \n");
        System.out.println("The following (" + countC +") flowers were correctly Identified as " + type + ":\n");
        for(Flower f : correct){
            System.out.println(f.toString());
        }
        System.out.println("\nThe following (" + countI +") flowers were incorrectly Identified: \n");
        for(Flower f : incorrect){
            System.out.println(f.toString());
        }

    }

    public static void main(String[] args){
        FlowerDataBase fsb1 = new FlowerDataBase();
        fsb1.readFlowerData();
    }
}