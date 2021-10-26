package ru.erminson.ec.impl.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.impl.utils.RecordBookInitializer;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.entity.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class RecordBookRepositoryImplTest {

    private RecordBookRepository recordBookRepository;

    private final Student STUDENT1 = new Student("Student1");
    private final Student STUDENT2 = new Student("Student2");

    private final List<Topic> topics1 = new ArrayList<>() {{
        add(new Topic("topic11", 1));
        add(new Topic("topic12", 4));
        add(new Topic("topic13", 8));
        add(new Topic("topic14", 8));
        add(new Topic("topic15", 16));
        add(new Topic("topic16", 9));
    }};
    private final Course COURSE1 = new Course("Course1", topics1);

    private final List<Topic> topics2 = new ArrayList<>() {{
        add(new Topic("topic21", 1));
        add(new Topic("topic22", 4));
        add(new Topic("topic23", 8));
        add(new Topic("topic24", 8));
        add(new Topic("topic25", 16));
        add(new Topic("topic26", 9));
    }};
    private final Course COURSE2 = new Course("Course2", topics2);

    @BeforeEach
    void setUp() {
        recordBookRepository = new RecordBookRepositoryImpl();
    }

    @Test
    void shouldBeSameRecordBook() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepository.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook actualRecordBook = recordBookRepository.getRecordBook(STUDENT1);
        assertThat(actualRecordBook, equalTo(expectedRecordBook1));
    }

    @Test
    void studentShouldBeOnCourse() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepository.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        assertThat(recordBookRepository.isStudentOnCourse(STUDENT1), is(true));
    }

    @Test
    void studentShouldNotBeOnCourse() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepository.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        assertThat(recordBookRepository.isStudentOnCourse(STUDENT2), is(false));
    }

    @Test
    void getAllStudentsTest() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepository.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook expectedRecordBook2 = RecordBookInitializer.createRecordBookByCourse(COURSE2);
        recordBookRepository.addStudentWithRecordBook(STUDENT2, expectedRecordBook2);

        List<Student> students = recordBookRepository.getAllStudents();
        assertAll(
                () -> assertThat(students, hasSize(2)),
                () -> assertThat(students, contains(STUDENT1, STUDENT2))
        );
    }

    @Test
    void removeStudentFromCourseTest() {
        RecordBook expectedRecordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        recordBookRepository.addStudentWithRecordBook(STUDENT1, expectedRecordBook1);

        RecordBook expectedRecordBook2 = RecordBookInitializer.createRecordBookByCourse(COURSE2);
        recordBookRepository.addStudentWithRecordBook(STUDENT2, expectedRecordBook2);

        recordBookRepository.removeStudentFromCourse(STUDENT1);

        List<Student> students = recordBookRepository.getAllStudents();
        assertAll(
                () -> assertThat(students, hasSize(1)),
                () -> assertThat(students, contains(STUDENT2))
        );
    }

    @Test
    void testInitFromParameterizedConstructor() {
        RecordBook recordBook1 = RecordBookInitializer.createRecordBookByCourse(COURSE1);

        Map<Student, RecordBook> storage = new HashMap<>() {{
            put(STUDENT1, recordBook1);
        }};

        recordBookRepository = new RecordBookRepositoryImpl(storage);

        List<Student> students = recordBookRepository.getAllStudents();
        assertAll(
                () -> assertThat(students, hasSize(1)),
                () -> assertThat(students, contains(STUDENT1)),
                () -> assertThat(recordBookRepository.isStudentOnCourse(STUDENT1), is(true)),
                () -> assertThat(recordBookRepository.isStudentOnCourse(STUDENT2), is(false))
        );

    }
}