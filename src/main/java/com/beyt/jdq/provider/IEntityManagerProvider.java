package com.beyt.jdq.provider;

import javax.persistence.EntityManager;

public interface IEntityManagerProvider {
    EntityManager provide();
}
