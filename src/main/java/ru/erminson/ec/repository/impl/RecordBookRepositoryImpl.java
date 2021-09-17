package ru.erminson.ec.repository.impl;

import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.Student;
import ru.erminson.ec.repository.RecordBookRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecordBookRepositoryImpl implements RecordBookRepository {
    private final Map<Student, RecordBook> storage = new HashMap<>();

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
    public Set<Student> getAllStudents() {
        return storage.keySet();
    }
}
