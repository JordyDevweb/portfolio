package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Livre {
    private final StringProperty titre;
    private final StringProperty auteur;
    private final StringProperty genre;
    private final StringProperty annee;
    private int id;  // Ajout de l'attribut id

    public Livre(String titre, String auteur, String genre, String annee) {
        this.id = id;  // Initialisation de l'id
        this.titre = new SimpleStringProperty(titre);
        this.auteur = new SimpleStringProperty(auteur);
        this.genre = new SimpleStringProperty(genre);
        this.annee = new SimpleStringProperty(annee);
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public StringProperty auteurProperty() {
        return auteur;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public StringProperty anneeProperty() {
        return annee;
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public String getAuteur() {
        return auteur.get();
    }

    public void setAuteur(String auteur) {
        this.auteur.set(auteur);
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getAnnee() {
        return annee.get();
    }

    public void setAnnee(String annee) {
        this.annee.set(annee);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
