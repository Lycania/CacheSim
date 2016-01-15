package application.model;

import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerModel;


public class SpinnerPowerOfTwo extends SpinnerNumberModel {
    Number value;
    int power ;

    public SpinnerPowerOfTwo() {
        this.power = 1;
        this.value = (int) 1;
    }
    
    @Override
    public Object getPreviousValue() {
        --power;
      
        if (power < 0) power = 1;
        return (int) Math.pow(2, power);
    }

    @Override
    public Object getNextValue() {
        return (int) Math.pow(2, ++power);
    }
    
    
}