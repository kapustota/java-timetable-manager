package com.mycompany.dao;

import com.mycompany.models.Teacher;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    private Connection connection;

    public TeacherDAO(Connection connection) {
        this.connection = connection;
    }


    // Создание новой записи
    public void addTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO teacher (first_name, last_name, email, phone_number, department_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setString(4, teacher.getPhoneNumber());
            if (teacher.getDepartmentId() != null) {
                stmt.setInt(5, teacher.getDepartmentId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.executeUpdate();

            // Получение сгенерированного ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    teacher.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Чтение записи по ID
    public Teacher getTeacherById(int id) throws SQLException {
        String sql = "SELECT * FROM teacher WHERE id = ?";
        Teacher teacher = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                teacher = new Teacher(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getInt("department_id") != 0 ? rs.getInt("department_id") : null
                );
            }
        }
        return teacher;
    }

    // Обновление записи
    public void updateTeacher(Teacher teacher) throws SQLException {
        String sql = "UPDATE teacher SET first_name = ?, last_name = ?, email = ?, phone_number = ?, department_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setString(4, teacher.getPhoneNumber());
            if (teacher.getDepartmentId() != null) {
                stmt.setInt(5, teacher.getDepartmentId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, teacher.getId());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteTeacher(int id) throws SQLException {
        String sql = "DELETE FROM teacher WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Teacher> searchTeachers(Integer teacherId, String lastName, Integer departmentId) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM teacher WHERE 1=1");

        if (teacherId != null) {
            sql.append(" AND id = ?");
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql.append(" AND last_name LIKE ?");
        }
        if (departmentId != null) {
            sql.append(" AND department_id = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (teacherId != null) {
                stmt.setInt(index++, teacherId);
            }
            if (lastName != null && !lastName.isEmpty()) {
                stmt.setString(index++, "%" + lastName + "%");
            }
            if (departmentId != null) {
                stmt.setInt(index++, departmentId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getInt("department_id") != 0 ? rs.getInt("department_id") : null
                    ));
                }
            }
        }
        return teachers;
    }

    // Получение всех записей
    public List<Teacher> getAllTeachers() throws SQLException {
        String sql = "SELECT * FROM teacher";
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                teachers.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getInt("department_id") != 0 ? rs.getInt("department_id") : null
                ));
            }
        }
        return teachers;
    }
}