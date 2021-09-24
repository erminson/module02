package ru.erminson.ec.repository;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.Main;
import ru.erminson.ec.dto.yaml.YamlCourseList;
import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.Topic;
import ru.erminson.ec.exception.IllegalInitialDataException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    private static final String COURSES_FILE_NAME = "courses.yaml";

    private List<Course> courses = new ArrayList<>();

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
                .orElseThrow(IllegalInitialDataException::new);
    }

    @Override
    public List<Topic> getTopicsByCourseTitle(String title) throws IllegalInitialDataException {
        Course course = this.getCourseByTitle(title);
        return course.getTopics();
    }

    private void init() {
        Yaml yaml = new Yaml(new Constructor(YamlCourseList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(COURSES_FILE_NAME);

        YamlCourseList list = yaml.loadAs(inputStream, YamlCourseList.class);
        courses.addAll(list.getCourses());
    }
}
