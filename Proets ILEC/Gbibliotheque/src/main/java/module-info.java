module org.example.gbibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.gbibliotheque to javafx.fxml;
    exports org.example.gbibliotheque;
}