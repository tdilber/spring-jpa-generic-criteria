package com.beyt.jdq.config;

import com.beyt.jdq.provider.IEntityManagerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;

@Configuration
public class EntityManagerProviderConfig {

    @Bean
    @ConditionalOnMissingBean
    public IEntityManagerProvider entityManagerProvider(EntityManager entityManager) {
        return () -> entityManager;
    }
}
