package ru.erminson.ec.dto.yaml;

import ru.erminson.ec.entity.Course;

import java.util.List;

public class YamlCourseList {
    List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
