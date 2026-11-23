package org.example.proyectofinalhospital.dao;

import org.example.proyectofinalhospital.model.Citas;

import java.util.List;

public class CitasDAO extends BaseDAO<Citas, Integer> {
    public CitasDAO() { super(Citas.class); }

    public List<Citas> listarTodos() {
        return conEntityManager(em -> em.createQuery("""
                SELECT c FROM Citas c JOIN FETCH c.paciente JOIN FETCH c.medico m JOIN FETCH m.especialidad
                ORDER BY c.fechaHora DESC
                """, Citas.class).getResultList());
    }

    public List<Citas> buscar(String termino) {
        String patron = "%" + (termino == null ? "" : termino.trim().toLowerCase()) + "%";
        return conEntityManager(em -> em.createQuery("""
                SELECT c FROM Citas c JOIN FETCH c.paciente p JOIN FETCH c.medico m JOIN FETCH m.especialidad
                WHERE LOWER(p.nombres) LIKE :q OR LOWER(p.apellidos) LIKE :q
                   OR LOWER(m.nombres) LIKE :q OR LOWER(m.apellidos) LIKE :q
                   OR LOWER(COALESCE(c.motivo, '')) LIKE :q OR LOWER(c.estado) LIKE :q
                ORDER BY c.fechaHora DESC
                """, Citas.class).setParameter("q", patron).getResultList());
    }
}