package org.example.proyectofinalhospital.dao;

import org.example.proyectofinalhospital.model.Medico;

import java.util.List;

public class MedicoDAO extends BaseDAO<Medico, Integer> {
    public MedicoDAO() { super(Medico.class); }

    public List<Medico> buscar(String termino) {
        String patron = "%" + (termino == null ? "" : termino.trim().toLowerCase()) + "%";
        return conEntityManager(em -> em.createQuery("""
                SELECT m FROM Medico m JOIN FETCH m.especialidad
                WHERE LOWER(m.nombres) LIKE :q OR LOWER(m.apellidos) LIKE :q
                   OR LOWER(m.codigoMedico) LIKE :q OR LOWER(m.especialidad.nombre) LIKE :q
                ORDER BY m.apellidos, m.nombres
                """, Medico.class).setParameter("q", patron).getResultList());
    }

    public List<Medico> listarActivos() {
        return conEntityManager(em -> em.createQuery(
                        "SELECT m FROM Medico m JOIN FETCH m.especialidad WHERE m.activo = true ORDER BY m.apellidos, m.nombres", Medico.class)
                .getResultList());
    }
}