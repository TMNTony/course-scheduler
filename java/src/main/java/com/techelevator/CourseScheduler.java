package com.techelevator;

import com.techelevator.model.Course;
import com.techelevator.model.Semester;

import java.util.*;

public class CourseScheduler {
    private List<Course> remainingCourses; // List of all courses yet to be completed
    private List<Semester> semesters; // List to store the resulting schedule
    private int maxHoursPerSemester = 18; // Maximum hours allowed per semester
    private int unchangedPasses = 0;
    private List<Semester> previousSchedule = new ArrayList<>();

    public CourseScheduler(List<Course> remainingCourses, int numOfSemesters) {
        this.remainingCourses = new ArrayList<>(remainingCourses);
        this.semesters = new ArrayList<>();
        initializeSemesters(numOfSemesters);
    }

    //    Allows instructor to override max hours for semester
    public CourseScheduler(List<Course> remainingCourses, int numOfSemesters, int maxHoursPerSemester) {
        this.remainingCourses = remainingCourses;
        this.semesters = new ArrayList<>();
        initializeSemesters(numOfSemesters);
        this.maxHoursPerSemester = maxHoursPerSemester;
    }

    private void initializeSemesters(int numSemesters) {
        for (int i = 0; i < numSemesters; i++) {
            semesters.add(new Semester(i + 1));
        }
    }

    public List<Semester> generateSchedule() {
        List<Course> remainingCoursesWithMUPrefix = new ArrayList<>();
        List<Course> remainingCoursesWithoutMUPrefix = new ArrayList<>();
        List<Course> studentTeaching = new ArrayList<>();

        for (Course course : remainingCourses) {
            if (course.getCourseId() == 55 || course.getCourseId() == 54){
                studentTeaching.add(course);
            } else if (course.getCoursePrefix().startsWith("MU")) {
                if (course.getTimesToTake() > 1) {
                    for (int i = 1; i < course.getTimesToTake(); i++){
                        remainingCoursesWithMUPrefix.add(course);
                    }
                }
                remainingCoursesWithMUPrefix.add(course);
            } else {
                remainingCoursesWithoutMUPrefix.add(course);
            }
        }

        boolean continueScheduling = true;
        int passCount = 0;

        while (continueScheduling) {
            passCount++;

            // First Pass: Assign courses with "MU" prefix based on prerequisites and credit hour limits
            for (Semester semester : semesters) {
                for (Course course : new ArrayList<>(remainingCoursesWithMUPrefix)) {
                    if (courseHasNoUnscheduledPrerequisites(course, semesters) && noPrerequisitesInSemester(course, semester) && noDuplicateCourses(course, semester)) {
                        if (courseFitsInSemester(course, semester, maxHoursPerSemester)) {
                            semester.addCourse(course);
                            remainingCoursesWithMUPrefix.remove(course); // Remove the course safely
                        } else {
                            int nextSemesterIndex = semesters.indexOf(semester) + 1;
                            if (nextSemesterIndex < semesters.size()) {
                                Semester nextSemester = semesters.get(nextSemesterIndex);
                                if (courseFitsInSemester(course, nextSemester, maxHoursPerSemester)) {
                                    nextSemester.addCourse(course);
                                    remainingCoursesWithMUPrefix.remove(course); // Remove the course safely
                                }
                            }
                        }
                    }
                }
            }

            // Second Pass: Assign other courses based on prerequisites and credit hour limits
            for (Semester semester : semesters) {
                for (Course course : new ArrayList<>(remainingCoursesWithoutMUPrefix)) {
                    if (courseHasNoUnscheduledPrerequisites(course, semesters) && noPrerequisitesInSemester(course, semester)) {
                        if (courseFitsInSemester(course, semester, maxHoursPerSemester) ) {
                            semester.addCourse(course);
                            remainingCoursesWithoutMUPrefix.remove(course); // Remove the course safely
                        } else {
                            int nextSemesterIndex = semesters.indexOf(semester) + 1;
                            if (nextSemesterIndex < semesters.size()) {
                                Semester nextSemester = semesters.get(nextSemesterIndex);
                                if (courseFitsInSemester(course, nextSemester, maxHoursPerSemester)) {
                                    nextSemester.addCourse(course);
                                    remainingCoursesWithoutMUPrefix.remove(course); // Remove the course safely
                                }
                            }
                        }
                    }
                }
            }


            // Additional passes: Refine the schedule (e.g., reassign unassigned courses, reorder semesters, adjust limits)

            // Check termination conditions (e.g., maximum passes, convergence)
            int maxPasses = 20;
            if (passCount >= maxPasses || scheduleConverged(semesters, 10)) {
                continueScheduling = false;
            }
        }
        Semester finalSemester = new Semester(semesters.size() + 1);
        for (Course teaching : studentTeaching) {
            finalSemester.addCourse(teaching);
        }
        semesters.add(finalSemester);
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

    private boolean noPrerequisitesInSemester(Course course, Semester semester) {
        for (Course check : course.getPrerequisites()) {
            return !semester.getCourses().contains(check);
        }
        return true;
    }
    private boolean noDuplicateCourses(Course course, Semester semester){
        for (Course check : semester.getCourses()) {
            if (check.getCourseId() == course.getCourseId()) {
                return false;
            }
        }
        return true;
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