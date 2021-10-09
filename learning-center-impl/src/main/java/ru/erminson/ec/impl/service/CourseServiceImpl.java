package ru.erminson.ec.impl.service;

import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.api.service.CourseService;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.Topic;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.Collections;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean add(Course course) {
        return courseRepository.add(course);
    }

    @Override
    public boolean isExistCourseByTitle(String courseTitle) {
        return courseRepository.isExistCourseByTitle(courseTitle);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    @Override
    public Course getCourseByTitle(String title) {
        try {
            return courseRepository.getCourseByTitle(title);
        } catch (IllegalInitialDataException e) {
            return null;
        }
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) {
        try {
            return courseRepository.getTopicsByCourseTitle(title);
        } catch (IllegalInitialDataException e) {
            return Collections.emptyList();
        }
    }
}
