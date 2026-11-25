package org.example.proyectofinalhospital.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.proyectofinalhospital.dao.UsuarioDAO;
import org.example.proyectofinalhospital.model.Usuario;
import org.example.proyectofinalhospital.database.PasswordUtil;
import org.example.proyectofinalhospital.database.Sesion;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pfContrasena;
    @FXML
    private Label lblError;

    private final UsuarioDAO dao = new UsuarioDAO();

    @FXML
    private void initialize(){
        lblError.setText(" ");
    }

    @FXML
    private void iniciarSesion(){
        String username = ControllerUtil.limpio(
                txtUsuario.getText()),
                password=pfContrasena.getText();

        if(username.isEmpty() || password.isEmpty()){
            lblError.setText("Ingrese usuario y contraseña.");
            return;

        }try{
            Usuario u;
            if(dao.listarTodos().isEmpty()){
                if(password.length() < 8) {
                    lblError.setText(
                            "Base de datos sin usuarios: cree el administrador inicial con una contraseña de al menos 8 caracteres.");
                    return;
                }
                u = new Usuario(
                        username,
                        PasswordUtil.hash(password),
                        "ADMINISTRADOR",
                        true,LocalDateTime.now());

                dao.guardar(u);

            }else{
                u=dao.buscarPorUsername(username).orElse(null);
                if(u == null || !u.isActivo()
                        || !PasswordUtil.verificar(password,u.getPasswordHash())){
                    lblError.setText("Usuario o contraseña incorrectos.");
                    pfContrasena.clear();
                    return;
                }
            }

            Sesion.iniciar(u);
            abrirDashboard();

        }catch(IOException|RuntimeException e){
            lblError.setText(e.getMessage() == null ?
                    "No se pudo iniciar sesión. Verifique la conexión con la base de datos.":
                    e.getMessage());
        }
    }

    private void abrirDashboard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
        Stage stage = (Stage)
                txtUsuario.getScene().getWindow();
        stage.setTitle("SIGAH - Gestión Hospitalaria");
        stage.setScene(new Scene(root));
        stage.setMinWidth(1000);stage.setMinHeight(650);
        stage.centerOnScreen();
    }
}