package com.beyt.jdq.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class PresentationUtil {

    @SneakyThrows
    public static void prettyPrint(Object object) {
        var objectMapper = new ObjectMapper();
        System.out.println("______________________________________________________________________________");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        System.out.println("========================================================================");
    }
}
