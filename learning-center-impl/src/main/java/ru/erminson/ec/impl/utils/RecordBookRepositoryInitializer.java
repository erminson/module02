package ru.erminson.ec.impl.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.impl.Main;
import ru.erminson.ec.impl.repository.RecordBookRepositoryImpl;
import ru.erminson.ec.model.dto.yaml.YamlRecordBookList;
import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RecordBookRepositoryInitializer {
    private static final String RECORD_BOOKS_FILE_NAME = "recordbooks.yaml";

    private RecordBookRepositoryInitializer() {
        throw new IllegalStateException("RecordBookRepositoryInitializer utility class");
    }

    public static RecordBookRepository create(StudentRepository studentRepository) {
        Map<Student, RecordBook> storage = createStorage(studentRepository);
        return new RecordBookRepositoryImpl(storage);
    }

    private static Map<Student, RecordBook> createStorage(StudentRepository studentRepository) {
        YamlRecordBookList list = loadYamlRecordListFromFile();
        Map<Student, RecordBook> storage = new HashMap<>();
        list.getRecordBooks().stream()
                .forEach(recordBookDto -> {
                    String studentName = recordBookDto.getStudentName();
                    RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(recordBookDto);
                    Student student;
                    try {
                        student = studentRepository.getStudentByName(studentName);
                    } catch (Exception e) {
                        student = new Student(studentName);
                    }
                    storage.put(student, recordBook);
                });

        return storage;
    }

    private static YamlRecordBookList loadYamlRecordListFromFile() {
        Yaml yaml = new Yaml(new Constructor(YamlRecordBookList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(RECORD_BOOKS_FILE_NAME);

        return yaml.loadAs(inputStream, YamlRecordBookList.class);
    }
}
