package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Adherent {
    private final StringProperty nom;
    private final StringProperty email;
    private final StringProperty telephone;
    private int id;  // Ajout de l'attribut id

    public Adherent(String nom, String email, String telephone) {
        this.nom = new SimpleStringProperty(nom);
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
    }

    // Propriétés de chaque champ
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    // Ajout des getters et setters pour l'id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
