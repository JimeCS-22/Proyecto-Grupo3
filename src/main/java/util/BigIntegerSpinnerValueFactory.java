package ucr.algoritmos.proyecto.util;

import javafx.scene.control.SpinnerValueFactory;

import java.math.BigInteger;

public class BigIntegerSpinnerValueFactory extends SpinnerValueFactory<BigInteger> {

    private final BigInteger min;
    private final BigInteger max;
    private final BigInteger step;

    public BigIntegerSpinnerValueFactory(BigInteger min, BigInteger max, BigInteger initial, BigInteger step) {
        this.min = min;
        this.max = max;
        this.step = step;
        setValue(initial);
    }

    @Override
    public void decrement(int steps) {
        BigInteger newValue = getValue().subtract(step.multiply(BigInteger.valueOf(steps)));
        if (newValue.compareTo(min) >= 0) {
            setValue(newValue);
        }
    }

    @Override
    public void increment(int steps) {
        BigInteger newValue = getValue().add(step.multiply(BigInteger.valueOf(steps)));
        if (newValue.compareTo(max) <= 0) {
            setValue(newValue);
        }
    }
}