package ru.erminson.ec.impl.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.impl.Main;
import ru.erminson.ec.impl.repository.StudentRepositoryImpl;
import ru.erminson.ec.model.dto.yaml.YamlStudentList;
import ru.erminson.ec.model.entity.Student;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryYamlInitializer {
    private static final String STUDENTS_FILE_NAME = "students.yaml";

    private StudentRepositoryYamlInitializer() {
        throw new IllegalStateException("StudentRepositoryYamlInitializer utility class");
    }

    public static StudentRepository create() {
        List<Student> students = loadStudentFromYamlFile();
        return new StudentRepositoryImpl(students);
    }

    private static List<Student> loadStudentFromYamlFile() {
        Yaml yaml = new Yaml(new Constructor(YamlStudentList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(STUDENTS_FILE_NAME);

        YamlStudentList list = yaml.loadAs(inputStream, YamlStudentList.class);
        return new ArrayList<>(list.getStudents());
    }
}
