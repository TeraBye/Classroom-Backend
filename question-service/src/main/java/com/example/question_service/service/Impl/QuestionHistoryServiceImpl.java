package com.example.question_service.service.Impl;

import com.example.question_service.context.UndoRedoContext;
import com.example.question_service.entity.QuestionAction;
import com.example.question_service.service.QuestionHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionHistoryServiceImpl implements QuestionHistoryService {
    ConcurrentHashMap<String, UndoRedoContext> userHistories = new ConcurrentHashMap<>();
    int defaultMaxHistory = 20;


    @Override
    public UndoRedoContext getContextForUser(String username) {
        return userHistories.computeIfAbsent(username, s -> new UndoRedoContext(defaultMaxHistory));
    }

    @Override
    public void pushAction(String username, QuestionAction action) {
        getContextForUser(username).pushUndo(action);
    }

    @Override
    public Optional<QuestionAction> undo(String username) {
        UndoRedoContext ctx = userHistories.get(username);
        if (ctx == null) return Optional.empty();
        return Optional.ofNullable(ctx.popUndo());
    }

    @Override
    public Optional<QuestionAction> redo(String username) {
        UndoRedoContext ctx = userHistories.get(username);
        if (ctx == null) return Optional.empty();
        return Optional.ofNullable(ctx.popRedo());
    }

    @Override
    public boolean canUndo(String username) {
        UndoRedoContext ctx = userHistories.get(username);
        return ctx != null && ctx.canUndo();
    }

    @Override
    public boolean canRedo(String username) {
        UndoRedoContext ctx = userHistories.get(username);
        return ctx != null && ctx.canRedo();
    }
}
