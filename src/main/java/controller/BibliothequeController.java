package controller;

import dao.AdherentDAO;
import dao.LivreDAO;
import dao.EmpruntDAO;
import dao.BibliothecaireDAO;
import model.Adherent;
import model.Livre;
import model.Emprunt;
import model.Bibliothecaire;
import java.time.LocalDate;
import java.util.List;

public class BibliothequeController {
    private AdherentDAO adherentDAO;
    private LivreDAO livreDAO;
    private EmpruntDAO empruntDAO;
    private BibliothecaireDAO bibliothecaireDAO;
    private Bibliothecaire bibliothecaireCourant;

    public BibliothequeController() {
        this.adherentDAO = new AdherentDAO();
        this.livreDAO = new LivreDAO();
        this.empruntDAO = new EmpruntDAO();
        this.bibliothecaireDAO = new BibliothecaireDAO();
        this.bibliothecaireCourant = bibliothecaireDAO.ensureBibliothecaire(
                new Bibliothecaire("Admin", "Bibliothecaire", "Bibliotheque", 1001));
    }

    public void inscrireAdherent(String nom, String prenom, String adresse, int matricule) {
        Adherent adherent = new Adherent(nom, prenom, adresse, matricule, LocalDate.now());
        adherentDAO.create(adherent);
    }

    public List<Adherent> getTousLesAdherents() {
        return adherentDAO.findAll();
    }

    public Adherent trouverAdherentParMatricule(int matricule) {
        return adherentDAO.findByMatricule(matricule);
    }

    public void ajouterLivre(int numero, String titre, String auteur,  int anneePublication) {
        Livre livre = new Livre(numero, titre, auteur, anneePublication);
        livreDAO.create(livre);
    }

    public List<Livre> getTousLesLivres() {
        return livreDAO.findAll();
    }

    public List<Livre> getLivresDisponibles() {
        return livreDAO.findAvailable();
    }

    public Livre trouverLivreParNumero(int numero) {
        return livreDAO.findByNumero(numero);
    }

    public boolean realiserEmprunt(int matriculeAdherent, int numeroLivre) {
        Adherent adherent = adherentDAO.findByMatricule(matriculeAdherent);
        Livre livre = livreDAO.findByNumero(numeroLivre);

        if (adherent == null || livre == null) {
            return false;
        }

        if (!adherent.peutEmprunter() || !livre.isDispo()) {
            return false;
        }

        Emprunt emprunt = new Emprunt(livre, adherent, bibliothecaireCourant, LocalDate.now());
        empruntDAO.create(emprunt);

        adherent.incrementerEmprunts();
        adherentDAO.incrementNbEmprunts(adherent.getId());
        livreDAO.updateDisponibilite(livre.getId(), false);

        return true;
    }

    public boolean restituerLivre(int empruntId) {
        Emprunt emprunt = empruntDAO.findCurrentEmpruntById(empruntId);

        if (emprunt == null) {
            return false;
        }

        empruntDAO.restituer(empruntId);

        if (emprunt.getAdherent_id() > 0) {
            adherentDAO.decrementNbEmprunts(emprunt.getAdherent_id());
        }
        if (emprunt.getLivre_id() > 0) {
            livreDAO.updateDisponibilite(emprunt.getLivre_id(), true);
        }

        return true;
    }

    public boolean restituerLivreParNumeroLivre(int numeroLivre) {
        Livre livre = livreDAO.findByNumero(numeroLivre);
        if (livre == null) {
            return false;
        }

        Emprunt emprunt = empruntDAO.findCurrentEmpruntByLivreId(livre.getId());
        if (emprunt != null) {
            return restituerLivre(emprunt.getId());
        }

        if (!livre.isDispo()) {
            livreDAO.updateDisponibilite(livre.getId(), true);
        }
        livreDAO.updateDisponibilite(livre.getId(), true);
        return true;
    }

    public boolean prolongerEmprunt(int empruntId) {
        EmpruntDAO empruntDAO = new EmpruntDAO();
        List<Emprunt> emprunts = empruntDAO.findCurrentEmprunts();

        for (Emprunt emprunt : emprunts) {
            if (emprunt.getId() == empruntId) {
                empruntDAO.prolonger(empruntId);
                return true;
            }
        }
        return false;
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return empruntDAO.findCurrentEmprunts();
    }
}