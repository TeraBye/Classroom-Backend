package com.example.import_service.parser;

import com.example.import_service.enums.ExcelColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericExcelParser {
    private static final DataFormatter FORMATTER = new DataFormatter();

    public static <T> List<T> parse(InputStream inputStream,
                                    Function<Row, T> rowParser,
                                    int skipRows) {
        List<T> result = new ArrayList<>();

        try (Workbook wb = createWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            int rowIndex = 0;

            for (Row row : sheet) {
                if (rowIndex++ < skipRows) continue;
                if (isRowEmpty(row)) continue;

                T record = rowParser.apply(row);
                if (record != null) {
                    result.add(record);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel", e);
        }
        return result;
    }

    private static Workbook createWorkbook(InputStream is) throws Exception {
        // Dùng SXSSF cho file lớn (>10k rows), XSSF cho file nhỏ
        return new XSSFWorkbook(is); // TODO: Switch to SXSSF for large files
    }

    private static String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        String value = FORMATTER.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer getIntegerCell(Row row, int columnIndex) {
        String value = getCellValue(row, columnIndex);
        return value != null ? Integer.valueOf(value) : null;
    }

    private static boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!getCellValue(row, i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Static utility methods for easy access
    public static String getString(Row row, ExcelColumn column) {
        return getCellValue(row, column.getIndex());
    }

    public static Integer getInteger(Row row, ExcelColumn column) {
        return getIntegerCell(row, column.getIndex());
    }
}
