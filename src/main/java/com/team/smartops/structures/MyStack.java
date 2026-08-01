package com.team.smartops.structures;

/**
 * OWNER: Team C (C2).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */
public class MyStack<T> {
    // TODO: implement required operations (see Section 6 of the brief)

    private Object[] data;
    private int size;


    public MyStack(){
        data = new Object[4];
        size = 0;
    }

    public void push(T value){
        if (size == data.length) resize();
        data[size++] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop(){
        if(isEmpty()) throw new CollectionStateException("Cannot pop from an empty stack");
        T top = (T) data[size - 1];
        data[size - 1] = null;
        size--;
        return top;
    }

    @SuppressWarnings("unchecked")
    public T peek(){
        if(isEmpty()) throw new CollectionStateException("Cannot peek from an empty stack");
        return (T) data[size - 1];
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
