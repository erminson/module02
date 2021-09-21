package ru.erminson.ec.dto.yaml;

import ru.erminson.ec.entity.Student;

import java.util.List;

public class YamlStudentList {
    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
