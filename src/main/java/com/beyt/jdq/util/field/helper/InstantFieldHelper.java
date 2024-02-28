package com.beyt.jdq.util.field.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class InstantFieldHelper implements IFieldHelper<Instant> {
    @Override
    public Instant fillRandom() {
        return Instant.ofEpochMilli(System.currentTimeMillis() + random.nextInt(1000000000));
    }

    @Override
    public Instant fillValue(String value) {
        return Instant.parse(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return "Instant.parse(\"" + value + "\")";
    }
}
