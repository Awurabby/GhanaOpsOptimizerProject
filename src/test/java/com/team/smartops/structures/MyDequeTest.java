package com.team.smartops.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MyDequeTest {
  
  @Test
  void normalCase_addFrontAndRemoveRearOnSingleElementDeque(){
    MyDeque<Integer> deque = new MyDeque<>();
    deque.addFront(2);
    assertEquals(2, deque.removeRear());
    deque.addFront(4);
    assertEquals(4, deque.removeFront());
  }

  @Test
  void normalCase_addFrontAndRemoveRearOnMultipleElementsDeque(){
    MyDeque<Integer> deque = new MyDeque<>();
    deque.addFront(2);
    deque.addFront(5);
    deque.addRear(6);
    assertEquals(5, deque.removeFront());
    assertEquals(6, deque.removeRear());
    deque.addFront(9);
    assertEquals(9, deque.removeFront());
  }

  @Test
void normalCase_peekFrontAndPeekRearDoNotRemove(){
    MyDeque<Integer> deque = new MyDeque<>();
    deque.addRear(1);
    deque.addRear(2);
    assertEquals(1, deque.peekFront());
    assertEquals(2, deque.peekRear());
    assertEquals(2, deque.size()); 
}

@Test
  void boundaryCase_addRearOnEmptyDequeWorks(){
    MyDeque<Integer> deque = new MyDeque<>();
    deque.addRear(7);
    assertEquals(7, deque.removeRear());
    deque.addRear(9);
    assertEquals(9, deque.removeFront());
  }

  @Test
void boundaryCase_dequeIsReusableAfterBecomingEmpty(){
    MyDeque<Integer> deque = new MyDeque<>();
    deque.addRear(1);
    deque.removeFront();          // deque empties via the front-removal branch
    assertTrue(deque.isEmpty());

    deque.addFront(2);            // now add from the OTHER end
    assertEquals(2, deque.peekFront());
    assertEquals(2, deque.peekRear());   // if tail wasn't reset properly, this could be wrong
}

@Test
void invalidInput_removeFrontOnEmptyThrows(){
  MyDeque<Integer> deque = new MyDeque<>();
  assertThrows(IllegalStateException.class, deque::removeFront);
}

@Test
void invalidInput_removeRearOnEmptyThrows(){
  MyDeque<Integer> deque = new MyDeque<>();
  assertThrows(IllegalStateException.class, deque::removeRear);
}

@Test
void invalidInput_peekFrontOnEmptyThrows(){
  MyDeque<Integer> deque = new MyDeque<>();
  assertThrows(IllegalStateException.class, deque::peekFront);
}

@Test
void invalidInput_peekRearOnEmptyThrows(){
  MyDeque<Integer> deque = new MyDeque<>();
  assertThrows(IllegalStateException.class, deque::peekRear);
}
}

