package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

public class Semester {
    private int semesterId;
    private int totalHours;
    private List<Course> courses;

    public Semester(){
        this.courses = new ArrayList<>();
    }
    public Semester(Semester semester){
        this.semesterId = semester.semesterId;
        this. totalHours = semester.getTotalHours();
        this.courses = semester.getCourses();
    }
    public void addCourse(Course course){
        if (totalHours + course.getHours() <= 18) {
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
        return totalHours;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
