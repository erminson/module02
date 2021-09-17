package ru.erminson.ec.service.impl;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.repository.RecordBookRepository;
import ru.erminson.ec.service.RecordBookService;
import ru.erminson.ec.utils.RecordBookInitializer;

import java.util.Set;

public class RecordBookServiceImpl implements RecordBookService {
    private final RecordBookRepository recordBookRepository;

    public RecordBookServiceImpl(RecordBookRepository recordBookRepository) {
        this.recordBookRepository = recordBookRepository;
    }

    @Override
    public boolean enrollStudentOnCourse(Student student, Course course) {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(course);
        return recordBookRepository.addStudentWithRecordBook(student, recordBook);
    }

    @Override
    public RecordBook getRecordBookByStudent(Student student) {
        if (recordBookRepository.isStudentOnCourse(student)) {
            return recordBookRepository.getRecordBook(student);
        }

        return null;
    }

    @Override
    public boolean dismissStudentFromCourse(Student student) {
        if (recordBookRepository.isStudentOnCourse(student)) {
            return recordBookRepository.removeStudentFromCourse(student);
        }

        return false;
    }

    @Override
    public Set<Student> getAllStudentsOnCourses() {
        return recordBookRepository.getAllStudents();
    }
}
