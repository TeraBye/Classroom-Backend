package com.example.import_service.enums;

public enum ExcelColumn { CONTENT(0), OPTION_A(1), OPTION_B(2), OPTION_C(3), OPTION_D(4),
    CORRECT_ANSWER(5), EXPLANATION(6), LEVEL(7), SUBJECT_ID(8), USERNAME(9),;
    private final int index;

    ExcelColumn(int index) {
        this.index = index;
    }

    public int getIndex() { return index; }
}
