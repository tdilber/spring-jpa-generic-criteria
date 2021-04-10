package com.beyt.util.field.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class IntegerFieldHelper implements IFieldHelper<Integer> {
    @Override
    public Integer fillRandom() {
        return random.nextInt();
    }

    @Override
    public Integer fillValue(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return value;
    }
}
