package com.project.model;

import java.util.List;

public class DegreePlanDTO {
    private List<Semester> plannedSemesters;
    private List<Course> completedCourses;

    public DegreePlanDTO(List<Semester> plannedSemesters, List<Course> completedCourses) {
        this.plannedSemesters = plannedSemesters;
        this.completedCourses = completedCourses;
    }

    public List<Semester> getPlannedSemesters() {
        return plannedSemesters;
    }

    public void setPlannedSemesters(List<Semester> plannedSemesters) {
        this.plannedSemesters = plannedSemesters;
    }

    public List<Course> getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(List<Course> completedCourses) {
        this.completedCourses = completedCourses;
    }
}
