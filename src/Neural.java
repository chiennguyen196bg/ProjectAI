package Model;

/**
 * Created by hamhochoi on 03/11/2016.
 */
public class Neural {
    private double value;
    private double[] weight;
    private double sigma;
    private double bias;

    public Neural(int prevLayerLength){
        this.weight = new double[prevLayerLength];
        this.value     = Math.random()/100000000000.0;
        this.bias     = Math.random()/100000000000.0;
        this.sigma     = Math.random()/100000000000.0;

        for (int i=0; i<prevLayerLength; i++){
            weight[i] = Math.random()/100000000000.0;
        }
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public void setWeight(int i, double weight){
        this.weight[i] = weight;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double delta) {
        this.bias = delta;
    }
}
