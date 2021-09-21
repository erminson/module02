package ru.erminson.ec.dto;

public class TopicScoreDto {
    private String topicTitle;
    private int score;
    private int durationInDays;

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    @Override
    public String toString() {
        return "TopicScoreDto{" +
                "topicTitle='" + topicTitle + '\'' +
                ", score=" + score +
                ", durationInDays=" + durationInDays +
                '}';
    }
}
