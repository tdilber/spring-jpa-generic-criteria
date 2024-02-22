package com.beyt.config;

import com.beyt.provider.IEntityManagerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class EntityManagerProviderConfig {

    @Bean
    @ConditionalOnMissingBean
    public IEntityManagerProvider entityManagerProvider(EntityManager entityManager) {
        return () -> entityManager;
    }
}
