package org.example.proyectofinalhospital.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.proyectofinalhospital.dao.CitasDAO;
import org.example.proyectofinalhospital.dao.MedicoDAO;
import org.example.proyectofinalhospital.dao.PacienteDAO;
import org.example.proyectofinalhospital.model.Citas;
import org.example.proyectofinalhospital.database.Sesion;
import java.io.IOException;

public class DashboardController {
    @FXML
    private Button btnUsuarios;

    @FXML
    private Label lblSeccion, lblUsuarioActual,lblBienvenida,lblTotalPacientes,lblMedicosActivos,lblCitasProgramadas;

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox panelBienvenida;

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    private final MedicoDAO medicoDAO = new MedicoDAO();

    private final CitasDAO citasDAO = new CitasDAO();

    @FXML
    private void initialize(){

        var usuario=Sesion.getUsuarioActual();

        if(usuario!=null){
            lblUsuarioActual.setText(
                    "Usuario: "+usuario.getUsername()+" ("+usuario.getRol()+")");
            lblBienvenida.setText("Bienvenido(a), "+usuario.getUsername());

            if(!"ADMINISTRADOR".equals(usuario.getRol()))
                btnUsuarios.setDisable(true);
        }

        mostrarInicio();
        actualizarResumen();
    }

    private void actualizarResumen(){

        try{
            lblTotalPacientes.setText(String.valueOf(pacienteDAO.listarTodos().size()));
            lblMedicosActivos.setText(String.valueOf(medicoDAO.listarActivos().size()));
            long citas=citasDAO.listarTodos().stream().filter(
                    c->"PROGRAMADA".equals(c.getEstado())
                            ||"CONFIRMADA".equals(c.getEstado())).count();
            lblCitasProgramadas.setText(String.valueOf(citas));

        }catch(RuntimeException e){
            ControllerUtil.mostrar(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Base de datos",
                    e.getMessage()==null?
                            "No se pudo cargar el resumen.":e.getMessage()
            );
        }
    }

    private void mostrarVista(String titulo,String archivo){

        try{
            Parent vista=FXMLLoader.load(getClass().getResource("/views/"+archivo));
            contentArea.getChildren().setAll(vista);
            lblSeccion.setText(titulo);

        }catch(IOException|RuntimeException e){
            ControllerUtil.mostrar(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "No se pudo abrir la vista",
                    e.getMessage()==null?archivo:e.getMessage()
            );
        }
    }

    @FXML
    private void mostrarInicio(){

        contentArea.getChildren().setAll(panelBienvenida);
        lblSeccion.setText("Inicio");
        actualizarResumen();
    }

    @FXML
    private void mostrarPacientes(){

        mostrarVista("Pacientes",
                "PacienteView.fxml");
    }

    @FXML
    private void mostrarMedicos(){

        mostrarVista("Médicos",
                "MedicoView.fxml");
    }

    @FXML
    private void mostrarEspecialidades(){

        mostrarVista("Especialidades",
                "EspecialidadView.fxml");
    }

    @FXML
    private void mostrarCitas(){

        mostrarVista("Citas","CitasView.fxml");
    }

    @FXML
    private void mostrarUsuarios(){

        if(Sesion.getUsuarioActual()!=null&&"ADMINISTRADOR"
                .equals(Sesion.getUsuarioActual()
                .getRol()))mostrarVista("Usuarios","UsuarioView.fxml"
        );
    }

    @FXML
    private void cerrarSesion(){

        try{Sesion.cerrar();
            Parent root=FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Stage stage=(Stage)contentArea.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("SIGAH - Iniciar sesión");
            stage.setWidth(860);
            stage.setHeight(560);
            stage.centerOnScreen();

        }catch(IOException e){
            ControllerUtil.mostrar(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",e.getMessage()
            );
        }
    }
}