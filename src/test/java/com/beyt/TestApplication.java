package com.beyt;

import com.beyt.annotation.EnableDynamicQueryArgumentResolvers;
import com.beyt.annotation.EnableJpaDynamicQuery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJpaDynamicQuery
@EnableDynamicQueryArgumentResolvers
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
