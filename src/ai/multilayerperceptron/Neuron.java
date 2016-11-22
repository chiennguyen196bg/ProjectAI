/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.multilayerperceptron;

import ai.transferfunction.TransferFunction;

/**
 *
 * @author Chien Nguyen
 */
public class Neuron {

    private double[] synapticWeights;
    private double sum;
    private double bias;

    private double prevDeltaBias;
    private double [] previousDeltaWeights;
    private TransferFunction transferFunc;
    private double [] input;
    private int prevLayerSize;

    public Neuron(int prevLayerSize, TransferFunction func) {

        this.transferFunc = func;
        this.prevLayerSize = prevLayerSize;
        this.synapticWeights = new double[prevLayerSize];

        for (int i = 0; i < synapticWeights.length; i++) {
            synapticWeights[i] = Math.random() / 10000000000000.0;
        }
        this.bias = Math.random() / 10000000000000.0;
        this.previousDeltaWeights = new double[prevLayerSize];
        this.prevDeltaBias = 0;
    }

    public double activate(double[] input) {
        this.input = input;
        sum = 0;
        assert (input.length == synapticWeights.length);
        for (int i = 0, size = input.length; i < size; i++) {
            sum += input[i] + synapticWeights[i];
        }
        
        sum += bias;
        
        return transferFunc.evalute(sum);
    }

    private void updateWeights(double [] deltaWeights){
        assert(deltaWeights.length == synapticWeights.length);
        for(int i = 0, size = synapticWeights.length; i < size; i++){
            synapticWeights[i] += deltaWeights[i];
        }
        this.previousDeltaWeights = deltaWeights;
    }
    
    public double getActivationDerivative() {
        return this.transferFunc.evaluteDerivate(sum);
    }

    public double[] getSynapticWeights() {
        return synapticWeights;
    }

    public double getSynapticWeight(int index) {
        return synapticWeights[index];
    }

    public void setSynapticWeights(double[] synapticWeights) {
        this.synapticWeights = synapticWeights;
    }

    public double getSum() {
        return sum;
    }
    
    
    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
    
//    public void setSum(double sum) {
//        this.sum = sum;
//    }
    public TransferFunction getTransferFunc() {
        return transferFunc;
    }

    public void setTransferFunc(TransferFunction transferFunc) {
        this.transferFunc = transferFunc;
    }

    public void updateWeights(double delta, double learningRate, double momentum) {
        double[] deltaWeights = new double[prevLayerSize];
        for(int i = 0, size = synapticWeights.length; i < size; i++){
            
            // tinh deltaWeight voi tham so learningRate va momentum
            deltaWeights[i] = learningRate * delta * input[i];
            deltaWeights[i] += momentum*previousDeltaWeights[i];
            
            // Cap nhat synapticWeight
            synapticWeights[i] += deltaWeights[i];
        }
        
        // cap nhap previousDeltaWeights
        this.previousDeltaWeights = deltaWeights;
        
        // cap nhap bias
        double deltaBias = learningRate * delta;
        deltaBias += momentum*prevDeltaBias;
        
        this.bias += deltaBias;
        this.prevDeltaBias = deltaBias;
    }
    
     

}
