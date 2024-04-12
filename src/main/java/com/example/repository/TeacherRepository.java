package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    public void save(ProfileEntity entity) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "INSERT INTO profile ( profile_id, group_name, student_count, exam_id, step, file_name, last_message_id, exam_finished_student_count, student_list_to_string, visible,role) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setLong(1, entity.getProfileId());
            preparedStatement.setString(2, entity.getGroup());
            preparedStatement.setInt(3, entity.getStudentCount());
            preparedStatement.setInt(4, entity.getExamId());
            preparedStatement.setString(5, entity.getStep().toString());
            preparedStatement.setString(6, entity.getFileName());
            preparedStatement.setInt(7, entity.getLastMessageId());
            preparedStatement.setInt(8, entity.getExamFinishedStudentCount());
            preparedStatement.setString(9, entity.getStudentListToString());
            preparedStatement.setBoolean(10, entity.getVisible());
            preparedStatement.setString(11, entity.getRole().toString());

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

            String sql = "UPDATE profile SET group_name = ?, student_count = ?, exam_id = ?, step = ?, file_name = ?, last_message_id = ?, " +
                    "exam_finished_student_count = ?, student_list_to_string = ?, visible = ?, role = ? WHERE profile_id = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Set parameters for the SQL statement based on the entity's attributes
            preparedStatement.setString(1, entity.getGroup());
            preparedStatement.setInt(2, entity.getStudentCount());
            preparedStatement.setInt(3, entity.getExamId());
            preparedStatement.setString(4, entity.getStep().toString());
            preparedStatement.setString(5, entity.getFileName());
            preparedStatement.setInt(6, entity.getLastMessageId());
            preparedStatement.setInt(7, entity.getExamFinishedStudentCount());
            preparedStatement.setString(8, entity.getStudentListToString());
            preparedStatement.setBoolean(9, entity.getVisible());
            preparedStatement.setString(10, entity.getRole().toString());
            preparedStatement.setLong(11, entity.getProfileId());

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
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "select * from profile where profile_id = ?; ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            ProfileEntity entity = null;
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id"));
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }
            con.close();
            if (entity != null && entity.getRole().equals(ProfileRole.TEACHER)) {
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ProfileEntity findByExamId(int examId) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE exam_id = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, examId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id")); // Assuming ID is part of the ProfileEntity
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (entity != null && entity.getRole().equals(ProfileRole.TEACHER)) {
            return entity;
        }
        return null;
    }

    public ProfileEntity findByGroup(String group) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE group_name = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, group);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id")); // Assuming ID is part of the ProfileEntity
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (entity != null && entity.getRole().equals(ProfileRole.TEACHER)) {
            return entity;
        }
        return null;
    }

    public ProfileEntity findByFileName(String fileName) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE file_name = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, fileName);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id")); // Assuming ID is part of the ProfileEntity
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (entity != null && entity.getRole().equals(ProfileRole.TEACHER)) {
            return entity;
        }
        return null;
    }
    public ProfileEntity findByIdAndRole(Long id, ProfileRole role) {
        ProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM profile WHERE profile_id = ? and role = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, role.name());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new ProfileEntity();
                entity.setId(rs.getInt("id")); // Assuming ID is part of the ProfileEntity
                entity.setProfileId(rs.getLong("profile_id"));
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
            return entity;

    }

    public List<ProfileEntity> findAll() {
        List<ProfileEntity> entities = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "select * from profile;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ProfileEntity entity = new ProfileEntity();
                entity.setGroup(rs.getString("group_name"));
                entity.setStudentCount(rs.getInt("student_count"));
                entity.setExamId(rs.getInt("exam_id"));
                entity.setStep(ProfileStep.valueOf(rs.getString("step")));
                entity.setFileName(rs.getString("file_name"));
                entity.setLastMessageId(rs.getInt("last_message_id"));
                entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
                entity.setStudentListToString(rs.getString("student_list_to_string"));
                entity.setVisible(rs.getBoolean("visible"));
                entity.setRole(ProfileRole.valueOf(rs.getString("role")));
                if (entity != null && entity.getRole().equals(ProfileRole.TEACHER)) {
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
