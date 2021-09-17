package ru.erminson.ec.service.impl;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.repository.CourseRepository;
import ru.erminson.ec.service.RecordBookService;
import ru.erminson.ec.service.StudentService;
import ru.erminson.ec.service.StudyService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudyServiceImpl implements StudyService {
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

    public boolean addStudentByName(String name) {
        return studentService.addStudent(name);
    }

    public boolean removeStudentByName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);
        studentService.removeStudent(name);

        return true;
    }

    public boolean enrollStudentOnCourse(String name, String courseTitle) throws Exception {
        Student student = studentService.getStudentByName(name);
        Course course = courseRepository.getCourseByTitle(courseTitle);
        recordBookService.enrollStudentOnCourse(student, course);

        return true;
    }

    public boolean dismissStudentFromCourse(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        recordBookService.dismissStudentFromCourse(student);

        return true;
    }

    public boolean rateTopic(String name, String topicTitle, int score) throws Exception {
        Student student = studentService.getStudentByName(name);
        RecordBook recordBook = recordBookService.getRecordBookByStudent(student);
        recordBook.rateTopicByTitle(topicTitle, score);

        return true;
    }

    public RecordBook getRecordBookByStudentName(String name) throws Exception {
        Student student = studentService.getStudentByName(name);
        return recordBookService.getRecordBookByStudent(student);
    }

    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    public Set<Student> getAllStudentsOnCourses() {
        return recordBookService.getAllStudentsOnCourses();
    }

    public List<Student> getAllStudentsOutCourses() {
        List<Student> students = getAllStudents();
        Set<Student> studentsOnCourses = getAllStudentsOnCourses();
        return students.stream()
                .filter(student -> !studentsOnCourses.contains(student))
                .collect(Collectors.toList());
    }
}
