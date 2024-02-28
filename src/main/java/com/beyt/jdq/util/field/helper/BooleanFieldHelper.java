package com.beyt.jdq.util.field.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class BooleanFieldHelper implements IFieldHelper<Boolean> {
    @Override
    public Boolean fillRandom() {
        return random.nextBoolean();
    }

    @Override
    public Boolean fillValue(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return value.toLowerCase();
    }
}
