package ru.erminson.ec.api.service;

import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.Topic;

import java.util.List;

public interface CourseService {
    boolean add(Course course);
    boolean isExistCourseByTitle(String courseTitle);
    List<Course> getAllCourses();
    Course getCourseByTitle(String title);
    List<Topic> getTopicsByCourseTitle(String title);
}
