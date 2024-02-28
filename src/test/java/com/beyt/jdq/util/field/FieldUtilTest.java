package com.beyt.jdq.util.field;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldUtilTest {

    @Test
    void fillValue() throws ParseException {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);


        assertEquals(TimeUnit.NANOSECONDS, FieldUtil.fillValue(TimeUnit.class, "NANOSECONDS"));
        assertEquals("NANOSECONDS", FieldUtil.fillValue(String.class, "NANOSECONDS"));
        assertEquals(Long.parseLong("155"), FieldUtil.fillValue(Long.class, "155"));
        assertEquals(Double.parseDouble("155"), FieldUtil.fillValue(Double.class, "155"));
        assertEquals(Integer.parseInt("155"), FieldUtil.fillValue(Integer.class, "155"));
        assertEquals(Boolean.TRUE, FieldUtil.fillValue(Boolean.class, "True"));
        assertEquals(simpleDateFormat.format(new Date(currentTimeMillis)), simpleDateFormat.format(FieldUtil.fillValue(Date.class, simpleDateFormat.format(new Date(currentTimeMillis)))));
        assertEquals(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()), FieldUtil.fillValue(ZonedDateTime.class, ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).toString()));
    }
}
