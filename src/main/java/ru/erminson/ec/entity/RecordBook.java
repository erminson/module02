package ru.erminson.ec.entity;

import ru.erminson.ec.exception.IllegalInitialDataException;

import java.util.List;

public class RecordBook {
    private final String courseTitle;
    private final List<TopicScore> topics;

    public RecordBook(String courseTitle, List<TopicScore> topics) {
        this.courseTitle = courseTitle;
        this.topics = topics;
    }

    public void rateTopicByTitle(String topicTitle, int score) throws IllegalInitialDataException {
        if (isExistsTopic(topicTitle)) {
            TopicScore topicScore = topics.stream()
                    .filter(topic -> topic.getTopicTitle().equals(topicTitle))
                    .findFirst()
                    .orElseThrow(() -> new IllegalInitialDataException());
            topicScore.setScore(score);
        }
    }

    public boolean isExistsTopic(String topicTitle) {
        return topics.stream()
                .map(TopicScore::getTopicTitle)
                .anyMatch(title -> title.equals(topicTitle));
    }

    @Override
    public String toString() {
        return "RecordBook{" +
                "courseTitle='" + courseTitle + '\'' +
                ", topics=" + topics +
                '}';
    }
}
