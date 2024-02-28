package com.beyt.jdq.testenv.config;

import com.beyt.jdq.repository.DynamicSpecificationRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = DynamicSpecificationRepositoryFactoryBean.class, basePackages = "com.beyt.jdq.testenv.repository")
public class DynamicSpecificationRepositoryConfiguration {

}
