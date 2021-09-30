package ru.erminson.ec.impl.repository;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.impl.Main;
import ru.erminson.ec.dto.yaml.YamlStudentList;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.api.repository.StudentRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private static final String STUDENTS_FILE_NAME = "students.yaml";

    private final List<Student> students = new ArrayList<>();

    public StudentRepositoryImpl() {
        init();
    }

    @Override
    public boolean addStudent(String name) {
        Student student = new Student(name);
        return students.add(student);
    }

    @Override
    public boolean removeStudent(String name) throws Exception {
        Student student = getStudentByName(name);
        return students.remove(student);
    }

    @Override
    public Student getStudentByName(String name) throws Exception {
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElseThrow(Exception::new);
    }

    @Override
    public List<Student> getAllStudents() {
        return students;
    }

    @Override
    public boolean isExistsStudent(String name) {
        return students.stream()
                .map(Student::getName)
                .anyMatch(n -> n.equals(name));
    }

    private void init() {
        Yaml yaml = new Yaml(new Constructor(YamlStudentList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(STUDENTS_FILE_NAME);

        YamlStudentList list = yaml.loadAs(inputStream, YamlStudentList.class);
        students.addAll(list.getStudents());
    }
}
