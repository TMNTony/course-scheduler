package com.techelevator.dao;

import com.techelevator.model.Course;

import java.util.List;

public interface CourseDao {
    List<Course> allCourses();
    List<Course> remainingCourses(int id);

    int remainingHours(int userId);

    int totalHours();

    boolean hasCircularDependencies();
    List<Course> performTopologicalSort(int id);
    List<Course> getPrerequisitesForCourse(Course course);
}
