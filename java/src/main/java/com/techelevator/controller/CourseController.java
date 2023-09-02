package com.techelevator.controller;

import com.techelevator.dao.CourseDao;
import com.techelevator.model.Course;
import com.techelevator.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("")
public class CourseController {
    private final CourseDao courseDao;


    @Autowired
    public CourseController(CourseService courseService, CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @GetMapping("/{id}/courses/recommended-order")
    public ResponseEntity<List<Course>> getRecommendedCourseOrder(@PathVariable int id) {
        if (courseDao.hasCircularDependencies()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            List<Course> sortedCourses = courseDao.performTopologicalSort();
            return ResponseEntity.ok(sortedCourses);
        }
    }

}
