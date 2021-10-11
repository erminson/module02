package ru.erminson.ec.impl.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.erminson.ec.api.repository.StudentRepository;
import ru.erminson.ec.model.entity.Student;
import ru.erminson.ec.model.exception.IllegalInitialDataException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        MockitoExtension.class
)
class StudentServiceImplTest {

    private final Student STUDENT1 = new Student("Student1");
    private final Student STUDENT2 = new Student("Student2");
    private final Student STUDENT3 = new Student("Student3");

    @Mock
    private StudentRepository studentRepositoryMock;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldBeReturnTrueWhenAddNewStudent() {
        Mockito.doReturn(true).when(studentRepositoryMock).addStudent(STUDENT1.getName());

        assertThat(studentService.addStudent(STUDENT1.getName()), equalTo(true));
    }

    @Test
    void shouldBeReturnFalseWhenAddStudentTwice() {
        String name = STUDENT1.getName();

        Mockito.when(studentRepositoryMock.addStudent(name))
                .thenReturn(true)
                .thenReturn(false);

        assertAll(
                () -> assertThat(studentService.addStudent(name), equalTo(true)),
                () -> assertThat(studentService.addStudent(name), equalTo(false))
        );
    }

    @Test
    @SneakyThrows
    void shouldReturnStudentWhenExists() {
        String name = STUDENT1.getName();

        Mockito.doReturn(STUDENT1).when(studentRepositoryMock).getStudentByName(name);

        assertThat(studentService.getStudentByName(name), equalTo(STUDENT1));
    }

    @Test
    @SneakyThrows
    void shouldReturnNullWhenStudentNotExists() {
        Mockito.doThrow(IllegalInitialDataException.class).when(studentRepositoryMock).getStudentByName(Mockito.anyString());

        assertThat(studentService.getStudentByName(Mockito.anyString()), nullValue());
    }

    @Test
    void shouldBeTreeStudents() {
        List<Student> expectedStudents = new ArrayList<>() {{
            add(STUDENT1);
            add(STUDENT2);
            add(STUDENT3);
        }};

        Mockito.doReturn(expectedStudents).when(studentRepositoryMock).getAllStudents();

        List<Student> actualStudents = studentService.getAllStudents();

        assertAll(
                () -> assertThat(actualStudents, hasSize(expectedStudents.size())),
                () -> assertThat(actualStudents, hasItems(STUDENT1, STUDENT2, STUDENT3))
        );
    }

    @Test
    @SneakyThrows
    void shouldReturnFalseWhenRemoveStudentTwice() {
        String name = STUDENT1.getName();
        Mockito.when(studentRepositoryMock.removeStudent(name))
                .thenReturn(true)
                .thenReturn(false);

        assertAll(
                () -> assertThat(studentService.removeStudent(name), equalTo(true)),
                () -> assertThat(studentService.removeStudent(name), equalTo(false))
        );
    }

    @Test
    @SneakyThrows
    void shouldReturnFalseWhenRemoveNotExistStudent() {
        String name = STUDENT1.getName();
        Mockito.doThrow(IllegalInitialDataException.class).when(studentRepositoryMock).removeStudent(Mockito.anyString());

        assertThat(studentService.removeStudent(name), equalTo(false));
    }

    @AfterEach
    void tearDown() {
    }
}