package ru.erminson.ec.model.dto.yaml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.erminson.ec.model.entity.Student;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class YamlStudentList {
    private List<Student> students;
}
