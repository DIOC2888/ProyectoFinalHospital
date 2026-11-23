
module org.example.proyectofinalhospital {

    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens org.example.proyectofinalhospital.controller to javafx.fxml;
    opens org.example.proyectofinalhospital.dao to org.hibernate.orm.core;
    opens org.example.proyectofinalhospital.database to rg.hibernate.orm.core;

    opens org.example.proyectofinalhospital.model to javafx.fxml, org.hibernate.orm.core;
    exports org.example.proyectofinalhospital.model;

    opens org.example.proyectofinalhospital to javafx.fxml;
    exports org.example.proyectofinalhospital;
}
