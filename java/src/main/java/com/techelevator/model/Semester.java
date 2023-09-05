package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class Semester {
    private int semesterId;
    private List<Course> courses;

    public Semester(int semesterId){
        this.courses = new ArrayList<>();
        this.semesterId = semesterId;
    }
    public Semester(Semester semester){
        this.semesterId = semester.semesterId;
        this.courses = semester.getCourses();
    }
    public void addCourse(Course course){
        if (getTotalHours() + course.getHours() <= 18) {
            courses.add(course);
        }
    }
    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public int getTotalHours() {
        int totalHours = 0;
        for (Course course : courses) {
            totalHours += course.getHours();
        }
        return totalHours;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
