package com.project;

import com.project.model.Course;
import com.project.model.Semester;

import java.util.*;

/**
 * The CourseScheduler class is responsible for generating a course schedule based on a list of remaining courses
 * and various constraints. It uses a set of rules to prioritize and schedule courses across multiple semesters.
 */
public class CourseScheduler {
    private List<Course> remainingCourses; // List of all courses yet to be completed
    private List<Semester> semesters; // List to store the resulting schedule
    private int maxHoursPerSemester = 18; // Maximum hours allowed per semester
    private int unchangedPasses = 0;
    private List<Semester> previousSchedule = new ArrayList<>();

    /**
     * Constructs a CourseScheduler with the list of remaining courses and the number of semesters.
     *
     * @param remainingCourses  List of courses yet to be completed.
     * @param numOfSemesters    Number of semesters to schedule the courses.
     */
    public CourseScheduler(List<Course> remainingCourses, int numOfSemesters) {
        this.remainingCourses = new ArrayList<>(remainingCourses);
        this.semesters = new ArrayList<>();
        initializeSemesters(numOfSemesters);
    }

    /**
     * Constructs a CourseScheduler with the list of remaining courses, the number of semesters, and a maximum
     * credit hours per semester.
     *
     * @param remainingCourses       List of courses yet to be completed.
     * @param numOfSemesters         Number of semesters to schedule the courses.
     * @param maxHoursPerSemester    Maximum credit hours allowed per semester.
     */
    public CourseScheduler(List<Course> remainingCourses, int numOfSemesters, int maxHoursPerSemester) {
        this.remainingCourses = remainingCourses;
        this.semesters = new ArrayList<>();
        initializeSemesters(numOfSemesters);
        this.maxHoursPerSemester = maxHoursPerSemester;
    }


    private void initializeSemesters(int numSemesters) {
        for (int i = 0; i < numSemesters; i++) {
            semesters.add(new Semester(i));
        }
    }
    /**
     * Generates a course schedule based on the remaining courses and constraints.
     *
     * @return A list of semesters representing the course schedule.
     */
    public List<Semester> generateSchedule() {
        List<Course> firstSemester = new ArrayList<>();
        List<Course> studentTeaching = new ArrayList<>();
        List<Course> remainingCoursesWithMUPrefix = new ArrayList<>();
        List<Course> remainingCoursesWithoutMUPrefix = new ArrayList<>();

        sortCourses(remainingCourses, firstSemester, studentTeaching, remainingCoursesWithMUPrefix, remainingCoursesWithoutMUPrefix);
        Set<Course> scheduledCourses = new HashSet<>();

        int passCount = 0;
        boolean continueScheduling = true;
        final int maxPasses = 20;

        while (continueScheduling && passCount < maxPasses) {
            passCount++;
            scheduleCourses(scheduledCourses, firstSemester);
            scheduleCourses(scheduledCourses, remainingCoursesWithMUPrefix);
            scheduleCourses(scheduledCourses, remainingCoursesWithoutMUPrefix);

            if (scheduleConverged(semesters, 10)) {
                continueScheduling = false;
            }
        }

        Semester finalSemester = new Semester(semesters.size());
        for (Course teaching : studentTeaching) {
            finalSemester.addCourse(teaching);
        }
        semesters.add(finalSemester);
        return semesters;
    }
    /**
     * Schedule unscheduled courses in available semesters.
     *
     * @param scheduledCourses     A set of courses that have already been scheduled.
     * @param unscheduledCourses   A list of courses that need to be scheduled.
     */
    private void scheduleCourses(Set<Course> scheduledCourses, List<Course> unscheduledCourses) {

        unscheduledCourses.sort(courseComparator);

        for (Course course : unscheduledCourses) {
            // Check if the course has already been scheduled
            if (!scheduledCourses.contains(course)) {
                int timesScheduled = 0;
                int semesterId = 0;

                while (timesScheduled < course.getTimesToTake() && semesterId < semesters.size()) {
                    Semester semester = semesters.get(semesterId);

                    // Check if all conditions are met for placing the course in this semester
                    if (allChecks(course, semester)) {
                        semester.addCourse(course);
                        scheduledCourses.add(course); // Mark the course as scheduled
                        timesScheduled++;
                    }

                    semesterId++; // Move on to the next semester
                }
            }
        }
        for (Course course : unscheduledCourses) {
            if (!scheduledCourses.contains(course)) {
                // Schedule the course in the last semester
                Semester lastSemester = semesters.get(semesters.size() - 1);
                lastSemester.addCourse(course);
                scheduledCourses.add(course); // Mark the course as scheduled
            }
        }
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
    /**
     * Checks all conditions to determine if a course can be scheduled in a semester.
     *
     * @param course           The course to be checked.
     * @param semester         The semester in which the course is being considered for scheduling.
     * @return                 True if all conditions are met, indicating the course can be scheduled; otherwise, false.
     */
    private boolean allChecks(Course course, Semester semester) {
        if (courseHasNoUnscheduledPrerequisites(course, semesters) && noPrerequisitesInSemester(course, semester) && noDuplicateCourses(course, semester)) {
            return courseFitsInSemester(course, semester, maxHoursPerSemester);
        }
        return false;
    }

    private boolean courseFitsInSemester(Course course, Semester semester, int maxCreditHoursPerSemester) {
        return semester.getTotalHours() + course.getHours() <= maxCreditHoursPerSemester;
    }

    private boolean noPrerequisitesInSemester(Course course, Semester semester) {
        for (Course check : course.getPrerequisites()) {
            if (semester.getCourses().contains(check)) {
                return false; // Found a prerequisite course in the semester
            }
        }
        return true; // None of the prerequisites are in the semester
    }

    private boolean noDuplicateCourses(Course course, Semester semester) {
        for (Course check : semester.getCourses()) {
            if (check.getCourseId() == course.getCourseId()) {
                return false;
            }
        }
        return true;
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

            // Break out of the loop if all prerequisites are scheduled
            if (unscheduledPrerequisites.isEmpty()) {
                break;
            }
        }

        // If any prerequisites are left unscheduled, return false
        return unscheduledPrerequisites.isEmpty();
    }
    /**
     * Sorts and categorizes courses into different lists based on specific criteria.
     *
     * @param remainingCourses              List of remaining courses to be sorted.
     * @param firstSemester                 List to store courses to be scheduled in the first semester.
     * @param studentTeaching               List to store student teaching courses.
     * @param remainingCoursesWithMUPrefix  List to store courses with a "MU" prefix.
     * @param remainingCoursesWithoutMUPrefix List to store courses without a "MU" prefix.
     */
    private void sortCourses(List<Course> remainingCourses, List<Course> firstSemester, List<Course> studentTeaching,
                             List<Course> remainingCoursesWithMUPrefix, List<Course> remainingCoursesWithoutMUPrefix) {
        for (Course course : remainingCourses) {
            int courseId = course.getCourseId();
            String coursePrefix = course.getCoursePrefix();

            if (isCourseInFirstSemester(courseId)) {
                addToSemesterList(firstSemester, course, course.getTimesToTake());
            } else if (isStudentTeachingCourse(courseId)) {
                studentTeaching.add(course);
            } else if (coursePrefix.startsWith("MU")) {
                addToSemesterList(remainingCoursesWithMUPrefix, course, course.getTimesToTake());
            } else {
                remainingCoursesWithoutMUPrefix.add(course);
            }
        }
    }

    private boolean isCourseInFirstSemester(int courseId) {
        int[] firstSemesterCourses = {16, 17, 1, 10, 15, 23, 19, 21, 36};
        for (int course : firstSemesterCourses) {
            if (courseId == course) {
                return true;
            }
        }
        return false;
    }

    private boolean isStudentTeachingCourse(int courseId) {
        return courseId == 55 || courseId == 54;
    }

    private void addToSemesterList(List<Course> semesterList, Course course, int timesToTake) {
        for (int i = 0; i < timesToTake; i++) {
            semesterList.add(course);
        }
    }
    /**
     * Comparator for comparing courses based on their course numbers.
     */
    Comparator<Course> courseComparator = new Comparator<Course>() {
        @Override
        public int compare(Course course1, Course course2) {
            // Assuming getCourseNumber() returns a string
            return course1.getCourseNumber().compareTo(course2.getCourseNumber());
        }
    };
}