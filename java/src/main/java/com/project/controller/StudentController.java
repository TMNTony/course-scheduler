package com.project.controller;

import com.project.dao.StudentDao;
import com.project.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("")
public class StudentController {
    private final StudentDao studentDao;

    @Autowired
    public StudentController(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity createStudent(@RequestBody Student student) {
        return ResponseEntity.ok("Student created successfully");
    }
}
