package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.DoubleNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class DoubleNodeTest {

  void testConstructor() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    assertEquals(SCORE, node.getData(), "Value equals constructor parameter.");
    assertNull(node.getNext(), "Next must be null.");
    assertNull(node.getPrev(), "Prev must be null.");
  }

  @SuppressWarnings("nullness:argument.type.incompatible")
  void testSetPrev() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    DoubleNode<Integer> prev = new DoubleNode<>(SCORE - 1);
    node.setPrev(prev);
    assertSame(prev, node.getPrev(), "References must be equal.");
    assertEquals(SCORE - 1, node.getPrev().getData(), "Values must be equal.");
  }

  void testSetNext() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    DoubleNode<Integer> next = new DoubleNode<>(TEN);
    node.setNext(next);
    assertSame(next, node.getNext(), "References must be equal.");
    assertEquals(TEN, node.getNext().getData(), () -> "Value must be " + TEN + ".");
  }

  void testSetData() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    node.setData(TEN);
    assertEquals(TEN, node.getData(), () -> "Value must be " + TEN + ".");
  }

  void testIsSameFalse() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    DoubleNode<Integer> next = new DoubleNode<>(TEN);
    assertFalse(node.isSame(next), () -> "Two different objects.");
  }

  void testIsSameTrue() {
    DoubleNode<Integer> node = new DoubleNode<>(SCORE);
    assertTrue(node.isSame(node), () -> "Same object.");
  }
}
