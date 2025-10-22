package ru.iot.api.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public interface Repository<T> {

    DataSource getDataSource();

    default void save(T entity) {
        try (EntityManager entityManager = getDataSource().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(entity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                throw new RepositoryException("Failed to save entity: " + entity, e);
            }
        }
    }
}
