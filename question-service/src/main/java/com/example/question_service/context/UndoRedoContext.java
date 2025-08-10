package com.example.question_service.context;

import com.example.question_service.entity.Question;
import com.example.question_service.entity.QuestionAction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayDeque;
import java.util.Deque;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UndoRedoContext {
    Deque<QuestionAction> undoStack;
    Deque<QuestionAction> redoStack;
    int maxHistory;

    public UndoRedoContext(int maxHistory) {
        this.undoStack = new ArrayDeque<>(maxHistory);
        this.redoStack = new ArrayDeque<>(maxHistory);
        this.maxHistory = maxHistory;
    }

    /*Dùng synchronized để tránh nhiều người dùng thao tác cùng lúc hoặc một người mở nhiều tab
    đảm bảo stack không bị thay đổi bởi nhiều thread cùng lúc */
    public synchronized void pushUndo(QuestionAction  action) {
        if (undoStack.size() == maxHistory) {
            undoStack.removeLast();
        }
        undoStack.push(action);
        redoStack.clear();
    }

    public synchronized QuestionAction popUndo() {
        QuestionAction action = undoStack.poll(); // dùng poll vì khi stack rỗng thì sẽ trả null, pop ném ngoại lệ => try/catch khi dùng pop, poll chỉ cần ktra null
        if (action != null)
            redoStack.push(action);
        return action;
    }

    public synchronized QuestionAction popRedo() {
        QuestionAction action = redoStack.poll();
        if (action != null)
            undoStack.push(action);
        return action;
    }

    public synchronized boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public synchronized boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
