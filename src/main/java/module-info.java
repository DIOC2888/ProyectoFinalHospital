
module org.example.proyectofinalhospital {

    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens org.example.proyectofinalhospital to javafx.fxml;
    exports org.example.proyectofinalhospital;

    opens org.example.proyectofinalhospital.model
            to javafx.fxml, org.hibernate.orm.core;

    exports org.example.proyectofinalhospital.model;
}
