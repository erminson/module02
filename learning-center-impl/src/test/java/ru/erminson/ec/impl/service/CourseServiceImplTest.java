package ru.erminson.ec.impl.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.Topic;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        MockitoExtension.class
)
class CourseServiceImplTest {
    @Mock
    private CourseRepository courseRepositoryMock;
    @InjectMocks
    private CourseServiceImpl courseService;

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

    List<Course> COURSES = new ArrayList<>() {{
        add(COURSE1);
        add(COURSE2);
    }};

    @BeforeEach
    void setUp() {
//        courseRepository = Mockito.mock(CourseRepository.class);
//        courseService = new CourseServiceImpl(courseRepository);
    }

    @Test
    void shouldReturnFalseWhenAddCourseTwice() {
        Mockito.when(courseService.add(COURSE1))
                .thenReturn(true)
                .thenReturn(false);

        assertAll(
                () -> assertThat(courseService.add(COURSE1), is(true)),
                () -> assertThat(courseService.add(COURSE1), is(false))
        );
    }

    @Test
    void shouldBeOnCourse() {
        List<Course> expectedCourses = new ArrayList<>() {{
            add(COURSE1);
        }};

        Mockito.when(courseRepositoryMock.getAllCourses()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();
        assertAll(
                () -> assertThat(actualCourses, hasSize(expectedCourses.size())),
                () -> assertThat(actualCourses, hasItems(expectedCourses.get(0)))
        );
    }

    @Test
    void shouldBeTrueWhenCourseExists() {
        courseService.add(COURSE1);

        Mockito.doReturn(true).when(courseRepositoryMock).isExistCourseByTitle(COURSE1.getTitle());

        assertThat(courseService.isExistCourseByTitle(COURSE1.getTitle()), is(true));
    }

    @Test
    void shouldBeFalseWhenCourseNoExists() {
        Mockito.doReturn(false).when(courseRepositoryMock).isExistCourseByTitle(Mockito.anyString());

        assertThat(courseService.isExistCourseByTitle("dummy"), is(false));
    }

    @Test
    @SneakyThrows
    void shouldBeListTopicsExistingCourse() {
        courseService.add(COURSE1);

        Mockito.doReturn(topics1).when(courseRepositoryMock).getTopicsByCourseTitle(COURSE1.getTitle());

        List<Topic> actualTopics = courseService.getTopicsByCourseTitle(COURSE1.getTitle());
        assertAll(
                () -> assertThat(actualTopics, hasSize(topics1.size())),
                () -> assertThat(actualTopics, hasItems(topics1.get(0), topics1.get(1)))
        );
    }

    @Test
    @SneakyThrows
    void shouldBeEmptyListTopicsIfCourseNotExists() {
        Mockito.doThrow(IllegalInitialDataException.class)
                .when(courseRepositoryMock)
                .getTopicsByCourseTitle(Mockito.anyString());

        List<Topic> actualTopics = courseService.getTopicsByCourseTitle(COURSE1.getTitle());
        assertThat(actualTopics, empty());
    }

    @Test
    @SneakyThrows
    void shouldBeCorrectCourseWhenCourseWasAdded() {
        courseService.add(COURSE1);

        Mockito.doReturn(COURSE1).when(courseRepositoryMock).getCourseByTitle(COURSE1.getTitle());

        assertThat(courseService.getCourseByTitle(COURSE1.getTitle()), equalTo(COURSE1));
    }

    @Test
    @SneakyThrows
    void shouldBeNullWhenCourseNotAdded() {
        Mockito.doThrow(IllegalInitialDataException.class)
                .when(courseRepositoryMock)
                .getCourseByTitle(Mockito.anyString());

        assertThat(courseService.getCourseByTitle(COURSE1.getTitle()), nullValue());
    }
}