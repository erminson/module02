package ru.erminson.ec.api.repository;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.Topic;
import ru.erminson.ec.exception.IllegalInitialDataException;

import java.util.List;

public interface CourseRepository {
    List<Course> getAllCourses();
    Course getCourseByTitle(String title) throws IllegalInitialDataException;
    List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException;
}
