package com.project.model;

import java.util.List;

public class Major {

    int majorId;
    String major;

    List<Course> majorRequirements;

    public  Major(int majorId, String major, List<Course> majorRequirements){
        this.majorId = majorId;
        this.major = major;
        this.majorRequirements = majorRequirements;
    }
    public Major(){};
    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


}
