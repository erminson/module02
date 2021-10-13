package ru.erminson.ec.impl.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erminson.ec.api.service.StudyService;
import ru.erminson.ec.impl.utils.RecordBookInitializer;
import ru.erminson.ec.model.dto.RecordBookDto;
import ru.erminson.ec.model.dto.TopicScoreDto;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.entity.Topic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(
        MockitoExtension.class
)
class StudyServiceImplTest {
    @Mock
    private StudentServiceImpl studentServiceMock;
    @Mock
    private RecordBookServiceImpl recordBookServiceMock;
    @Mock
    private CourseServiceImpl courseServiceMock;
    @InjectMocks
    private StudyServiceImpl studyService;

    private final Student STUDENT1 = new Student("B");
    private final Student STUDENT2 = new Student("A");
    private final Student STUDENT3 = new Student("C");

    private final List<Student> ALL_STUDENTS = new ArrayList<>() {{
        add(STUDENT1);
        add(STUDENT2);
        add(STUDENT3);
    }};

    private final List<Student> ALL_STUDENTS_ON_COURSES = new ArrayList<>() {{
        add(STUDENT1);
        add(STUDENT2);
    }};

    private final List<Topic> TOPICS1 = new ArrayList<>() {{
        add(new Topic("topic11", 1));
        add(new Topic("topic12", 4));
        add(new Topic("topic13", 8));
        add(new Topic("topic14", 8));
        add(new Topic("topic15", 7));
        add(new Topic("topic16", 7));
    }};
    private final Course COURSE1 = new Course("CourseB", TOPICS1);

    private final List<Topic> TOPICS2 = new ArrayList<>() {{
        add(new Topic("topic11", 1));
        add(new Topic("topic12", 4));
        add(new Topic("topic13", 8));
        add(new Topic("topic14", 8));
        add(new Topic("topic15", 7));
        add(new Topic("topic16", 7));
    }};
    private final Course COURSE2 = new Course("CourseA", TOPICS2);

    private final List<TopicScoreDto> topicScoresDto1 = new ArrayList<>() {{
        add(new TopicScoreDto("topic11", 86, 1));
        add(new TopicScoreDto("topic12", 100, 1));
        add(new TopicScoreDto("topic13", 67, 1));
        add(new TopicScoreDto("topic14", 90, 1));
        add(new TopicScoreDto("topic15", 99, 1));
        add(new TopicScoreDto("topic16", 0, 1));
    }};

    private final RecordBookDto recordBookDto1 = new RecordBookDto(
            STUDENT1.getName(), COURSE1.getTitle(), "2021-01-01", topicScoresDto1
    );

    private final List<TopicScoreDto> topicScoresDto2 = new ArrayList<>() {{
        add(new TopicScoreDto("topic11", 10, 1));
        add(new TopicScoreDto("topic12", 10, 1));
        add(new TopicScoreDto("topic13", 0, 1));
        add(new TopicScoreDto("topic14", 0, 1));
        add(new TopicScoreDto("topic15", 0, 1));
        add(new TopicScoreDto("topic16", 0, 1));
    }};
    private final RecordBookDto recordBookDto2 = new RecordBookDto(
            STUDENT2.getName(), COURSE2.getTitle(), "2021-01-03", topicScoresDto2
    );

    private final RecordBook RECORD_BOOK1 = RecordBookInitializer.createRecordBookByRecordBookDto(recordBookDto1);
    private final RecordBook RECORD_BOOK2 = RecordBookInitializer.createRecordBookByRecordBookDto(recordBookDto2);

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldBeTrueWhenAddStudentTest() {
        Mockito.doReturn(true).when(studentServiceMock).addStudent(Mockito.anyString());
        assertThat(studyService.addStudentByName(Mockito.anyString()), is(true));
    }

    @SneakyThrows
    @Test
    void shouldBeReturnTrueWhenRemoveStudent() {
        String name = STUDENT1.getName();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
        Mockito.doReturn(true).when(recordBookServiceMock).dismissStudentFromCourse(STUDENT1);
        Mockito.doReturn(true).when(studentServiceMock).removeStudent(name);

        assertThat(studyService.removeStudentByName(name), is(true));
    }

    @Test
    void shouldBeReturnTrueWhenEnrollStudentOnCourseTest() {
        String studentName = STUDENT1.getName();
        String courseTitle = COURSE1.getTitle();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(studentName);
        Mockito.doReturn(COURSE1).when(courseServiceMock).getCourseByTitle(courseTitle);
        Mockito.doReturn(true).when(recordBookServiceMock).enrollStudentOnCourse(STUDENT1, COURSE1);

        assertThat(studyService.enrollStudentOnCourse(studentName, courseTitle), is(true));
    }

    @Test
    void shouldBeReturnFalseWhenEnrollNotExistStudentOnCourseTest() {
        String studentName = STUDENT1.getName();
        String courseTitle = COURSE1.getTitle();

        Mockito.doReturn(null).when(studentServiceMock).getStudentByName(studentName);
        Mockito.doReturn(COURSE1).when(courseServiceMock).getCourseByTitle(courseTitle);

        assertThat(studyService.enrollStudentOnCourse(studentName, courseTitle), is(false));
    }

    @Test
    void shouldBeReturnFalseWhenEnrollStudentOnNoExistsCourseTest() {
        String studentName = STUDENT1.getName();
        String courseTitle = COURSE1.getTitle();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(studentName);
        Mockito.doReturn(null).when(courseServiceMock).getCourseByTitle(courseTitle);

        assertThat(studyService.enrollStudentOnCourse(studentName, courseTitle), is(false));
    }

    @Test
    void shouldReturnTrueWhenDismissStudentFromCourse() {
        String name = STUDENT1.getName();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
        Mockito.doReturn(true).when(recordBookServiceMock).dismissStudentFromCourse(STUDENT1);

        assertThat(studyService.dismissStudentFromCourse(name), is(true));
    }

    @Test
    void shouldReturnFalseWhenDismissNotExistsStudentFromCourse() {
        String name = STUDENT1.getName();

        Mockito.doReturn(null).when(studentServiceMock).getStudentByName(name);

        assertThat(studyService.dismissStudentFromCourse(name), is(false));
    }

    @Tag("rateTopic")
    @Nested
    @DisplayName("Rate Topic")
    class RateTopicTests {
        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -100, -1, 0, 101, Integer.MAX_VALUE})
        void shouldReturnFalseWhenScoreOutOfRange(int score) {
            assertThat(studyService.rateTopic("dummy student", "dummy topic", score), is(false));
        }

        @Test
        void shouldReturnFalseWhenRateNotExistStudent() {
            String name = STUDENT1.getName();

            Mockito.doReturn(null).when(studentServiceMock).getStudentByName(name);

            assertThat(studyService.rateTopic(name, "dummy topic title", 50), is(false));
        }

        @Test
        void shouldReturnFalseWhenRecordBookNotExists() {
            String name = STUDENT1.getName();

            Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(null).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);

            assertThat(studyService.rateTopic(name, "Topic title", 50), is(false));
        }

        @ParameterizedTest(name = "[{index}] - [{argumentsWithNames}]")
        @NullAndEmptySource
        @ValueSource(strings = {"dummy topic", " dummy topic ", "dummy topic    ", "    dummy topic", "dummy topic\n"})
        void shouldReturnFalseWhenTopicNotExists(String topicTitle) {
            String name = STUDENT1.getName();

            Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);

            assertThat(studyService.rateTopic(name, topicTitle, 50), is(false));
        }

        @ParameterizedTest
        @MethodSource("ru.erminson.ec.impl.service.StudyServiceImplTest#getArgumentsForRateTopicTest")
        void testRateTopicWithOtherDates(LocalDate nowDate, boolean result) {
            String name = STUDENT1.getName();
            String topicTitle = "topic14";

            Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);

            assertThat(studyService.rateTopic(name, topicTitle, 50, nowDate), is(result));
        }

        @ParameterizedTest
        @MethodSource("ru.erminson.ec.impl.service.StudyServiceImplTest#getArgumentsForGetRecordBookTest")
        @DisplayName("Get Record Book")
        void test(Student student, String studentName, RecordBook recordBook) {
            Mockito.doReturn(student).when(studentServiceMock).getStudentByName(studentName);
            Mockito.lenient().doReturn(recordBook).when(recordBookServiceMock).getRecordBookByStudent(student);

            assertThat(studyService.getRecordBookByStudentName(studentName), equalTo(recordBook));
        }
    }

    private static Stream<Arguments> getArgumentsForRateTopicTest() {
        return Stream.of(
                Arguments.of(LocalDate.of(2021, 1, 3), false),
                Arguments.of(LocalDate.of(2021, 1, 4), false),
                Arguments.of(LocalDate.of(2021, 1, 5), true),
                Arguments.of(LocalDate.of(2021, 1, 7), true)
        );
    }

    private static Stream<Arguments> getArgumentsForGetRecordBookTest() {
        return Stream.of(
                Arguments.of(new Student("dummy"), "dummy", new RecordBook("dummy", LocalDate.now(), Collections.emptyList())),
                Arguments.of(null, "dummy", null)
        );
    }

    @Test
    void shouldReturnCorrectListOfAllStudents() {
        Mockito.doReturn(ALL_STUDENTS).when(studentServiceMock).getAllStudents();

        assertAll(
                () -> assertThat(studyService.getAllStudents(), hasSize(ALL_STUDENTS.size())),
                () -> assertThat(studyService.getAllStudents(), contains(STUDENT1, STUDENT2, STUDENT3))
        );
    }

    @Test
    void shouldReturnCorrectListOfAllStudentsOnCourses() {
        Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();

        assertAll(
                () -> assertThat(studyService.getAllStudentsOnCourses(), hasSize(ALL_STUDENTS_ON_COURSES.size())),
                () -> assertThat(studyService.getAllStudentsOnCourses(), contains(STUDENT1, STUDENT2))
        );
    }

    @Tag("sortStudents")
    @Nested
    @DisplayName("Sort Student")
    class SortStudentsTests {
        @BeforeEach
        void setUp() {
            Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();
        }

        @Test
        void sortByAverageAscTest() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.AVR, true);

            assertThat(sorted, contains(STUDENT2, STUDENT1));

        }

        @Test
        void sortByAverageDescTest() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.AVR, false);

            assertThat(sorted, contains(STUDENT1, STUDENT2));
        }

        @Test
        void sortByDaysAsc() {
            LocalDate nowDate = LocalDate.of(2021, 1, 4);

            Mockito.when(recordBookServiceMock.getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate)).thenReturn(3);
            Mockito.when(recordBookServiceMock.getDaysUntilEndOfCourseByStudent(STUDENT2, nowDate)).thenReturn(5);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.DAYS, true, nowDate);

            assertThat(sorted, contains(STUDENT1, STUDENT2));
        }

        @Test
        void sortByDaysDesc() {
            LocalDate nowDate = LocalDate.of(2021, 1, 4);

            Mockito.when(recordBookServiceMock.getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate)).thenReturn(3);
            Mockito.when(recordBookServiceMock.getDaysUntilEndOfCourseByStudent(STUDENT2, nowDate)).thenReturn(5);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.DAYS, false, nowDate);

            assertThat(sorted, contains(STUDENT2, STUDENT1));
        }

        @Test
        void sortByNamesAsc() {
            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.NAME, true);

            assertThat(sorted, contains(STUDENT2, STUDENT1));
        }

        @Test
        void sortByNamesDesc() {
            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.NAME, false);

            assertThat(sorted, contains(STUDENT1, STUDENT2));
        }

        @Test
        void sortByStartDateAsc() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.START, true);

            assertThat(sorted, contains(STUDENT1, STUDENT2));
        }

        @Test
        void sortByStartDateDesc() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.START, false);

            assertThat(sorted, contains(STUDENT2, STUDENT1));
        }

        @Test
        void sortByCourseTitleAsc() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.COURSE, true);
            assertThat(sorted, contains(STUDENT2, STUDENT1));
        }

        @Test
        void sortByCourseTitleDesc() {
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

            List<Student> sorted = studyService.getAllStudentsOnCoursesSortedBy(StudyService.SortType.COURSE, false);
            assertThat(sorted, contains(STUDENT1, STUDENT2));
        }
    }

    @Test
    void allStudentsAbilityToCompleteCourseTest() {
        LocalDate nowDate = LocalDate.of(2021, 1, 6);
        Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(STUDENT1.getName());
        Mockito.doReturn(STUDENT2).when(studentServiceMock).getStudentByName(STUDENT2.getName());

        Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
        Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

        Mockito.doReturn(1).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate);
        Mockito.doReturn(3).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT2, nowDate);

        Mockito.doReturn(6).when(recordBookServiceMock).getNumberTopics(STUDENT1);
        Mockito.doReturn(5).when(recordBookServiceMock).getNumberRatedTopics(STUDENT1);

        Mockito.doReturn(6).when(recordBookServiceMock).getNumberTopics(STUDENT2);
        Mockito.doReturn(2).when(recordBookServiceMock).getNumberRatedTopics(STUDENT2);

        assertThat(studyService.getAllStudentsAbilityToCompleteCourse(nowDate), contains(STUDENT1));
    }

    @Test
    void allStudentsOutCoursesTest() {
        Mockito.doReturn(ALL_STUDENTS).when(studentServiceMock).getAllStudents();
        Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();

        assertThat(studyService.getAllStudentsOutCourses(), contains(STUDENT3));
    }

    @Test
    void shouldBeGetZeroDaysWhenStudentNotExists() {
        String dummyName = "dummy";

        Mockito.doReturn(null).when(studentServiceMock).getStudentByName(dummyName);

        assertThat(studyService.getDaysUntilEndOfCourseByStudentName(dummyName), equalTo(0));
    }

    @Nested
    @DisplayName("Can Student Complete Course By Student Name")
    class CanStudentCompleteCourseByStudentName {
        @Test
        void shouldBeFalseIfStudentNotExist_checkCanStudentCompleteCourseByStudentName() {
            String dummyName = "dummy";

            Mockito.doReturn(null).when(studentServiceMock).getStudentByName(dummyName);

            assertThat(studyService.canStudentCompleteCourseByStudentName(dummyName), is(false));
        }

        @Test
        void shouldBeFalseIfDaysLeftIsZero_checkCanStudentCompleteCourseByStudentName() {
            String name = STUDENT1.getName();
            LocalDate nowDate = LocalDate.now();

            Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(0).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate);

            assertThat(studyService.canStudentCompleteCourseByStudentName(name, nowDate), is(false));
        }

        @Test
        void shouldBeTrueWithGoodStudent() {
            String name = STUDENT1.getName();
            LocalDate nowDate = LocalDate.of(2021, 1, 5);

            Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
            Mockito.doReturn(1).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate);

            assertThat(studyService.canStudentCompleteCourseByStudentName(name, nowDate), is(true));
        }

        @Test
        void shouldBeFalseWithBadStudent() {
            String name = STUDENT2.getName();
            LocalDate nowDate = LocalDate.of(2021, 1, 6);

            Mockito.doReturn(STUDENT2).when(studentServiceMock).getStudentByName(name);
            Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);
            Mockito.doReturn(3).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT2, nowDate);

            assertThat(studyService.canStudentCompleteCourseByStudentName(name, nowDate), is(false));
        }
    }

    @Test
    void studentReportShouldBeNullIfStudentNotExists_getStudentReportByStudentName() {
        String name = "dummy";

        Mockito.doReturn(null).when(studentServiceMock).getStudentByName(name);

        assertThat(studyService.getStudentReportByStudentName(name), nullValue());
    }

    @Test
    void studentReportShouldBeNotNull_getStudentReportByStudentName() {
        String name = STUDENT1.getName();
        LocalDate nowDate = LocalDate.of(2021, 1, 5);

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(name);
        Mockito.doReturn(1).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate);
        Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);

        assertThat(studyService.getStudentReportByStudentName(name, nowDate), notNullValue());
    }

    @Test
    void saveStudentReportsTest() {
        LocalDate nowDate = LocalDate.of(2021, 1, 6);
        int numberStudentsOnCourse = ALL_STUDENTS_ON_COURSES.size();

        Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(STUDENT1.getName());
        Mockito.doReturn(STUDENT2).when(studentServiceMock).getStudentByName(STUDENT2.getName());

        Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
        Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

        Mockito.doReturn(1).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT1, nowDate);
        Mockito.doReturn(3).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(STUDENT2, nowDate);

        studyService.saveStudentReports(nowDate);

        Mockito.verify(recordBookServiceMock, Mockito.times(1)).getAllStudentsOnCourses();
        Mockito.verify(recordBookServiceMock, Mockito.times(numberStudentsOnCourse * 2)).getRecordBookByStudent(Mockito.any(Student.class));
        Mockito.verify(studentServiceMock, Mockito.times(numberStudentsOnCourse * 3)).getStudentByName(Mockito.anyString());
    }

    @Test
    void saveStudentReportsTest2() {
        int numberStudentsOnCourse = ALL_STUDENTS_ON_COURSES.size();

        Mockito.doReturn(ALL_STUDENTS_ON_COURSES).when(recordBookServiceMock).getAllStudentsOnCourses();

        Mockito.doReturn(STUDENT1).when(studentServiceMock).getStudentByName(STUDENT1.getName());
        Mockito.doReturn(STUDENT2).when(studentServiceMock).getStudentByName(STUDENT2.getName());

        Mockito.doReturn(RECORD_BOOK1).when(recordBookServiceMock).getRecordBookByStudent(STUDENT1);
        Mockito.doReturn(RECORD_BOOK2).when(recordBookServiceMock).getRecordBookByStudent(STUDENT2);

        Mockito.doReturn(1).when(recordBookServiceMock).getDaysUntilEndOfCourseByStudent(Mockito.any(), Mockito.any());

        studyService.saveStudentReports();

        Mockito.verify(recordBookServiceMock, Mockito.times(1)).getAllStudentsOnCourses();
        Mockito.verify(recordBookServiceMock, Mockito.times(numberStudentsOnCourse * 2)).getRecordBookByStudent(Mockito.any(Student.class));
        Mockito.verify(studentServiceMock, Mockito.times(numberStudentsOnCourse * 3)).getStudentByName(Mockito.anyString());
    }


    @AfterEach
    void tearDown() {
    }
}