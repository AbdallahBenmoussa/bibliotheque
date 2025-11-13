package dao;

import database.DatabaseConnection;
import model.Emprunt;
import model.Livre;
import model.Adherent;
import model.Bibliothecaire;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public void create(Emprunt emprunt) {
        String sql = "INSERT INTO Emprunt (date_emprunt, date_restitution_prevue, livre_id, adherent_id, bibliothecaire_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(emprunt.getDate_emprunt()));
            stmt.setDate(2, Date.valueOf(emprunt.getDate_restitution_prevue()));
            stmt.setInt(3, emprunt.getLivre_id());
            stmt.setInt(4, emprunt.getAdherent_id());
            stmt.setInt(5, emprunt.getBibliothecaire_id());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emprunt.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Emprunt> findCurrentEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.numero, l.titre, l.auteur, a.matricule as adherent_matricule, " +
                "a.nb_emprunts_encours, p_a.nom as adherent_nom, p_a.prenom as adherent_prenom, " +
                "b.matricule as bibliothecaire_matricule, p_b.nom as bibliothecaire_nom, " +
                "p_b.prenom as bibliothecaire_prenom " +
                "FROM Emprunt e " +
                "JOIN Livre l ON e.livre_id = l.id " +
                "JOIN Adherent a ON e.adherent_id = a.id " +
                "JOIN Personne p_a ON a.personne_id = p_a.id " +
                "JOIN Bibliothecaire b ON e.bibliothecaire_id = b.id " +
                "JOIN Personne p_b ON b.personne_id = p_b.id " +
                "WHERE e.date_restitution_effective IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprunt emprunt = new Emprunt();
                emprunt.setId(rs.getInt("id"));
                emprunt.setDate_emprunt(rs.getDate("date_emprunt").toLocalDate());
                emprunt.setDate_restitution_prevue(rs.getDate("date_restitution_prevue").toLocalDate());

                if (rs.getDate("date_prolongation") != null) {
                    emprunt.setDate_prolongation(rs.getDate("date_prolongation").toLocalDate());
                }

                Livre livre = new Livre();
                livre.setId(rs.getInt("livre_id"));
                livre.setNumero(rs.getInt("numero"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));
                emprunt.setLivre(livre);

                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("adherent_id"));
                adherent.setMatricule(rs.getInt("adherent_matricule"));
                adherent.setNom(rs.getString("adherent_nom"));
                adherent.setPrenom(rs.getString("adherent_prenom"));
                adherent.setNb_emprunts_encours(rs.getInt("nb_emprunts_encours"));
                emprunt.setAdherent(adherent);

                Bibliothecaire bibliothecaire = new Bibliothecaire();
                bibliothecaire.setId(rs.getInt("bibliothecaire_id"));
                bibliothecaire.setMatricule(rs.getInt("bibliothecaire_matricule"));
                bibliothecaire.setNom(rs.getString("bibliothecaire_nom"));
                bibliothecaire.setPrenom(rs.getString("bibliothecaire_prenom"));
                emprunt.setBibliothecaire(bibliothecaire);

                emprunts.add(emprunt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    public Emprunt findCurrentEmpruntByLivreId(int livreId) {
        String sql = "SELECT * FROM Emprunt WHERE livre_id = ? AND date_restitution_effective IS NULL LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livreId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Emprunt emprunt = new Emprunt();
                emprunt.setId(rs.getInt("id"));
                emprunt.setDate_emprunt(rs.getDate("date_emprunt").toLocalDate());
                emprunt.setDate_restitution_prevue(rs.getDate("date_restitution_prevue").toLocalDate());

                if (rs.getDate("date_prolongation") != null) {
                    emprunt.setDate_prolongation(rs.getDate("date_prolongation").toLocalDate());
                }

                emprunt.setLivre_id(rs.getInt("livre_id"));
                emprunt.setAdherent_id(rs.getInt("adherent_id"));
                emprunt.setBibliothecaire_id(rs.getInt("bibliothecaire_id"));

                return emprunt;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Emprunt findCurrentEmpruntById(int empruntId) {
        String sql = "SELECT * FROM Emprunt WHERE id = ? AND date_restitution_effective IS NULL LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empruntId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Emprunt emprunt = new Emprunt();
                emprunt.setId(rs.getInt("id"));
                emprunt.setDate_emprunt(rs.getDate("date_emprunt").toLocalDate());
                emprunt.setDate_restitution_prevue(rs.getDate("date_restitution_prevue").toLocalDate());

                if (rs.getDate("date_prolongation") != null) {
                    emprunt.setDate_prolongation(rs.getDate("date_prolongation").toLocalDate());
                }

                emprunt.setLivre_id(rs.getInt("livre_id"));
                emprunt.setAdherent_id(rs.getInt("adherent_id"));
                emprunt.setBibliothecaire_id(rs.getInt("bibliothecaire_id"));

                return emprunt;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void restituer(int empruntId) {
        String sql = "UPDATE Emprunt SET date_restitution_effective = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, empruntId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void prolonger(int empruntId) {
        String sql = "UPDATE Emprunt SET date_prolongation = ?, date_restitution_prevue = DATE_ADD(date_restitution_prevue, INTERVAL 14 DAY) WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, empruntId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}