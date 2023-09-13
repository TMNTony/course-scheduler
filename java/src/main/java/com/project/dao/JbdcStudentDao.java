package com.project.dao;

import com.project.model.Course;
import com.project.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
public class JbdcStudentDao implements StudentDao {
    private final JdbcTemplate jdbcTemplate;

    public JbdcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Create a KeyHolder to retrieve the generated key

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"student_id"});
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            return ps;
        }, keyHolder);

        // Get the generated student ID
        int studentId = keyHolder.getKey().intValue();

        // Now you have the generated studentId, and you can use it to insert into other tables
        insertIntoStudentMajor(studentId, student.getMajorId());
        insertIntoStudentAdvisor(studentId, student.getAdvisorId());
    }

    private void insertIntoStudentMajor(int studentId, int majorId) {
        String sql = "INSERT INTO student_major (student_id, major_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, studentId, majorId);
    }

    private void insertIntoStudentAdvisor(int studentId, int advisorId) {
        String sql = "INSERT INTO student_advisor (student_id, advisor_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, studentId, advisorId);
    }


    @Override
    public List<Student> getStudents(int id) {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT s.*, sa.advisor_id, sm.major_id " +
                "FROM students s " +
                "INNER JOIN student_advisor sa ON s.student_id = sa.student_id " +
                "INNER JOIN student_major sm ON s.student_id = sm.student_id " +
                "WHERE sa.advisor_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        while (results.next()) {
            Student student = mapRowToStudent(results);
            students.add(student);
        }
        return students;
    }

    private Student mapRowToStudent(SqlRowSet rs) {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setAdvisorId(rs.getInt("advisor_id"));
        student.setMajorId(rs.getInt("major_id"));
        return student;
    }
}
