package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    private int size;

    public CustomTree() {
        root = new Entry<>("root");
        size = 0;
    }

    private void walkEntryTree(Entry entry, EntryVisitor visitor, String key) {
        Queue<Entry> queue = new LinkedList<>();
        do {
            if (visitor.visitEntry(entry, key)) break;
            if (entry.leftChild != null) queue.add(entry.leftChild);
            if (entry.rightChild != null) queue.add(entry.rightChild);
        } while ((entry = queue.poll()) != null);
    }

    @Override
    public boolean add(String s) {
        Add add = new Add();
        walkEntryTree(root, add, s);
        if (!add.result) {
            ResetChild resetChild = new ResetChild();
            walkEntryTree(root, resetChild, null);
            walkEntryTree(root, add, s);
        }
        return add.result;
    }

    @Override
    public int size() {
        return size;
    }

    public String getParent(String s) {
        GetParent getParent = new GetParent();
        walkEntryTree(root, getParent, s);
        return getParent.result;
    }

    @Override
    public boolean remove (Object o) {
        if (!(o instanceof String))
            throw new UnsupportedOperationException();
        Remove remove = new Remove();
        walkEntryTree(root, remove, (String)o);
        return remove.result;
    }

    // недоступны методы, принимающие в качестве параметра индекс элемента
    @Override
    public String get(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    public String set(int index, String element) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    public void add(int index, String element) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    public String remove(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Index operations not supported");
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry (String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren () {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }

    interface EntryVisitor {
        boolean visitEntry(Entry entry, String key);        // if return true - walkEntryTree break
    }

    private class GetSize implements EntryVisitor {
        private int result;
        @Override
        public boolean visitEntry(Entry entry, String key) {
            result++;
            return false;
        }
    }

    private class ResetChild implements EntryVisitor {
        @Override
        public boolean visitEntry(Entry entry, String key) {
            if (entry.leftChild == null) entry.availableToAddLeftChildren = true;
            if (entry.rightChild == null) entry.availableToAddRightChildren = true;
            return false;
        }
    }

    private class Add implements EntryVisitor {
        private boolean result;
        @Override
        public boolean visitEntry(Entry entry, String key) {
            if (entry.isAvailableToAddChildren()) {
                Entry newEntry = new Entry(key);
                if (entry.availableToAddLeftChildren) {
                    entry.leftChild = newEntry;
                    entry.availableToAddLeftChildren = false;
                } else {
                    entry.rightChild = newEntry;
                    entry.availableToAddRightChildren = false;
                }
                newEntry.parent = entry;
                size++;
                result = true;
            }
            return result;
        }
    }

    private class GetParent implements EntryVisitor {
        private String result;
        @Override
        public boolean visitEntry(Entry entry, String key) {
            if (entry.elementName.equals(key)) {
                result = entry.parent.elementName;
                return true;
            }
            return false;
        }
    }

    private class Remove implements EntryVisitor {
        private boolean result;
        @Override
        public boolean visitEntry(Entry entry, String key) {
            GetSize getSize = new GetSize();
            if (entry.leftChild != null && entry.leftChild.elementName.equals(key)) {
                walkEntryTree(entry.leftChild, getSize, null);
                size -= getSize.result;
                entry.leftChild = null;
                result = true;
            }
            if (entry.rightChild != null && entry.rightChild.elementName.equals(key)) {
                walkEntryTree(entry.rightChild, getSize, null);
                size -= getSize.result;
                entry.rightChild = null;
                result = true;
            }
            return result;
        }
    }
}
