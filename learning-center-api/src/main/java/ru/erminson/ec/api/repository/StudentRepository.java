package ru.erminson.ec.api.repository;

import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.List;

public interface StudentRepository {
    boolean addStudent(String name);
    boolean removeStudent(String name) throws IllegalInitialDataException;
    Student getStudentByName(String name) throws IllegalInitialDataException;
    List<Student> getAllStudents();
    boolean isExistsStudent(String name);
}
