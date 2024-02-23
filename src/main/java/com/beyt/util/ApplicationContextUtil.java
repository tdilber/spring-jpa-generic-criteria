package com.beyt.util;

import com.beyt.deserializer.IDeserializer;
import com.beyt.provider.IEntityManagerProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;

public class ApplicationContextUtil implements ApplicationContextAware {
 private static ApplicationContext applicationContext;
    private static IEntityManagerProvider entityManagerProvider;
    private static IDeserializer deserializer;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ApplicationContextUtil.applicationContext = applicationContext;
      ApplicationContextUtil.entityManagerProvider = applicationContext.getBean(IEntityManagerProvider.class);
      ApplicationContextUtil.deserializer = applicationContext.getBean(IDeserializer.class);
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static EntityManager getEntityManager(){
      return entityManagerProvider.provide();
  }

    public static IDeserializer getDeserializer() {
        return deserializer;
    }
}
