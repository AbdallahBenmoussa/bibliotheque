package model;


import java.time.LocalDate;

public class Adherent extends Personne {
    private int id;
    private int matricule;
    private LocalDate date_ins;
    private int nb_emprunts_encours;

    public Adherent() {}

    public Adherent(String nom, String prenom, String adresse, int matricule, LocalDate date_ins) {
        super(nom, prenom, adresse);
        this.matricule = matricule;
        this.date_ins = date_ins;
        this.nb_emprunts_encours = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMatricule() { return matricule; }
    public void setMatricule(int matricule) { this.matricule = matricule; }

    public LocalDate getDate_ins() { return date_ins; }
    public void setDate_ins(LocalDate date_ins) { this.date_ins = date_ins; }

    public int getNb_emprunts_encours() { return nb_emprunts_encours; }
    public void setNb_emprunts_encours(int nb_emprunts_encours) {
        this.nb_emprunts_encours = nb_emprunts_encours;
    }

    public void incrementerEmprunts() {
        this.nb_emprunts_encours++;
    }

    public void decrementerEmprunts() {
        if (this.nb_emprunts_encours > 0) {
            this.nb_emprunts_encours--;
        }
    }

    public boolean peutEmprunter() {
        return nb_emprunts_encours < 5;
    }
}