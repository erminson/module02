package ru.erminson.ec.impl.utils;

import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import ru.erminson.ec.api.service.CourseService;
import ru.erminson.ec.api.service.RecordBookService;
import ru.erminson.ec.api.service.StudentService;
import ru.erminson.ec.api.service.StudyService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class StudyServiceFactoryTest {
    @Test
    @SneakyThrows
    void defaultConstructorShouldBePrivate() {
        Constructor<?> con = StudyServiceFactory.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(con.getModifiers()));
    }

    @Test
    @SneakyThrows
    void shouldBeCorrectInstanceStudyService() {
        StudyService studyService = StudyServiceFactory.createStudyService();
        Field studentServiceField = studyService.getClass().getDeclaredField("studentService");
        Field recordBookServiceField = studyService.getClass().getDeclaredField("recordBookService");
        Field courseServiceField = studyService.getClass().getDeclaredField("courseService");

        assertAll(
                () -> assertThat(Modifier.isPrivate(studentServiceField.getModifiers()), is(true)),
                () -> assertThat(Modifier.isPrivate(recordBookServiceField.getModifiers()), is(true)),
                () -> assertThat(Modifier.isPrivate(courseServiceField.getModifiers()), is(true))
        );

        studentServiceField.setAccessible(true);
        recordBookServiceField.setAccessible(true);
        courseServiceField.setAccessible(true);

        Object obj = studentServiceField.get(studyService);
        assertAll(
                () -> assertThat(studentServiceField.get(studyService), notNullValue()),
                () -> assertThat(recordBookServiceField.get(studyService), notNullValue()),
                () -> assertThat(courseServiceField.get(studyService), notNullValue())
        );
    }
}