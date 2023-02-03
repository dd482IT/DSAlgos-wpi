package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.IQueue;
import ds.NoCountQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class NoCountQueueTest {

  private static final String SIZE_ZERO = "Size must be zero.";
  private static final String SIZE_ONE = "Size must be one.";
  private static final String QUEUE_EMPTY = "Queue must be empty.";
  private static final long VAL = 20;

  void testConstructorException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new NoCountQueue(-1),
        "Constructor throws exception.");
  }

  void testPollEmpty() {
    IQueue queue = new NoCountQueue(0);
    assertThrows(
        IllegalStateException.class, () -> queue.poll(), "Empty queue poll throws exception.");
  }

  void testPollEmptyOne() {
    IQueue queue = new NoCountQueue(1);
    assertThrows(
        IllegalStateException.class, () -> queue.poll(), "Empty queue poll throws exception.");
  }

  void testPoll() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    assertEquals(val, queue.poll(), "Poll returns first value inserted.");
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testCircularInsert() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    queue.poll();
    queue.insert(val);
    assertEquals(1, queue.size(), SIZE_ONE);
  }

  void testCircularPoll() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    queue.poll();
    queue.insert(val);
    queue.poll();
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testInsertSizeOne() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    assertEquals(val, queue.peek(), "Peek returns first value inserted.");
    assertEquals(1, queue.size(), SIZE_ONE);
  }

  void testInsertException() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    assertThrows(IllegalStateException.class, () -> queue.insert(val), "Insert throws exception.");
  }

  void testInsertZeroSizeException() {
    IQueue queue = new NoCountQueue(0);
    long val = VAL;
    assertThrows(IllegalStateException.class, () -> queue.insert(val), "Insert throws exception.");
  }

  void testIsEmpty() {
    IQueue queue = new NoCountQueue(0);
    assertTrue(queue.isEmpty(), QUEUE_EMPTY);
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testIsNotEmpty() {
    IQueue queue = new NoCountQueue(1);
    queue.insert(VAL);
    assertFalse(queue.isEmpty(), "Queue must not be empty.");
    assertTrue(queue.isFull(), "Queue must be full.");
    assertEquals(1, queue.size(), SIZE_ONE);
  }

  void testIsEmptySizeOne() {
    IQueue queue = new NoCountQueue(1);
    assertTrue(queue.isEmpty(), QUEUE_EMPTY);
    assertFalse(queue.isFull(), QUEUE_EMPTY);
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testIsFull() {
    IQueue queue = new NoCountQueue(0);
    assertTrue(queue.isFull(), "Queue must be full.");
    assertTrue(queue.isEmpty(), QUEUE_EMPTY);
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testIsFullSizeOne() {
    IQueue queue = new NoCountQueue(1);
    assertFalse(queue.isFull(), QUEUE_EMPTY);
    assertTrue(queue.isEmpty(), QUEUE_EMPTY);
    assertEquals(0, queue.size(), SIZE_ZERO);
  }

  void testPeekEmpty() {
    IQueue queue = new NoCountQueue(0);
    assertThrows(
        IllegalStateException.class, () -> queue.peek(), "Empty queue peep throws exception.");
  }

  void testPeekEmptyOne() {
    IQueue queue = new NoCountQueue(1);
    assertThrows(
        IllegalStateException.class, () -> queue.peek(), "Empty queue peek throws exception.");
  }

  void testPeek() {
    IQueue queue = new NoCountQueue(1);
    long val = VAL;
    queue.insert(val);
    assertEquals(val, queue.peek(), "Peek returns first value inserted.");
    assertEquals(1, queue.size(), SIZE_ONE);
  }

  void testTwoElementQueueInsert() {
    IQueue queue = new NoCountQueue(2);
    long val = VAL;
    queue.insert(val);
    queue.insert(val + 1);
    assertEquals(val, queue.peek(), "Peek returns first value inserted.");
    assertEquals(2, queue.size(), "Size must be two.");
  }

  void testTwoElementQueuePoll() {
    IQueue queue = new NoCountQueue(2);
    long val = VAL;
    queue.insert(val);
    queue.insert(val + 1);
    assertEquals(val, queue.poll(), "Poll returns first value inserted.");
    assertEquals(1, queue.size(), SIZE_ONE);
  }

  void testTwoElementQueuePollTwice() {
    IQueue queue = new NoCountQueue(2);
    long val = VAL;
    queue.insert(val);
    queue.insert(val + 1);
    assertEquals(val, queue.poll(), "Poll returns first value inserted.");
    assertEquals(val + 1, queue.poll(), "Poll returns second value inserted.");
    assertEquals(0, queue.size(), SIZE_ONE);
  }
}
