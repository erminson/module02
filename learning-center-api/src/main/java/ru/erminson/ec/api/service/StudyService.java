package ru.erminson.ec.api.service;

import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.dto.report.StudentReport;

import java.util.List;

public interface StudyService {
    enum SortType { AVR, DAYS, NAME, START, COURSE }

    boolean addStudentByName(String name);

    boolean removeStudentByName(String name) throws Exception;

    boolean enrollStudentOnCourse(String name, String courseTitle) throws Exception;

    boolean dismissStudentFromCourse(String name) throws Exception;

    boolean rateTopic(String name, String topicTitle, int score) throws Exception;

    RecordBook getRecordBookByStudentName(String name) throws Exception;

    List<Student> getAllStudents();

    List<Student> getAllStudentsOnCourses();

    List<Student> getAllStudentsOutCourses();

    List<Student> getAllStudentsAbilityToCompleteCourse();

    List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending) throws Exception;

    int getDaysUntilEndOfCourseByStudentName(String name) throws Exception;

    boolean canStudentCompleteCourseByStudentName(String name) throws Exception;

    StudentReport getStudentReportByStudentName(String name) throws Exception;

    void saveStudentReports() throws Exception;
}
