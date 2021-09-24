package ru.erminson.ec;

import ru.erminson.ec.entity.Student;
import ru.erminson.ec.dto.report.StudentReport;
import ru.erminson.ec.service.StudyService;
import ru.erminson.ec.utils.StudyServiceFactory;
import ru.erminson.ec.view.View;
import ru.erminson.ec.view.ConsoleView;

import java.util.List;

public class Main {
    private final View view;
    private final StudyService studyService;

    public Main() {
        view = new ConsoleView();
        studyService = StudyServiceFactory.createStudyService();
    }

    public static void main(String[] args) {
        Main app = new Main();

        try {
            app.fakeBusinessActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fakeBusinessActivities() throws Exception {
        List<Student> studentsOnCourses = studyService.getAllStudentsOnCourses();

        // print student reports
        for (Student student : studentsOnCourses) {
            StudentReport report = studyService.getStudentReportByStudentName(student.getName());
            view.printStudentReport(report);
        }

        System.out.println("-----------------------------------------------------------------");

        String studentName = "Lev";
        String courseTitle = "Course1";
        studyService.addStudentByName(studentName);
        studyService.enrollStudentOnCourse(studentName, courseTitle);

        StudentReport studentReportLev = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(studentReportLev);

        System.out.println("-----------------------------------------------------------------");

        studentName = "Ivan";
        StudentReport report = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(report);
        studyService.rateTopic(studentName, "topic15", 90);
        report = studyService.getStudentReportByStudentName(studentName);
        view.printStudentReport(report);

        System.out.println("-----------------------------------------------------------------");

//        System.out.println("All students:                 " + studyService.getAllStudents());
//        System.out.println("Students on courses:          " + studyService.getAllStudentsOnCourses());
//        System.out.println("Students on courses (sorted AVR asc): " + studyService.getAllStudentsOnCoursesSortedBy(AVR, true));
//        System.out.println("Students on courses (sorted AVR des): " + studyService.getAllStudentsOnCoursesSortedBy(AVR, false));
//        System.out.println("Students on courses (sorted DAYS asc): " + studyService.getAllStudentsOnCoursesSortedBy(DAYS, true));
//        System.out.println("Students on courses (sorted DAYS des): " + studyService.getAllStudentsOnCoursesSortedBy(DAYS, false));
//        System.out.println("Students on courses (sorted NAME asc): " + studyService.getAllStudentsOnCoursesSortedBy(NAME, true));
//        System.out.println("Students on courses (sorted NAME des): " + studyService.getAllStudentsOnCoursesSortedBy(NAME, false));
//        System.out.println("Students on courses (sorted START asc): " + studyService.getAllStudentsOnCoursesSortedBy(START, true));
//        System.out.println("Students on courses (sorted START des): " + studyService.getAllStudentsOnCoursesSortedBy(START, false));
//        System.out.println("Students on courses (sorted COURSE asc): " + studyService.getAllStudentsOnCoursesSortedBy(COURSE, true));
//        System.out.println("Students on courses (sorted COURSE des): " + studyService.getAllStudentsOnCoursesSortedBy(COURSE, false));
//        System.out.println("Students on courses:          " + studyService.getAllStudentsOnCourses());
//        System.out.println("Students out of courses:      " + studyService.getAllStudentsOutCourses());
    }
}
