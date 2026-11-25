package org.example.proyectofinalhospital.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

final class ControllerUtil {

    private ControllerUtil() {}

    static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    static <T> T seleccion(TableView<T> tabla, String entidad) {
        T seleccionado = tabla.getSelectionModel().getSelectedItem();

        if (seleccionado == null) mostrar(
                Alert.AlertType.WARNING,
                "Selección requerida",
                "Seleccione " + entidad + " en la tabla."
        );
        return seleccionado;
    }

    static String limpio(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
