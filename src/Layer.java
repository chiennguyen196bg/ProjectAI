package Model;

/**
 * Created by hamhochoi on 03/11/2016.
 */
public class Layer {
    private Neural[] neurals;
    private int leng;

    public Layer(int leng, int prevLeng){

        this.leng = leng;
        neurals = new Neural[leng];

        for (int i=0; i<leng; i++){
            neurals[i] = new Neural(prevLeng);
        }

    }

    public int getLeng() {
        return leng;
    }

    public void setLeng(int leng) {
        this.leng = leng;
    }

    public Neural[] getNeurals() {
        return neurals;
    }

    public void setNeurals(Neural[] neurals) {
        this.neurals = neurals;
    }
}
