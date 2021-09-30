package ru.erminson.ec.api.repository;

import ru.erminson.ec.entity.Student;

import java.util.List;

public interface StudentRepository {
    boolean addStudent(String name);
    boolean removeStudent(String name) throws Exception;
    Student getStudentByName(String name) throws Exception;
    List<Student> getAllStudents();
    boolean isExistsStudent(String name);
}
