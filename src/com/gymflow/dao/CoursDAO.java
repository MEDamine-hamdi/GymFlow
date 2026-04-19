package com.gymflow.dao;

import com.gymflow.model.Cours;
import com.gymflow.util.DBConnection;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.*;
public class CoursDAO {

    public void addCours(Cours c) {

        String sql = "INSERT INTO cours(specialite, coach_id, salle_id, date, heure_debut, heure_fin, nb_participants) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getSpecialite());
            ps.setInt(2, c.getCoachId());
            ps.setInt(3, c.getSalleId());
            ps.setDate(4, Date.valueOf(c.getDate()));
            ps.setTime(5, Time.valueOf(c.getHeureDebut()));
            ps.setTime(6, Time.valueOf(c.getHeureFin()));
            ps.setInt(7, c.getNbParticipants());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getAllCours() {

        List<Cours> list = new ArrayList<>();

        String sql = "SELECT c.*, co.nom AS coach_nom, s.num_salle FROM cours c " +
                "JOIN coach co ON c.coach_id = co.id " +
                "JOIN salle s ON c.salle_id = s.id";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Cours c = new Cours();

                c.setId(rs.getInt("id"));
                c.setSpecialite(rs.getString("specialite"));
                c.setCoachId(rs.getInt("coach_id"));
                c.setSalleId(rs.getInt("salle_id"));

                c.setCoachNom(rs.getString("coach_nom"));
                c.setSalleLabel("Salle " + rs.getInt("num_salle"));

                c.setDate(rs.getDate("date").toLocalDate());
                c.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
                c.setHeureFin(rs.getTime("heure_fin").toLocalTime());

                c.setNbParticipants(rs.getInt("nb_participants"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteCours(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cours WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}