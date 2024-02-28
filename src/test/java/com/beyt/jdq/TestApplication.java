package com.beyt.jdq;

import com.beyt.jdq.annotation.EnableJpaDynamicQuery;
import com.beyt.jdq.annotation.EnableJpaDynamicQueryArgumentResolvers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJpaDynamicQuery
@EnableJpaDynamicQueryArgumentResolvers
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
