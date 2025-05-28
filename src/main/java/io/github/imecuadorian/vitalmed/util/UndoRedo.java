package io.github.imecuadorian.vitalmed.util;

import org.jetbrains.annotations.*;

import java.util.*;


public class UndoRedo<E> implements Iterable<E> {

    private final Deque<E> undoStack;
    private final Deque<E> redoStack;

    public UndoRedo() {
        this.undoStack = new LinkedList<>();
        this.redoStack = new LinkedList<>();
    }


    public void add(E item) {
        if (item != null) {
            undoStack.push(item);
            redoStack.clear();
        }
    }


    public E undo() {
        if (undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            return undoStack.peek();
        }
        return null;
    }

    public E redo() {
        if (!redoStack.isEmpty()) {
            E item = redoStack.pop();
            undoStack.push(item);
            return item;
        }
        return null;
    }

    public E getCurrent() {
        return undoStack.peek();
    }


    public boolean isUndoAble() {
        return undoStack.size() > 1;
    }

    public boolean isRedoAble() {
        return !redoStack.isEmpty();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    public void clearRedo() {
        redoStack.clear();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new HistoryIterator();
    }

    private class HistoryIterator implements Iterator<E> {
        private final List<E> combinedHistory;
        private int index = 0;

        public HistoryIterator() {
            List<E> undoList = new ArrayList<>(undoStack);
            undoList.reversed();

            List<E> redoList = new ArrayList<>(redoStack);

            combinedHistory = new ArrayList<>();
            combinedHistory.addAll(undoList);
            combinedHistory.addAll(redoList);
        }

        @Override
        public boolean hasNext() {
            return index < combinedHistory.size();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the history.");
            }
            return combinedHistory.get(index++);
        }
    }
}
