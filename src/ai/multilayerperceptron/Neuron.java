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

    private double prevDeltaBias;
    private double[] previousDeltaWeights;

    public Neuron(int prevLayerSize, TransferFunction func) {

        this.transferFunc = func;
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

    protected void updateSynapticWeightsAndBias(double learningRate, double momentum) {
        int size = synapticWeights.length;
        for (int i = 0; i < size; i++) {
            double deltaWeight = learningRate * delta * input[i];
            deltaWeight += momentum * previousDeltaWeights[i];
            synapticWeights[i] += deltaWeight;
            previousDeltaWeights[i] = deltaWeight;
        }
        double deltaBias = learningRate * delta + momentum * prevDeltaBias;
        bias += deltaBias;
        prevDeltaBias = deltaBias;
        delta = 0;
    }

    public double[] getSynapticWeights() {
        return synapticWeights;
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

    public void resetDelta() {
        this.delta = 0;
    }

    public TransferFunction getTransferFunc() {
        return transferFunc;
    }

    public void setTransferFunc(TransferFunction transferFunc) {
        this.transferFunc = transferFunc;
    }

//    protected void updateWeights(double learningRate) {
//        int size = synapticWeights.length;
//        double[] deltaWeights = new double[size];
//        for(int i = 0; i < size; i++){
//            
//            // tinh deltaWeight voi tham so learningRate va momentum
//            deltaWeights[i] = learningRate * delta * input[i];
////            deltaWeights[i] += momentum*previousDeltaWeights[i];
//            
//            // Cap nhat synapticWeight
//            synapticWeights[i] += deltaWeights[i];
//        }
//        
//        // cap nhap previousDeltaWeights
////        this.previousDeltaWeights = deltaWeights;
//        
//        // cap nhap bias
//        double deltaBias = learningRate * delta;
////        deltaBias += momentum*prevDeltaBias;
//        
//        this.bias += deltaBias;
////        this.prevDeltaBias = deltaBias;
//    }
}
