package com.project.model;

import java.util.List;

public class StudentMajorDto {
    private List<Student> students;
    private List<Major> majors;

    public StudentMajorDto(List<Student> students, List<Major> majors) {
        this.students = students;
        this.majors = majors;
    }

    // Getters and setters for students and majors

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Major> getMajors() {
        return majors;
    }

    public void setMajors(List<Major> majors) {
        this.majors = majors;
    }
}
