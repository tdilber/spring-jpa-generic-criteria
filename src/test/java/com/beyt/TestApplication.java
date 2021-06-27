package com.beyt;

import com.beyt.annotation.EnableGenericSpecificationArgumentResolvers;
import com.beyt.util.ApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationContextUtil.class)
@EnableGenericSpecificationArgumentResolvers
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
