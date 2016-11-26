package Model;

/**
 * Created by hamhochoi on 05/11/2016.
 */
public class SigmoidFunction implements TranferFunction {

    public double sigmoidFunction(double output, double target, int p){  // p: neural in layer i-1
        double delta=0.0;

        delta = 1/(1 + Math.pow(Math.E,output));
        delta = delta*(1-delta)*(Math.pow(target,p) - Math.pow(output,p));
        delta = Math.pow(delta,1/p);

        return delta;

    }

    @Override
    public double evalute(double value)
    {
        return 1 / (1 + Math.pow(Math.E, - value));
    }

    @Override
    public double evaluteDerivate(double value)
    {
        return (value - Math.pow(value, 2));
    }
}
