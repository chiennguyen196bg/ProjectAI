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
public class MultilayerPerceptron {

    private ArrayList<Layer> layers;
    private int nLayers;
    private int inputSize;

    public MultilayerPerceptron(int[] layers, TransferFunction func) {
        this.inputSize = layers[0];
        this.layers = new ArrayList<>();
        for (int i = 1, size = layers.length; i < size; i++) {
            this.layers.add(new Layer(layers[i - 1], layers[i], func));
        }
        this.nLayers = this.layers.size();
    }

    public MultilayerPerceptron(int[] layers) {
        this(layers, new SigmoidalTransfer());
    }

    public double[] execute(double[] input) {

        assert (input.length == inputSize);

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

    // tinh delta
    private void computeDeltas(double[][] deltas, double[] desiredOutput) {

        // tinh delta cho output layer
        int lastLayer = nLayers - 1;
        double[] output = layers.get(lastLayer).getOutput();
        for (int i = 0; i < layers.get(lastLayer).size(); i++) {

            double error = desiredOutput[i] - output[i];
            Neuron neuron = layers.get(lastLayer).getNeuron(i);
            deltas[lastLayer][i] = neuron.getActivationDerivative() * error;
        }

        // tinh delta cho hidden layer
        for (int indexLayer = nLayers - 2; indexLayer > 0; indexLayer--) {
            Layer layer = layers.get(indexLayer);
            for (int indexNeuron = 0; indexNeuron < layer.size(); indexNeuron++) {
                double error = 0;
                Layer nextLayer = layers.get(indexLayer + 1);
                for (int iNextLayerNeuron = 0; iNextLayerNeuron < nextLayer.size(); iNextLayerNeuron++) {
                    error += nextLayer.getNeuron(iNextLayerNeuron).getSynapticWeight(indexNeuron)
                            * deltas[indexLayer + 1][iNextLayerNeuron];
                }
                deltas[indexLayer][indexNeuron] = error * layer.getNeuron(indexNeuron).getActivationDerivative();
            }
        }
    }

    //Tinh deltaWeight
    private void computeDeltaWeights(double[][][] deltaWeights, double[][] deltas, double learningRate) {
        for (int indexLayer = 0; indexLayer < nLayers; indexLayer++) {
            Layer layer = layers.get(indexLayer);
            int inputSize = layer.getPrevSize();
            for (int indexNeuron = 0; indexNeuron < layer.size(); indexNeuron++) {
                for (int indexWeight = 0; indexWeight < inputSize; indexWeight++) {
                    deltaWeights[indexLayer][indexNeuron][indexWeight]
                            += learningRate * deltas[indexLayer][indexNeuron]
                            * layer.getInput()[indexWeight];
                }
            }
        }
    }

    public double backPropagate(double[] example, double[] desiredOutput, double learningRate) {
        assert (example.length == inputSize);

        double[][] deltas = initDeltas();
        double[][][] deltaWeights = initDeltaWeights();

        double output[] = execute(example);

        computeDeltas(deltas, desiredOutput);
        computeDeltaWeights(deltaWeights, deltas, learningRate);

        updateWeights(deltaWeights);
        
        return evaluteError(output, desiredOutput);
    }

    public double batchBackPropagation(ArrayList<double[]> examples, ArrayList<double[]> desiredOutputs, double learningRate) {
        assert (examples.size() == desiredOutputs.size());
        int size = examples.size();

        double[][] deltas = initDeltas();
        double[][][] deltaWeights = initDeltaWeights();
        double err = 0;
        for (int k = 0; k < size; k++) {
            double[] example = examples.get(k);
            double[] desiredOutput = desiredOutputs.get(k);

            double output[] = execute(example);
            computeDeltas(deltas, desiredOutput);
            computeDeltaWeights(deltaWeights, deltas, learningRate);
            err += evaluteError(output, desiredOutput);
        }

        //update weights
        updateWeights(deltaWeights);
        return err;
    }

    private void updateWeights(double[][][] deltaWeights) {
        for (int i = 0; i < nLayers; i++) {
            layers.get(i).updateWeights(deltaWeights[i]);
        }
    }
    
    protected double evaluteError(double output[], double desiredOutput[]){
        double error = 0;
        for(int i = 0, size = output.length; i < size; i++){
            double e = desiredOutput[i] - output[i];
            error += e*e;
        }
        return error;
    }
    
    public void saveNetwork(String patch){
        try (FileWriter out = new FileWriter(patch); 
                BufferedWriter bw = new BufferedWriter(out)) {
            
            bw.write(String.valueOf(inputSize));
            bw.write(" ");
            for(Layer layer : layers){
                bw.write(String.valueOf(layer.size()));
                bw.write(" ");
            }
            bw.newLine();
            
            for(Layer layer : layers){
                for(Neuron neuron : layer.getNeurons()){
                    for(double d : neuron.getSynapticWeights()){
                        bw.write(String.valueOf(d));
                        bw.write(" ");
                    }
                    bw.newLine();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MultilayerPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MultilayerPerceptron loadNetwork(String patch){
        try (FileReader in = new FileReader(patch); 
                BufferedReader br = new BufferedReader(in)) {
            String str;
            ArrayList<Integer> layer = new ArrayList<>();
            str = br.readLine();
            StringTokenizer st = new StringTokenizer(str, " ");
            while(st.hasMoreTokens()){
                String tempStr = st.nextToken();
                layer.add(Integer.parseInt(tempStr));
            }
            
            
            int[] intLayer = new int[layer.size()];
            for(int i = 0; i < intLayer.length; i++){
                intLayer[i] = layer.get(i);
            }
            
            MultilayerPerceptron mlp = new MultilayerPerceptron(intLayer);
            
            for(Layer _layer : mlp.layers){
                for(Neuron neuron : _layer.getNeurons()){
                    double[] weights = neuron.getSynapticWeights();
                    st = new StringTokenizer(br.readLine(), " ");
                    int k = 0;
                    while(st.hasMoreTokens()){
                        weights[k] = Double.parseDouble(st.nextToken());
                        k++;
                    }
                }
            }
            return mlp;
        } catch (IOException ex) {
            Logger.getLogger(MultilayerPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
