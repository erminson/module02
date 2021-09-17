package ru.erminson.ec.utils;

import ru.erminson.ec.repository.CourseRepository;
import ru.erminson.ec.repository.RecordBookRepository;
import ru.erminson.ec.repository.StudentRepository;
import ru.erminson.ec.repository.impl.CourseRepositoryImpl;
import ru.erminson.ec.repository.impl.RecordBookRepositoryImpl;
import ru.erminson.ec.repository.impl.StudentRepositoryImpl;
import ru.erminson.ec.service.RecordBookService;
import ru.erminson.ec.service.StudentService;
import ru.erminson.ec.service.StudyService;
import ru.erminson.ec.service.impl.StudyServiceImpl;
import ru.erminson.ec.service.impl.RecordBookServiceImpl;
import ru.erminson.ec.service.impl.StudentServiceImpl;

public class StudyServiceFactory {
    private StudyServiceFactory() {
        throw new IllegalStateException("StudyServiceFactory utility class");
    }

    public static StudyService createStudyService() {
        StudentRepository studentRepository = new StudentRepositoryImpl();
        StudentService studentService = new StudentServiceImpl(studentRepository);

        RecordBookRepository recordBookRepository = new RecordBookRepositoryImpl();
        RecordBookService recordBookService = new RecordBookServiceImpl(recordBookRepository);

        CourseRepository courseRepository = new CourseRepositoryImpl();

        return new StudyServiceImpl(studentService, recordBookService, courseRepository);
    }
}
