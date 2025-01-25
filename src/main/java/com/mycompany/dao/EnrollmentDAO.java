package com.mycompany.dao;

import com.mycompany.models.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

    // Метод для добавления нового Enrollment
    public void addEnrollment(Enrollment enrollment) throws SQLException {
        String sql = "INSERT INTO enrollment (student_id, course_id, date, final_grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setDate(3, enrollment.getDate());
            pstmt.setBigDecimal(4, enrollment.getFinalGrade());
            pstmt.executeUpdate();
        }
    }

    // Метод для поиска Enrollment по student_id и/или course_id
    public List<Enrollment> searchEnrollments(Integer studentId, Integer courseId) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM enrollment WHERE 1=1");

        if (studentId != null) {
            sql.append(" AND student_id = ?");
        }
        if (courseId != null) {
            sql.append(" AND course_id = ?");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (studentId != null) {
                pstmt.setInt(index++, studentId);
            }
            if (courseId != null) {
                pstmt.setInt(index++, courseId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("date"),
                        rs.getBigDecimal("final_grade")
                    );
                    enrollments.add(enrollment);
                }
            }
        }

        return enrollments;
    }

    // Чтение записи по ID
    public Enrollment getEnrollmentById(int id) throws SQLException {
        String sql = "SELECT * FROM enrollment WHERE id = ?";
        Enrollment enrollment = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                enrollment = new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("date"),
                        rs.getBigDecimal("final_grade")
                );
            }
        }
        return enrollment;
    }

    // Обновление записи
    public void updateEnrollment(Enrollment enrollment) throws SQLException {
        String sql = "UPDATE enrollment SET student_id = ?, course_id = ?, date = ?, final_grade = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setDate(3, enrollment.getDate());
            stmt.setBigDecimal(4, enrollment.getFinalGrade());
            stmt.setInt(5, enrollment.getId());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteEnrollment(int id) throws SQLException {
        String sql = "DELETE FROM enrollment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получение всех записей
    public List<Enrollment> getAllEnrollments() throws SQLException {
        String sql = "SELECT * FROM enrollment";
        List<Enrollment> enrollments = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                enrollments.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("date"),
                        rs.getBigDecimal("final_grade")
                ));
            }
        }
        return enrollments;
    }
}