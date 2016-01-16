package application.model;

import javax.swing.SpinnerNumberModel;


public class SpinnerPowerOfTwo extends SpinnerNumberModel {
    Number value;
    int power ;

    public SpinnerPowerOfTwo() {
        this.power = 0;
        this.value = (int) 1;
    }
    
    @Override
    public Object getPreviousValue() {
        --power;
      
        if (power < 0) power = 0;
        return (int) Math.pow(2, power);
    }

    @Override
    public Object getNextValue() {
        return (int) Math.pow(2, ++power);
    }
    
    
}