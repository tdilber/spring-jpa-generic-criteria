package com.beyt.util;

import com.beyt.provider.IEntityManagerProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;

public class ApplicationContextUtil implements ApplicationContextAware {
 private static ApplicationContext applicationContext;
    private static IEntityManagerProvider entityManagerProvider;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ApplicationContextUtil.applicationContext = applicationContext;
      ApplicationContextUtil.entityManagerProvider = applicationContext.getBean(IEntityManagerProvider.class);
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static EntityManager getEntityManager(){
      return entityManagerProvider.provide();
  }
}
