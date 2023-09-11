package com.project.model;

public class Student {
    int studentId;
    String firstName;
    String lastName;
    int majorId;
    int advisorId;

    public Student(String firstName, String lastName, int majorId, int advisorId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.majorId = majorId;
        this.advisorId = advisorId;
    }
}
