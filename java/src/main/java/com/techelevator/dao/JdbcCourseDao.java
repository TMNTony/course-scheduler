package com.techelevator.dao;

import com.techelevator.model.Course;
import com.techelevator.model.CourseGraph;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCourseDao implements CourseDao {
    private final JdbcTemplate jdbcTemplate;
    private final CourseGraph courseGraph;

    public JdbcCourseDao(JdbcTemplate jdbcTemplate, CourseGraph courseGraph) {

        this.jdbcTemplate = jdbcTemplate;
        this.courseGraph = courseGraph;
    }

    public List<Course> allCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "select * from courses";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            course.setPrerequisites(getPrerequisitesForCourse(course));
            courses.add(course);
        }

        return courses;
    }

    @Override
    public List<Course> remainingCourses(int id) {
        List<Course> remainingCourses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE course_id NOT IN (SELECT course_id FROM course_enrollments WHERE user_id = ?)";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            remainingCourses.add(course);
        }

        return remainingCourses;
    }

    int remainingHours(int userId) {
        int remainingHours = totalHours();
        String sql = "SELECT c.course_id, c.hours, c.times_to_take, COALESCE(SUM(ce.times_taken), 0) AS total_times_taken " +
                "FROM courses c " +
                "LEFT JOIN course_enrollments ce ON c.course_id = ce.course_id " +
                "WHERE ce.user_id = ? " +
                "GROUP BY c.course_id, c.hours, c.times_to_take";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            int timesTaken = results.getInt("total_times_taken");
            remainingHours -= course.getHours() * (course.getTimesToTake() - timesTaken);
        }
        return remainingHours;
    }

    public int totalHours() {
        int totalHours = 0;
        String sql = "SELECT course_id, hours, times_to_take FROM courses";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            totalHours += course.getHours() * course.getTimesToTake();
        }
        return totalHours;
    }

    @Override
    public int remainingHours() {
        return 0;
    }


    private Course mapRowToCourse(SqlRowSet rs) {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseNumber(rs.getString("course_number"));
        course.setCourseName(rs.getString("course_name"));
        course.setHours(rs.getInt("hours"));
        course.setTimesToTake(rs.getInt("times_to_take"));
        return course;
    }

    public boolean hasCircularDependencies() {
        return courseGraph.hasCircularDependencies();
    }

    public List<Course> performTopologicalSort() {
        // Create an instance of CourseGraph
        CourseGraph courseGraph = new CourseGraph();

        // Retrieve all courses from the database
        List<Course> allCourses = allCourses();

        // Add each course to the CourseGraph
        for (Course course : allCourses) {
            courseGraph.addCourse(course);

            // Fetch prerequisites for the current course from the database
            List<Course> prerequisites = course.getPrerequisites();

            // Add prerequisites to the CourseGraph
            for (Course prerequisite : prerequisites) {
                courseGraph.addPrerequisite(course, prerequisite);

            }
        }

        // Perform topological sort on the CourseGraph
        List<Course> sortedCourses = courseGraph.performTopologicalSort();

        return sortedCourses;
    }

    private List<Course> getPrerequisitesForCourse(Course course) {
        List<Course> prerequisites = new ArrayList<>();
        String sql = "SELECT c.course_id, c.course_number, c.course_name, c.hours, c.times_to_take " +
                "FROM courses c " +
                "JOIN course_prerequisites cp " +
                "ON c.course_id = cp.prerequisite_course_id " +
                "WHERE cp.course_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, course.getCourseId());
        while (results.next()) {
            Course prerequisite = mapRowToCourse(results);
            prerequisites.add(prerequisite);
        }
        return prerequisites;
    }
}
