package com.example.repository;


import com.example.entity.StudentProfileEntity;
import com.example.enums.ProfileStep;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class StudentRepository {
    public void save(StudentProfileEntity entity) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "INSERT INTO student ( profile_id, name, surname, grade, exam_id, step, finished_option_count, random_test_id, options, last_message_id, visible) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


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

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(StudentProfileEntity entity) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "UPDATE student SET name = ?, surname = ?, grade = ?, exam_id = ?, step = ?, " +
                    "finished_option_count = ?, random_test_id = ?, options = ?, last_message_id = ?, visible = ? " +
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
            preparedStatement.setLong(11, entity.getProfileId()); // Set the ID parameter

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public StudentProfileEntity findById(Long id) {
        StudentProfileEntity entity = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM student WHERE profile_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                entity = new StudentProfileEntity();
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
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }
    public List<StudentProfileEntity> findAllByExamID(int examId) {
        List<StudentProfileEntity> entities = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM student WHERE exam_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, examId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StudentProfileEntity entity = new StudentProfileEntity();
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
                entities.add(entity);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
    public List<StudentProfileEntity> findAll() {
        List<StudentProfileEntity> entities = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

            String sql = "SELECT * FROM student;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StudentProfileEntity entity = new StudentProfileEntity();
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
                entities.add(entity);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
    /*public void save(StudentProfileEntity entity){
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        session.save(entity);
        t.commit();
        System.out.println("successfully saved");
        factory.close();
        session.close();
    }
    public void update(StudentProfileEntity entity){
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        StudentProfileEntity studentProfileEntity = session.get(StudentProfileEntity.class,entity.getId());
        if (studentProfileEntity == null){
            System.out.println("Student not found");
            return;
        }
        session.update(entity);
        System.out.println("successfully saved");
        factory.close();
        session.close();
    }
    public StudentProfileEntity findById(Long id){
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Query query = session.createQuery("from StudentProfileEntity where id = " + id,StudentProfileEntity.class);
        List<StudentProfileEntity>list = query.getResultList();
        factory.close();
        session.close();
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    public List<StudentProfileEntity> findAllByExamID(Integer id){
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Query query = session.createQuery("from StudentProfileEntity where examId = " + id,StudentProfileEntity.class);
        List<StudentProfileEntity>list = query.getResultList();
        factory.close();
        session.close();
        return list;
    }
    public List<StudentProfileEntity> findAll( ){
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Query query = session.createQuery("from StudentProfileEntity",StudentProfileEntity.class);
        List<StudentProfileEntity> list = query.getResultList();
        System.out.println(list.toString());
        return list;
    }*/
}
