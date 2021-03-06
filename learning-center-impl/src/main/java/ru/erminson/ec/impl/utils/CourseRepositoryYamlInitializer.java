package ru.erminson.ec.impl.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.impl.Main;
import ru.erminson.ec.impl.repository.CourseRepositoryImpl;
import ru.erminson.ec.model.dto.yaml.YamlCourseList;
import ru.erminson.ec.model.entity.Course;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryYamlInitializer {
    private static final String COURSES_FILE_NAME = "courses.yaml";

    private CourseRepositoryYamlInitializer() {
        throw new IllegalStateException("CourseRepositoryYamlInitializer utility class");
    }

    public static CourseRepository create() {
        List<Course> courses = loadCoursesFromYamlFile();
        return new CourseRepositoryImpl(courses);
    }

    private static List<Course> loadCoursesFromYamlFile() {
        Yaml yaml = new Yaml(new Constructor(YamlCourseList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(COURSES_FILE_NAME);

        YamlCourseList list = yaml.loadAs(inputStream, YamlCourseList.class);
        return new ArrayList<>(list.getCourses());
    }
}
