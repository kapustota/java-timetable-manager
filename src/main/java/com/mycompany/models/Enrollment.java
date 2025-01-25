package com.mycompany.models;

import java.sql.Date;
import java.math.BigDecimal;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private Date date;
    private BigDecimal finalGrade;

    // Конструкторы
    public Enrollment() {}

    public Enrollment(int id, int studentId, int courseId, Date date, BigDecimal finalGrade) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.finalGrade = finalGrade;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
    }
}