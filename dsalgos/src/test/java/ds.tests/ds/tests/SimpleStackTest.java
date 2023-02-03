package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.IStack;
import ds.SimpleStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class SimpleStackTest {

  private static final long VAL = 20;

  void testPopEmpty() {
    IStack stack = new SimpleStack(0);
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.pop(),
        "Empty stack pop throws exception.");
  }

  void testPopEmptyOne() {
    IStack stack = new SimpleStack(1);
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.pop(),
        "Empty stack pop throws exception.");
  }

  void testPop() {
    IStack stack = new SimpleStack(1);
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.pop(), "Pop returns last value pushed.");
  }

  void testSize() {
    IStack stack = new SimpleStack(1);
    long val = VAL;
    stack.push(val);
    assertEquals(1, stack.size(), "Size is one.");
  }

  void testSizeEmpty() {
    IStack stack = new SimpleStack(1);
    assertEquals(0, stack.size(), "Size is zero.");
  }

  void testPushSizeOne() {
    IStack stack = new SimpleStack(1);
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.peek(), "Pop returns last value pushed.");
  }

  void testPushException() {
    IStack stack = new SimpleStack(1);
    long val = VAL;
    stack.push(val);
    assertThrows(
        ArrayIndexOutOfBoundsException.class, () -> stack.push(val), "Push throws exception.");
  }

  void testPushZeroSizeException() {
    IStack stack = new SimpleStack(0);
    long val = VAL;
    assertThrows(
        ArrayIndexOutOfBoundsException.class, () -> stack.push(val), "Push throws exception.");
  }

  void testIsEmpty() {
    IStack stack = new SimpleStack(0);
    assertTrue(stack.isEmpty(), "Stack must be empty.");
  }

  void testIsNotEmpty() {
    IStack stack = new SimpleStack(1);
    stack.push(VAL);
    assertFalse(stack.isEmpty(), "Stack must not be empty.");
  }

  void testIsEmptySizeOne() {
    IStack stack = new SimpleStack(1);
    assertTrue(stack.isEmpty(), "Stack must be empty.");
  }

  void testIsFull() {
    IStack stack = new SimpleStack(0);
    assertTrue(stack.isFull(), "Stack must be full.");
  }

  void testIsFullSizeOne() {
    IStack stack = new SimpleStack(1);
    assertFalse(stack.isFull(), "Stack must be empty.");
  }

  void testPeekEmpty() {
    IStack stack = new SimpleStack(0);
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.peek(),
        "Empty stack peep throws exception.");
  }

  void testPeekEmptyOne() {
    IStack stack = new SimpleStack(1);
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> stack.peek(),
        "Empty stack peek throws exception.");
  }

  void testPeek() {
    IStack stack = new SimpleStack(1);
    long val = VAL;
    stack.push(val);
    assertEquals(val, stack.peek(), "Peek returns last value pushed.");
  }
}
