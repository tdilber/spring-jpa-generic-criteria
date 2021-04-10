package com.beyt.util.field.helper;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tdilber at 11/17/2020
 */
@Slf4j
public class DateFieldHelper implements IFieldHelper<Date> {
    @Override
    public Date fillRandom() {
        return new Date(System.currentTimeMillis() + random.nextInt(1000000000));
    }

    @Override
    public Date fillValue(String value) {
        DateFormat dateFormat = new SimpleDateFormat();
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String createGeneratorCode(String value) {
        return "new Date(" + fillValue(value).getTime() + "L)";
    }
}
