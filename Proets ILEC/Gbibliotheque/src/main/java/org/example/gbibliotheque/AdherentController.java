package org.example.gbibliotheque;

import Database.BaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Adherent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class AdherentController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;

    @FXML
    private TableView<Adherent> tableAdherents;
    @FXML
    private TableColumn<Adherent, String> colNom;
    @FXML
    private TableColumn<Adherent, String> colEmail;
    @FXML
    private TableColumn<Adherent, String> colTelephone;

    private ObservableList<Adherent> adherents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colTelephone.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());

        tableAdherents.setItems(adherents);
    }

    @FXML
    private void ajouterAdherent() {
        if (nomField.getText().isEmpty() || emailField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
            afficherAlerte("Tous les champs doivent être remplis !");
            return;
        }

        Adherent adherent = new Adherent(nomField.getText(), emailField.getText(), telephoneField.getText());
        adherents.add(adherent);
        viderChamps();

        String query = "INSERT INTO adherents (nom, email, telephone) VALUES (?, ?, ?)";
        try (Connection conn = BaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, adherent.getNom());
            stmt.setString(2, adherent.getEmail());
            stmt.setString(3, adherent.getTelephone());
            stmt.executeUpdate();

        } catch (SQLException e) {
            afficherAlerte("Erreur SQL lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierAdherent() {
        Adherent selectedAdherent = tableAdherents.getSelectionModel().getSelectedItem();
        if (selectedAdherent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modification de l'adhérent");
            alert.setHeaderText("Quel champ souhaitez-vous modifier ?");

            ButtonType nomBtn = new ButtonType("Nom");
            ButtonType emailBtn = new ButtonType("Email");
            ButtonType telBtn = new ButtonType("Téléphone");
            ButtonType cancelBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(nomBtn, emailBtn, telBtn, cancelBtn);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() != cancelBtn) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Modification");
                dialog.setHeaderText("Entrez la nouvelle valeur :");

                String champ;
                if (result.get() == nomBtn) {
                    champ = "nom";
                    dialog.setContentText("Nouveau nom :");
                } else if (result.get() == emailBtn) {
                    champ = "email";
                    dialog.setContentText("Nouvel email :");
                } else {
                    champ = "telephone";
                    dialog.setContentText("Nouveau téléphone :");
                }

                dialog.showAndWait().ifPresent(nouvelleValeur -> {
                    String query = "UPDATE adherents SET " + champ + " = ? WHERE id = ?";
                    try (Connection conn = BaseManager.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, nouvelleValeur);
                        stmt.setInt(2, selectedAdherent.getId());
                        stmt.executeUpdate();

                        // Mettre à jour l'objet dans la liste et rafraîchir l'affichage
                        if (champ.equals("nom")) selectedAdherent.setNom(nouvelleValeur);
                        else if (champ.equals("email")) selectedAdherent.setEmail(nouvelleValeur);
                        else selectedAdherent.setTelephone(nouvelleValeur);

                        tableAdherents.refresh();

                    } catch (SQLException e) {
                        afficherAlerte("Erreur SQL lors de la modification : " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } else {
            afficherAlerte("Veuillez sélectionner un adhérent à modifier.");
        }
    }

    @FXML
    private void supprimerAdherent() {
        Adherent selectedAdherent = tableAdherents.getSelectionModel().getSelectedItem();
        if (selectedAdherent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer l'adhérent ?");
            alert.setContentText("Voulez-vous vraiment supprimer " + selectedAdherent.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adherents.remove(selectedAdherent);

                String query = "DELETE FROM adherents WHERE id = ?";
                try (Connection conn = BaseManager.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, selectedAdherent.getId());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    afficherAlerte("Erreur SQL lors de la suppression : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            afficherAlerte("Veuillez sélectionner un adhérent à supprimer.");
        }
    }

    private void viderChamps() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
    }

    private void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
