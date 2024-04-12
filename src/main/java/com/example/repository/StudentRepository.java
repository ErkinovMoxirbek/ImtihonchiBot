package com.example.repository;


import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class StudentRepository {
    public void save(ProfileEntity entity) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "INSERT INTO profile ( profile_id, name, surname, grade, exam_id, step, finished_option_count, random_test_id, options, last_message_id, visible, role) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";


            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Set parameters for the SQL statement based on the entity's attributes
            preparedStatement.setLong(1, entity.getProfileId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSurname());
            preparedStatement.setDouble(4, entity.getGrade());
            preparedStatement.setInt(5, entity.getExamId());
            preparedStatement.setString(6, entity.getStep().toString());
            preparedStatement.setInt(7, entity.getFinishedOptionCount());
            preparedStatement.setString(8, entity.getRandomTestId());
            preparedStatement.setString(9, entity.getOptions());
            preparedStatement.setInt(10, entity.getLastMessageId());
            preparedStatement.setBoolean(11, entity.getVisible());
            preparedStatement.setString(12, entity.getRole().toString());

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(ProfileEntity entity) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "UPDATE profile SET name = ?, surname = ?, grade = ?, exam_id = ?, step = ?, " +
                    "finished_option_count = ?, random_test_id = ?, options = ?, last_message_id = ?, visible = ?, role = ? " +
                    "WHERE profile_id = ?;";

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Set parameters for the SQL statement based on the entity's attributes
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurname());
            preparedStatement.setDouble(3, entity.getGrade());
            preparedStatement.setInt(4, entity.getExamId());
            preparedStatement.setString(5, entity.getStep().toString());
            preparedStatement.setInt(6, entity.getFinishedOptionCount());
            preparedStatement.setString(7, entity.getRandomTestId());
            preparedStatement.setString(8, entity.getOptions());
            preparedStatement.setInt(9, entity.getLastMessageId());
            preparedStatement.setBoolean(10, entity.getVisible());
            preparedStatement.setString(11, entity.getRole().toString());
            preparedStatement.setLong(12, entity.getProfileId()); // Set the ID parameter

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ProfileEntity findById(Long id) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE profile_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id"));
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setName(rs.getString("name"));
                entity.setSurname(rs.getString("surname"));
                entity.setGrade(rs.getDouble("grade"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFinishedOptionCount(rs.getInt("finished_option_count"));
                entity.setRandomTestId(rs.getString("random_test_id"));
                entity.setOptions(rs.getString("options"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (entity != null && entity.getRole().equals(ProfileRole.STUDENT)) {
            return entity;
        }
        return null;
    } public ProfileEntity findByIdAndRole(Long id,ProfileRole role) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE profile_id = ? and role = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, role.name());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id"));
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setName(rs.getString("name"));
                entity.setSurname(rs.getString("surname"));
                entity.setGrade(rs.getDouble("grade"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFinishedOptionCount(rs.getInt("finished_option_count"));
                entity.setRandomTestId(rs.getString("random_test_id"));
                entity.setOptions(rs.getString("options"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (entity != null && entity.getRole().equals(ProfileRole.STUDENT)) {
            return entity;
        }
        return null;
    }

    public List<ProfileEntity> findAllByExamID(int examId) {
        List<ProfileEntity> entities = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE exam_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, examId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ProfileEntity entity = new ProfileEntity();
                entity.setId(rs.getInt("id"));
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setName(rs.getString("name"));
                entity.setSurname(rs.getString("surname"));
                entity.setGrade(rs.getDouble("grade"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFinishedOptionCount(rs.getInt("finished_option_count"));
                entity.setRandomTestId(rs.getString("random_test_id"));
                entity.setOptions(rs.getString("options"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
                if (entity != null && entity.getRole().equals(ProfileRole.STUDENT)) {
                    entities.add(entity);
                }
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    public List<ProfileEntity> findAll() {
        List<ProfileEntity> entities = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ProfileEntity entity = new ProfileEntity();
                entity.setId(rs.getInt("id"));
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setName(rs.getString("name"));
                entity.setSurname(rs.getString("surname"));
                entity.setGrade(rs.getDouble("grade"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFinishedOptionCount(rs.getInt("finished_option_count"));
                entity.setRandomTestId(rs.getString("random_test_id"));
                entity.setOptions(rs.getString("options"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
                if (entity != null && entity.getRole().equals(ProfileRole.STUDENT)) {
                    entities.add(entity);
                }
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

}
