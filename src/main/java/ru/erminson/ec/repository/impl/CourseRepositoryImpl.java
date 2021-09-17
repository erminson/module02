package ru.erminson.ec.repository.impl;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.Main;
import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.Topic;
import ru.erminson.ec.exception.IllegalInitialDataException;
import ru.erminson.ec.repository.CourseRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    private static final String COURSES_FILE_NAME = "courses.yaml";

    private List<Course> courses = new ArrayList<>();

    private void init() {
        TypeDescription customTypeDescription = new TypeDescription(Course.class);
        customTypeDescription.addPropertyParameters("topics", Topic.class);

        Constructor constructor = new Constructor(Course.class);
        constructor.addTypeDescription(customTypeDescription);

        Yaml yaml = new Yaml(constructor);
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(COURSES_FILE_NAME);

        for (Object object: yaml.loadAll(inputStream)) {
            if (object instanceof Course) {
                Course course = (Course) object;
                courses.add(course);
            }
        }
    }

    public CourseRepositoryImpl() {
        init();
    }

    @Override
    public List<Course> getAllCourses() {
        return courses;
    }

    @Override
    public Course getCourseByTitle(String title) throws IllegalInitialDataException {
        return courses.stream()
                .filter(course -> course.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalInitialDataException());
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException {
        Course course = this.getCourseByTitle(title);
        return course.getTopics();
    }
}
