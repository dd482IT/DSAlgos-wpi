package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.SingleNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class SingleNodeTest {
  private static final String EXCEPTION_EXPECTED = "Exception expected.";

  void testConstructor() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    assertEquals(SCORE, node.getData(), "Value equals constructor parameter.");
    assertNull(node.getNext(), "Next must be null.");
    assertThrows(UnsupportedOperationException.class, () -> node.getPrev(), EXCEPTION_EXPECTED);
  }

  @SuppressWarnings("nullness:argument.type.incompatible")
  void testSetPrev() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    assertThrows(UnsupportedOperationException.class, () -> node.setPrev(null), EXCEPTION_EXPECTED);
  }

  void testSetNext() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    SingleNode<Integer> next = new SingleNode<>(TEN);
    node.setNext(next);
    assertEquals(TEN, node.getNext().getData(), () -> "Value must be " + TEN + ".");
  }

  void testSetData() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    node.setData(TEN);
    assertEquals(TEN, node.getData(), () -> "Value must be " + TEN + ".");
  }

  void testIsSameFalse() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    SingleNode<Integer> next = new SingleNode<>(TEN);
    assertFalse(node.isSame(next), () -> "Two different objects.");
  }

  void testIsSameTrue() {
    SingleNode<Integer> node = new SingleNode<>(SCORE);
    assertTrue(node.isSame(node), () -> "Same object.");
  }
}
