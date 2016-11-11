/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.multilayerperceptron;

import ai.transferfunction.TransferFunction;
import java.util.ArrayList;

/**
 *
 * @author Chien Nguyen
 */
public class Layer {

    private ArrayList<Neuron> neurons;
    private int size, prevSize;
    private double[] output;
    private double[] input;

    public Layer(int prevSize, int size, TransferFunction func) {

        this.size = size;
        this.prevSize = prevSize;
        
        output = new double[size];
        
        this.neurons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.neurons.add(new Neuron(prevSize, func));
        }
    }

    public double[] evaluate(double[] input) {
        assert (input.length == prevSize);
        this.input = input;
        
        for (int iNeuron = 0; iNeuron < size; iNeuron++) {
            output[iNeuron] = neurons.get(iNeuron).activate(input);
        }
        return output;
    }

    public double[] getInput() {
        return input;
    }

    
    
    public void updateWeights(double[][] deltaWeights){
        assert(deltaWeights.length == size);
        for(int i = 0; i < size; i++){
            neurons.get(i).updateWeights(deltaWeights[i]);
        }
    }

    //setter and getter
    public double[] getOutput() {
        return output;
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public Neuron getNeuron(int index) {
        return this.neurons.get(index);
    }

    public void setNeurons(ArrayList<Neuron> neurons) {
        this.neurons = neurons;
    }

    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrevSize() {
        return prevSize;
    }

    public void setPrevSize(int prevSize) {
        this.prevSize = prevSize;
    }

}
