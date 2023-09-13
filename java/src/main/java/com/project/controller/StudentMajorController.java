package com.project.controller;

import com.project.dao.StudentDao;
import com.project.dao.MajorDao;
import com.project.model.Student;
import com.project.model.Major;
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
    public ResponseEntity<CombinedDataResponse> getCombinedData(@RequestParam int id) {
        List<Student> students = studentDao.getStudents(id);
        List<Major> majors = majorDao.getMajors();

        // Combine the data as needed into a custom response object
        CombinedDataResponse combinedData = new CombinedDataResponse(students, majors);

        return ResponseEntity.ok(combinedData);
    }

    // Define a custom response object to hold combined data
    private static class CombinedDataResponse {
        private List<Student> students;
        private List<Major> majors;

        public CombinedDataResponse(List<Student> students, List<Major> majors) {
            this.students = students;
            this.majors = majors;
        }

        // Getters and setters for students and majors

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        public List<Major> getMajors() {
            return majors;
        }

        public void setMajors(List<Major> majors) {
            this.majors = majors;
        }
    }
}

