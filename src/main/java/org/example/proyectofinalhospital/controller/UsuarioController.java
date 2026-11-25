package org.example.proyectofinalhospital.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.proyectofinalhospital.dao.UsuarioDAO;
import org.example.proyectofinalhospital.model.Usuario;
import org.example.proyectofinalhospital.database.PasswordUtil;
import org.example.proyectofinalhospital.database.Sesion;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField pfContrasena;

    @FXML
    private ComboBox<String> cmbRol;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private Button btnActualizar,btnDesactivar;

    @FXML
    private TableView<Usuario> tblUsuarios;

    @FXML
    private TableColumn<Usuario,Integer> colId;

    @FXML
    private TableColumn<Usuario,String> colUsername,colRol,colFechaCreacion;

    @FXML
    private TableColumn<Usuario,Boolean> colActivo;

    private final UsuarioDAO dao = new UsuarioDAO();

    private Integer idSeleccionado;

    @FXML
    private void initialize(){

        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdUsuario()).asObject());
        colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colRol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRol()));
        colActivo.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isActivo()).asObject());
        colFechaCreacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()
                .getFechaCreacion() == null ? "" :c.getValue().getFechaCreacion()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((o,a,u) -> seleccionar(u));
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true);

        cargarTabla();
    }

    private void cargarTabla(){

        try{
            tblUsuarios.getItems().setAll(dao.listarTodos());
        }catch(RuntimeException e){
            error(e);
        }
    }

    private void seleccionar(Usuario u){

        if(u == null)
            return;

        idSeleccionado=u.getIdUsuario();
        txtUsername.setText(u.getUsername());
        pfContrasena.clear();
        cmbRol.setValue(u.getRol());
        chkActivo.setSelected(u.isActivo());
        btnActualizar.setDisable(false);
        btnDesactivar.setDisable(false);
    }

    private String validar(){

        String username = ControllerUtil.limpio(txtUsername.getText());
        if(username.isEmpty() || cmbRol.getValue() == null)
            throw new IllegalArgumentException(
                    "El usuario y el rol son obligatorios."
            );

        if(username.length() > 50)
            throw new IllegalArgumentException(
                    "El usuario no puede superar 50 caracteres."
            );

        String pass=pfContrasena.getText();
        if(idSeleccionado == null && (pass == null || pass.length() < 8))
            throw new IllegalArgumentException(
                    "La contraseña inicial debe tener al menos 8 caracteres."
            );

        if(pass != null && !pass.isEmpty() && pass.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres."
            );

        return username;
    }

    @FXML
    private void guardar(){

        try{

            String username=validar();
            Usuario u=new Usuario(username,PasswordUtil.hash(
                    pfContrasena.getText()),
                    cmbRol.getValue(),
                    chkActivo.isSelected(),
                    LocalDateTime.now());

            dao.guardar(u);
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
            String username=validar();
            Usuario u=dao.buscarPorId(idSeleccionado);

            if(u == null)
                throw new IllegalStateException(
                        "El usuario ya no existe."
                );

            u.setUsername(username);
            u.setRol(cmbRol.getValue());
            u.setActivo(chkActivo.isSelected());

            if(!pfContrasena.getText().isEmpty())
                u.setPasswordHash(PasswordUtil.hash(pfContrasena.getText()));
            dao.actualizar(u);

            if(Sesion.getUsuarioActual()!=null&&Sesion
                    .getUsuarioActual().getIdUsuario()
                    .equals(u.getIdUsuario()))Sesion.iniciar(u);
            cargarTabla();limpiar();

        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void desactivar(){

        if(idSeleccionado == null)
            return;

        if(Sesion.getUsuarioActual() != null && Sesion.getUsuarioActual()
                .getIdUsuario().equals(idSeleccionado)){
            error(new IllegalArgumentException(
                    "No puede desactivar la cuenta que está usando.")
            );

            return;
        }

        try{
            Usuario u=dao.buscarPorId(idSeleccionado);
            u.setActivo(false);
            dao.actualizar(u);cargarTabla();
            limpiar();
        }catch(Exception e){
            error(e);
        }
    }

    @FXML
    private void limpiar(){

        txtUsername.clear();
        pfContrasena.clear();
        cmbRol.getSelectionModel().clearSelection();
        chkActivo.setSelected(true);
        idSeleccionado=null;
        tblUsuarios.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnDesactivar.setDisable(true);
    }

    private void error(Exception e){

        ControllerUtil.mostrar(
                Alert.AlertType.ERROR,
                "Error",
                e.getMessage() == null?
                        "No se pudo completar la operación.":e.getMessage()
        );
    }
}