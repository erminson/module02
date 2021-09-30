package ru.erminson.ec.api.service;

import ru.erminson.ec.entity.Student;

import java.util.List;

public interface StudentService {
    boolean addStudent(String name);
    Student getStudentByName(String name) throws Exception;
    List<Student> getAllStudents();
    boolean removeStudent(String name) throws Exception;
}
