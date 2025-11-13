package model;

public class Bibliothecaire extends Personne {
    private int id;
    private int matricule;

    public Bibliothecaire() {}

    public Bibliothecaire(String nom, String prenom, String adresse, int matricule) {
        super(nom, prenom, adresse);
        this.matricule = matricule;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMatricule() { return matricule; }
    public void setMatricule(int matricule) { this.matricule = matricule; }
}