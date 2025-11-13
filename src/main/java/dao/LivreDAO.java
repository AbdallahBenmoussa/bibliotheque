package dao;

import database.DatabaseConnection;
import model.Livre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    public void create(Livre livre) {
        String sql = "INSERT INTO Livre (numero, titre, auteur, annee_publication, dispo) VALUES (?, ?,  ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, livre.getNumero());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnnee_publication());
            stmt.setBoolean(5, livre.isDispo());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                livre.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> findAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livre livre = new Livre();
                livre.setId(rs.getInt("id"));
                livre.setNumero(rs.getInt("numero"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));
                livre.setAnnee_publication(rs.getInt("annee_publication"));
                livre.setDispo(rs.getBoolean("dispo"));
                livres.add(livre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    public List<Livre> findAvailable() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livre WHERE dispo = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livre livre = new Livre();
                livre.setId(rs.getInt("id"));
                livre.setNumero(rs.getInt("numero"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));
                livre.setAnnee_publication(rs.getInt("annee_publication"));
                livre.setDispo(rs.getBoolean("dispo"));
                livres.add(livre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    public Livre findByNumero(int numero) {
        String sql = "SELECT * FROM Livre WHERE numero = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livre livre = new Livre();
                livre.setId(rs.getInt("id"));
                livre.setNumero(rs.getInt("numero"));
                livre.setTitre(rs.getString("titre"));
                livre.setAuteur(rs.getString("auteur"));
                livre.setAnnee_publication(rs.getInt("annee_publication"));
                livre.setDispo(rs.getBoolean("dispo"));
                return livre;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDisponibilite(int livreId, boolean dispo) {
        String sql = "UPDATE Livre SET dispo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, dispo);
            stmt.setInt(2, livreId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}