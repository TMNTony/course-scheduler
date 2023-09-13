package com.project.dao;

import com.project.model.Major;

import java.util.List;

public interface MajorDao {
    void createMajor();
    List<Major> getMajors();
}
