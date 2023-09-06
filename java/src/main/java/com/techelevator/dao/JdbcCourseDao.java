package com.techelevator.dao;

import com.techelevator.model.Course;
import com.techelevator.CourseGraph;
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
        String sql = "SELECT * FROM courses WHERE course_id NOT IN (SELECT course_id FROM course_enrollments WHERE user_id = ?) " +
                "ORDER BY course_number";


        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            course.setPrerequisites(getPrerequisitesForCourse(course));
            remainingCourses.add(course);
        }
        return remainingCourses;
    }

    @Override
    public int remainingHours(int userId) {
        int remainingHours = 0;
        List<Course> remainingCourses = remainingCourses(userId);
        for (Course course : remainingCourses){
            remainingHours += course.getHours() * course.getTimesToTake();
        }
        return remainingHours;
    }

    @Override
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


    private Course mapRowToCourse(SqlRowSet rs) {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCoursePrefix(rs.getString("course_prefix"));
        course.setCourseNumber(rs.getString("course_number"));
        course.setCourseName(rs.getString("course_name"));
        course.setHours(rs.getInt("hours"));
        course.setTimesToTake(rs.getInt("times_to_take"));
        return course;
    }

    public boolean hasCircularDependencies() {
        return courseGraph.hasCircularDependencies();
    }

    public List<Course> performTopologicalSort(int id) {
        // Create an instance of CourseGraph
        CourseGraph courseGraph = new CourseGraph();

        // Retrieve all courses from the database that student is yet to take
        List<Course> remainingCourses = remainingCourses(id);

        // Add each course to the CourseGraph
        for (Course course : remainingCourses) {
            courseGraph.addCourse(course);
        }
        // Add prerequisite edges to vertexes with prerequisite courses
        for (Course course : remainingCourses) {
            if (course.getPrerequisites().size() > 0) {
                // Fetch prerequisites for the current course from the database
                List<Course> prerequisites = getPrerequisitesForCourse(course);

                // Add prerequisites to the CourseGraph
                for (Course prerequisite : prerequisites) {
                    courseGraph.addPrerequisite(course, prerequisite);
                }
            }
        }

        return courseGraph.performTopologicalSort();
    }

    private List<Course> getPrerequisitesForCourse(Course course) {
        List<Course> prerequisites = new ArrayList<>();
        String sql = "SELECT c.course_id, c. course_prefix, c.course_number, c.course_name, c.hours, c.times_to_take " +
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
