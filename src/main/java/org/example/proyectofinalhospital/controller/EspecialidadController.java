package org.example.proyectofinalhospital.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.proyectofinalhospital.dao.EspecialidadDAO;
import org.example.proyectofinalhospital.model.Especialidad;

public class EspecialidadController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Button btnActualizar,btnEliminar;

    @FXML
    private TableView<Especialidad> tblEspecialidades;

    @FXML
    private TableColumn<Especialidad,Integer> colId;

    @FXML
    private TableColumn<Especialidad,String> colNombre,colDescripcion;

    private final EspecialidadDAO dao = new EspecialidadDAO();

    private Integer idSeleccionado;

    @FXML
    private void initialize(){
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdEspecialidad()).asObject());
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        tblEspecialidades.getSelectionModel().selectedItemProperty().addListener((o, a, e) -> seleccionar(e));
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);cargarTabla();
    }

    private void seleccionar(Especialidad e){
        if(e == null)
            return;
        idSeleccionado=e.getIdEspecialidad();
        txtNombre.setText(e.getNombre());
        txtDescripcion.setText(e.getDescripcion());
        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    private void cargarTabla(){

        try{
            tblEspecialidades.getItems().setAll(dao.listarTodos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private Especialidad leer(){

        String nombre = ControllerUtil.limpio(txtNombre.getText());
        if(nombre.isEmpty())
            throw new IllegalArgumentException("El nombre de la especialidad es obligatorio.");
        return new Especialidad(
                nombre,
                ControllerUtil.limpio(txtDescripcion.getText())
        );
    }

    @FXML
    private void guardar(){

        try{
            dao.guardar(leer());
            cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void actualizar(){

        if(idSeleccionado == null)
            return;

        try{
            Especialidad e = leer();
            e.setIdEspecialidad(idSeleccionado);
            dao.actualizar(e);
            cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void eliminar(){

        if(idSeleccionado == null)
            return;

        try{
            dao.eliminar(dao.buscarPorId(idSeleccionado));
            cargarTabla();limpiar();
        }catch(Exception e){
            ControllerUtil.mostrar(
                    Alert.AlertType.ERROR,"No se pudo eliminar",
                    "La especialidad puede estar asignada a uno o más médicos. Desactive o reasigne esos médicos antes de eliminarla."
            );
        }
    }

    @FXML
    private void limpiar(){

        txtNombre.clear();
        txtDescripcion.clear();
        idSeleccionado = null;
        tblEspecialidades.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private void error(Exception e){

        ControllerUtil.mostrar(
                Alert.AlertType.ERROR,"Error",
                e.getMessage() == null ? "No se pudo completar la operación.":e.getMessage()
        );
    }
}