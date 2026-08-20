package com.team.smartops.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CircularQueueTest {
  
  @Test
  void normalCase_enqueueThenDequeueReturnsSame(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(1);
    cQueue.enqueue(5);
    assertEquals(5, cQueue.dequeue());
  }

  @Test
  void normalCase_maintainsFullFIFOOrder(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(3);
    cQueue.enqueue(1);
    cQueue.enqueue(2);
    cQueue.enqueue(3);
    assertEquals(1, cQueue.dequeue());
    assertEquals(2, cQueue.dequeue());
    assertEquals(3, cQueue.dequeue());
}

@Test
  void normalCase_peekDoesNotRemoveItem(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(1);
    cQueue.enqueue(8);
    assertEquals(8, cQueue.peek());
    assertEquals(8, cQueue.dequeue());
  }

  @Test
  void boundaryCase_isEmptyWhenNew(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(0);
    assertTrue(cQueue.isEmpty());
  }

  @Test
  void boundaryCase_isNotEmptyAfterEnqueue(){
   CircularQueue<Integer> cQueue = new CircularQueue<>(1);
    cQueue.enqueue(6);
    assertFalse(cQueue.isEmpty());
  }

  @Test
  void boundaryCase_enqueuePastCapacityWrapsCorrectly(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(3);
    cQueue.enqueue(2);
    cQueue.enqueue(4);
    cQueue.enqueue(6);
    assertEquals(2, cQueue.dequeue());  
    cQueue.enqueue(8);                    
    assertEquals(4, cQueue.dequeue());   
    assertEquals(6, cQueue.dequeue());
    assertEquals(8, cQueue.dequeue());
  }

  @Test
  void invalidInput_dequeueOnEmptyThrows(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(0);
    assertThrows(IllegalStateException.class, cQueue::dequeue);
  }

@Test 
void invalidInput_enqueueOnFullThrows(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(2);
    cQueue.enqueue(0);
    cQueue.enqueue(5);
    assertThrows(IllegalStateException.class, () -> cQueue.enqueue(8));
  }


@Test
void invalidInput_peekOnEmptyThrows(){
    CircularQueue<Integer> cQueue = new CircularQueue<>(0);
    assertThrows(IllegalStateException.class, cQueue::peek);
}
}
