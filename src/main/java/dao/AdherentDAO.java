package dao;

import database.DatabaseConnection;
import model.Adherent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {

    public void create(Adherent adherent) {
        String sqlPersonne = "INSERT INTO Personne (nom, prenom, adresse) VALUES (?, ?, ?)";
        String sqlAdherent = "INSERT INTO Adherent (matricule, date_ins, nb_emprunts_encours, personne_id) VALUES (?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtPersonne = conn.prepareStatement(sqlPersonne, Statement.RETURN_GENERATED_KEYS);
            stmtPersonne.setString(1, adherent.getNom());
            stmtPersonne.setString(2, adherent.getPrenom());
            stmtPersonne.setString(3, adherent.getAdresse());
            stmtPersonne.executeUpdate();

            ResultSet rs = stmtPersonne.getGeneratedKeys();
            int personneId = 0;
            if (rs.next()) {
                personneId = rs.getInt(1);
            }

            PreparedStatement stmtAdherent = conn.prepareStatement(sqlAdherent, Statement.RETURN_GENERATED_KEYS);
            stmtAdherent.setInt(1, adherent.getMatricule());
            stmtAdherent.setDate(2, Date.valueOf(adherent.getDate_ins()));
            stmtAdherent.setInt(3, adherent.getNb_emprunts_encours());
            stmtAdherent.setInt(4, personneId);
            stmtAdherent.executeUpdate();

            ResultSet rs2 = stmtAdherent.getGeneratedKeys();
            if (rs2.next()) {
                adherent.setId(rs2.getInt(1));
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Adherent> findAll() {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT a.*, p.nom, p.prenom, p.adresse FROM Adherent a " +
                "JOIN Personne p ON a.personne_id = p.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("id"));
                adherent.setMatricule(rs.getInt("matricule"));
                adherent.setDate_ins(rs.getDate("date_ins").toLocalDate());
                adherent.setNb_emprunts_encours(rs.getInt("nb_emprunts_encours"));
                adherent.setNom(rs.getString("nom"));
                adherent.setPrenom(rs.getString("prenom"));
                adherent.setAdresse(rs.getString("adresse"));
                adherents.add(adherent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adherents;
    }

    public Adherent findByMatricule(int matricule) {
        String sql = "SELECT a.*, p.nom, p.prenom, p.adresse FROM Adherent a " +
                "JOIN Personne p ON a.personne_id = p.id WHERE a.matricule = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, matricule);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("id"));
                adherent.setMatricule(rs.getInt("matricule"));
                adherent.setDate_ins(rs.getDate("date_ins").toLocalDate());
                adherent.setNb_emprunts_encours(rs.getInt("nb_emprunts_encours"));
                adherent.setNom(rs.getString("nom"));
                adherent.setPrenom(rs.getString("prenom"));
                adherent.setAdresse(rs.getString("adresse"));
                return adherent;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Adherent findById(int id) {
        String sql = "SELECT a.*, p.nom, p.prenom, p.adresse FROM Adherent a " +
                "JOIN Personne p ON a.personne_id = p.id WHERE a.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("id"));
                adherent.setMatricule(rs.getInt("matricule"));
                adherent.setDate_ins(rs.getDate("date_ins").toLocalDate());
                adherent.setNb_emprunts_encours(rs.getInt("nb_emprunts_encours"));
                adherent.setNom(rs.getString("nom"));
                adherent.setPrenom(rs.getString("prenom"));
                adherent.setAdresse(rs.getString("adresse"));
                return adherent;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateNbEmprunts(int adherentId, int nbEmprunts) {
        String sql = "UPDATE Adherent SET nb_emprunts_encours = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nbEmprunts);
            stmt.setInt(2, adherentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementNbEmprunts(int adherentId) {
        String sql = "UPDATE Adherent SET nb_emprunts_encours = nb_emprunts_encours + 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adherentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decrementNbEmprunts(int adherentId) {
        String sql = "UPDATE Adherent SET nb_emprunts_encours = CASE " +
                "WHEN nb_emprunts_encours > 0 THEN nb_emprunts_encours - 1 ELSE 0 END WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adherentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}