package com.beyt.annotation;

import com.beyt.config.ArgumentResolversInitConfig;
import com.beyt.resolver.CriteriaListArgumentResolver;
import com.beyt.resolver.DynamicQueryArgumentResolver;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Import({ArgumentResolversInitConfig.class, CriteriaListArgumentResolver.class, DynamicQueryArgumentResolver.class})
@Target({ElementType.TYPE})
@EnableJpaDynamicQuery
public @interface EnableJpaDynamicQueryArgumentResolvers {
}
