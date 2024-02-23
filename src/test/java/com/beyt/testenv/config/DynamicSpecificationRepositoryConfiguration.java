package com.beyt.testenv.config;

import com.beyt.repository.DynamicSpecificationRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = DynamicSpecificationRepositoryFactoryBean.class, basePackages = "com.beyt.testenv.repository")
public class DynamicSpecificationRepositoryConfiguration {

}
