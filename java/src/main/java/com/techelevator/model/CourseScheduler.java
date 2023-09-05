package com.techelevator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseScheduler {
    private List<Course> remainingCourses; // List of all courses yet to be completed
    private List<Semester> semesters; // List to store the resulting schedule
    private int maxHoursPerSemester = 18; // Maximum hours allowed per semester
    private int unchangedPasses = 0;
    private List<Semester> previousSchedule = new ArrayList<>();

    public CourseScheduler(List<Course> allCourses) {
        this.remainingCourses = new ArrayList<>(allCourses);
        this.semesters = new ArrayList<>();
    }

    //    Allows instructor to override max hours for semester
    public CourseScheduler(List<Course> allCourses, int maxHoursPerSemester) {
        this.remainingCourses = allCourses;
        this.semesters = new ArrayList<>();
        this.maxHoursPerSemester = maxHoursPerSemester;
    }

    public List<Semester> generateSchedule() {
        boolean continueScheduling = true;
        int passCount = 0;

        while (continueScheduling) {
            passCount++;

            // First Pass: Assign courses based on prerequisites and credit hour limits
            for (Semester semester : semesters) {
                List<Course> coursesToRemove = new ArrayList<>();
                for (Course course : remainingCourses) {
                    if (courseHasNoUnscheduledPrerequisites(course, semesters) &&
                            courseFitsInSemester(course, semester, this.maxHoursPerSemester)) {
                        semester.addCourse(course);
                        coursesToRemove.add(course);
                    }
                }
                remainingCourses.removeAll(coursesToRemove);
            }

            // Additional passes: Refine the schedule (e.g., reassign unassigned courses, reorder semesters, adjust limits)

            // Check termination conditions (e.g., maximum passes, convergence)
            int maxPasses = 20;
            if (passCount >= maxPasses || scheduleConverged(semesters, 10)) {
                continueScheduling = false;
            }
        }

        return semesters;
    }

    private boolean courseHasNoUnscheduledPrerequisites(Course course, List<Semester> semesters) {
        Set<Course> unscheduledPrerequisites = new HashSet<>(course.getPrerequisites());

        for (Semester semester : semesters) {
            for (Course courseInSemester : semester.getCourses()) {
                if (unscheduledPrerequisites.contains(courseInSemester)) {
                    unscheduledPrerequisites.remove(courseInSemester);
                    if (unscheduledPrerequisites.isEmpty()) {
                        // All prerequisites have been scheduled
                        return true;
                    }
                }
            }
        }

        // If any prerequisites are left unscheduled, return false
        return unscheduledPrerequisites.isEmpty();
    }

    private boolean courseFitsInSemester(Course course, Semester semester, int maxCreditHoursPerSemester) {
        return semester.getTotalHours() + course.getHours() <= maxCreditHoursPerSemester;
    }

    private boolean scheduleConverged(List<Semester> semesters, int consecutiveUnchangedPasses) {
        // Create a deep copy of the schedule
        List<Semester> copiedSchedule = new ArrayList<>();
        for (Semester semester : semesters) {
            copiedSchedule.add(new Semester(semester));
        }

        if (copiedSchedule.equals(previousSchedule)) {
            unchangedPasses++;
            if (unchangedPasses >= consecutiveUnchangedPasses) {
                return true; // Converged
            }
        } else {
            unchangedPasses = 0; // Reset counter
        }

        previousSchedule = copiedSchedule; // Update the previous schedule
        return false;
    }

}