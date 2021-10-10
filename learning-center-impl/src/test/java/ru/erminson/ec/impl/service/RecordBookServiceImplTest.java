package ru.erminson.ec.impl.service;

import com.sun.source.tree.ModuleTree;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.impl.utils.RecordBookInitializer;
import ru.erminson.ec.model.entity.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(
        MockitoExtension.class
)
class RecordBookServiceImplTest {
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

    @Mock
    private RecordBookRepository recordBookRepositoryMock;
    @InjectMocks
    private RecordBookServiceImpl recordBookService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void studentShouldBeEnrollOnCourseOnce() {
        Mockito.when(recordBookRepositoryMock
                        .addStudentWithRecordBook(any(), any()))
                .thenReturn(true)
                .thenReturn(false);

        assertThat(recordBookService.enrollStudentOnCourse(STUDENT1, COURSE1), is(true));
        assertThat(recordBookService.enrollStudentOnCourse(STUDENT1, COURSE1), is(false));
    }

    @Test
    void shouldReturnNotNullIfStudentIsNotOnCourse() {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1);

        Mockito.doReturn(true).when(recordBookRepositoryMock).isStudentOnCourse(STUDENT1);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getRecordBookByStudent(STUDENT1), equalTo(recordBook));
    }

    @Test
    void shouldReturnNullIfStudentIsNotOnCourse() {
        Mockito.doReturn(false).when(recordBookRepositoryMock).isStudentOnCourse(any());

        assertThat(recordBookService.getRecordBookByStudent(STUDENT1), nullValue());
    }

    @Test
    void shouldReturnTrueWhenDismissStudentFromCourse() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.removeStudentFromCourse(STUDENT1)).thenReturn(true);

        assertThat(recordBookService.dismissStudentFromCourse(STUDENT1), is(true));
    }

    @Test
    void ifStudentNotOnCourseShouldReturnFalseWhenDismissOnCourse() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(false);

        assertThat(recordBookService.dismissStudentFromCourse(STUDENT1), is(false));
    }

    @Test
    void shouldReturnCorrectStudentList() {
        List<Student> students = new ArrayList<>() {{
           add(STUDENT1);
           add(STUDENT2);
        }};

        Mockito.when(recordBookRepositoryMock.getAllStudents()).thenReturn(students);

        assertAll(
                () -> assertThat(recordBookService.getAllStudentsOnCourses(), hasSize(2)),
                () -> assertThat(recordBookService.getAllStudentsOnCourses(), hasItems(STUDENT1, STUDENT2))
        );
    }

    @Test
    void shouldBeCorrectNumberRatedTopics() {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        List<TopicScore> topics = recordBook.getTopics().stream()
                .map(topicScore -> new TopicScore(topicScore.getTopicTitle(), getRandomNumber(1, 100), topicScore.getDurationInDays()))
                .collect(Collectors.toList());
        recordBook.setTopics(topics);
        int countRatedTopics = (int) topics.stream().filter(topicScore -> topicScore.getScore() != 0).count();

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getNumberRatedTopics(STUDENT1), equalTo(countRatedTopics));
    }

    @Test
    void shouldBeZeroRatedTopicsWhenRecordBookIsNull() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(false);

        assertThat(recordBookService.getNumberRatedTopics(STUDENT1), equalTo(0));
    }

    @Test
    void shouldBeZeroRatedTopicsAtBeginningOfStudies() {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getNumberRatedTopics(STUDENT1), equalTo(0));
    }

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Test
    void shouldBeCorrectNumberTopics() {
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1);
        int expectedNumberTopics = recordBook.getTopics().size();

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getNumberTopics(STUDENT1), equalTo(expectedNumberTopics));
    }

    @Test
    void shouldBeCorrectDaysUntilCourse() {
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1, startDate);
        LocalDate endOfCourseDate = recordBook.getEndDate();

        int expectedDays = 3;

        LocalDate now = endOfCourseDate.minusDays(expectedDays);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getDaysUntilEndOfCourseByStudent(STUDENT1, now), equalTo(expectedDays));
    }

    @Test
    void shouldBeCorrectZeroDaysUntilCourse() {
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(COURSE1, startDate);
        LocalDate endOfCourseDate = recordBook.getEndDate();

        LocalDate now = endOfCourseDate.plusDays(10);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(recordBook);

        assertThat(recordBookService.getDaysUntilEndOfCourseByStudent(STUDENT1, now), equalTo(0));
    }

    @AfterEach
    void tearDown() {
    }
}