package ru.erminson.ec.dto;

import java.util.List;

public class RecordBookDto {
    private String studentName;
    private String courseTitle;
    private String startDate;
    private List<TopicScoreDto> topics;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<TopicScoreDto> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicScoreDto> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "RecordBookDto{" +
                "studentName='" + studentName + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", startDate='" + startDate + '\'' +
                ", topics=" + topics +
                '}';
    }
}
