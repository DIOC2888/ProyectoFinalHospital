package org.example.proyectofinalhospital.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.proyectofinalhospital.database.JPAUtil;

import java.util.List;
import java.util.function.Function;

/** Operaciones JPA comunes con cierre y transacciones consistentes. */
public abstract class BaseDAO<T, ID> {
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T guardar(T entidad) {
        return enTransaccion(em -> {
            em.persist(entidad);
            return entidad;
        });
    }

    public T actualizar(T entidad) {
        return enTransaccion(em -> em.merge(entidad));
    }

    public void eliminar(T entidad) {
        enTransaccion(em -> {
            T administrada = em.contains(entidad) ? entidad : em.merge(entidad);
            em.remove(administrada);
            return null;
        });
    }

    public T buscarPorId(ID id) {
        return conEntityManager(em -> em.find(entityClass, id));
    }

    public List<T> listarTodos() {
        return conEntityManager(em -> em.createQuery(
                        "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList());
    }

    protected <R> R conEntityManager(Function<EntityManager, R> operacion) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return operacion.apply(em);
        }
    }

    protected <R> R enTransaccion(Function<EntityManager, R> operacion) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                R resultado = operacion.apply(em);
                tx.commit();
                return resultado;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
}