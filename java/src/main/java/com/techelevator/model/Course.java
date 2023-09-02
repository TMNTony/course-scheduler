package com.techelevator.model;

import java.util.List;

public class Course {


    int courseId;
    String courseNumber;
    String courseName;
    int hours;


    int timesToTake;
    List<Course> prerequisites;

    public Course(int courseId, String courseNumber, String courseName, int hours, int timesToTake, List<Course> prerequisites) {
        this.courseId = courseId;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.hours = hours;
        this.timesToTake = timesToTake;
        this.prerequisites = prerequisites;
    }

    public Course(int courseId, String courseNumber, String courseName, int hours, int timesToTake) {
        this.courseId = courseId;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.hours = hours;
        this.timesToTake = timesToTake;
    }

    public Course() {
    }

    ;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getTimesToTake() {
        return timesToTake;
    }

    public void setTimesToTake(int timesToTake) {
        this.timesToTake = timesToTake;
    }


}
