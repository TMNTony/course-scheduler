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

    public CourseScheduler(List<Course> remainingCourses) {
        this.remainingCourses = new ArrayList<>(remainingCourses);
        this.semesters = new ArrayList<>();
        initializeSemesters(8);
    }

    //    Allows instructor to override max hours for semester
    public CourseScheduler(List<Course> remainingCourses, int maxHoursPerSemester) {
        this.remainingCourses = remainingCourses;
        this.semesters = new ArrayList<>();
        initializeSemesters(8);
        this.maxHoursPerSemester = maxHoursPerSemester;
    }

    private void initializeSemesters(int numSemesters) {
        for (int i = 0; i < numSemesters; i++) {
            semesters.add(new Semester(i));
        }
    }

    public List<Semester> generateSchedule() {
        List<Course> remainingCoursesWithMUPrefix = new ArrayList<>();
        List<Course> remainingCoursesWithoutMUPrefix = new ArrayList<>();

        for (Course course : remainingCourses) {
            if (course.getCoursePrefix().startsWith("MU")) {
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
            for (int i = 0; i < semesters.size(); i++) {
                Semester semester = semesters.get(i);
                ListIterator<Course> courseIterator = remainingCoursesWithMUPrefix.listIterator();
                while (courseIterator.hasNext()) {
                    Course course = courseIterator.next();
                    if (courseHasNoUnscheduledPrerequisites(course, semesters)) {
                        if (courseFitsInSemester(course, semester, this.maxHoursPerSemester)) {
                            semester.addCourse(course);
                            courseIterator.remove(); // Remove the course safely
                        } else {
                            if (i + 1 < semesters.size()) {
                                Semester nextSemester = semesters.get(i + 1);
                                if (courseFitsInSemester(course, nextSemester, this.maxHoursPerSemester)) {
                                    nextSemester.addCourse(course);
                                    courseIterator.remove(); // Remove the course safely
                                }
                            }
                        }
                    }
                }
            }

            // Second Pass: Assign other courses based on prerequisites and credit hour limits
            for (int i = 0; i < semesters.size(); i++) {
                Semester semester = semesters.get(i);
                ListIterator<Course> courseIterator = remainingCoursesWithoutMUPrefix.listIterator();
                while (courseIterator.hasNext()) {
                    Course course = courseIterator.next();
                    if (courseHasNoUnscheduledPrerequisites(course, semesters)) {
                        if (courseFitsInSemester(course, semester, this.maxHoursPerSemester)) {
                            semester.addCourse(course);
                            courseIterator.remove(); // Remove the course safely
                        } else {
                            if (i + 1 < semesters.size()) {
                                Semester nextSemester = semesters.get(i + 1);
                                if (courseFitsInSemester(course, nextSemester, this.maxHoursPerSemester)) {
                                    nextSemester.addCourse(course);
                                    courseIterator.remove(); // Remove the course safely
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