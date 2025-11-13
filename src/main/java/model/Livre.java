package model;


public class Livre {
    private int id;
    private int numero;
    private String titre;
    private String auteur;
    private int annee_publication;
    private boolean dispo;

    public Livre() {}

    public Livre(int numero, String titre, String auteur, int annee_publication) {
        this.numero = numero;
        this.titre = titre;
        this.auteur = auteur;
        this.annee_publication = annee_publication;
        this.dispo = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }


    public int getAnnee_publication() { return annee_publication; }
    public void setAnnee_publication(int annee_publication) { this.annee_publication = annee_publication; }

    public boolean isDispo() { return dispo; }
    public void setDispo(boolean dispo) { this.dispo = dispo; }

    @Override
    public String toString() {
        return titre + " - " + auteur + " (" + annee_publication + ")";
    }
}