package com.beyt.provider;

import javax.persistence.EntityManager;

public interface IEntityManagerProvider {
    EntityManager provide();
}
