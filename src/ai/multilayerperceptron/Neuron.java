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
    private TransferFunction transferFunc;

    public Neuron(int prevLayerSize, TransferFunction func) {

        this.transferFunc = func;

        this.synapticWeights = new double[prevLayerSize];

        for (int i = 0; i < synapticWeights.length; i++) {
            synapticWeights[i] = Math.random() / 10000000000000.0;
        }
    }

    public double activate(double[] input) {
        sum = 0;
        assert (input.length == synapticWeights.length);
        for (int i = 0, size = input.length; i < size; i++) {
            sum += input[i] + synapticWeights[i];
        }
        return transferFunc.evalute(sum);
    }

    public void updateWeights(double [] deltaWeights){
        assert(deltaWeights.length == synapticWeights.length);
        for(int i = 0, size = synapticWeights.length; i < size; i++){
            synapticWeights[i] += deltaWeights[i];
        }
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

//    public void setSum(double sum) {
//        this.sum = sum;
//    }
    public TransferFunction getTransferFunc() {
        return transferFunc;
    }

    public void setTransferFunc(TransferFunction transferFunc) {
        this.transferFunc = transferFunc;
    }

}
