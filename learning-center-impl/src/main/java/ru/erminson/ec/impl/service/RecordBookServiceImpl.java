package ru.erminson.ec.impl.service;

import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.api.service.RecordBookService;
import ru.erminson.ec.impl.utils.RecordBookInitializer;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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
    public List<Student> getAllStudentsOnCourses() {
        return recordBookRepository.getAllStudents();
    }

    @Override
    public int getNumberRatedTopics(Student student) {
        RecordBook recordBook = getRecordBookByStudent(student);
        if (recordBook == null) {
            return 0;
        }

        return (int)recordBook.getTopics().stream()
                .filter(topicScore -> topicScore.getScore() != 0)
                .count();
    }

    @Override
    public int getNumberTopics(Student student) {
        RecordBook recordBook = getRecordBookByStudent(student);

        return recordBook.getTopics().size();
    }

    @Override
    public int getDaysUntilEndOfCourseByStudent(Student student, LocalDate nowDate) {
        RecordBook recordBook = getRecordBookByStudent(student);
        LocalDate endOfCourseDate = recordBook.getEndDate();

        if (nowDate.isAfter(endOfCourseDate)) {
            return 0;
        }

        Period endOfCoursePeriod = Period.between(nowDate, endOfCourseDate);

        return endOfCoursePeriod.getDays();
    }
}
