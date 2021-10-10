package ru.erminson.ec.impl.utils;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.impl.repository.StudentRepositoryImpl;
import ru.erminson.ec.impl.service.RecordBookServiceImpl;
import ru.erminson.ec.impl.service.StudentServiceImpl;
import ru.erminson.ec.model.dto.RecordBookDto;
import ru.erminson.ec.model.dto.TopicScoreDto;
import ru.erminson.ec.model.entity.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        MockitoExtension.class
)
class StudentComparatorFactoryTest {
    private final Student STUDENT1 = new Student("B");
    private final Student STUDENT2 = new Student("A");

    private final List<Student> STUDENTS = new ArrayList<>() {{
        add(STUDENT1);
        add(STUDENT2);
    }};

    private final List<TopicScoreDto> topicScoreDtos1 = new ArrayList<>() {{
        add(new TopicScoreDto("topic11", 86, 1));
        add(new TopicScoreDto("topic12", 100, 1));
        add(new TopicScoreDto("topic13", 67, 1));
        add(new TopicScoreDto("topic14", 90, 1));
        add(new TopicScoreDto("topic15", 99, 1));
        add(new TopicScoreDto("topic16", 0, 1));
    }};
    private final RecordBookDto recordBookDto1 = new RecordBookDto("B", "CourseB", "2021-01-18", topicScoreDtos1);

    private final List<TopicScoreDto> topicScoreDtos2 = new ArrayList<>() {{
        add(new TopicScoreDto("topic11", 100, 1));
        add(new TopicScoreDto("topic12", 100, 1));
        add(new TopicScoreDto("topic13", 100, 1));
        add(new TopicScoreDto("topic14", 90, 1));
        add(new TopicScoreDto("topic15", 0, 1));
        add(new TopicScoreDto("topic16", 0, 1));
    }};
    private final RecordBookDto recordBookDto2 = new RecordBookDto("A", "CourseA", "2021-01-16", topicScoreDtos2);

    private final RecordBook RECORD_BOOK1 = RecordBookInitializer.createRecordBookByCourse(recordBookDto1);
    private final RecordBook RECORD_BOOK2 = RecordBookInitializer.createRecordBookByCourse(recordBookDto2);

    @Mock
    StudentRepositoryImpl studentRepositoryMock;
    @InjectMocks
    StudentServiceImpl studentService;

    @Mock
    RecordBookRepository recordBookRepositoryMock;
    @InjectMocks
    RecordBookServiceImpl recordBookService;

    @Test
    @SneakyThrows
    void defaultConstructorShouldBePrivate() {
        Constructor<?> con = StudentComparatorFactory.class.getDeclaredConstructor();
        con.setAccessible(true);
        assertAll(
                () -> assertTrue(Modifier.isPrivate(con.getModifiers())),
                () -> assertThrows(Throwable.class, con::newInstance)
        );
        con.setAccessible(false);
    }

    @Test
    void testAverageComparator() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(RECORD_BOOK1);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT2)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT2)).thenReturn(RECORD_BOOK2);

        Comparator<Student> comparator = StudentComparatorFactory.getAverageComparator(recordBookService);

        STUDENTS.sort(comparator);

        assertThat(STUDENTS, Matchers.contains(STUDENT2, STUDENT1));
    }

    @Test
    void testDaysUntilEndOfCourseComparator() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(RECORD_BOOK1);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT2)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT2)).thenReturn(RECORD_BOOK2);

        LocalDate nowDate = LocalDate.of(2021, 1, 20);
        Comparator<Student> comparator = StudentComparatorFactory.getDaysUntilEndOfCourseComparator(recordBookService, nowDate);

        STUDENTS.sort(comparator);

        assertThat(STUDENTS, Matchers.contains(STUDENT2, STUDENT1));
    }

    @Test
    void testStudentNameComparator() {
        Comparator<Student> comparator = StudentComparatorFactory.getStudentNameComparator();

        STUDENTS.sort(comparator);

        assertThat(STUDENTS, Matchers.contains(STUDENT2, STUDENT1));
    }

    @Test
    void testStartDateComparator() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(RECORD_BOOK1);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT2)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT2)).thenReturn(RECORD_BOOK2);

        Comparator<Student> comparator = StudentComparatorFactory.getStartDateComparator(recordBookService);

        STUDENTS.sort(comparator);

        assertThat(STUDENTS, Matchers.contains(STUDENT2, STUDENT1));
    }

    @Test
    void testCourseComparator() {
        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT1)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT1)).thenReturn(RECORD_BOOK1);

        Mockito.when(recordBookRepositoryMock.isStudentOnCourse(STUDENT2)).thenReturn(true);
        Mockito.when(recordBookRepositoryMock.getRecordBook(STUDENT2)).thenReturn(RECORD_BOOK2);

        Comparator<Student> comparator = StudentComparatorFactory.getCourseComparator(recordBookService);

        STUDENTS.sort(comparator);

        assertThat(STUDENTS, Matchers.contains(STUDENT2, STUDENT1));
    }
}