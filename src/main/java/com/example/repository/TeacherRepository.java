package com.example.repository;

import com.example.entity.StudentProfileEntity;
import com.example.entity.TeacherProfileEntity;
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
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
  public void save(TeacherProfileEntity entity) {
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "INSERT INTO teacher ( profile_id, group_name, student_count, exam_id, step, file_name, last_message_id, exam_finished_student_count, student_list_to_string, visible) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

      // Execute the SQL statement
      preparedStatement.executeUpdate();

      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }



  public void update(TeacherProfileEntity entity) {
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "UPDATE teacher SET group_name = ?, student_count = ?, exam_id = ?, step = ?, file_name = ?, last_message_id = ?, " +
              "exam_finished_student_count = ?, student_list_to_string = ?, visible = ? WHERE profile_id = ?;";
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
      preparedStatement.setLong(10, entity.getProfileId());

      // Execute the SQL statement
      preparedStatement.executeUpdate();

      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public TeacherProfileEntity findById(Long id) {
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql="select * from teacher where profile_id = ?; ";
      PreparedStatement preparedStatement = con.prepareStatement(sql);
      preparedStatement.setLong(1, id);

      ResultSet rs = preparedStatement.executeQuery();
      TeacherProfileEntity entity = null;
      if (rs.next()) {
        entity = new TeacherProfileEntity();
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
      }
      con.close();
      return entity;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
}
  public TeacherProfileEntity findByExamId(int examId) {
    TeacherProfileEntity entity = null;
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "SELECT * FROM teacher WHERE exam_id = ?;";
      PreparedStatement preparedStatement = con.prepareStatement(sql);
      preparedStatement.setInt(1, examId);

      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        entity = new TeacherProfileEntity();
        entity.setId(rs.getInt("id")); // Assuming ID is part of the TeacherProfileEntity
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
      }

      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return entity;
  }
  public TeacherProfileEntity findByGroup(String group) {
    TeacherProfileEntity entity = null;
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "SELECT * FROM teacher WHERE group_name = ?;";
      PreparedStatement preparedStatement = con.prepareStatement(sql);
      preparedStatement.setString(1, group);

      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        entity = new TeacherProfileEntity();
        entity.setId(rs.getInt("id")); // Assuming ID is part of the TeacherProfileEntity
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
      }

      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return entity;
  }
  public TeacherProfileEntity findByFileName(String fileName) {
    TeacherProfileEntity entity = null;
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "SELECT * FROM teacher WHERE file_name = ?;";
      PreparedStatement preparedStatement = con.prepareStatement(sql);
      preparedStatement.setString(1, fileName);

      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        entity = new TeacherProfileEntity();
        entity.setId(rs.getInt("id")); // Assuming ID is part of the TeacherProfileEntity
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
      }

      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return entity;
  }

  public List<TeacherProfileEntity> findAll() {
    List<TeacherProfileEntity> entities = new ArrayList<>();
    try {
      Class.forName("org.postgresql.Driver");
      Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_project", "postgres", "root");

      String sql = "select * from teacher;";
      PreparedStatement preparedStatement = con.prepareStatement(sql);

      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        TeacherProfileEntity entity = new TeacherProfileEntity();
        entity.setGroup(rs.getString("group_name"));
        entity.setStudentCount(rs.getInt("student_count"));
        entity.setExamId(rs.getInt("exam_id"));
        entity.setStep(ProfileStep.valueOf(rs.getString("step")));
        entity.setFileName(rs.getString("file_name"));
        entity.setLastMessageId(rs.getInt("last_message_id"));
        entity.setExamFinishedStudentCount(rs.getInt("exam_finished_student_count"));
        entity.setStudentListToString(rs.getString("student_list_to_string"));
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


//  public void save(TeacherProfileEntity entity){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Transaction t = session.beginTransaction();
//    session.save(entity);
//    t.commit();
//    System.out.println("successfully saved");
//    factory.close();
//    session.close();
//  }
//  public void update(TeacherProfileEntity entity){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    TeacherProfileEntity studentProfileEntity = session.get(TeacherProfileEntity.class,entity.getId());
//    if (studentProfileEntity == null){
//      System.out.println("Student not found");
//      return;
//    }
//    session.update(entity);
//    System.out.println("successfully saved");
//    factory.close();
//    session.close();
//  }
//  public TeacherProfileEntity findById(Long id){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Query query = session.createQuery("from TeacherProfileEntity where id = " + id,TeacherProfileEntity.class);
//    List<TeacherProfileEntity>list = query.getResultList();
//    factory.close();
//    session.close();
//    if (list.isEmpty()){
//      return null;
//    }
//    return list.get(0);
//  }
//  public TeacherProfileEntity findByExamId(Integer id){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Query query = session.createQuery("from TeacherProfileEntity where examId = " + id,TeacherProfileEntity.class);
//    List<TeacherProfileEntity>list = query.getResultList();
//    factory.close();
//    session.close();
//    if (list.isEmpty()){
//      return null;
//    }
//    return list.get(0);
//  }
//  public TeacherProfileEntity findByGroup(String group){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Query query = session.createQuery("from TeacherProfileEntity where group = " + group,TeacherProfileEntity.class);
//    List<TeacherProfileEntity>list = query.getResultList();
//    factory.close();
//    session.close();
//    if (list.isEmpty()){
//      return null;
//    }
//    return list.get(0);
//  }
//  public TeacherProfileEntity findByFileName(String name){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Query query = session.createQuery("from TeacherProfileEntity where fileName = " + name,TeacherProfileEntity.class);
//    List<TeacherProfileEntity>list = query.getResultList();
//    factory.close();
//    session.close();
//    if (list.isEmpty()){
//      return null;
//    }
//    return list.get(0);
//  }
//  public List<TeacherProfileEntity> findAll( ){
//    StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//    Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
//    SessionFactory factory = meta.getSessionFactoryBuilder().build();
//    Session session = factory.openSession();
//    Query query = session.createQuery("from TeacherProfileEntity",TeacherProfileEntity.class);
//    List<TeacherProfileEntity> list = query.getResultList();
//    System.out.println(list.toString());
//    return list;
//  }
}
