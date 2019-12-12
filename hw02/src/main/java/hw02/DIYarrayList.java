package hw02;

import java.util.*;

public class DIYarrayList<E> implements List<E> {
    private int size = 0;
    private Object[] storage;

    static final int INIT_SIZE = 10;

    public DIYarrayList() {
        storage = new Object[INIT_SIZE];
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public Object[] toArray() {
        return Arrays.copyOf(storage, size);
    }

    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E e) {
        if (storage.length == size) {
            storage = grow(size + 1);
        }
        storage[size] = e;
        size += 1;
        return true;
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.storage).length - (s = size))
            elementData = grow(s + numNew);
        System.arraycopy(a, 0, elementData, s, numNew);
        size = s + numNew;
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            storage[i] = null;
        size = 0;
    }

    public E get(int index) {
        checkIndex(index);
        return (E)storage[index];
    }

    public E set(int index, E element) {
        checkIndex(index);

        E previous = (E)storage[index];
        storage[index] = element;

        return previous;
    }

    public void add(int index, E element) {
        if (index < 0)
            throw new IndexOutOfBoundsException();

        if (storage.length >= size)
            storage = grow(storage.length + 1);

        System.arraycopy(storage, index, storage, index + 1, size - index);
        storage[index] = element;
        size++;
    }

    public E remove(int index) {
        checkIndex(index);

        E oldValue = (E)storage[index];

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(
                    storage, index+1,
                    storage, index,
                    numMoved);

        storage[--size] = null;
        return oldValue;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (storage[i] == null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (storage[i].equals(o))
                    return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size; i >= 0; --i)
                if (storage[i] == null)
                    return i;
        } else {
            for (int i = size; i >= 0; --i)
                if (storage[i].equals(o))
                    return i;
        }
        return -1;
    }

    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public ListIterator<E> listIterator(int index) {
        checkIndex(index);
        return new ListItr(index);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private Object[] grow (int newSize) {

        Object[] newStorage = new Object[newSize];
        for (int i = 0; i < size; i++) {
            newStorage[i]  = storage[i];
        }
        return newStorage;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException();
    }

    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        // prevent creating a synthetic constructor
        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = DIYarrayList.this.storage;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            DIYarrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = DIYarrayList.this.storage;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                DIYarrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {

                int i = cursor;
                DIYarrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
        }
    }
}
