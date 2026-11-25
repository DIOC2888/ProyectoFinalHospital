package org.example.proyectofinalhospital.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import org.example.proyectofinalhospital.dao.CitasDAO;
import org.example.proyectofinalhospital.dao.MedicoDAO;
import org.example.proyectofinalhospital.dao.PacienteDAO;
import org.example.proyectofinalhospital.model.Citas;
import org.example.proyectofinalhospital.model.Medico;
import org.example.proyectofinalhospital.model.Paciente;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CitasController {

    @FXML
    private ComboBox<Paciente> cmbPaciente;

    @FXML
    private ComboBox<Medico> cmbMedico;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private ComboBox<String> cmbHora,cmbEstado;

    @FXML
    private TextField txtMotivo,txtBuscar;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Button btnActualizar,btnCancelarCita;

    @FXML
    private TableView<Citas> tblCitas;

    @FXML
    private TableColumn<Citas,Integer> colId;

    @FXML
    private TableColumn<Citas,String> colPaciente,colMedico,colFecha,colHora,colMotivo,colEstado;

    private final CitasDAO dao = new CitasDAO();

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    private final MedicoDAO medicoDAO = new MedicoDAO();

    private Integer idSeleccionado;

    @FXML private void initialize(){

        cmbPaciente.setConverter(new StringConverter<>(){
            public String toString(Paciente p){
                return p==null?"":p.getNombres()+" "+p.getApellidos()+" ("+p.getDocumento()+")";
            }
            public Paciente fromString(String s){
                return null;
            }
        } );

        cmbMedico.setConverter(new StringConverter<>(){
            public String toString(Medico m){
                return m==null?"":m.getNombres()+" "+m.getApellidos()+" - "+m.getEspecialidad().getNombre();
            }
            public Medico fromString(String s){
                return null;
            }
        } );

        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCita()).asObject());
        colPaciente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPaciente().getNombres()+" "+c.getValue().getPaciente().getApellidos()));
        colMedico.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMedico().getNombres()+" "+c.getValue().getMedico().getApellidos()));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        colMotivo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMotivo()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado()));

        tblCitas.getSelectionModel().selectedItemProperty().addListener((o,a,c)->seleccionar(c));

        btnActualizar.setDisable(true);
        btnCancelarCita.setDisable(true);

        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {

        try{
            cmbPaciente.getItems().setAll(pacienteDAO.listarActivos());
            cmbMedico.getItems().setAll(medicoDAO.listarActivos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private void cargarTabla(){

        try{
            tblCitas.getItems().setAll(dao.listarTodos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private void seleccionar(Citas c){
        if( c == null)
            return;
        idSeleccionado=c.getIdCita();
        cmbPaciente.getSelectionModel().select(
                cmbPaciente.getItems().stream().filter(p -> p.getIdPaciente()
                        .equals(c.getPaciente().getIdPaciente()))
                        .findFirst().orElse(null));
        cmbMedico.getSelectionModel().select(
                cmbMedico.getItems().stream().filter(m->m.getIdMedico()
                        .equals(c.getMedico().getIdMedico()))
                        .findFirst().orElse(null));
        dpFecha.setValue(c.getFechaHora().toLocalDate());
        cmbHora.setValue(c.getFechaHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        txtMotivo.setText(c.getMotivo());
        cmbEstado.setValue(c.getEstado());
        txtObservaciones.setText(c.getObservaciones());
        btnActualizar.setDisable(false);
        btnCancelarCita.setDisable(false);
    }

    private Citas leer(){

        if(cmbPaciente.getValue()==null
                ||cmbMedico.getValue()==null
                ||dpFecha.getValue()==null
                ||cmbHora.getValue()==null)
            throw new IllegalArgumentException("Seleccione paciente, médico, fecha y hora.");
        LocalTime hora=LocalTime.parse(cmbHora.getValue());

        if(hora.getMinute() != 0 && hora.getMinute() != 30)
            throw new IllegalArgumentException("La hora debe ser en punto o a media hora.");

        if(idSeleccionado == null && LocalDateTime
                .of(dpFecha.getValue(),hora)
                .isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(
                    "La cita debe programarse para una fecha y hora futuras."
            );
        String estado = cmbEstado.getValue() == null ? "PROGRAMADA":cmbEstado.getValue();

        return new Citas(LocalDateTime.of(
                dpFecha.getValue(),hora),
                ControllerUtil.limpio(txtMotivo.getText()),
                estado,
                ControllerUtil.limpio(txtObservaciones.getText()),
                LocalDateTime.now(),
                cmbPaciente.getValue(),
                cmbMedico.getValue());
    }

    @FXML
    private void programar(){

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
            Citas c=leer();
            c.setIdCita(idSeleccionado);
            c.setFechaRegistro(dao.buscarPorId(idSeleccionado).getFechaRegistro());
            dao.actualizar(c);cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void cancelarCita(){
        if(idSeleccionado==null)return;
        try{
            Citas c=dao.buscarPorId(idSeleccionado);
            c.setEstado("CANCELADA");dao.actualizar(c);
            cargarTabla();limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void buscar(){

        try{
            String q = ControllerUtil.limpio(txtBuscar.getText());
            tblCitas.getItems().setAll(q.isEmpty() ? dao.listarTodos():dao.buscar(q));
        }catch(RuntimeException e){
            error(e);
        }
    }

    @FXML
    private void limpiar(){

        cmbPaciente.getSelectionModel().clearSelection();
        cmbMedico.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        cmbHora.getSelectionModel().clearSelection();
        cmbEstado.setValue("PROGRAMADA");
        txtMotivo.clear();
        txtObservaciones.clear();
        idSeleccionado=null;
        tblCitas.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnCancelarCita.setDisable(true);
    }

    private void error(Exception e){

        ControllerUtil.mostrar(
                Alert.AlertType.ERROR,"Error",
                e.getMessage() == null ? "No se pudo completar la operación.":e.getMessage()
        );
    }
}

