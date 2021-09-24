package ru.erminson.ec.utils;

import ru.erminson.ec.repository.CourseRepository;
import ru.erminson.ec.repository.RecordBookRepository;
import ru.erminson.ec.repository.StudentRepository;
import ru.erminson.ec.repository.CourseRepositoryImpl;
import ru.erminson.ec.repository.RecordBookRepositoryImpl;
import ru.erminson.ec.repository.StudentRepositoryImpl;
import ru.erminson.ec.service.RecordBookService;
import ru.erminson.ec.service.StudentService;
import ru.erminson.ec.service.StudyService;
import ru.erminson.ec.service.StudyServiceImpl;
import ru.erminson.ec.service.RecordBookServiceImpl;
import ru.erminson.ec.service.StudentServiceImpl;

public class StudyServiceFactory {
    private StudyServiceFactory() {
        throw new IllegalStateException("StudyServiceFactory utility class");
    }

    public static StudyService createStudyService() {
        StudentRepository studentRepository = new StudentRepositoryImpl();
        StudentService studentService = new StudentServiceImpl(studentRepository);

        RecordBookRepository recordBookRepository = new RecordBookRepositoryImpl(studentRepository);
        RecordBookService recordBookService = new RecordBookServiceImpl(recordBookRepository);

        CourseRepository courseRepository = new CourseRepositoryImpl();

        return new StudyServiceImpl(studentService, recordBookService, courseRepository);
    }
}
