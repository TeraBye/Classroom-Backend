package com.example.import_service.parser;

import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface FileParser<T> {
    List<T> parse(InputStream is, Function<Row, T> rowParser); // Adapt for non-Excel
}