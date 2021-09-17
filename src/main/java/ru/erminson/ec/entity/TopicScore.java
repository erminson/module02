package ru.erminson.ec.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class TopicScore {
    private String topicTitle;
    private int score;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TopicScore(Topic topic) {
        this.topicTitle = topic.getTitle();
        this.score = 0;
        initDates(topic.getDuration());
    }

    public TopicScore(Topic topic, int score) {
        this.topicTitle = topic.getTitle();
        this.score = score;
        initDates(topic.getDuration());
    }

    private void initDates(int duration) {
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusHours(duration);
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicScore that = (TopicScore) o;

        if (score != that.score) return false;
        if (!Objects.equals(topicTitle, that.topicTitle)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        return Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        int result = topicTitle != null ? topicTitle.hashCode() : 0;
        result = 31 * result + score;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicScore{" +
                "topicTitle=" + topicTitle +
                ", score=" + score +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
