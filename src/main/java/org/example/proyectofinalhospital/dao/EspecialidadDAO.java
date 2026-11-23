package org.example.proyectofinalhospital.dao;

import org.example.proyectofinalhospital.model.Especialidad;

import java.util.List;

public class EspecialidadDAO extends BaseDAO<Especialidad, Integer> {
    public EspecialidadDAO() { super(Especialidad.class); }

    public List<Especialidad> buscar(String termino) {
        String patron = "%" + (termino == null ? "" : termino.trim().toLowerCase()) + "%";
        return conEntityManager(em -> em.createQuery(
                        "SELECT e FROM Especialidad e WHERE LOWER(e.nombre) LIKE :q ORDER BY e.nombre", Especialidad.class)
                .setParameter("q", patron).getResultList());
    }
}