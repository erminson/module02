package ru.erminson.ec.utils;

import ru.erminson.ec.entity.Course;
import ru.erminson.ec.entity.RecordBook;
import ru.erminson.ec.entity.TopicScore;

import java.util.List;
import java.util.stream.Collectors;

public class RecordBookInitializer {
    private RecordBookInitializer() {
        throw new IllegalStateException("RecordBookInitializer utility class");
    }

    public static RecordBook createRecordBookByCourse(Course course) {
        String courseTitle = course.getTitle();
        List<TopicScore> topics = course.getTopics().stream()
                .map(TopicScore::new)
                .collect(Collectors.toList());

        return new RecordBook(courseTitle, topics);
    }
}
