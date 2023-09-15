package com.project.controller;

import com.project.dao.StudentDao;
import com.project.dao.MajorDao;
import com.project.model.Student;
import com.project.model.Major;
import com.project.model.StudentMajorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("")  // Unique URL for combined data
public class StudentMajorController {

    private final StudentDao studentDao;
    private final MajorDao majorDao;

    @Autowired
    public StudentMajorController(StudentDao studentDao, MajorDao majorDao) {
        this.studentDao = studentDao;
        this.majorDao = majorDao;
    }

    @GetMapping  // Use @GetMapping to specify the HTTP method and URL
    public ResponseEntity<StudentMajorDto> getCombinedData(@RequestParam int id) {
        List<Student> students = studentDao.getStudents(id);
        List<Major> majors = majorDao.getMajors();

        // Combine the data as needed into a custom response object
        StudentMajorDto combinedData = new StudentMajorDto(students, majors);

        return ResponseEntity.ok(combinedData);
    }
}

