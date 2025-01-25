package com.mycompany.models;

public class Schedule {
    private int id;
    private int courseId;
    private int teacherId;
    private int dayNumber;
    private int lessonId;
    private int roomNumber;

    // Конструкторы
    public Schedule() {}

    public Schedule(int id, int courseId, int teacherId, int dayNumber, int lessonId, int roomNumber) {
        this.id = id;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.dayNumber = dayNumber;
        this.lessonId = lessonId;
        this.roomNumber = roomNumber;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}