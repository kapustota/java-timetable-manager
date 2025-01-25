package com.mycompany.dao;

import com.mycompany.models.Lesson;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {
    private Connection connection;

    public LessonDAO(Connection connection) {
        this.connection = connection;
    }

    // Создание новой записи
    public void addLesson(Lesson lesson) throws SQLException {
        String sql = "INSERT INTO lesson (lesson_number, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lesson.getLessonNumber());
            stmt.setTime(2, lesson.getStartTime());
            stmt.setTime(3, lesson.getEndTime());
            stmt.executeUpdate();
        }
    }

    // Обновление записи
    public void updateLesson(Lesson lesson) throws SQLException {
        String sql = "UPDATE lesson SET lesson_number = ?, start_time = ?, end_time = ? WHERE lesson_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lesson.getLessonNumber());
            stmt.setTime(2, lesson.getStartTime());
            stmt.setTime(3, lesson.getEndTime());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteLesson(int lessonNumber) throws SQLException {
        String sql = "DELETE FROM lesson WHERE lesson_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lessonNumber);
            stmt.executeUpdate();
        }
    }

    // Получение занятия по номеру
    public Lesson getLessonById(int lessonNumber) throws SQLException {
        String sql = "SELECT * FROM lesson WHERE lesson_number = ?";
        Lesson lesson = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lesson = new Lesson(
                        rs.getInt("lesson_number"),
                        rs.getTime("start_time"),
                        rs.getTime("end_time")
                );
            }
        }
        return lesson;
    }

    // Получение всех записей
    public List<Lesson> getAllLessons() throws SQLException {
        String sql = "SELECT * FROM lesson";
        List<Lesson> lessons = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lessons.add(new Lesson(
                        rs.getInt("lesson_number"),
                        rs.getTime("start_time"),
                        rs.getTime("end_time")
                ));
            }
        }
        return lessons;
    }
}