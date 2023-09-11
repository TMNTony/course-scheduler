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
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }
}
