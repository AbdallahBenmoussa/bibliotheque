package dao;

import database.DatabaseConnection;
import model.Bibliothecaire;

import java.sql.*;

public class BibliothecaireDAO {

    public Bibliothecaire findByMatricule(int matricule) {
        String sql = "SELECT b.id, b.matricule, p.nom, p.prenom, p.adresse " +
                "FROM Bibliothecaire b " +
                "JOIN Personne p ON b.personne_id = p.id " +
                "WHERE b.matricule = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, matricule);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Bibliothecaire bibliothecaire = new Bibliothecaire();
                bibliothecaire.setId(rs.getInt("id"));
                bibliothecaire.setMatricule(rs.getInt("matricule"));
                bibliothecaire.setNom(rs.getString("nom"));
                bibliothecaire.setPrenom(rs.getString("prenom"));
                bibliothecaire.setAdresse(rs.getString("adresse"));
                return bibliothecaire;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bibliothecaire ensureBibliothecaire(Bibliothecaire bibliothecaire) {
        Bibliothecaire existing = findByMatricule(bibliothecaire.getMatricule());
        if (existing != null) {
            return existing;
        }

        Connection conn = DatabaseConnection.getConnection();
        String insertPersonne = "INSERT INTO Personne (nom, prenom, adresse) VALUES (?, ?, ?)";
        String insertBibliothecaire = "INSERT INTO Bibliothecaire (matricule, personne_id) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtPersonne = conn.prepareStatement(insertPersonne, Statement.RETURN_GENERATED_KEYS);
            stmtPersonne.setString(1, bibliothecaire.getNom());
            stmtPersonne.setString(2, bibliothecaire.getPrenom());
            stmtPersonne.setString(3, bibliothecaire.getAdresse());
            stmtPersonne.executeUpdate();

            ResultSet rsPersonne = stmtPersonne.getGeneratedKeys();
            int personneId = 0;
            if (rsPersonne.next()) {
                personneId = rsPersonne.getInt(1);
            }

            PreparedStatement stmtBibliothecaire = conn.prepareStatement(insertBibliothecaire, Statement.RETURN_GENERATED_KEYS);
            stmtBibliothecaire.setInt(1, bibliothecaire.getMatricule());
            stmtBibliothecaire.setInt(2, personneId);
            stmtBibliothecaire.executeUpdate();

            ResultSet rsBibliothecaire = stmtBibliothecaire.getGeneratedKeys();
            if (rsBibliothecaire.next()) {
                bibliothecaire.setId(rsBibliothecaire.getInt(1));
            }

            conn.commit();
            return bibliothecaire;

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

        return bibliothecaire;
    }
}

