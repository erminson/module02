package ru.erminson.ec.impl.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryYamlInitializerTest {
    @Test
    @SneakyThrows
    void defaultConstructorShouldBePrivate() {
        Constructor<?> con = CourseRepositoryYamlInitializer.class.getDeclaredConstructor();
        con.setAccessible(true);
        assertAll(
                () -> assertTrue(Modifier.isPrivate(con.getModifiers())),
                () -> assertThrows(Throwable.class, con::newInstance)
        );
        con.setAccessible(false);
    }
}