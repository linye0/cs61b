public class LinkedListDeque<T> {
    private int size;
    private Node sentinel;
    private class Node {
        Node prev;
        T item;
        Node next;
        public Node(LinkedListDeque<T>.Node prev, T item, LinkedListDeque<T>.Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public int size() {
        return size;
    }

    public LinkedListDeque() {
        sentinel = new Node(null, (T) new Object(), null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }
    public void addFirst(T x) {
        Node node = new Node(sentinel, x, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        size++;
    }

    public void addLast(T x) {
        Node node = new Node(sentinel.prev, x, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (Node i = sentinel.next; i != sentinel; i = i.next) {
            if (i == sentinel) {
                System.out.print(i.item);
                break;
            }
            System.out.print(i.item + " ");
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T res = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return res;
    }

    public T removeLast() {
        if(isEmpty()) {
            return null;
        }
        T res = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return res;
    }

    public T get(int index) {
        if(size < index) {
            return null;
        }
        Node p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }
     public T getRecursive(int index) {
        if(size < index) {
            return null;
        }
        return getRecursive(sentinel.next, index);
     }

     private T getRecursive(LinkedListDeque<T>.Node node, int i){
        if (i == 0) {
            return node.item;
        }
        return getRecursive(node.next, i - 1);
     }
}

