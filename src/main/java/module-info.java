module org.example.proyectofinalhospital {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.proyectofinalhospital to javafx.fxml;
    exports org.example.proyectofinalhospital;
}