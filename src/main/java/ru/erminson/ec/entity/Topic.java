package ru.erminson.ec.entity;

import java.util.Objects;

public class Topic {
    private String title;
    private Integer duration;

    public Topic() {
    }

    public Topic(String title, Integer duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic module = (Topic) o;

        if (!Objects.equals(title, module.title)) return false;
        return Objects.equals(duration, module.duration);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Module{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }
}
