package flowerpack;
public class Flower {
    
    private String flowerType;

    private float width;
    private float length;

    private double setosaProb;
    private double versiProb;
    private double virginProb;

    private String guess;

    /*****************************************************************
     * Constuctor to create a flower.
     * 
     * @param width a double representing the petal width.
     * @param len a double representing the petal length.
     * @param type a String representing the flower type.
     ****************************************************************/
    public Flower(float width, float len, String type){
        this.width = width;
        this.length = len;
        setFlowerType(type);
    }

    /*****************************************************************
     * This Method sets the petal length of a flower.
     * 
     * @param width1 a double representing the petal length.
     ****************************************************************/
    public void setLength(float len){
        length = len;
    }

    /*****************************************************************
     * This Method sets the petal length of a flower.
     * 
     * @param width1 a double representing the width.
     ****************************************************************/
    public void setWidth(float width1){
        width = width1;
    }

    /*****************************************************************
     * This Method sets the flower type of a flower.
     * 
     * @param type a String representing the type.
     ****************************************************************/
    public void setFlowerType(String type){

        if(type.equals("Iris-setosa")){
            flowerType = "setosa";
        }
        else if(type.equals("Iris-versicolor")){
            flowerType = "versicolor";
        }
        else if(type.equals("Iris-virginica")){
            flowerType = "virginica";
        }
        else{
            System.out.println("Not Valid Type! Try Again");
        }
    }

    /*****************************************************************
     * This Method gets the petal width of a flower.
     * 
     * @return a double represnting the flower width.
     ****************************************************************/
    public float getWidth(){
        return width;
    }

    /*****************************************************************
     * This Method gets the petal length of a flower.
     * 
     * @return a double represnting the flower petal length.
     ****************************************************************/
    public float getLength(){
        return length;
    }


    /*****************************************************************
     * This Method gets the flower type of a flower.
     * 
     * @return a string represnting the flower type.
     ****************************************************************/
    public String getFlowerType(){
        return flowerType;
    }

    /*****************************************************************
     * This Method sets the probability that this flower belongs to
     * the setosa class based on it petal width.
     * 
     * @param prob a double representing the probability.
     ****************************************************************/
    public void setSetosaProb(double prob){
        setosaProb = prob;
    }

    /*****************************************************************
     * This Method gets the setosa probability.
     * 
     * @return a double represnting the probability.
     ****************************************************************/
    public double getSetosaProb(){
        return setosaProb;
    }

    /*****************************************************************
     * This Method sets the probability that this flower belongs to
     * the versicolor class based on it petal width.
     * 
     * @param prob a double representing the probability.
     ****************************************************************/
    public void setVersiProb(double prob){
        versiProb = prob;
    }

    /*****************************************************************
     * This Method gets the versicolor probability.
     * 
     * @return a double represnting the probability.
     ****************************************************************/
    public double getVersiProb(){
        return versiProb;
    }

    /*****************************************************************
     * This Method sets the probability that this flower belongs to
     * the virginica class based on it petal width.
     * 
     * @param prob a double representing the probability.
     ****************************************************************/
    public void setVirginProb(double prob){
        virginProb = prob;
    }

    /*****************************************************************
     * This Method gets the virginica probability.
     * 
     * @return a double represnting the probability.
     ****************************************************************/
    public double getVirginProb(){
        return virginProb;
    }

    /*****************************************************************
     * This Method sets the algorithms guess flower type to the given 
     * type.
     * 
     * @param g a string representing the guess type.
     ****************************************************************/
    public void setGuess(String g){
        guess = g;
    }

    /*****************************************************************
     * This Method gets the flower guess type.
     * 
     * @return a string represnting the guess.
     ****************************************************************/
    public String getGuess(){
        return guess;
    }

    /*****************************************************************
     * This Method converts the flower object into a string.
     * 
     * @return a string represnting the flower.
     ****************************************************************/
    public String toString(){
        return "Petal Width: " + getWidth() + "\t Correct Flower Type: " + getFlowerType() + "\t Guess FlowerType: " + getGuess();
    }
}
