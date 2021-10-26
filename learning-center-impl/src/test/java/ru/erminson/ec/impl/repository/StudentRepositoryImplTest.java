package ru.erminson.ec.impl.repository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentRepositoryImplTest {

    private StudentRepository studentRepository;

    private final Student STUDENT1 = new Student("Student1");
    private final Student STUDENT2 = new Student("Student2");
    private final Student STUDENT3 = new Student("Student3");

    @BeforeEach
    void setUp() {
        studentRepository = new StudentRepositoryImpl();
    }

    @Test
    void shouldBeZeroStudents() {
        List<Student> students = studentRepository.getAllStudents();
        assertThat(students, hasSize(0));
    }

    @Test
    void shouldBeTreeStudents() {
        studentRepository.addStudent(STUDENT1.getName());
        studentRepository.addStudent(STUDENT2.getName());
        studentRepository.addStudent(STUDENT3.getName());

        List<Student> students = studentRepository.getAllStudents();
        assertThat(students, hasSize(3));
    }

    @Test
    void shouldBeFalseWhenAddStudentTwice() {
        assertAll(
                () -> assertThat(studentRepository.addStudent(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepository.addStudent(STUDENT1.getName()), is(false))
        );
    }

    @Test
    void shouldBeTrueWhenRemoveOneStudent() {
        studentRepository.addStudent(STUDENT1.getName());
        studentRepository.addStudent(STUDENT2.getName());
        studentRepository.addStudent(STUDENT3.getName());

        assertAll(
                () -> assertThat(studentRepository.removeStudent(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepository.getAllStudents(), hasSize(2))
        );
    }

    @Test
    void shouldBeExceptionWhenRemoveNotExistsStudent() {
        assertThrows(IllegalInitialDataException.class, () -> studentRepository.removeStudent(STUDENT1.getName()));
    }

    @Test
    void shouldBeZeroStudentsWhenRemoveAll() {
        studentRepository.addStudent(STUDENT1.getName());
        studentRepository.addStudent(STUDENT2.getName());
        studentRepository.addStudent(STUDENT3.getName());

        assertAll(
                () -> assertThat(studentRepository.removeStudent(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepository.removeStudent(STUDENT2.getName()), is(true)),
                () -> assertThat(studentRepository.removeStudent(STUDENT3.getName()), is(true)),
                () -> assertThat(studentRepository.getAllStudents(), hasSize(0))
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    void shouldFindStudents(String name, Student student) throws Exception {
        studentRepository.addStudent(name);
        assertThat(studentRepository.getStudentByName(name), equalTo(student));
    }

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("Student1", new Student("Student1")),
                Arguments.of("Student2", new Student("Student2")),
                Arguments.of("Student3", new Student("Student3"))
        );
    }

    @Test
    void shouldThrowExceptionWhenFindNotExistStudents() {
        assertThrows(IllegalInitialDataException.class, () -> studentRepository.getStudentByName("dummy name"));
    }

    @Test
    void isExistStudentTestMethod() {
        studentRepository.addStudent(STUDENT1.getName());

        assertAll(
                () -> assertThat(studentRepository.isExistsStudent(STUDENT1.getName()), is(true)),
                () -> assertThat(studentRepository.isExistsStudent("dummy name"), is(false))
        );
    }

    @Test
    void initFromParameterizedConstructor() {
        List<Student> students = new ArrayList<>() {{
            add(STUDENT1);
            add(STUDENT2);
            add(STUDENT3);
        }};
        studentRepository = new StudentRepositoryImpl(students);

        List<Student> actualStudents = studentRepository.getAllStudents();

        assertAll(
                () -> assertThat(actualStudents, hasSize(students.size())),
                () -> assertThat(actualStudents, equalTo(actualStudents))
        );
    }

    @AfterEach
    void tearDown() {
    }
}