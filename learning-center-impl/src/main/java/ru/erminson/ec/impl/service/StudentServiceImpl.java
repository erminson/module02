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
        return studentRepository.addStudent(name);

    }

    @Override
    public Student getStudentByName(String name) {
        try {
            return studentRepository.getStudentByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public boolean removeStudent(String name) {
        try {
            return studentRepository.removeStudent(name);
        } catch (Exception e) {
            return false;
        }
    }
}
