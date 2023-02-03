package ds.tests;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import ds.BinaryTree;
import ds.Tree;
import ds.TreeChecks;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.UseDiamondOperator"})
class BSTIntegerTest extends AbstractBinaryTreeTest<Integer> {

  private static final String TREE_MUST_NOT_PASS_CHECKS = "Tree must not pass all checks.";
  Tree<Integer> leftSidedTree;
  Tree<Integer> rightSidedTree;

  public void init() {
    randomTree = new BinaryTree<Integer>();
    anotherRandomTree = new BinaryTree<Integer>();
    leftSidedTree = new BinaryTree<Integer>();
    rightSidedTree = new BinaryTree<Integer>();
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      randomTree.add(random.nextInt(1000));
      anotherRandomTree.add(random.nextInt(1000));
    }
    for (int i = 10; i > 0; i--) leftSidedTree.add(i);
    for (int i = 1; i <= 10; i++) rightSidedTree.add(i);
    singleElement = 0;
    singleElementList = singletonList(0);
    severalElementsList = asList(new Integer[] {1, 2, 5, 8, 9, 10});
    severalElementsInOrderList = asList(new Integer[] {1, 2, 5, 8, 9, 10});
    severalElementsPreOrderList = asList(new Integer[] {5, 2, 1, 9, 8, 10});
    severalElementsPostOrderList = asList(new Integer[] {1, 2, 8, 10, 9, 5});
    severalElementsLevelOrderList = asList(new Integer[] {5, 2, 9, 1, 8, 10});
    severalNonExistentList = asList(new Integer[] {-1, 0, 3, 4, 6, 7, 11});

    duplicateElement = 1;
    duplicateElementList = singletonList(1);
    duplicateTwoElementList = asList(new Integer[] {1, 1});
    duplicateThreeElementList = asList(new Integer[] {1, 1, 1});
  }

  public void setup() {
    empty = new BinaryTree<Integer>();
    one = new BinaryTree<Integer>();
    one.add(0);
    several = new BinaryTree<Integer>();
    several.add(5);
    several.add(2);
    several.add(1);
    several.add(9);
    several.add(8);
    several.add(10);
    duplicates = new BinaryTree<Integer>();
    duplicates.add(5);
    duplicates.add(3);
    duplicates.add(3);
    duplicates.add(5);
    duplicates.add(8);
    duplicates.add(5);
    duplicates.add(10);
  }

  public void testTrees() {
    assertFalse(TreeChecks.check(leftSidedTree), TREE_MUST_NOT_PASS_CHECKS);
    assertFalse(TreeChecks.check(duplicates), TREE_MUST_NOT_PASS_CHECKS);
    assertFalse(TreeChecks.check(several), TREE_MUST_NOT_PASS_CHECKS);
    assertTrue(TreeChecks.check(empty), TREE_MUST_NOT_PASS_CHECKS);
    assertFalse(TreeChecks.check(one), TREE_MUST_NOT_PASS_CHECKS);
  }
}
