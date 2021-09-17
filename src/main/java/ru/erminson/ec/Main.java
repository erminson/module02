package ru.erminson.ec;

import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.service.StudyService;
import ru.erminson.ec.utils.StudyServiceFactory;

public class Main {
    public static void main(String[] args)  {
        try {
            fakeBusinessActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fakeBusinessActivities() throws Exception {
        StudyService studyService = StudyServiceFactory.createStudyService();

        // add new student
        String studentName1 = "Ivan";
        studyService.addStudentByName(studentName1);

        // enroll student on course
        String courseTitle = "Course1";
        studyService.enrollStudentOnCourse(studentName1, courseTitle);

        // print record book
        RecordBook recordBook = studyService.getRecordBookByStudentName(studentName1);
        System.out.println(recordBook);

        // rate
        String topicTitle = "topic11";
        studyService.rateTopic(studentName1, topicTitle, 20);

        // print record book
        RecordBook recordBook2 = studyService.getRecordBookByStudentName(studentName1);
        System.out.println(recordBook2);

        // add new student
        String studentName2 = "Peter";
        studyService.addStudentByName(studentName2);

        // print students
        System.out.println("All students: " + studyService.getAllStudents());
        System.out.println("Students on courses: " + studyService.getAllStudentsOnCourses());
        System.out.println("Students out of courses: " + studyService.getAllStudentsOutCourses());

        // dismiss student from course
        studyService.dismissStudentFromCourse(studentName1);

        // print students
        System.out.println("All students: " + studyService.getAllStudents());
        System.out.println("Students on courses: " + studyService.getAllStudentsOnCourses());
        System.out.println("Students out of courses: " + studyService.getAllStudentsOutCourses());
    }
}
