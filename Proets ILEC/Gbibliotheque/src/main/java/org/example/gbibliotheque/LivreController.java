package org.example.gbibliotheque;

import Database.BaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Livre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class LivreController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField auteurField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField anneeField;

    @FXML
    private TableView<Livre> tableLivres;  // TableView pour afficher les livres
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, String> colGenre;
    @FXML
    private TableColumn<Livre, String> colAnnee;

    private ObservableList<Livre> livres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialiser les colonnes du TableView
        colTitre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        colAuteur.setCellValueFactory(cellData -> cellData.getValue().auteurProperty());
        colGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        colAnnee.setCellValueFactory(cellData -> cellData.getValue().anneeProperty());

        // Ajouter les livres à la table
        tableLivres.setItems(livres);
    }

    @FXML
    private void ajouterLivre() {
        if (titreField.getText().isEmpty() || auteurField.getText().isEmpty() ||
                genreField.getText().isEmpty() || anneeField.getText().isEmpty()) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        Livre livre = new Livre(titreField.getText(), auteurField.getText(), genreField.getText(), anneeField.getText());
        livres.add(livre);
        viderChamps();

        // Ajout du livre à la base de données
        String query = "INSERT INTO livres (titre, auteur, genre, anneePublication) VALUES (?, ?, ?, ?)";
        try (Connection conn = BaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getGenre());
            stmt.setString(4, livre.getAnnee());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierLivre() {
        Livre selectedLivre = tableLivres.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modification du livre");
            alert.setHeaderText("Quel champ souhaitez-vous modifier ?");

            ButtonType titreBtn = new ButtonType("Titre");
            ButtonType auteurBtn = new ButtonType("Auteur");
            ButtonType genreBtn = new ButtonType("Genre");
            ButtonType anneeBtn = new ButtonType("Année de publication");
            ButtonType cancelBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(titreBtn, auteurBtn, genreBtn, anneeBtn, cancelBtn);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() != cancelBtn) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Modification");
                dialog.setHeaderText("Entrez la nouvelle valeur :");

                result.ifPresent(button -> {
                    String champ;
                    if (button == titreBtn) {
                        champ = "titre";
                        dialog.setContentText("Nouveau titre :");
                    } else if (button == auteurBtn) {
                        champ = "auteur";
                        dialog.setContentText("Nouvel auteur :");
                    } else if (button == genreBtn) {
                        champ = "genre";
                        dialog.setContentText("Nouveau genre :");
                    } else if (button == anneeBtn) {
                        champ = "anneePublication";
                        dialog.setContentText("Nouvelle année de publication :");
                    } else {
                        champ = "";
                    }

                    dialog.showAndWait().ifPresent(nouvelleValeur -> {
                        String query = "UPDATE livres SET " + champ + " = ? WHERE id = ?";
                        try (Connection conn = BaseManager.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(query)) {

                            stmt.setString(1, nouvelleValeur);
                            stmt.setInt(2, selectedLivre.getId());
                            stmt.executeUpdate();

                            // Mise à jour locale dans la table
                            if (button == titreBtn) {
                                selectedLivre.setTitre(nouvelleValeur);
                            } else if (button == auteurBtn) {
                                selectedLivre.setAuteur(nouvelleValeur);
                            } else if (button == genreBtn) {
                                selectedLivre.setGenre(nouvelleValeur);
                            } else if (button == anneeBtn) {
                                selectedLivre.setAnnee(nouvelleValeur);
                            }

                            // Rafraîchissement de la TableView
                            tableLivres.refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun livre sélectionné");
            alert.setHeaderText("Veuillez sélectionner un livre à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    private void supprimerLivre() {
        Livre selectedLivre = tableLivres.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le livre ?");
            alert.setContentText("Voulez-vous vraiment supprimer " + selectedLivre.getTitre() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                livres.remove(selectedLivre);

                // Suppression du livre de la base de données
                String query = "DELETE FROM livres WHERE id = ?";
                try (Connection conn = BaseManager.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, selectedLivre.getId());
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void viderChamps() {
        titreField.clear();
        auteurField.clear();
        genreField.clear();
        anneeField.clear();
    }

}


