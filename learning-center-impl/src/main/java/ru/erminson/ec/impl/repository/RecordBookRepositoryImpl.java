package ru.erminson.ec.impl.repository;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.ec.impl.Main;
import ru.erminson.ec.dto.yaml.YamlRecordBookList;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.api.repository.RecordBookRepository;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.impl.utils.RecordBookInitializer;

import java.io.InputStream;
import java.util.*;

public class RecordBookRepositoryImpl implements RecordBookRepository {
    private static final String RECORD_BOOKS_FILE_NAME = "recordbooks.yaml";

    private final Map<Student, RecordBook> storage = new HashMap<>();
    private StudentRepository studentRepository;

    public RecordBookRepositoryImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        init();
    }

    @Override
    public boolean addStudentWithRecordBook(Student student, RecordBook recordBook) {
        storage.putIfAbsent(student, recordBook);
        return true;
    }

    @Override
    public RecordBook getRecordBook(Student student) {
        return storage.get(student);
    }

    @Override
    public boolean isStudentOnCourse(Student student) {
        return storage.containsKey(student);
    }

    @Override
    public boolean removeStudentFromCourse(Student student) {
        storage.remove(student);
        return true;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(storage.keySet());
    }

    private void init() {
        Yaml yaml = new Yaml(new Constructor(YamlRecordBookList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(RECORD_BOOKS_FILE_NAME);

        YamlRecordBookList list = yaml.loadAs(inputStream, YamlRecordBookList.class);
        list.getRecordBooks().stream()
                        .forEach(recordBookDto -> {
                            String studentName = recordBookDto.getStudentName();
                            try {
                                Student student = studentRepository.getStudentByName(studentName);
                                RecordBook recordBook = RecordBookInitializer.createRecordBookByCourse(recordBookDto);
                                storage.put(student, recordBook);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
    }
}
