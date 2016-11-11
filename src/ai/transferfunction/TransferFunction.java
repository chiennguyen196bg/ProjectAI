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
public interface TransferFunction {

    public double evalute(double value);

    public double evaluteDerivate(double value);
}
