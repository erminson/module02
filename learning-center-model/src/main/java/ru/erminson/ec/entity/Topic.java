package ru.erminson.ec.entity;

import java.util.Objects;

public class Topic {
    private String title;
    private Integer durationInHours;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(Integer durationInHours) {
        this.durationInHours = durationInHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic module = (Topic) o;

        if (!Objects.equals(title, module.title)) return false;
        return Objects.equals(durationInHours, module.durationInHours);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (durationInHours != null ? durationInHours.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Module{" +
                "title='" + title + '\'' +
                ", durationInHours=" + durationInHours +
                '}';
    }
}
