package ru.iot.api.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

public interface DataSource extends PersistenceUnitDescriptor {

    EntityManagerFactory getEntityManagerFactory();

    default EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    default void close() {
        getEntityManagerFactory().close();
    }

}
