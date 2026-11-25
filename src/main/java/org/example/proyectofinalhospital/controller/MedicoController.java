package org.example.proyectofinalhospital.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.proyectofinalhospital.dao.EspecialidadDAO;
import org.example.proyectofinalhospital.dao.MedicoDAO;
import org.example.proyectofinalhospital.model.Especialidad;
import org.example.proyectofinalhospital.model.Medico;

public class MedicoController {

    @FXML
    private TextField txtNombres,txtApellidos,txtCodigoMedico,txtTelefono,txtCorreo,txtBuscar;

    @FXML
    private ComboBox<Especialidad> cmbEspecialidad;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private Button btnActualizar,btnDesactivar;

    @FXML
    private TableView<Medico> tblMedicos;

    @FXML
    private TableColumn<Medico,Integer> colId;

    @FXML
    private TableColumn<Medico,String> colCodigo,colNombres,colApellidos,colEspecialidad,colTelefono,colCorreo;

    @FXML
    private TableColumn<Medico,Boolean> colActivo;

    private final MedicoDAO dao = new MedicoDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    private Integer idSeleccionado;

    @FXML
    private void initialize(){

        cmbEspecialidad.setConverter(new StringConverter<>(){
            public String toString(Especialidad e){
                return e == null ? "" :e.getNombre();
            }
            public Especialidad fromString(String s){
                return null;
            }
        } );

        colId.setCellValueFactory( c -> new SimpleIntegerProperty(c.getValue().getIdMedico()).asObject());
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigoMedico()));
        colNombres.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombres()));
        colApellidos.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellidos()));
        colEspecialidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEspecialidad().getNombre()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colActivo.setCellValueFactory(c -> new SimpleBooleanProperty(Boolean.TRUE.equals(c.getValue().getActivo())).asObject());
        tblMedicos.getSelectionModel().selectedItemProperty().addListener((o,a,m) -> cargarSeleccion(m));
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true);cargarEspecialidades();cargarTabla();
    }

    private void cargarEspecialidades(){

        try{
            cmbEspecialidad.getItems().setAll(especialidadDAO.listarTodos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private void cargarTabla(){

        try{
            tblMedicos.getItems().setAll(dao.listarTodos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private void cargarSeleccion(Medico m){

        if(m == null)
            return;

        idSeleccionado=m.getIdMedico();
        txtNombres.setText(m.getNombres());
        txtApellidos.setText(m.getApellidos());
        txtCodigoMedico.setText(m.getCodigoMedico());
        txtTelefono.setText(m.getTelefono());
        txtCorreo.setText(m.getCorreo());
        chkActivo.setSelected(Boolean.TRUE.equals(m.getActivo()));
        cmbEspecialidad.getSelectionModel().select(
                cmbEspecialidad.getItems().stream()
                        .filter(e->e.getIdEspecialidad()
                                .equals(m.getEspecialidad().getIdEspecialidad()))
                        .findFirst()
                        .orElse(null));
        btnActualizar.setDisable(false);
        btnDesactivar.setDisable(false);}

    private Medico leer(){

        String n = ControllerUtil.limpio(
                txtNombres.getText()),
                a = ControllerUtil.limpio(txtApellidos.getText()),
                codigo = ControllerUtil.limpio(txtCodigoMedico.getText());

        if(n.isEmpty()
                ||a.isEmpty()
                ||codigo.isEmpty()
                ||cmbEspecialidad.getValue() == null)
            throw new IllegalArgumentException(
                    "Nombres, apellidos, código y especialidad son obligatorios.");

        return new Medico(
                n,a,codigo,
                ControllerUtil.limpio(txtTelefono.getText()),
                ControllerUtil.limpio(txtCorreo.getText()),
                chkActivo.isSelected(),
                cmbEspecialidad.getValue());
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

        if(idSeleccionado==null)
            return;

        try{Medico m = leer();
            m.setIdMedico(idSeleccionado);
            dao.actualizar(m);
            cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void desactivar(){

        if(idSeleccionado == null)
            return;

        try{
            Medico m = dao.buscarPorId(idSeleccionado);
            m.setActivo(false);
            dao.actualizar(m);
            cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void buscar(){

        try{
            String q = ControllerUtil.limpio(txtBuscar.getText());
            tblMedicos.getItems().setAll(
                    q.isEmpty() ? dao.listarTodos():dao.buscar(q));
        }catch(RuntimeException e){
            error(e);
        }
    }

    @FXML
    private void limpiar(){

        txtNombres.clear();
        txtApellidos.clear();
        txtCodigoMedico.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        cmbEspecialidad.getSelectionModel().clearSelection();
        chkActivo.setSelected(true);
        idSeleccionado=null;
        tblMedicos.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true);
    }

    private void error(Exception e){

        ControllerUtil.mostrar(
                Alert.AlertType.ERROR,
                "Error",
                e.getMessage() == null ?
                        "No se pudo completar la operación.":e.getMessage());
    }
}