package com.mycompany.models;

import java.sql.Time;

public class Lesson {
    private int id;
    private int lessonNumber;
    private Time startTime;
    private Time endTime;

    // Конструкторы
    public Lesson() {}

    public Lesson(int lessonNumber, Time startTime, Time endTime) {
        this.lessonNumber = lessonNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Геттеры и сеттеры
    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}