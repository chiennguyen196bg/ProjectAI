package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by hamhochoi on 25/11/2016.
 */
public class MultiLayerPerceptron {

    // Cong viec:
    //1. execute()
    //2, backPropagate


    private Layer[] layer=null;
    private TranferFunction tranFunct = new SigmoidFunction();
    private double learningRate = 0.6;

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public Layer[] getLayer() {
        return layer;

    }

    public TranferFunction getTranFunct() {
        return tranFunct;
    }

    public void setTranFunct(TranferFunction tranFunct) {
        this.tranFunct = tranFunct;
    }

    public void setLayer(Layer[] layer) {
        this.layer = layer;
    }

    public MultiLayerPerceptron(int numLayers[], double learningRate, TranferFunction tranFunct){

        this.layer = new Layer[numLayers.length];
        this.tranFunct = tranFunct;
        this.learningRate = learningRate;

        for (int i=0; i<layer.length; i++){
            if (i==0){
                layer[i] = new Layer(numLayers[i],0);
            }
            else{
                layer[i] = new Layer(numLayers[i], numLayers[i-1]);
            }
        }


    }

    public double[] execute(double[] input){

        double[] output = new double[layer[layer.length-1].getLeng()];

        // Load INPUT layer
        for (int i=0; i<layer[0].getLeng(); i++){
            layer[0].getNeurals()[i].setValue(input[i]);
        }

        // Execute Hidden Layer and Output Layer

        for (int i=1; i<layer.length; i++){ // Xet tung layer
            for (int j=0; j<layer[i].getLeng(); j++){   // Xet tung Neural trong tung Layer

                double newValue = 0.0;
                for (int k=0; k<layer[i-1].getLeng(); k++){     // Xet tung Neural trong Layer ngay truoc no
                    newValue = newValue + layer[i].getNeurals()[j].getWeight()[k] * layer[i-1].getNeurals()[k].getValue();
                }

                newValue = newValue + layer[i].getNeurals()[j].getBias();
                layer[i].getNeurals()[j].setValue(tranFunct.evalute(newValue));     // value = f(u)
            }
        }

        // Get OUTPUT layer
        for (int i=0; i<output.length; i++){
            output[i] = layer[layer.length-1].getNeurals()[i].getValue();
        }


        return output;
    }

    public double backPropagate(double[] input, double[] output){

        double error = 0.0;
        double[] newOutput;
        newOutput = execute(input);

        // Truyen nguoc OUTPUT layer
        for (int i=0; i<output.length; i++){
            error = output[i] - newOutput[i];

            double sigma = error * tranFunct.evaluteDerivate(newOutput[i]);
            layer[layer.length-1].getNeurals()[i].setSigma(sigma);
        }

        // Truyen nguoc HIDDEN Layer
        for (int i= layer.length-2 ; i>=0; i--){ // Xet tung lop con lai theo chieu nguoc
            for (int j=0; j<layer[i].getLeng(); j++){       // Xet tung Neural trong cac lop con lai
                double sigma=0;
                for (int k=0; k<layer[i+1].getLeng(); k++){
                    sigma = sigma + layer[i+1].getNeurals()[k].getSigma() * layer[i+1].getNeurals()[k].getWeight()[j];
                }
                sigma = sigma * tranFunct.evaluteDerivate(layer[i].getNeurals()[j].getValue());
                layer[i].getNeurals()[j].setSigma(sigma);
            }


            // Cap nhap cac gia tri cua cac Neural trong Layer i
            for (int j=0; j<layer[i+1].getLeng(); j++){
                // Cap nhap trong so cua tung Neural tren Layer dang xet toi cac Neural tren Layer ngay truoc no
                for (int k=0; k<layer[i].getLeng(); k++){
                    double weight = layer[i+1].getNeurals()[j].getWeight()[k] + learningRate * layer[i+1].getNeurals()[j].getSigma() * layer[i].getNeurals()[k].getValue();
                    layer[i+1].getNeurals()[j].setWeight(k,weight);

                }

                // Cap nhap bias cua tung Neural trong Layer i
                double bias = layer[i+1].getNeurals()[j].getBias() + learningRate * layer[i+1].getNeurals()[j].getSigma();
                layer[i+1].getNeurals()[j].setBias(bias);
            }



        }

        // Calculate error after each time backPropagate called

        error = 0.0;
        for (int i=0; i<output.length; i++){
            error = error + Math.abs(output[i] - newOutput[i]);
        }

        error = error/output.length;
        return error;
    }

    public void saveNetwork(String patch){
        try (FileWriter out = new FileWriter(patch);
             BufferedWriter bw = new BufferedWriter(out)) {

//            bw.write(String.valueOf(layer[0].getLeng()));
//            bw.write(" ");
            for(Layer layers : layer){
                bw.write(String.valueOf(layers.getLeng()));
                bw.write(" ");
            }
            bw.newLine();

            for(Layer layers : layer){
                for(Neural neural : layers.getNeurals()){
                    for(double d : neural.getWeight()){
                        bw.write(String.valueOf(d));
                        bw.write(" ");
                    }
                    bw.write(String.valueOf(neural.getBias()));
                    bw.newLine();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(MultiLayerPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static MultiLayerPerceptron loadNetwork(String patch){
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

            MultiLayerPerceptron mlp = new MultiLayerPerceptron(intLayer, 0.6, new SigmoidFunction());

            for(Layer _layer : mlp.layer){
                for(Neural neuron : _layer.getNeurals()){
                    double[] weights = neuron.getWeight();
                    st = new StringTokenizer(br.readLine(), " ");
//                    int k = 0;
//                    while(st.hasMoreTokens()){
//                        weights[k] = Double.parseDouble(st.nextToken());
//                        k++;
//                    }
                    for(int i = 0, size = weights.length; i < size; i++){
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
