package com.example.question_service.service;

import com.example.question_service.context.UndoRedoContext;
import com.example.question_service.entity.QuestionAction;

import java.util.Optional;

public interface QuestionHistoryService {
    UndoRedoContext getContextForUser(String username);
    void pushAction(String username, QuestionAction action);
    Optional<QuestionAction> undo(String username);
    Optional<QuestionAction> redo(String username);
    boolean canUndo(String username);
    boolean canRedo(String username);
}
