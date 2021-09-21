package ru.erminson.ec.entity;

import java.time.Duration;
import java.util.Objects;

public class TopicScore {
    private final String topicTitle;
    private int score;
    private final Duration durationInDays;

    public TopicScore(String topicTitle, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = 0;
        this.durationInDays = durationInDays;
    }

    public TopicScore(String topicTitle, int score, Duration durationInDays) {
        this.topicTitle = topicTitle;
        this.score = score;
        this.durationInDays = durationInDays;
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

    public Duration getDurationInDays() {
        return durationInDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicScore that = (TopicScore) o;

        if (score != that.score) return false;
        if (!Objects.equals(topicTitle, that.topicTitle)) return false;
        return Objects.equals(durationInDays, that.durationInDays);
    }

    @Override
    public int hashCode() {
        int result = topicTitle != null ? topicTitle.hashCode() : 0;
        result = 31 * result + score;
        result = 31 * result + (durationInDays != null ? durationInDays.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicScore{" +
                "topicTitle='" + topicTitle + '\'' +
                ", score=" + score +
                ", durationInDays=" + durationInDays.toDays() +
                '}';
    }
}
