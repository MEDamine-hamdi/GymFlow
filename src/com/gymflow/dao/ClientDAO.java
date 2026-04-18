package com.gymflow.dao;

import com.gymflow.model.Client;
import com.gymflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // ================= ADD CLIENT =================
    public void addClient(Client client) {

        String sql = "INSERT INTO client(nom, prenom, telephone, abonnement_id, date_abonnement, date_expiration) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());

            stmt.setInt(4, client.getAbonnementId());
            stmt.setDate(5, Date.valueOf(client.getDateAbonnement()));
            stmt.setDate(6, Date.valueOf(client.getDateExpiration()));

            stmt.executeUpdate();

            // ✅ get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE EXPIRATION =================
    public void updateClient(Client client) {
        String sql = "UPDATE client SET date_expiration=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(client.getDateExpiration()));
            ps.setInt(2, client.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET ALL CLIENTS =================
    public List<Client> getAllClients() {

        List<Client> list = new ArrayList<>();

        String sql = "SELECT c.*, a.nom AS abonnement_nom " +
                "FROM client c " +
                "LEFT JOIN abonnement a ON c.abonnement_id = a.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Client c = new Client();

                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setTelephone(rs.getString("telephone"));

                c.setAbonnementId(rs.getInt("abonnement_id"));
                c.setAbonnementNom(rs.getString("abonnement_nom"));

                Date d1 = rs.getDate("date_abonnement");
                Date d2 = rs.getDate("date_expiration");

                if (d1 != null) c.setDateAbonnement(d1.toLocalDate());
                if (d2 != null) c.setDateExpiration(d2.toLocalDate());

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}