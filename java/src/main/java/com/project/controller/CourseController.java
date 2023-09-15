package com.project.controller;

import com.project.CourseScheduler;
import com.project.dao.CourseDao;
import com.project.model.Course;
import com.project.model.DegreePlanDTO;
import com.project.model.Semester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("")
public class CourseController {
    private final CourseDao courseDao;


    @Autowired
    public CourseController(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @RequestMapping(path = "/courses/{id}/recommended-order", method = RequestMethod.GET)
    public ResponseEntity<DegreePlanDTO> getRecommendedCourseOrder(@PathVariable int id) {
        List<Course> sortedCourses = courseDao.performTopologicalSort(id);
        int numOfSemesters = (int) Math.ceil((double) (courseDao.remainingHours(id) - 13) / 18);
        CourseScheduler courseScheduler = new CourseScheduler(sortedCourses, numOfSemesters);
        if (courseDao.hasCircularDependencies()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            List<Semester> semesters = courseScheduler.generateSchedule();
            List<Course> takenCourses = courseDao.takenCourses(id);

            DegreePlanDTO degreePlan = new DegreePlanDTO(semesters, takenCourses);
            return ResponseEntity.ok(degreePlan);
        }
    }

}
