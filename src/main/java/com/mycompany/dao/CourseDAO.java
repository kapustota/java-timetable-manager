package com.mycompany.dao;

import com.mycompany.models.Course;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }

    // Создание новой записи
    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO course (name, description, credits) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.executeUpdate();

            // Получение сгенерированного ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Чтение записи по ID
    public Course getCourseById(int id) throws SQLException {
        String sql = "SELECT * FROM course WHERE id = ?";
        Course course = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                course = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits")
                );
            }
        }
        return course;
    }

    // Обновление записи
    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE course SET name = ?, description = ?, credits = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getId());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public List<Course> searchCourses(Integer id, String name) throws SQLException {
        List<Course> courses = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE 1=1"); // Changed "courses" to "course"

        if (id != null) {
            sql.append(" AND id = ?");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (id != null) {
                stmt.setInt(paramIndex++, id);
            }
            if (name != null && !name.isEmpty()) {
                stmt.setString(paramIndex++, "%" + name + "%");
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits")
                    );
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    // Получение всех записей
    public List<Course> getAllCourses() throws SQLException {
        String sql = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits")
                ));
            }
        }
        return courses;
    }

    
}