package com.techelevator.controller;

import com.techelevator.dao.CourseDao;
import com.techelevator.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("")
public class CourseController {
    private final CourseDao courseDao;


    @Autowired
    public CourseController(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @RequestMapping(path = "/courses/{id}/recommended-order", method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getRecommendedCourseOrder(@PathVariable int id) {
        if (courseDao.hasCircularDependencies()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            List<Course> sortedCourses = courseDao.performTopologicalSort(id);
            return ResponseEntity.ok(sortedCourses);
        }
    }

}
