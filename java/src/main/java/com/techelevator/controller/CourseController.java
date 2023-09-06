package com.techelevator.controller;

import com.techelevator.CourseScheduler;
import com.techelevator.dao.CourseDao;
import com.techelevator.model.Course;
import com.techelevator.model.Semester;
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
    public ResponseEntity<List<Semester>> getRecommendedCourseOrder(@PathVariable int id) {
        List<Course> sortedCourses = courseDao.performTopologicalSort(id);
        int numOfSemesters = (int) Math.ceil((double) (courseDao.remainingHours(id) - 13) / 18);
        CourseScheduler courseScheduler = new CourseScheduler(sortedCourses, numOfSemesters);
        if (courseDao.hasCircularDependencies()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            List<Semester> semesters = courseScheduler.generateSchedule();
            return ResponseEntity.ok(semesters);
        }
    }

}
