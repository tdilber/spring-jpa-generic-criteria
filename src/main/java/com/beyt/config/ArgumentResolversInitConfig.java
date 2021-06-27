package com.beyt.config;

import com.beyt.resolver.CriteriaFilterArgumentResolver;
import com.beyt.resolver.SearchQueryArgumentResolver;
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
    private final SearchQueryArgumentResolver searchQueryArgumentResolver;

    public ArgumentResolversInitConfig(CriteriaFilterArgumentResolver criteriaFilterArgumentResolver, SearchQueryArgumentResolver searchQueryArgumentResolver) {
        this.criteriaFilterArgumentResolver = criteriaFilterArgumentResolver;
        this.searchQueryArgumentResolver = searchQueryArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(criteriaFilterArgumentResolver);
        resolvers.add(searchQueryArgumentResolver);
    }
}
