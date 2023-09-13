package com.project.controller;

import com.project.dao.MajorDao;
import com.project.model.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("")
public class MajorController {

    private final MajorDao majorDao;

    @Autowired
    public MajorController(MajorDao majorDao){
        this.majorDao = majorDao;
    }
//    @GetMapping
//    public ResponseEntity<List<Major>> getMajors(){
//        List<Major> majors = majorDao.getMajors();
//        return ResponseEntity.ok(majors);
//    }
}
