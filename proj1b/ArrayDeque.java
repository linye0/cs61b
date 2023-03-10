public class ArrayDeque<T> implements Deque<T> {
    private int left;
    private int right;
    private T[] items;
    private int size;
    private int capacity = 8;

    public ArrayDeque() {
        left = 0;
        right = 0;
        items = (T[]) new Object[capacity];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    private boolean isFull() {
        return size == capacity;
    }

    private boolean isLowUsageRate() {
        return capacity >= 16 && size() / (double) capacity < 0.25;
    }
    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resize((int) (capacity * 1.5));
        }
        left = (left - 1 + capacity) % capacity;
        items[left] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (isFull()) {
            resize((int) (capacity * 1.5));
        }
        items[right] = item;
        right = (right + 1 + capacity) % capacity;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        for (int i = left; i != right; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.print(items[right]);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T res = items[left];
        left = (left + 1) % capacity;
        size--;
        if (isLowUsageRate()) {
            resize((int) (capacity * 0.5));
        }
        return res;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        right = (right - 1 + capacity) % capacity;
        T res = items[right];
        size--;
        if (isLowUsageRate()) {
            resize((int) (capacity * 0.5));
        }
        return res;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size() || isEmpty()) {
            return null;
        }
        return items[(left + index) % capacity];
    }
    private void resize(int newCapacity) {
        T[] newItems = (T[]) new Object[newCapacity];
        if (left >= right) {
            System.arraycopy(items, left, newItems, 0, capacity - left);
            System.arraycopy(items, 0, newItems, capacity - left, right);
        } else {
            System.arraycopy(items, left, newItems, 0, right - left);
        }
        left = 0;
        right = size;
        capacity = newCapacity;
        items = newItems;
    }
}
