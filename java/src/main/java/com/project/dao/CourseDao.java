package com.project.dao;

import com.project.model.Course;

import java.util.List;

public interface CourseDao {
    List<Course> allCourses(int studentId);
    List<Course> remainingCourses(int studentId);

    int remainingHours(int studentId);

    int totalHours(int majorId);

    boolean hasCircularDependencies();
    List<Course> performTopologicalSort(int id);
    List<Course> getPrerequisitesForCourse(Course course);
}
