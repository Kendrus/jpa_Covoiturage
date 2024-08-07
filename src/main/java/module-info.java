module com.example.jpa_covoiturage {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires static lombok;
     requires java.mail;
    requires org.hibernate.orm.core;
    requires java.persistence;
    opens com.example.jpa_covoiturage to javafx.fxml;

    exports com.example.jpa_covoiturage.Model;
    exports com.example.jpa_covoiturage.Repository;
    opens com.example.jpa_covoiturage.Model;

    exports com.example.jpa_covoiturage;

}