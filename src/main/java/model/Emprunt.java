package model;


import java.time.LocalDate;

public class Emprunt {
    private int id;
    private LocalDate date_emprunt;
    private LocalDate date_restitution_prevue;
    private LocalDate date_restitution_effective;
    private LocalDate date_prolongation;
    private int livre_id;
    private int adherent_id;
    private int bibliothecaire_id;

    private Livre livre;
    private Adherent adherent;
    private Bibliothecaire bibliothecaire;

    public Emprunt() {}

    public Emprunt(Livre livre, Adherent adherent, Bibliothecaire bibliothecaire, LocalDate date_emprunt) {
        this.livre = livre;
        this.adherent = adherent;
        this.bibliothecaire = bibliothecaire;
        this.livre_id = livre.getId();
        this.adherent_id = adherent.getId();
        this.bibliothecaire_id = bibliothecaire.getId();
        this.date_emprunt = date_emprunt;
        this.date_restitution_prevue = date_emprunt.plusWeeks(3);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate_emprunt() { return date_emprunt; }
    public void setDate_emprunt(LocalDate date_emprunt) { this.date_emprunt = date_emprunt; }

    public LocalDate getDate_restitution_prevue() { return date_restitution_prevue; }
    public void setDate_restitution_prevue(LocalDate date_restitution_prevue) { this.date_restitution_prevue = date_restitution_prevue; }

    public LocalDate getDate_restitution_effective() { return date_restitution_effective; }
    public void setDate_restitution_effective(LocalDate date_restitution_effective) { this.date_restitution_effective = date_restitution_effective; }

    public LocalDate getDate_prolongation() { return date_prolongation; }
    public void setDate_prolongation(LocalDate date_prolongation) { this.date_prolongation = date_prolongation; }

    public int getLivre_id() { return livre_id; }
    public void setLivre_id(int livre_id) { this.livre_id = livre_id; }

    public int getAdherent_id() { return adherent_id; }
    public void setAdherent_id(int adherent_id) { this.adherent_id = adherent_id; }

    public int getBibliothecaire_id() { return bibliothecaire_id; }
    public void setBibliothecaire_id(int bibliothecaire_id) { this.bibliothecaire_id = bibliothecaire_id; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }

    public Adherent getAdherent() { return adherent; }
    public void setAdherent(Adherent adherent) { this.adherent = adherent; }

    public Bibliothecaire getBibliothecaire() { return bibliothecaire; }
    public void setBibliothecaire(Bibliothecaire bibliothecaire) { this.bibliothecaire = bibliothecaire; }

    public void prolonger() {
        this.date_prolongation = LocalDate.now();
        this.date_restitution_prevue = this.date_restitution_prevue.plusWeeks(2);
    }

    public boolean estEnRetard() {
        LocalDate now = LocalDate.now();
        return now.isAfter(date_restitution_prevue) && date_restitution_effective == null;
    }
}