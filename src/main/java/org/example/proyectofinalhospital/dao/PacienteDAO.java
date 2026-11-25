package org.example.proyectofinalhospital.dao;

import org.example.proyectofinalhospital.model.Paciente;

import java.util.List;

public class PacienteDAO extends BaseDAO<Paciente, Integer> {
    public PacienteDAO() { super(Paciente.class); }

    public List<Paciente> buscar(String termino) {
        String patron = "%" + (termino == null ? "" : termino.trim().toLowerCase()) + "%";
        return conEntityManager(em -> em.createQuery("""
                SELECT p FROM Paciente p
                WHERE LOWER(p.nombres) LIKE :q OR LOWER(p.apellidos) LIKE :q
                   OR LOWER(p.documento) LIKE :q OR LOWER(COALESCE(p.telefono, '')) LIKE :q
                ORDER BY p.apellidos, p.nombres
                """, Paciente.class).setParameter("q", patron).getResultList());
    }

    public List<Paciente> listarActivos() {
        return conEntityManager(em -> em.createQuery(
                        "SELECT p FROM Paciente p WHERE p.activo = true ORDER BY p.apellidos, p.nombres", Paciente.class)
                .getResultList());
    }
}