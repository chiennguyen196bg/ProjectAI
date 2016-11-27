/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.multilayerperceptron;

import ai.transferfunction.SigmoidalTransfer;
import ai.transferfunction.TransferFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chien Nguyen
 */
public class MultiLayerPerceptron {
    
    private ArrayList<Layer> layers;
    private int nLayers;
    private int inputSize;
    
    public MultiLayerPerceptron(int[] layers, TransferFunction func) {
        this.inputSize = layers[0];
        this.layers = new ArrayList<>();
        for (int i = 1, size = layers.length; i < size; i++) {
            this.layers.add(new Layer(layers[i - 1], layers[i], func));
        }
        this.nLayers = this.layers.size();
    }
    
    public MultiLayerPerceptron(int[] layers) {
        this(layers, new SigmoidalTransfer());
    }
    
    public double[] execute(double[] input) {
        for (Layer layer : layers) {
            input = layer.evaluate(input);
        }
        return input;
    }

    // khoi tao mang delta
    private double[][] initDeltas() {
        double[][] deltas = new double[nLayers][];
        for (int i = 0; i < nLayers; i++) {
            deltas[i] = new double[layers.get(i).size()];
        }
        return deltas;
    }

    //Khoi tao mang delta weights
    private double[][][] initDeltaWeights() {
        double[][][] deltaWeights = new double[nLayers][][];
        for (int i = 0; i < nLayers; i++) {
            Layer layer = layers.get(i);
            deltaWeights[i] = new double[layer.size()][layer.getPrevSize()];
        }
        return deltaWeights;
    }

    

    private void computeDeltaOfNeuron(double[] targetOutput) {
        // tinh delta cho output layer
        Layer lastLayer = layers.get(nLayers - 1);
        for (int i = 0, size = lastLayer.size(); i < size; i++) {
            Neuron neuron = lastLayer.getNeuron(i);
            double e = targetOutput[i] - neuron.getOutput();
            neuron.setDelta(e * neuron.getActivationDerivative());
        }
        //tinh delta cho hidden layer
        for(int i = nLayers - 2; i >= 0; i--){
            Layer layer = layers.get(i);
            Layer nextLayer = layers.get(i + 1);
            for(int iNeuron = 0, size1 = layer.size(); iNeuron < size1; iNeuron++){
                double e = 0;
                for(Neuron neuron : nextLayer.getNeurons()){
                    e += neuron.getDelta() * neuron.getSynapticWeights()[iNeuron];
                }
                Neuron neuron = layer.getNeuron(iNeuron);
                neuron.setDelta(e * neuron.getActivationDerivative());
            }
        }
        
    }
    
    public double backPropagate(
            double[] example, double[] targetOutput,
            double learningRate, double momentum
    ) {
        
        double output[] = execute(example);
        
        computeDeltaOfNeuron(targetOutput);
        
        // update weight and bias
        for(Layer layer : layers){
            for(Neuron neuron : layer.getNeurons()){
                neuron.updateSynapticWeightsAndBias(learningRate, momentum);
            }
        }

        return evaluteError(output, targetOutput);
    }

//    public double batchBackPropagation(ArrayList<double[]> examples, ArrayList<double[]> desiredOutputs, double learningRate) {
//        assert (examples.size() == desiredOutputs.size());
//        int size = examples.size();
//
//        double[][] deltas = initDeltas();
//        double[][][] deltaWeights = initDeltaWeights();
//        double err = 0;
//        for (int k = 0; k < size; k++) {
//            double[] example = examples.get(k);
//            double[] desiredOutput = desiredOutputs.get(k);
//
//            double output[] = execute(example);
//            computeDeltas(deltas, desiredOutput);
//            computeDeltaWeights(deltaWeights, deltas, learningRate);
//            err += evaluteError(output, desiredOutput);
//        }
//
//        //update weights
//        updateWeights(deltaWeights);
//        return err/size;
//    }
//    private void updateWeights(double[][][] deltaWeights) {
//        for (int iLayer = 0; iLayer < nLayers; iLayer++) {
//            layers.get(iLayer).updateWeights(deltaWeights[iLayer]);
//        }
//    }
//    
//    private void updateWeights(double[][][] deltaWeights, double momentum) {
//        for (int iLayer = 0; iLayer < nLayers; iLayer++) {
//            layers.get(iLayer).updateWeights(deltaWeights[iLayer], momentum);
//        }
//    }
    protected double evaluteError(double output[], double targetOutput[]) {
        double error = 0;
        for (int i = 0, size = output.length; i < size; i++) {
            double e = targetOutput[i] - output[i];
            error += e * e;
        }
        return error / 2;
    }
    
    public void resetDelta(){
        for(Layer layer : layers){
            for(Neuron neuron : layer.getNeurons()){
                neuron.resetDelta();
            }
        }
    }
    
    public void saveNetwork(String patch) {
        try (FileWriter out = new FileWriter(patch);
                BufferedWriter bw = new BufferedWriter(out)) {
            
            bw.write(String.valueOf(inputSize));
            bw.write(" ");
            for (Layer layer : layers) {
                bw.write(String.valueOf(layer.size()));
                bw.write(" ");
            }
            bw.newLine();
            
            for (Layer layer : layers) {
                for (Neuron neuron : layer.getNeurons()) {
                    for (double d : neuron.getSynapticWeights()) {
                        bw.write(String.valueOf(d));
                        bw.write(" ");
                    }
                    bw.write(String.valueOf(neuron.getBias()));
                    bw.newLine();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MultiLayerPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MultiLayerPerceptron loadNetwork(String patch) {
        try (FileReader in = new FileReader(patch);
                BufferedReader br = new BufferedReader(in)) {
            String str;
            ArrayList<Integer> layer = new ArrayList<>();
            str = br.readLine();
            StringTokenizer st = new StringTokenizer(str, " ");
            while (st.hasMoreTokens()) {
                String tempStr = st.nextToken();
                layer.add(Integer.parseInt(tempStr));
            }
            
            int[] intLayer = new int[layer.size()];
            for (int i = 0; i < intLayer.length; i++) {
                intLayer[i] = layer.get(i);
            }
            
            MultiLayerPerceptron mlp = new MultiLayerPerceptron(intLayer);
            
            for (Layer _layer : mlp.layers) {
                for (Neuron neuron : _layer.getNeurons()) {
                    double[] weights = neuron.getSynapticWeights();
                    st = new StringTokenizer(br.readLine(), " ");
                    for (int i = 0, size = weights.length; i < size; i++) {
                        weights[i] = Double.parseDouble(st.nextToken());
                    }
                    neuron.setBias(Double.parseDouble(st.nextToken()));
                }
            }
            return mlp;
        } catch (IOException ex) {
            Logger.getLogger(MultiLayerPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
