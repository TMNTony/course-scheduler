package com.project.controller;

import com.project.dao.StudentDao;
import com.project.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        studentDao.createStudent(student);
        return ResponseEntity.ok("Student created successfully");
    }

    @RequestMapping(path = "/students", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> getStudents(@RequestParam int id) {
        List<Student> students = studentDao.getStudents(id);
        return ResponseEntity.ok(students);
    }

}
