package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.Tree;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AvoidFieldNameMatchingMethodName", "PMD.TooManyFields"})
abstract class AbstractBinaryTreeTest<T extends Comparable<T>> extends BaseTreeTest<T> {

  Tree<T> empty;
  Tree<T> one;
  Tree<T> several;
  Tree<T> duplicates;
  Tree<T> randomTree;
  Tree<T> anotherRandomTree;
  T singleElement;
  List<T> singleElementList;
  List<T> severalElementsList;
  List<T> severalElementsInOrderList;
  List<T> severalElementsPreOrderList;
  List<T> severalElementsPostOrderList;
  List<T> severalElementsLevelOrderList;
  List<T> severalNonExistentList;

  T duplicateElement;
  List<T> duplicateElementList;
  List<T> duplicateTwoElementList;
  List<T> duplicateThreeElementList;

  public void testRandomInserts() {
    for (T s : randomTree.values()) assertEquals(s, randomTree.find(s).value(), "Value not found!");
  }

  public void testRandomRemoves() {
    for (T s : randomTree.values()) randomTree.remove(s);
    assertTreeEmpty(randomTree);
  }

  public void testMoreRandomRemoves() {
    for (T s : anotherRandomTree.valuesLevelOrder()) anotherRandomTree.remove(s);
    assertTreeEmpty(anotherRandomTree);
  }

  public void testEmptyContainsZeroItems() {
    assertTreeEmpty(empty);
  }

  public void testEmptyTreeIteratorException() {
    assertTreeEmptyIteratorException(empty);
  }

  public void testOneContainsOneItem() {
    assertTrue(one.contains(singleElement), () -> "One should contain " + singleElement);
    assertIterationValid(one, singleElementList);
  }

  public void testSeveralContainsSixItems() {
    assertContains(several, severalElementsList);
    assertIterationValid(several, severalElementsInOrderList);
  }

  public void testPreOrderIteration() {
    assertPreOrderIterationValid(several, severalElementsPreOrderList);
  }

  public void testPostOrderIteration() {
    assertPostOrderIterationValid(several, severalElementsPostOrderList);
  }

  public void testBreadthFirstOrderIteration() {
    assertBreadthFirstOrderIterationValid(several, severalElementsLevelOrderList);
  }

  public void testSeveralDoesNotContain() {
    assertDoesNotContain(several, severalNonExistentList);
  }

  public void testRemoveFromEmpty() {
    empty.remove(singleElement);
    assertTreeEmpty(empty);
  }

  public void testRemoveFromOne() {
    one.remove(singleElement);
    assertFalse(one.contains(singleElement), () -> singleElement + "not removed from one");
    assertTreeEmpty(one);
  }

  public void testRemoveByLeaf() {
    assertRemoveAll(several, severalElementsList);
  }

  public void testRemoveByRoot() {
    assertRemoveAll(several, severalElementsList);
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  public void testDuplicates() {
    empty.add(duplicateElement);
    empty.add(duplicateElement);
    empty.add(duplicateElement);
    assertIterationValid(empty, duplicateThreeElementList);
    assertTrue(empty.contains(duplicateElement), () -> "Should contain " + duplicateElement);
    empty.remove(duplicateElement);
    assertTrue(empty.contains(duplicateElement), () -> "Should still contain " + duplicateElement);
    assertIterationValid(empty, duplicateTwoElementList);
    empty.remove(duplicateElement);
    assertTrue(empty.contains(duplicateElement), () -> "Should still contain " + duplicateElement);
    assertIterationValid(empty, duplicateElementList);
    empty.remove(duplicateElement);
    assertFalse(empty.contains(duplicateElement), () -> "Should not contain " + duplicateElement);
    assertTreeEmpty(empty);
  }
}
