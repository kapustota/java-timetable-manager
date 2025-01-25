package com.mycompany.dao;

import com.mycompany.models.Student;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    // Создание новой записи
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO student (first_name, last_name, email, phone_number, enrollment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhoneNumber());
            stmt.setDate(5, student.getEnrollmentDate());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Student> searchStudents(String firstName, String lastName) throws SQLException {
        List<Student> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        if (firstName != null && !firstName.isEmpty()) {
            sql.append(" AND first_name LIKE ?");
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql.append(" AND last_name LIKE ?");
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (firstName != null && !firstName.isEmpty()) {
                pstmt.setString(index++, "%" + firstName + "%");
            }
            if (lastName != null && !lastName.isEmpty()) {
                pstmt.setString(index++, "%" + lastName + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("enrollment_date")
                    ));
                }
            }
        }
        return students;
    }

    // Чтение записи по ID
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        Student student = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student = new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("enrollment_date")
                );
            }
        }
        return student;
    }

    // Обновление записи
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE student SET first_name = ?, last_name = ?, email = ?, phone_number = ?, enrollment_date = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhoneNumber());
            stmt.setDate(5, student.getEnrollmentDate());
            stmt.setInt(6, student.getId());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получение всех записей
    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM student";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("enrollment_date")
                ));
            }
        }
        return students;
    }
}