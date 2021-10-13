package ru.erminson.ec.impl.service;

import ru.erminson.ec.api.service.CourseService;
import ru.erminson.ec.api.service.RecordBookService;
import ru.erminson.ec.api.service.StudentService;
import ru.erminson.ec.api.service.StudyService;
import ru.erminson.ec.impl.utils.ReportSaverUtils;
import ru.erminson.ec.impl.utils.StudentComparatorFactory;
import ru.erminson.ec.impl.utils.StudentReportFactory;
import ru.erminson.ec.model.dto.report.StudentReport;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.entity.TopicScore;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudyServiceImpl implements StudyService {
    private static final int PASSING_SCORE = 75;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;

    private final StudentService studentService;
    private final RecordBookService recordBookService;
    private final CourseService courseService;

    public StudyServiceImpl(
            StudentService studentService,
            RecordBookService recordBookService,
            CourseService courseService
    ) {
        this.studentService = studentService;
        this.recordBookService = recordBookService;
        this.courseService = courseService;
    }

    @Override
    public boolean addStudentByName(String name) {
        return studentService.addStudent(name);
    }

    @Override
    public boolean removeStudentByName(String name) {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);
        studentService.removeStudent(name);

        return true;
    }

    @Override
    public boolean enrollStudentOnCourse(String name, String courseTitle) {
        Student student = studentService.getStudentByName(name);
        Course course = courseService.getCourseByTitle(courseTitle);
        if (student == null || course == null) {
            return false;
        }

        return recordBookService.enrollStudentOnCourse(student, course);
    }

    @Override
    public boolean dismissStudentFromCourse(String name) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return false;
        }

        return recordBookService.dismissStudentFromCourse(student);
    }

    @Override
    public boolean rateTopic(String name, String topicTitle, int score, LocalDate nowDate) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            return false;
        }

        Student student = studentService.getStudentByName(name);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        if (student == null || recordBook == null) {
            return false;
        }

        if (recordBook.isExistsTopic(topicTitle)) {
            LocalDate endDate = recordBook.getEndDateTopic(topicTitle);
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
    public boolean rateTopic(String name, String topicTitle, int score) {
        return rateTopic(name, topicTitle, score, LocalDate.now());
    }

    @Override
    public RecordBook getRecordBookByStudentName(String name) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return null;
        }

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
    public List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending) {
        return getAllStudentsOnCoursesSortedBy(sortType, ascending, LocalDate.now());
    }

    @Override
    public List<Student> getAllStudentsOnCoursesSortedBy(SortType sortType, boolean ascending, LocalDate nowDate) {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        Comparator<Student> comparator;
        switch (sortType) {
            case AVR:
                comparator = StudentComparatorFactory.getAverageComparator(recordBookService);
                break;
            case DAYS:
                comparator = StudentComparatorFactory.getDaysUntilEndOfCourseComparator(recordBookService, nowDate);
                break;
            case START:
                comparator = StudentComparatorFactory.getStartDateComparator(recordBookService);
                break;
            case COURSE:
                comparator = StudentComparatorFactory.getCourseComparator(recordBookService);
                break;
            default:
                comparator = StudentComparatorFactory.getStudentNameComparator();
        }

        if (ascending) {
            students.sort(comparator);
        } else {
            students.sort(comparator.reversed());
        }

        return students;
    }

    @Override
    public List<Student> getAllStudentsAbilityToCompleteCourse(LocalDate nowDate) {
        List<Student> students = recordBookService.getAllStudentsOnCourses();

        return students.stream()
                .filter(student -> canStudentCompleteCourseByStudentName(student.getName(), nowDate))
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
    public int getDaysUntilEndOfCourseByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return 0;
        }

        return recordBookService.getDaysUntilEndOfCourseByStudent(student, nowDate);
    }

    @Override
    public int getDaysUntilEndOfCourseByStudentName(String name) {
        return getDaysUntilEndOfCourseByStudentName(name, LocalDate.now());
    }

    @Override
    public boolean canStudentCompleteCourseByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return false;
        }

        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        // TODO: check recordBook for null

        int daysLeft = getDaysUntilEndOfCourseByStudentName(student.getName(), nowDate);
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
    public boolean canStudentCompleteCourseByStudentName(String name) {
        return canStudentCompleteCourseByStudentName(name, LocalDate.now());
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name, LocalDate nowDate) {
        Student student = studentService.getStudentByName(name);
        if (student == null) {
            return null;
        }

        boolean ability = canStudentCompleteCourseByStudentName(name, nowDate);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);

        return StudentReportFactory.create(student, recordBook, ability);
    }

    @Override
    public StudentReport getStudentReportByStudentName(String name) {
        return getStudentReportByStudentName(name, LocalDate.now());
    }

    @Override
    public void saveStudentReports(LocalDate nowDate) {
        List<Student> students = getAllStudentsOnCourses();
        List<StudentReport> reports = students.stream()
                .map(student -> getStudentReportByStudentName(student.getName(), nowDate))
                .collect(Collectors.toList());

        ReportSaverUtils.saveStudentReports(reports);
    }

    @Override
    public void saveStudentReports() {
        saveStudentReports(LocalDate.now());
    }
}
