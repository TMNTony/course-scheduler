package com.project.dao;

import com.project.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JbdcStudentDao implements StudentDao {
    private final JdbcTemplate jdbcTemplate;

    public JbdcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, major_id, advisor_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), student.getMajorId(), student.getAdvisorId());
    }
}
