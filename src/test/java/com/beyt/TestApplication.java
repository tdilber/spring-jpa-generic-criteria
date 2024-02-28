package com.beyt;

import com.beyt.annotation.EnableJpaDynamicQuery;
import com.beyt.annotation.EnableJpaDynamicQueryArgumentResolvers;
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
