package com.beyt.jdq.provider;

import jakarta.persistence.EntityManager;

public interface IEntityManagerProvider {
    EntityManager provide();
}
