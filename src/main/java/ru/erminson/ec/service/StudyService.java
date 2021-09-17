package ru.erminson.ec.service;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface StudyService {
    boolean addStudentByName(String name);
    boolean removeStudentByName(String name) throws Exception;
    boolean enrollStudentOnCourse(String name, String courseTitle) throws Exception;
    boolean dismissStudentFromCourse(String name) throws Exception;
    boolean rateTopic(String name, String topicTitle, int score) throws Exception;
    RecordBook getRecordBookByStudentName(String name) throws Exception;
    List<Student> getAllStudents();
    Set<Student> getAllStudentsOnCourses();
    List<Student> getAllStudentsOutCourses();
}
