package com.beyt.annotation;

import com.beyt.config.DeserializerConfig;
import com.beyt.config.EntityManagerProviderConfig;
import com.beyt.util.ApplicationContextUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ApplicationContextUtil.class, EntityManagerProviderConfig.class, DeserializerConfig.class})
public @interface EnableJpaDynamicQuery {
}
