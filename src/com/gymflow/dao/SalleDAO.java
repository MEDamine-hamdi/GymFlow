package com.gymflow.dao;

import com.gymflow.model.Salle;
import com.gymflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {

    public void addSalle(Salle s) {

        String sql = "INSERT INTO salle(num_salle, capacite) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getNumSalle());
            ps.setInt(2, s.getCapacite());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Salle> getAllSalles() {

        List<Salle> list = new ArrayList<>();

        String sql = "SELECT * FROM salle";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Salle s = new Salle();
                s.setId(rs.getInt("id"));
                s.setNumSalle(rs.getInt("num_salle"));
                s.setCapacite(rs.getInt("capacite"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteSalle(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM salle WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}