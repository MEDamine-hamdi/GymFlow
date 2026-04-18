package com.gymflow.dao;

import com.gymflow.model.Coach;
import com.gymflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {

    public void addCoach(Coach c) {

        String sql = "INSERT INTO coach(nom, prenom, telephone, annee_experience, salaire, specialite) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getTelephone());
            ps.setInt(4, c.getAnneeExperience());
            ps.setDouble(5, c.getSalaire());
            ps.setString(6, c.getSpecialite());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Coach> getAllCoachs() {

        List<Coach> list = new ArrayList<>();

        String sql = "SELECT * FROM coach";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Coach c = new Coach();

                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setTelephone(rs.getString("telephone"));
                c.setAnneeExperience(rs.getInt("annee_experience"));
                c.setSpecialite(rs.getString("specialite"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}