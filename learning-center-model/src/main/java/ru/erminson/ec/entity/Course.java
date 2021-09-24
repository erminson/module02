package ru.erminson.ec.entity;

import java.util.List;
import java.util.Objects;

public class Course {
    private String title;
    private List<Topic> topics;

    public Course() {
    }

    public Course(String title, List<Topic> modules) {
        this.title = title;
        this.topics = modules;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course path = (Course) o;

        if (!Objects.equals(title, path.title)) return false;
        return Objects.equals(topics, path.topics);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (topics != null ? topics.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Path{" +
                "title='" + title + '\'' +
                ", modules=" + topics +
                '}';
    }


}
