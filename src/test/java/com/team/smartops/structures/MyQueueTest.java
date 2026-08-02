
package com.team.smartops.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * MyQueueTest
 */
public class MyQueueTest {
  @Test
  void normalCase_enqueueThenDequeueReturnsSame(){
    MyQueue<Integer> queue = new MyQueue<>();
    queue.enqueue(5);
    assertEquals(5, queue.dequeue());
  }

  @Test
  void normalCase_peekDoesNotRemoveItem(){
    MyQueue<Integer> queue = new MyQueue<>();
    queue.enqueue(8);
    assertEquals(8, queue.peek());
    assertEquals(8, queue.dequeue());
  }

  @Test 
  void normalCase_enqueueAddsToBackAndDequeueRemovesFromFront(){
    MyQueue<Integer> queue = new MyQueue<>();
    queue.enqueue(10);
    queue.enqueue(9);
    assertEquals(10, queue.peek());
    assertEquals(10, queue.dequeue());
  }

  @Test
void normalCase_maintainsFullFIFOOrder(){
    MyQueue<Integer> queue = new MyQueue<>();
    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    assertEquals(1, queue.dequeue());
    assertEquals(2, queue.dequeue());
    assertEquals(3, queue.dequeue());
}

  @Test
  void boundaryCase_isEmptyWhenNew(){
    MyQueue<Integer> queue = new MyQueue<>();
    assertTrue(queue.isEmpty());
  }

  @Test
  void boundaryCase_isNotEmptyAfterEnqueue(){
    MyQueue<Integer> queue = new MyQueue<>();
    queue.enqueue(6);
    assertFalse(queue.isEmpty());
  }

  @Test
void boundaryCase_forcesResizeBeyondInitialCapacity(){
    MyQueue<Integer> queue = new MyQueue<>();
    for (int i = 1; i <= 6; i++) queue.enqueue(i); // initial array holds 4
    for (int i = 1; i <= 6; i++) assertEquals(i, queue.dequeue());
}

  @Test
  void invalidInput_dequeueOnEmptyThrows(){
    MyQueue<Integer> queue = new MyQueue<>();
    assertThrows(IllegalStateException.class, queue::dequeue);
  }

@Test
void invalidInput_peekOnEmptyThrows(){
    MyQueue<Integer> queue = new MyQueue<>();
    assertThrows(IllegalStateException.class, queue::peek);
}
}