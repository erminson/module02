package ru.erminson.ec.entity;

import ru.erminson.ec.exception.IllegalInitialDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecordBook {
    private final String courseTitle;
    private final LocalDate startDate;
    private List<TopicScore> topics;

    public RecordBook(String courseTitle, LocalDate startDate, List<TopicScore> topics) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.topics = topics;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<TopicScore> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicScore> topics) {
        this.topics = topics;
    }

    public TopicScore getTopicScoreByTitle(String topicTitle) throws IllegalInitialDataException {
        return topics.stream()
                .filter(topic -> topic.getTopicTitle().equals(topicTitle))
                .findFirst()
                .orElseThrow(IllegalInitialDataException::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordBook that = (RecordBook) o;

        if (!Objects.equals(courseTitle, that.courseTitle)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        return Objects.equals(topics, that.topics);
    }

    @Override
    public int hashCode() {
        int result = courseTitle != null ? courseTitle.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (topics != null ? topics.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecordBook{" +
                "courseTitle='" + courseTitle + '\'' +
                ", topics=" + topics +
                '}';
    }

    public boolean isExistsTopic(String topicTitle) {
        return topics.stream()
                .map(TopicScore::getTopicTitle)
                .anyMatch(title -> title.equals(topicTitle));
    }

    public LocalDate getEndDateTopic(String topicTitle) {
        int index = topics.stream()
                .map(TopicScore::getTopicTitle)
                .collect(Collectors.toList())
                .indexOf(topicTitle);

        if (index < 0) {
            // TODO: Throw Exception
            return null;
        }

        LocalDate endDate = startDate;
        List<TopicScore> subTopics = topics.subList(0, index + 1);
        for (final TopicScore topicScore : subTopics) {
            endDate = endDate.plusDays(topicScore.getDurationInDays().toDays());
        }

        return endDate;
    }

    public LocalDate getEndDate() {
        long durationInHours = topics.stream()
                .mapToLong(topicScore -> topicScore.getDurationInDays().toDays())
                .sum();
        return startDate.plusDays(durationInHours);
    }

    public double getAverageScore() {
        return topics.stream()
                .mapToDouble(TopicScore::getScore)
                .average()
                .orElse(0.0);
    }

    public double getAverageScoreInBestCase() {
        return topics.stream()
                .mapToDouble(topicScore -> topicScore.getScore() == 0 ? 100.0 : topicScore.getScore())
                .average()
                .orElse(0.0);
    }
}
