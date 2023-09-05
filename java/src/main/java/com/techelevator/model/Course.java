package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {


    int courseId;

    String coursePrefix;
    String courseNumber;
    String courseName;
    int hours;


    int timesToTake;
    List<Course> prerequisites;

    public Course(int courseId, String coursePrefix, String courseNumber, String courseName, int hours, int timesToTake, List<Course> prerequisites) {
        this.courseId = courseId;
        this.coursePrefix = coursePrefix;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.hours = hours;
        this.timesToTake = timesToTake;
        this.prerequisites = prerequisites;
    }

    public Course(int courseId, String coursePrefix, String courseNumber, String courseName, int hours, int timesToTake) {
        this.courseId = courseId;
        this.coursePrefix = coursePrefix;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.hours = hours;
        this.timesToTake = timesToTake;
        this.prerequisites = new ArrayList<>();
    }

    public Course() {
        this.prerequisites = new ArrayList<>();
    }

    ;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCoursePrefix() {
        return coursePrefix;
    }

    public void setCoursePrefix(String coursePrefix) {
        this.coursePrefix = coursePrefix;
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

    public void addPrerequisite(Course course) {
        this.prerequisites.add(course);
    }

    public int getTimesToTake() {
        return timesToTake;
    }

    public void setTimesToTake(int timesToTake) {
        this.timesToTake = timesToTake;
    }

    //    @Override
//    public String toString() {
//        return "Course{" +
//                "courseId=" + courseId +
//                ", courseNumber='" + courseNumber + '\'' +
//                ", courseName='" + courseName + '\'' +
//                ", hours=" + hours +
//                ", timesToTake=" + timesToTake +
//                ", prerequisites=" + prerequisites +
//                '}';
//    }
// Ensures nodes in graph are comparing correctly
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId == course.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
