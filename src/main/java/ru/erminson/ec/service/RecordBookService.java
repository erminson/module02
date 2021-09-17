package ru.erminson.ec.service;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;

import java.util.Set;

public interface RecordBookService {
    boolean enrollStudentOnCourse(Student student, Course course);
    RecordBook getRecordBookByStudent(Student student);
    boolean dismissStudentFromCourse(Student student);
    Set<Student> getAllStudentsOnCourses();
}
