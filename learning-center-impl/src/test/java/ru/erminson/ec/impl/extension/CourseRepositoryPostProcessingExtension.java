package ru.erminson.ec.impl.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import ru.erminson.ec.impl.annotation.TestAutowired;
import ru.erminson.ec.impl.repository.CourseRepositoryImpl;

import java.lang.reflect.Field;

public class CourseRepositoryPostProcessingExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
        Field[] declaredFields = testInstance.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(TestAutowired.class)) {
                field.setAccessible(true);
                field.set(testInstance, new CourseRepositoryImpl());
                field.setAccessible(false);
            }
        }
    }
}

