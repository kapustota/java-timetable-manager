package com.mycompany.dao;

import com.mycompany.models.Department;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private Connection connection;

    public DepartmentDAO(Connection connection) {
        this.connection = connection;
    }

    // Создание новой записи
    public void addDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO department (name, chief_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, department.getName());
            if (department.getChiefId() != null) {
                stmt.setInt(2, department.getChiefId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.executeUpdate();

            // Получение сгенерированного ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Чтение записи по ID
    public Department getDepartmentById(int id) throws SQLException {
        String sql = "SELECT * FROM department WHERE id = ?";
        Department department = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                department = new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("chief_id") != 0 ? rs.getInt("chief_id") : null
                );
            }
        }
        return department;
    }

    // Обновление записи
    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE department SET name = ?, chief_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getName());
            if (department.getChiefId() != null) {
                stmt.setInt(2, department.getChiefId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, department.getId());
            stmt.executeUpdate();
        }
    }

    // Удаление записи
    public void deleteDepartment(int id) throws SQLException {
        String sql = "DELETE FROM department WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получение всех записей
    public List<Department> getAllDepartments() throws SQLException {
        String sql = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("chief_id") != 0 ? rs.getInt("chief_id") : null
                ));
            }
        }
        return departments;
    }
}