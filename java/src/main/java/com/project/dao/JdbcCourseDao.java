package com.project.dao;

import com.project.model.Course;
import com.project.CourseGraph;
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

    @Override
    public List<Course> allCourses(int id) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.* FROM courses c " +
                "INNER JOIN major_courses mc ON c.course_id = mc.course_id " +
                "WHERE mc.major_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            Course course = mapRowToCourse(results);
            course.setPrerequisites(getPrerequisitesForCourse(course));
            courses.add(course);
        }

        return courses;
    }

    // Return list of courses not yet taken
    @Override
    public List<Course> remainingCourses(int studentId) {
        List<Course> remainingCourses = new ArrayList<>();

        String majorIdSql = "SELECT major_id FROM student_major WHERE student_id = ?";
        int majorId = jdbcTemplate.queryForObject(majorIdSql, Integer.class, studentId);

        String sql = "SELECT c.* FROM courses c " +
                "INNER JOIN major_courses mc ON c.course_id = mc.course_id " +
                "WHERE c.course_id NOT IN (SELECT course_id FROM course_enrollments WHERE user_id = ?) " +
                "AND mc.major_id = ? " +
                "ORDER BY c.course_number";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, studentId, majorId);

        while (results.next()) {
            Course course = mapRowToCourse(results);
            course.setPrerequisites(getPrerequisitesForCourse(course));
            remainingCourses.add(course);
        }

        return remainingCourses;
    }

    @Override
    public List<Course> takenCourses(int studentId) {
        List<Course> takenCourses = new ArrayList<>();

        String majorIdSql = "SELECT major_id FROM student_major WHERE student_id = ?";
        int majorId = jdbcTemplate.queryForObject(majorIdSql, Integer.class, studentId);

        String sql = "SELECT c.* FROM courses c " +
                "INNER JOIN major_courses mc ON c.course_id = mc.course_id " +
                "WHERE c.course_id IN (SELECT course_id FROM course_enrollments WHERE user_id = ?) " +
                "AND mc.major_id = ? " +
                "ORDER BY c.course_number";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, studentId, majorId);

        while (results.next()) {
            Course course = mapRowToCourse(results);
            course.setPrerequisites(getPrerequisitesForCourse(course));
            takenCourses.add(course);
        }

        return takenCourses;
    }


    // Calculates hours remaining to graduate
    @Override
    public int remainingHours(int studentId) {
        int remainingHours = 0;
        List<Course> remainingCourses = remainingCourses(studentId);
        for (Course course : remainingCourses) {
            remainingHours += course.getHours() * course.getTimesToTake();
        }
        return remainingHours;
    }

    //Calculates total hours in degree plan
    @Override
    public int totalHours(int majorId) {
        int totalHours = 0;
        String sql = "SELECT c.* FROM courses c " +
                "INNER JOIN major_courses mc ON c.course_id = mc.course_id " +
                "WHERE mc.major_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, majorId);
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

    @Override
    public boolean hasCircularDependencies() {
        return courseGraph.hasCircularDependencies();
    }

    @Override
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

    @Override
    public List<Course> getPrerequisitesForCourse(Course course) {
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
