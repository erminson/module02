package ru.erminson.ec.api.view;

import ru.erminson.ec.model.dto.report.StudentReport;

public interface View {
    void printStudentReport(StudentReport studentReport);
}
