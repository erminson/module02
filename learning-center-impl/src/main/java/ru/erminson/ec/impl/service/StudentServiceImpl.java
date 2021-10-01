package ru.erminson.ec.impl.service;

import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.api.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean addStudent(String name) {
        if (!studentRepository.isExistsStudent(name)) {
            return studentRepository.addStudent(name);
        }

        return false;
    }

    @Override
    public Student getStudentByName(String name) throws Exception {
        if (studentRepository.isExistsStudent(name)) {
            return studentRepository.getStudentByName(name);
        }

        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public boolean removeStudent(String name) throws Exception {
        if (studentRepository.isExistsStudent(name)) {
            return studentRepository.removeStudent(name);
        }

        return false;
    }
}