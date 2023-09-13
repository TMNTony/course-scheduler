package com.project.dao;

import com.project.model.Student;

import java.util.List;

public interface StudentDao {
    void createStudent(Student student);
    List<Student> getStudents(int id);
}
