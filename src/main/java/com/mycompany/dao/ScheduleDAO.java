package com.mycompany.dao;

import com.mycompany.models.Schedule;
import com.mycompany.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private Connection connection;

    public ScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    // Метод для добавления расписания
    public void addSchedule(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO schedule (course_id, teacher_id, day_number, lesson_id, room_number) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getTeacherId());
            pstmt.setInt(3, schedule.getDayNumber());
            pstmt.setInt(4, schedule.getLessonId());
            pstmt.setInt(5, schedule.getRoomNumber());
            pstmt.executeUpdate();
        }
    }


    // Check if the teacher is busy
    public boolean isTeacherBusy(int teacherId, int dayNumber, int lessonId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM schedule WHERE teacher_id = ? AND day_number = ? AND lesson_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, dayNumber);
            pstmt.setInt(3, lessonId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    // Check if the room is busy
    public boolean isRoomBusy(int roomNumber, int dayNumber, int lessonId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM schedule WHERE room_number = ? AND day_number = ? AND lesson_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomNumber);
            pstmt.setInt(2, dayNumber);
            pstmt.setInt(3, lessonId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }


    // Метод для обновления расписания
    public void updateSchedule(Schedule schedule) throws SQLException {
        String sql = "UPDATE schedule SET course_id = ?, teacher_id = ?, day_number = ?, lesson_id = ?, room_number = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getCourseId());
            pstmt.setInt(2, schedule.getTeacherId());
            pstmt.setInt(3, schedule.getDayNumber());
            pstmt.setInt(4, schedule.getLessonId());
            pstmt.setInt(5, schedule.getRoomNumber());
            pstmt.setInt(6, schedule.getId());
            pstmt.executeUpdate();
        }
    }

    // Метод для удаления расписания
    public void deleteSchedule(int id) throws SQLException {
        String sql = "DELETE FROM schedule WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Метод для получения всех расписаний
    public List<Schedule> getAllSchedules() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("id"),
                    rs.getInt("course_id"),
                    rs.getInt("teacher_id"),
                    rs.getInt("day_number"),
                    rs.getInt("lesson_id"),
                    rs.getInt("room_number")
                );
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    public List<Schedule> searchByStudentId(int studentId) throws SQLException {
        String sql = "SELECT s.* FROM schedule s " +
                     "JOIN enrollment e ON s.course_id = e.course_id " +
                     "WHERE e.student_id = ?";
        List<Schedule> schedules = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setCourseId(rs.getInt("course_id"));
                schedule.setTeacherId(rs.getInt("teacher_id"));
                schedule.setDayNumber(rs.getInt("day_number"));
                schedule.setLessonId(rs.getInt("lesson_id"));
                schedule.setRoomNumber(rs.getInt("room_number"));
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    // Search schedules by Teacher ID
    public List<Schedule> searchByTeacherId(int teacherId) throws SQLException {
        String sql = "SELECT * FROM schedule WHERE teacher_id = ?";
        List<Schedule> schedules = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setCourseId(rs.getInt("course_id"));
                schedule.setTeacherId(rs.getInt("teacher_id"));
                schedule.setDayNumber(rs.getInt("day_number"));
                schedule.setLessonId(rs.getInt("lesson_id"));
                schedule.setRoomNumber(rs.getInt("room_number"));
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    // Search schedules by Room ID
    public List<Schedule> searchByRoomId(int roomId) throws SQLException {
        String sql = "SELECT * FROM schedule WHERE room_number = ?";
        List<Schedule> schedules = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(rs.getInt("id"));
                schedule.setCourseId(rs.getInt("course_id"));
                schedule.setTeacherId(rs.getInt("teacher_id"));
                schedule.setDayNumber(rs.getInt("day_number"));
                schedule.setLessonId(rs.getInt("lesson_id"));
                schedule.setRoomNumber(rs.getInt("room_number"));
                schedules.add(schedule);
            }
        }
        return schedules;
    }

}