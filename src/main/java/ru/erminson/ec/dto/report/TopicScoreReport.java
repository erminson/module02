package ru.erminson.ec.dto.report;

import java.util.Objects;

public class TopicScoreReport {
    private final String topicTitle;
    private final String startDate;
    private final String endDate;
    private final int score;

    public TopicScoreReport(String topicTitle, String startDate, String endDate, int score) {
        this.topicTitle = topicTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.score = score;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicScoreReport that = (TopicScoreReport) o;

        if (score != that.score) return false;
        if (!Objects.equals(topicTitle, that.topicTitle)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        return Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        int result = topicTitle != null ? topicTitle.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + score;
        return result;
    }

    @Override
    public String toString() {
        return "TopicScoreReport{" +
                "topicTitle='" + topicTitle + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", score=" + score +
                '}';
    }
}
