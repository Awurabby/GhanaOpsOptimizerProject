package com.team.smartops.structures;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyStackTest {
  @Test
  void normalCase_pushThenPopReturnsSameValue(){
    MyStack<Integer> stack = new MyStack<>();
    stack.push(5);
    assertEquals(5, stack.pop());
  }

  @Test
  void normalCase_peekDoesNotRemoveItem(){
    MyStack<Integer> stack = new MyStack<>();
    stack.push(10);
    assertEquals(10, stack.peek());
    assertEquals(10, stack.pop());
  }

  @Test
  void boundaryCase_isEmptyWhenNew(){
    MyStack<Integer> stack = new MyStack<>();
    assertTrue(stack.isEmpty());
  }

  @Test
  void boundaryCase_isNotEmptyAfterPush(){
    MyStack<Integer> stack = new MyStack<>();
    stack.push(1);
    assertFalse(stack.isEmpty());
  }

  @Test
  void invalidInput_popFromEmptyThrows(){
    MyStack<Integer> stack = new MyStack<>();
    assertThrows(IllegalStateException.class, stack::pop);
  }

  @Test
  void invalidInput_peekFromEmptyThrows(){
    MyStack<Integer> stack = new MyStack<>();
    assertThrows(IllegalStateException.class, stack::peek);
  }
}
