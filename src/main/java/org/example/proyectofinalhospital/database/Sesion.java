package org.example.proyectofinalhospital.database;

import org.example.proyectofinalhospital.model.Usuario;

public class Sesion {

    private static Usuario usuarioActual;

    private Sesion() {}

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void iniciar(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrar() {
        usuarioActual = null;
    }
}
