package com.beyt.util.field.helper;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class LocalDateFieldHelper implements IFieldHelper<LocalDate> {
    @Override
    public LocalDate fillRandom() {
        return Instant.ofEpochMilli(System.currentTimeMillis() + random.nextInt(1000000000))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Override
    public LocalDate fillValue(String value) {
        return LocalDate.parse(value);
    }

    @Override
    public String createGeneratorCode(String value) {
        return "LocalDate.parse(\"" + value + "\")";
    }
}
