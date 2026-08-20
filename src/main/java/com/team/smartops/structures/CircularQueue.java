package com.team.smartops.structures;

/**
 * OWNER: Team C (C2).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */
public class CircularQueue<T> {
    // TODO: implement required operations (see Section 6 of the brief)
    private Object[] data;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public CircularQueue(int capacity){
        this.capacity = capacity;
        data = new Object[capacity];
        front = 0;
        rear = 0;
        size= 0;
    }

    public void enqueue(T value) {
        if (size == capacity) throw new IllegalStateException("Cannot enqueue: queue is full");
        data[rear] = value;
        rear = (rear + 1) % capacity;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Cannot dequeue from an empty queue");
        T value = (T) data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        return value;
    } 

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Cannot peek from an empty queue");
        return (T) data[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }
}
