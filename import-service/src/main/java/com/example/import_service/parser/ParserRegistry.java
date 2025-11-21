package com.example.import_service.parser;

import com.example.import_service.dto.QuestionImportDTO;
import com.example.import_service.enums.ExcelColumn;
import com.example.import_service.enums.ImportType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParserRegistry {
    Map<ImportType, Function<Row, ?>> parsers;
    public ParserRegistry() {
        this.parsers = Map.of(
                ImportType.QUESTION, this::parseQuestion
                // Auto register more types here
        );
    }

    @SuppressWarnings("unchecked")
    public <T> Function<Row, T> getParser(ImportType type) {
        return (Function<Row, T>) parsers.get(type);
    }

    private QuestionImportDTO parseQuestion(Row row) {
        return QuestionImportDTO.builder()
                .content(GenericExcelParser.getString(row, ExcelColumn.CONTENT))
                .optionA(GenericExcelParser.getString(row, ExcelColumn.OPTION_A))
                .optionB(GenericExcelParser.getString(row, ExcelColumn.OPTION_B))
                .optionC(GenericExcelParser.getString(row, ExcelColumn.OPTION_C))
                .optionD(GenericExcelParser.getString(row, ExcelColumn.OPTION_D))
                .correctAnswer(GenericExcelParser.getString(row, ExcelColumn.CORRECT_ANSWER))
                .explanation(GenericExcelParser.getString(row, ExcelColumn.EXPLANATION))
                .level(GenericExcelParser.getString(row, ExcelColumn.LEVEL))
                .subjectId(GenericExcelParser.getInteger(row, ExcelColumn.SUBJECT_ID))
                .username(GenericExcelParser.getString(row, ExcelColumn.USERNAME))
                .build();
    }
}
