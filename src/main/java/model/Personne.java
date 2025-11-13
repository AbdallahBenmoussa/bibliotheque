package model;


import java.util.Objects;

public class Personne {
    private int id;
    private String nom;
    private String prenom;
    private String adresse;

    public Personne() {}

    public Personne(String nom, String prenom, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personne personne = (Personne) o;
        return id == personne.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}