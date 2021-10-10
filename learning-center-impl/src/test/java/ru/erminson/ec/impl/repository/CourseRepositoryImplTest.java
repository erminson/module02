package ru.erminson.ec.impl.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.impl.annotation.TestAutowired;
import ru.erminson.ec.impl.extension.CourseRepositoryPostProcessingExtension;
import ru.erminson.ec.model.entity.Course;
import ru.erminson.ec.model.entity.Topic;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({
        CourseRepositoryPostProcessingExtension.class
})
public class CourseRepositoryImplTest {

    @TestAutowired
    public CourseRepository courseRepository;

    private final List<Topic> topics1 = new ArrayList<>() {{
        add(new Topic("topic11", 1));
        add(new Topic("topic12", 4));
        add(new Topic("topic13", 8));
        add(new Topic("topic14", 8));
        add(new Topic("topic15", 16));
        add(new Topic("topic16", 9));
    }};
    private final Course course1 = new Course("Course1", topics1);

    private final List<Topic> topics2 = new ArrayList<>() {{
        add(new Topic("topic21", 1));
        add(new Topic("topic22", 4));
        add(new Topic("topic23", 8));
        add(new Topic("topic24", 8));
        add(new Topic("topic25", 16));
        add(new Topic("topic26", 9));
    }};
    private final Course course2 = new Course("Course2", topics2);

    @Test
    void whenAddTwoDifferentCoursesShouldBeTwo() {
        courseRepository.add(course1);
        courseRepository.add(course2);

        List<Course> courses = courseRepository.getAllCourses();
        assertEquals(2, courses.size());
    }

    @Test
    void whenAddCourseTwoTimesMustBeOneCourse() {
        courseRepository.add(course1);
        courseRepository.add(course1);

        List<Course> courses = courseRepository.getAllCourses();
        assertThat(courses, hasSize(1));
    }

    @Test
    void shouldBeEqualsTitle() throws IllegalInitialDataException {
        courseRepository.add(course1);

        String expectedCourseTitle = course1.getTitle();
        Course course = courseRepository.getCourseByTitle(expectedCourseTitle);
        assertEquals(expectedCourseTitle, course.getTitle());
    }

    @Test
    void shouldThrowExceptionIfWrongCourseTitle() {
        assertThrows(
                IllegalInitialDataException.class,
                () -> courseRepository.getCourseByTitle("Wrong course title")
        );
    }

    @Test
    void shouldGetCorrectListOfTopics() throws IllegalInitialDataException {
        courseRepository.add(course1);

        String expectedTitle = course1.getTitle();
        int expectedCountTopics = course1.getTopics().size();

        List<Topic> actualTopics = courseRepository.getTopicsByCourseTitle(expectedTitle);
        assertAll(
                () -> assertThat(actualTopics, hasSize(expectedCountTopics)),
                () -> assertThat(actualTopics, hasItems(topics1.get(0), topics1.get(1), topics1.get(2))),
                () -> assertEquals(expectedCountTopics, actualTopics.size())
        );
    }

    @Test
    void initFromParameterizedConstructorShouldBeTwoCourses() {
        List<Course> courses = new ArrayList<>() {{
            add(course1);
            add(course2);
        }};

        courseRepository = new CourseRepositoryImpl(courses);
        List<Course> actualCourses = courseRepository.getAllCourses();
        assertThat(actualCourses, hasSize(courses.size()));
    }

    @AfterEach
    void tearDown() {
        courseRepository = null;
    }
}