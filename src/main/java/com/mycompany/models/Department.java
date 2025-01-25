package com.mycompany.models;

public class Department {
    private int id;
    private String name;
    private Integer chiefId; // Может быть null

    // Конструкторы
    public Department() {}

    public Department(int id, String name, Integer chiefId) {
        this.id = id;
        this.name = name;
        this.chiefId = chiefId;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChiefId() {
        return chiefId;
    }

    public void setChiefId(Integer chiefId) {
        this.chiefId = chiefId;
    }
}