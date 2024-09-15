package com.beyt.jdq.util;

import com.beyt.jdq.deserializer.IDeserializer;
import com.beyt.jdq.provider.IEntityManagerProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import jakarta.persistence.EntityManager;

public class ApplicationContextUtil implements ApplicationContextAware {
 private static ApplicationContext applicationContext;
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ApplicationContextUtil.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static EntityManager getEntityManager(){
      return applicationContext.getBean(IEntityManagerProvider.class).provide();
  }

    public static IDeserializer getDeserializer() {
        return applicationContext.getBean(IDeserializer.class);
    }
}
