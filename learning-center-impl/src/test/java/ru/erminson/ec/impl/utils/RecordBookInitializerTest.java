package ru.erminson.ec.impl.utils;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.erminson.ec.model.dto.RecordBookDto;
import ru.erminson.ec.model.dto.TopicScoreDto;
import ru.erminson.ec.model.entity.RecordBook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RecordBookInitializerTest {
    private final List<TopicScoreDto> topicScoreDtos = new ArrayList<>() {{
        add(new TopicScoreDto("topic11", 100, 1));
        add(new TopicScoreDto("topic12", 100, 1));
        add(new TopicScoreDto("topic13", 100, 1));
        add(new TopicScoreDto("topic14",  90, 1));
        add(new TopicScoreDto("topic15",   0, 1));
        add(new TopicScoreDto("topic16",   0, 1));
    }};
    private final RecordBookDto recordBookDto = new RecordBookDto("Ivan", "Course1", "2021-09-15", topicScoreDtos);

    @BeforeEach
    void setUp() {
    }

    @Test
    @SneakyThrows
    void defaultConstructorShouldBePrivate() {
        Constructor<?> con = RecordBookInitializer.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(con.getModifiers()));
    }

    @Test
    void shouldBeCreateCorrectRecordBookFromDto() {
        RecordBook actualRecordBook = RecordBookInitializer.createRecordBookByRecordBookDto(recordBookDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(recordBookDto.getStartDate(), formatter);

        assertAll(
                () -> assertThat(actualRecordBook.getCourseTitle(), Matchers.equalTo(recordBookDto.getCourseTitle())),
                () -> assertThat(actualRecordBook.getTopics(), Matchers.hasSize(topicScoreDtos.size())),
                () -> assertThat(actualRecordBook.getStartDate(), Matchers.equalTo(localDate))
        );
    }

    @AfterEach
    void tearDown() {
    }
}