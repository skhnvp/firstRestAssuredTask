package ru.stepup.mockClasses;

import lombok.Data;

import java.util.List;

@Data
public class Student {
    Integer id;
    String name;
    List<Integer> marks;

    public Student() {
    }

    public Student(int id, String name, Integer... grades) {
        this.id = id;
        this.name = name;
        this.marks = List.of(grades);
    }

    public Student(String name, Integer... grades) {
        this.name = name;
        this.marks = List.of(grades);
    }

}
