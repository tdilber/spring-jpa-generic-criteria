package com.beyt.testenv.config;

import com.beyt.repository.GenericSpecificationRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = GenericSpecificationRepositoryFactoryBean.class, basePackages = "com.beyt.testenv.repository")
public class GenericSpecificationRepositoryConfiguration {

}
