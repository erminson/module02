package ru.erminson.ec.impl.service;

import ru.erminson.ec.dto.report.StudentReport;
import ru.erminson.ec.entity.*;
import ru.erminson.ec.exception.ComparatorException;
import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.api.service.RecordBookService;
import ru.erminson.ec.api.service.StudentService;
import ru.erminson.ec.api.service.StudyService;
import ru.erminson.ec.impl.utils.ReportSaverUtils;
import ru.erminson.ec.impl.utils.StudentComparatorFactory;
import ru.erminson.ec.impl.utils.StudentReportFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StudyServiceImpl implements StudyService {
    private static final int PASSING_SCORE = 75;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;

    private final StudentService studentService;
    private final RecordBookService recordBookService;
    private final CourseRepository courseRepository;

    public StudyServiceImpl(
            StudentService studentService,
            RecordBookService recordBookService,
            CourseRepository courseRepository
    ) {
        this.studentService = studentService;
        this.recordBookService = recordBookService;
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean addStudentByName(String name) {
        return studentService.addStudent(name);
    }

    @Override
    public boolean removeStudentByName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);
        studentService.removeStudent(name);

        return true;
    }

    @Override
    public boolean enrollStudentOnCourse(String name, String courseTitle) throws Exception {
        Student student = studentService.getStudentByName(name);
        Course course = courseRepository.getCourseByTitle(courseTitle);
        recordBookService.enrollStudentOnCourse(student, course);

        return true;
    }

    @Override
    public boolean dismissStudentFromCourse(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);

        return true;
    }

    @Override
    public boolean rateTopic(String name, String topicTitle, int score) throws Exception {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            return false;
        }

        Student student = studentService.getStudentByName(name);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);

        if (recordBook.isExistsTopic(topicTitle)) {
            LocalDate endDate = recordBook.getEndDateTopic(topicTitle);
            LocalDate nowDate = LocalDate.now();
            if (endDate.isAfter(nowDate)) {
                return false;
            }

            TopicScore topicScore = recordBook.getTopicScoreByTitle(topicTitle);
            topicScore.setScore(score);
            return true;
        }

        return false;
    }

    @Override
    public RecordBook getRecordBookByStudentName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);

        return recordBookService.getRecordBookByStudent(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @Override
    public List<Student> getAllStudentsOnCourses() {
        return recordBookService.getAllStudentsOnCourses();
    }

    @Override
    public List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending) throws Exception {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        Comparator<Student> comparator;
        switch (sortType) {
            case AVR:
                comparator = StudentComparatorFactory.getAverageComparator(recordBookService);
                break;
            case DAYS:
                comparator = StudentComparatorFactory.getDaysUntilEndOfCourseComparator(recordBookService);
                break;
            case NAME:
                comparator = StudentComparatorFactory.getStudentNameComparator();
                break;
            case START:
                comparator = StudentComparatorFactory.getStartDateComparator(recordBookService);
                break;
            case COURSE:
                comparator = StudentComparatorFactory.getCourseComparator(recordBookService);
                break;
            default:
                throw new ComparatorException("Comparator not created");
        }

        if (ascending) {
            students.sort(comparator);
        } else {
            students.sort(comparator.reversed());
        }

        return students;
    }

    @Override
    public List<Student> getAllStudentsAbilityToCompleteCourse() {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        return students.stream()
                .filter(student -> {
                    try {
                        return canStudentCompleteCourseByStudentName(student.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getAllStudentsOutCourses() {
        List<Student> students = getAllStudents();
        List<Student> studentsOnCourses = getAllStudentsOnCourses();

        return students.stream()
                .filter(student -> !studentsOnCourses.contains(student))
                .collect(Collectors.toList());
    }

    @Override
    public int getDaysUntilEndOfCourseByStudentName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        return recordBookService.getDaysUntilEndOfCourseByStudent(student, LocalDate.now());
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        int daysLeft = getDaysUntilEndOfCourseByStudentName(student.getName());
        if (daysLeft == 0) {
            return false;
        }

        int numberTopicsLeftBeRated =
                recordBookService.getNumberTopics(student) - recordBookService.getNumberRatedTopics(student);
        if (daysLeft < numberTopicsLeftBeRated) {
            return false;
        }

        return recordBook.getAverageScoreInBestCase() >= PASSING_SCORE;
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name) {
        Student student = null;
        boolean ability = false;
        try {
            student = studentService.getStudentByName(name);
            ability = canStudentCompleteCourseByStudentName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);

        return StudentReportFactory.create(student, recordBook, ability);
    }

    public void saveStudentReports() {
        List<Student> students = getAllStudentsOnCourses();
        List<StudentReport> reports = students.stream()
                .map(student -> getStudentReportByStudentName(student.getName()))
                .collect(Collectors.toList());

        ReportSaverUtils.saveStudentReports(reports);
    }
}
