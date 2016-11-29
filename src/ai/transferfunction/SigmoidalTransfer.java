/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.transferfunction;

/**
 *
 * @author Chien Nguyen
 */
public class SigmoidalTransfer implements TransferFunction{
    
    public SigmoidalTransfer(){
       
    }
    
    @Override
    public double evalute(double value) {
        return 1 / (1 + Math.pow(Math.E, -value));
    }

    @Override
    /**
     * @params value = f(x)
     */
    public double evaluteDerivate(double value) {
        return value*(1 - value);
    }
    
}
