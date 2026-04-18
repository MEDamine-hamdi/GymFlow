package com.gymflow.dao;

import com.gymflow.model.Abonnement;
import com.gymflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO {

    // ================= GET ALL =================
    public List<Abonnement> getAllAbonnements() {

        List<Abonnement> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT id, nom, prix FROM abonnement";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Abonnement ab = new Abonnement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix")
                );
                list.add(ab);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= ADD =================
    public void addAbonnement(Abonnement ab) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO abonnement (nom, prix) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ab.getNom());
            ps.setDouble(2, ab.getPrix());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    public void deleteAbonnement(int id) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "DELETE FROM abonnement WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}