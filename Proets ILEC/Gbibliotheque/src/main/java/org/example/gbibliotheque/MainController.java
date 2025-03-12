package org.example.gbibliotheque;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {

    @FXML
    private AnchorPane contentPane;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gbibliotheque/adherents.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Gestion d'une Bibliothèque");
        stage.setScene(scene);
        stage.show();
    }

    public void loadView(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gbibliotheque/livres.fxml"));
        AnchorPane view = loader.load();
        contentPane.getChildren().setAll(view);
    }

    public static void main(String[] args) {
        launch();
    }


}
