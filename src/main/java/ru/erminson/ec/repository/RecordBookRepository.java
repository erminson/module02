package ru.erminson.ec.repository;

import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;

import java.util.List;

public interface RecordBookRepository {
    boolean addStudentWithRecordBook(Student student, RecordBook recordBook);
    RecordBook getRecordBook(Student student);
    boolean isStudentOnCourse(Student student);
    boolean removeStudentFromCourse(Student student);
    List<Student> getAllStudents();
}
