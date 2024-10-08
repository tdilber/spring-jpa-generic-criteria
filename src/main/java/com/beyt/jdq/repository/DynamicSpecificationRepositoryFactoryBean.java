package com.beyt.jdq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import jakarta.persistence.EntityManager;
import java.io.Serializable;


public class DynamicSpecificationRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, ID> {

  public DynamicSpecificationRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new GenericSpecificationRepositoryFactory<T, ID>(entityManager);
  }

  private static class GenericSpecificationRepositoryFactory<T, ID extends Serializable>
          extends JpaRepositoryFactory {

    public GenericSpecificationRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      Class<?> repositoryInterface = metadata.getRepositoryInterface();
      if (DynamicSpecificationRepository.class.isAssignableFrom(repositoryInterface)) {
        return DynamicSpecificationRepositoryImpl.class;
      } else {
        return super.getRepositoryBaseClass(metadata);
      }
    }
  }
}
