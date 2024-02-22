package com.beyt.config;

import com.beyt.resolver.CriteriaFilterArgumentResolver;
import com.beyt.resolver.DynamicQueryArgumentResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ArgumentResolversInitConfig implements WebMvcConfigurer {

    private final CriteriaFilterArgumentResolver criteriaFilterArgumentResolver;
    private final DynamicQueryArgumentResolver dynamicQueryArgumentResolver;

    public ArgumentResolversInitConfig(CriteriaFilterArgumentResolver criteriaFilterArgumentResolver, DynamicQueryArgumentResolver dynamicQueryArgumentResolver) {
        this.criteriaFilterArgumentResolver = criteriaFilterArgumentResolver;
        this.dynamicQueryArgumentResolver = dynamicQueryArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(criteriaFilterArgumentResolver);
        resolvers.add(dynamicQueryArgumentResolver);
    }
}
