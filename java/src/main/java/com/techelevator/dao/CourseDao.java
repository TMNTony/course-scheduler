package com.techelevator.dao;

import com.techelevator.model.Course;

import java.util.List;

public interface CourseDao {
    List<Course> allCourses();
    List<Course> remainingCourses(int id);
    int totalHours();
    int remainingHours();

    boolean hasCircularDependencies();

    List<Course> performTopologicalSort(int id);
}
