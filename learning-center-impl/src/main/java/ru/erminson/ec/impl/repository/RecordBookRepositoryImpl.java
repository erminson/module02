package ru.erminson.ec.impl.repository;

import ru.erminson.ec.model.entity.RecordBook;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.api.repository.RecordBookRepository;

import java.util.*;

public class RecordBookRepositoryImpl implements RecordBookRepository {

    private final Map<Student, RecordBook> storage;

    public RecordBookRepositoryImpl() {
        this.storage = new HashMap<>();
    }

    public RecordBookRepositoryImpl(Map<Student, RecordBook> storage) {
        this.storage = storage;
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
}
