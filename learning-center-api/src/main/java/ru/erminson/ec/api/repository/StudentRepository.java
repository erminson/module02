package ru.erminson.ec.api.repository;

import ru.erminson.ec.model.entity.Student;

import java.util.List;

public interface StudentRepository {
    boolean addStudent(String name);
    boolean removeStudent(String name);
    Student getStudentByName(String name) throws Exception;
    List<Student> getAllStudents();
    boolean isExistsStudent(String name);
}
