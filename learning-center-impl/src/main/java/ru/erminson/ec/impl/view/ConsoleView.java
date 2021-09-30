package ru.erminson.ec.impl.view;

import ru.erminson.ec.dto.report.TopicScoreReport;
import ru.erminson.ec.dto.report.StudentReport;
import ru.erminson.ec.api.view.View;

import java.util.List;

public class ConsoleView implements View {
    private static final int NUMBER_FIXED_SYMBOLS = 25;
    private static final int NUMBER_SPACES = 3;
    private static final int COURSE_WIDTH = 6;

    @Override
    public void printStudentReport(StudentReport studentReport) {
        String headerTemplate = "%10s: %s%n";
        System.out.printf("Student report (%s)%n", studentReport.getReportDate());
        System.out.printf(headerTemplate, "Name", studentReport.getStudentName());
        System.out.printf(headerTemplate, "Course", studentReport.getCourseTitle());
        System.out.printf("%10s: %.1f%n", "Average", studentReport.getAverage());
        System.out.printf(headerTemplate, "Start Date", studentReport.getStartDate());
        System.out.printf(headerTemplate, "End Date", studentReport.getEndDate());
        System.out.printf(headerTemplate, "Ability", studentReport.getAbility());

        List<TopicScoreReport> topicScores = studentReport.getTopicScores();
        int maxIndents = topicScores.stream()
                .map(TopicScoreReport::getTopicTitle)
                .mapToInt(String::length)
                .max()
                .orElse(10);

        int maxCourseIndents = Math.max(COURSE_WIDTH, maxIndents);

        printLine(maxCourseIndents);

        String tableTemplate =
                "%-10s %-10s %-" + maxCourseIndents + "s %5s%n";

        System.out.printf(tableTemplate, "Start", "End", "Title", "Score");

        printLine(maxCourseIndents);

        topicScores.stream().forEach(
                topicScore -> System.out.printf(
                        tableTemplate,
                        topicScore.getStartDate(),
                        topicScore.getEndDate(),
                        topicScore.getTopicTitle(),
                        topicScore.getScore()
                )
        );

        printLine(maxCourseIndents);
    }

    private void printLine(int indents) {
        System.out.println("-".repeat(NUMBER_FIXED_SYMBOLS + NUMBER_SPACES + indents));
    }
}
