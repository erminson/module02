package ru.erminson.ec.impl.utils;

import ru.erminson.ec.api.repository.CourseRepository;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.api.service.CourseService;
import ru.erminson.ec.api.service.RecordBookService;
import ru.erminson.ec.api.service.StudentService;
import ru.erminson.ec.api.service.StudyService;
import ru.erminson.ec.impl.service.CourseServiceImpl;
import ru.erminson.ec.impl.service.StudyServiceImpl;
import ru.erminson.ec.impl.service.RecordBookServiceImpl;
import ru.erminson.ec.impl.service.StudentServiceImpl;

public class StudyServiceFactory {
    private StudyServiceFactory() {
        throw new IllegalStateException("StudyServiceFactory utility class");
    }

    public static StudyService createStudyService() {
        StudentRepository studentRepository = StudentRepositoryYamlInitializer.create();
        StudentService studentService = new StudentServiceImpl(studentRepository);

        RecordBookRepository recordBookRepository = RecordBookRepositoryInitializer.create(studentRepository);
        RecordBookService recordBookService = new RecordBookServiceImpl(recordBookRepository);

        CourseRepository courseRepository = CourseRepositoryYamlInitializer.create();
        CourseService courseService = new CourseServiceImpl(courseRepository);

        return new StudyServiceImpl(studentService, recordBookService, courseService);
    }
}
