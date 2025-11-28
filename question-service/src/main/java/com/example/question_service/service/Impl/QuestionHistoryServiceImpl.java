package com.example.question_service.service.Impl;

import com.example.question_service.entity.QuestionAction;
import com.example.question_service.service.QuestionHistoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionHistoryServiceImpl implements QuestionHistoryService {
    ConcurrentHashMap<String, Deque<QuestionAction>> undoStacks = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Deque<QuestionAction>> redoStacks = new ConcurrentHashMap<>();

    private static final int MAX_HISTORY = 30;

    @Override
    public void pushAction(String username, QuestionAction action) {
        undoStacks.computeIfAbsent(username, k -> new ArrayDeque<>(MAX_HISTORY))
                .push(action);

        // Giới hạn độ dài
        Deque<QuestionAction> stack = undoStacks.get(username);
        if (stack.size() > MAX_HISTORY) {
            stack.removeLast();
        }

        // Xóa redo stack khi có hành động mới
        redoStacks.remove(username);
    }

    @Override
    public Optional<QuestionAction> undo(String username) {
        Deque<QuestionAction> undoStack = undoStacks.get(username);
        if (undoStack == null || undoStack.isEmpty()) return Optional.empty();

        QuestionAction action = undoStack.pop();
        redoStacks.computeIfAbsent(username, k -> new ArrayDeque<>()).push(action);
        return Optional.of(action);
    }

    @Override
    public Optional<QuestionAction> redo(String username) {
        Deque<QuestionAction> redoStack = redoStacks.get(username);
        if (redoStack == null || redoStack.isEmpty()) return Optional.empty();

        QuestionAction action = redoStack.pop();
        undoStacks.computeIfAbsent(username, k -> new ArrayDeque<>()).push(action);
        return Optional.of(action);
    }

    @Override
    public boolean canUndo(String username) {
        Deque<QuestionAction> stack = undoStacks.get(username);
        return stack != null && !stack.isEmpty();
    }

    @Override
    public boolean canRedo(String username) {
        Deque<QuestionAction> stack = redoStacks.get(username);
        return stack != null && !stack.isEmpty();
    }
}
