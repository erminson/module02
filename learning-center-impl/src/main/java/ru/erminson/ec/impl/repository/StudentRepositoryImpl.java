package ru.erminson.ec.impl.repository;

import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.api.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private final List<Student> students;

    public StudentRepositoryImpl() {
        this.students = new ArrayList<>();
    }

    public StudentRepositoryImpl(List<Student> students) {
        this.students = students;
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
}
