package com.beyt.jdq.util.field.helper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class LongFieldHelper implements IFieldHelper<Long> {
    @Override
    public Long fillRandom() {
        return random.nextLong();
    }

    @Override
    public Long fillValue(String value) {
        return Long.parseLong(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return value + "L";
    }
}
