package org.example.proyectofinalhospital.dao;

import org.example.proyectofinalhospital.model.Usuario;

import java.util.List;
import java.util.Optional;

public class UsuarioDAO extends BaseDAO<Usuario, Integer> {
    public UsuarioDAO() { super(Usuario.class); }

    public Optional<Usuario> buscarPorUsername(String username) {
        return conEntityManager(em -> em.createQuery(
                        "SELECT u FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)", Usuario.class)
                .setParameter("username", username).getResultStream().findFirst());
    }

    public List<Usuario> buscar(String termino) {
        String patron = "%" + (termino == null ? "" : termino.trim().toLowerCase()) + "%";
        return conEntityManager(em -> em.createQuery(
                        "SELECT u FROM Usuario u WHERE LOWER(u.username) LIKE :q ORDER BY u.username", Usuario.class)
                .setParameter("q", patron).getResultList());
    }

    public long contarActivos() {
        return conEntityManager(em -> em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.activo = true", Long.class).getSingleResult());
    }
}