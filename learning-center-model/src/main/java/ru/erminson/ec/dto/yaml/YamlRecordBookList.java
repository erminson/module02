package ru.erminson.ec.dto.yaml;

import ru.erminson.ec.dto.RecordBookDto;

import java.util.List;

public class YamlRecordBookList {
    List<RecordBookDto> recordBooks;

    public List<RecordBookDto> getRecordBooks() {
        return recordBooks;
    }

    public void setRecordBooks(List<RecordBookDto> recordBooks) {
        this.recordBooks = recordBooks;
    }
}
