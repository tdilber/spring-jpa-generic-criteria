package com.beyt.util.field.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class DoubleFieldHelper implements IFieldHelper<Double> {
    @Override
    public Double fillRandom() {
        return random.nextDouble();
    }

    @Override
    public Double fillValue(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return value + "d";
    }
}
