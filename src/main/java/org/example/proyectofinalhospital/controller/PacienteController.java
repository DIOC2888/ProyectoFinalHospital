package org.example.proyectofinalhospital.controller;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.proyectofinalhospital.dao.PacienteDAO;
import org.example.proyectofinalhospital.model.Paciente;
import java.time.LocalDate;

public class PacienteController {

    @FXML
    private TextField txtNombres, txtApellidos, txtDocumento, txtTelefono, txtCorreo, txtDireccion, txtContactoEmergencia, txtTelefonoEmergencia, txtBuscar;

    @FXML
    private DatePicker dpFechaNacimiento;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private Button btnActualizar, btnDesactivar;

    @FXML
    private TableView<Paciente> tblPacientes;

    @FXML
    private TableColumn<Paciente, Integer> colId;

    @FXML
    private TableColumn<Paciente, String> colNombres, colApellidos, colDocumento, colTelefono, colCorreo;

    @FXML
    private TableColumn<Paciente, LocalDate> colFechaNacimiento;

    @FXML
    private TableColumn<Paciente, Boolean> colActivo;

    private final PacienteDAO dao = new PacienteDAO();

    private Integer idSeleccionado;

    @FXML
    private void initialize() {

        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdPaciente()).asObject());
        colNombres.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombres()));
        colApellidos.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellidos()));
        colDocumento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDocumento()));
        colFechaNacimiento.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getFechaNacimiento()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colActivo.setCellValueFactory(c -> new SimpleBooleanProperty(Boolean.TRUE.equals(c.getValue().getActivo())).asObject());
        tblPacientes.getSelectionModel().selectedItemProperty().addListener((o, a, p) -> cargarSeleccion(p));
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true); cargarTabla();
    }

    private void cargarTabla() {

        try {
            tblPacientes.getItems().setAll(dao.listarTodos());
        } catch (RuntimeException e) {
            error(e);
        }
    }

    private void cargarSeleccion(Paciente p) {

        if (p == null) return;
        idSeleccionado = p.getIdPaciente();
        txtNombres.setText(p.getNombres());
        txtApellidos.setText(p.getApellidos());
        txtDocumento.setText(p.getDocumento());
        dpFechaNacimiento.setValue(p.getFechaNacimiento());
        txtTelefono.setText(p.getTelefono());
        txtCorreo.setText(p.getCorreo());
        txtDireccion.setText(p.getDireccion());
        txtContactoEmergencia.setText(p.getContactoEmergencia());
        txtTelefonoEmergencia.setText(p.getTelefonoEmergencia());
        chkActivo.setSelected(Boolean.TRUE.equals(p.getActivo()));
        btnActualizar.setDisable(false);
        btnDesactivar.setDisable(false);
    }

    private Paciente leerFormulario() {

        String nombres = ControllerUtil.limpio(txtNombres.getText()),
                apellidos = ControllerUtil.limpio(txtApellidos.getText()),
                documento = ControllerUtil.limpio(txtDocumento.getText());

        if (nombres.isEmpty()
                || apellidos.isEmpty()
                || documento.isEmpty()
                || dpFechaNacimiento.getValue() == null)
            throw new IllegalArgumentException(
                    "Nombres, apellidos, documento y fecha de nacimiento son obligatorios.");

        if (dpFechaNacimiento.getValue().isAfter(LocalDate.now()))
            throw new IllegalArgumentException(
                    "La fecha de nacimiento no puede estar en el futuro.");

        Paciente p = new Paciente();
        p.setNombres(nombres);
        p.setApellidos(apellidos);
        p.setDocumento(documento);
        p.setFechaNacimiento(dpFechaNacimiento.getValue());
        p.setTelefono(ControllerUtil.limpio(txtTelefono.getText()));
        p.setCorreo(ControllerUtil.limpio(txtCorreo.getText()));
        p.setDireccion(ControllerUtil.limpio(txtDireccion.getText()));
        p.setContactoEmergencia(ControllerUtil.limpio(txtContactoEmergencia.getText()));
        p.setTelefonoEmergencia(ControllerUtil.limpio(txtTelefonoEmergencia.getText()));
        p.setActivo(chkActivo.isSelected());

        return p;
    }

    @FXML
    private void guardar() {

        try {
            dao.guardar(leerFormulario());
            cargarTabla();
            limpiar();

            ControllerUtil.mostrar(
                    Alert.AlertType.INFORMATION,
                    "Guardado",
                    "Paciente registrado correctamente."
            );
        } catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void actualizar() {

        if(idSeleccionado == null)
            return;

        try {
            Paciente p=leerFormulario();
            p.setIdPaciente(idSeleccionado);
            p.setFechaRegistro(dao.buscarPorId(idSeleccionado).getFechaRegistro());
            dao.actualizar(p);
            cargarTabla();
            limpiar();
        } catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void desactivar() {

        if(idSeleccionado == null)
            return;

        try {
            Paciente p = dao.buscarPorId(idSeleccionado);
            p.setActivo(false);
            dao.actualizar(p);
            cargarTabla();
            limpiar();
        } catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void buscar() {

        try {
            String q = ControllerUtil.limpio(txtBuscar.getText());
            tblPacientes.getItems().setAll(q.isEmpty() ? dao.listarTodos():dao.buscar(q));
        } catch(RuntimeException e){
            error(e);
        }
    }

    @FXML
    private void limpiar() {

        txtNombres.clear();
        txtApellidos.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtDireccion.clear();
        txtContactoEmergencia.clear();
        txtTelefonoEmergencia.clear();
        dpFechaNacimiento.setValue(null);
        chkActivo.setSelected(true);
        idSeleccionado=null;
        tblPacientes.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true);
    }

    private void error(Exception e){

        ControllerUtil.mostrar(
                Alert.AlertType.ERROR,
                "Error",
                e.getMessage() == null ?
                        "No se pudo completar la operación." :e.getMessage()
        );
    }
}