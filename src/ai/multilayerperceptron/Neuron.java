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
    private double output;
    private double bias;
    private double delta;
    private TransferFunction transferFunc;
    private double[] input;

    public Neuron(int prevLayerSize, TransferFunction func) {

        this.transferFunc = func;
        this.synapticWeights = new double[prevLayerSize];

        for (int i = 0; i < synapticWeights.length; i++) {
            synapticWeights[i] = Math.random() / 10000000000000.0;
        }
        this.bias = Math.random() / 10000000000000.0;
        
    }

    public double activate(double[] input) {
        this.input = input;
        double sum = 0;
        for (int i = 0, size = input.length; i < size; i++) {
            sum += input[i] * synapticWeights[i];
        }

        sum += bias;
        this.output = transferFunc.evalute(sum);
        return this.output;
    }

//    private void updateWeights(double [] deltaWeights){
//        assert(deltaWeights.length == synapticWeights.length);
//        for(int i = 0, size = synapticWeights.length; i < size; i++){
//            synapticWeights[i] += deltaWeights[i];
//        }
//        this.previousDeltaWeights = deltaWeights;
//    }
    public double getActivationDerivative() {
        return this.transferFunc.evaluteDerivate(output);
    }

    protected void updateSynapticWeightsAndBias(double learningRate) {
        int size = synapticWeights.length;
        for (int i = 0; i < size; i++) {
            double deltaWeight = learningRate * delta * input[i];
            synapticWeights[i] += deltaWeight;
        }
        bias += learningRate * delta;
        delta = 0;
    }

    public double[] getSynapticWeights() {
        return synapticWeights;
    }
    
    protected double getSynapticWeight(int index){
        return synapticWeights[index];
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getOutput() {
        return output;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void resetTempVar() {
        delta = 0;
        output = 0;
        for(int i = 0, size = input.length; i < size; i++){
            input[i] = 0;
        }
    }

    public TransferFunction getTransferFunc() {
        return transferFunc;
    }

    public void setTransferFunc(TransferFunction transferFunc) {
        this.transferFunc = transferFunc;
    }

}
