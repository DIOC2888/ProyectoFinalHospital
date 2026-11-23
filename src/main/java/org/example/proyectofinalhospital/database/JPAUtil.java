package org.example.proyectofinalhospital.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    private static EntityManagerFactory em;

    public static EntityManagerFactory getEntityManagerFactory() {

        if (em == null) {

            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || url.isBlank()
                    || user == null || user.isBlank()
                    || password == null || password.isBlank()) {
                throw new IllegalStateException(
                        "Faltan variables de entorno de PostgreSQL"
                );
            }

            Map<String, Object> propiedades = new HashMap<>();

            propiedades.put("jakarta.persistence.jdbc.url", url);
            propiedades.put("jakarta.persistence.jdbc.user", user);
            propiedades.put("jakarta.persistence.jdbc.password", password);

            em = Persistence.createEntityManagerFactory(
                    "hospitalPU", propiedades
            );
        }

        return em;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
            em = null;
        }
    }
}
