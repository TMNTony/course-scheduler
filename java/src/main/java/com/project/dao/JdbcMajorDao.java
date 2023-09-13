package com.project.dao;

import com.project.model.Major;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcMajorDao implements MajorDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcMajorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createMajor() {

    }

    @Override
    public List<Major> getMajors() {
        List<Major> majors = new ArrayList<>();
        String sql = "SELECT * FROM majors";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()){
            Major major = mapRowToMajor(results);
            majors.add(major);
        }
        return majors;
    }

    private Major mapRowToMajor(SqlRowSet rs) {
        Major major = new Major();
        major.setMajorId(rs.getInt("major_id"));
        major.setMajor(rs.getString("major"));

        return major;
    }
}
