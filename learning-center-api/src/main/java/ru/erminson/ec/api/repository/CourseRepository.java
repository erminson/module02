package ru.erminson.ec.api.repository;

import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.Topic;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.List;

public interface CourseRepository {
    List<Course> getAllCourses();
    Course getCourseByTitle(String title) throws IllegalInitialDataException;
    List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException;
}
