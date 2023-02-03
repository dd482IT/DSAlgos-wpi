package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.IStack;
import ds.LinkedListStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class LinkedListStackTest {

  private static final long VAL = 20;

  void testPopEmpty() {
    IStack stack = new LinkedListStack();
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.pop(),
        "Empty stack pop throws exception.");
  }

  void testPopEmptyOne() {
    IStack stack = new LinkedListStack();
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.pop(),
        "Empty stack pop throws exception.");
  }

  void testPop() {
    IStack stack = new LinkedListStack();
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.pop(), "Pop returns last value pushed.");
  }

  void testPushOne() {
    IStack stack = new LinkedListStack();
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.peek(), "Pop returns last value pushed.");
  }

  void testPushTwo() {
    IStack stack = new LinkedListStack();
    long val = VAL;
    stack.push(val);
    stack.push(val + 1);
    assertEquals(val + 1, stack.peek(), "Peek returns last value pushed.");
  }

  void testIsEmpty() {
    IStack stack = new LinkedListStack();
    assertTrue(stack.isEmpty(), "Stack must be empty.");
  }

  void testIsNotEmpty() {
    IStack stack = new LinkedListStack();
    stack.push(VAL);
    assertFalse(stack.isEmpty(), "Stack must not be empty.");
  }

  void testIsEmptySizeOne() {
    IStack stack = new LinkedListStack();
    assertTrue(stack.isEmpty(), "Stack must be empty.");
  }

  void testIsFull() {
    IStack stack = new LinkedListStack();
    assertFalse(stack.isFull(), "Stack must not be full.");
  }

  void testIsFullSizeOne() {
    IStack stack = new LinkedListStack();
    assertFalse(stack.isFull(), "Stack must be empty.");
  }

  void testPeekEmpty() {
    IStack stack = new LinkedListStack();
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.peek(),
        "Empty stack peep throws exception.");
  }

  void testPeekEmptyOne() {
    IStack stack = new LinkedListStack();
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.peek(),
        "Empty stack peek throws exception.");
  }

  void testPeek() {
    IStack stack = new LinkedListStack();
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.peek(), "Peek returns last value pushed.");
  }

  void testSize() {
    IStack stack = new LinkedListStack();
    long val = VAL;
    stack.push(val);
    assertEquals(1, stack.size(), "Size is one.");
  }

  void testSizeEmpty() {
    IStack stack = new LinkedListStack();
    assertEquals(0, stack.size(), "Size is zero.");
  }
}
