package ru.erminson.ec.api.service;

import ru.erminson.ec.model.entity.Student;

import java.util.List;

public interface StudentService {
    boolean addStudent(String name);
    Student getStudentByName(String name);
    List<Student> getAllStudents();
    boolean removeStudent(String name);
}
