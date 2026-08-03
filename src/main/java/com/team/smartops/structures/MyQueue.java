package com.team.smartops.structures;

/**
 * OWNER: Team C (C2).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */
public class MyQueue<T> {
    // TODO: implement required operations (see Section 6 of the brief)
    private Object[] data;
    private int front;
    private int size;

    public MyQueue(){
        data = new Object[4];
        front = 0;
        size = 0;
    }

    public void enqueue(T value){
        if (front + size == data.length) resize();
        data[front + size] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new CollectionStateException("Cannot dequeue from an empty queue");
        T value = (T) data[front];
        data[front] = null;
        front++;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek(){
        if(isEmpty()) throw new CollectionStateException("Cannot peek from an empty queue");
        return (T) data[front];
    }

    public boolean isEmpty(){
        return size == 0;
    }

    private void resize(){
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) bigger[i] = data[i];
        data = bigger;
    }

     public int size(){
        return size;
    }
}
