package ds.tests;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import ds.AVLTree;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.UseDiamondOperator"})
class AVLIntegerTest extends AbstractBinaryTreeTest<Integer> {

  public void init() {
    randomTree = new AVLTree<Integer>();
    anotherRandomTree = new AVLTree<Integer>();
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      randomTree.add(random.nextInt(1000));
      anotherRandomTree.add(random.nextInt(1000));
    }

    singleElement = 0;
    singleElementList = singletonList(0);
    severalElementsList = asList(new Integer[] {1, 2, 5, 8, 9, 10});
    severalElementsInOrderList = asList(new Integer[] {1, 2, 5, 8, 9, 10});
    severalElementsPreOrderList = asList(new Integer[] {8, 2, 1, 5, 9, 10});
    severalElementsPostOrderList = asList(new Integer[] {1, 5, 2, 10, 9, 8});
    severalElementsLevelOrderList = asList(new Integer[] {8, 2, 9, 1, 5, 10});
    severalNonExistentList = asList(new Integer[] {-1, 0, 3, 4, 6, 7, 11});

    duplicateElement = 1;
    duplicateElementList = singletonList(1);
    duplicateTwoElementList = asList(new Integer[] {1, 1});
    duplicateThreeElementList = asList(new Integer[] {1, 1, 1});
  }

  public void setup() {
    empty = new AVLTree<Integer>();
    one = new AVLTree<Integer>();
    one.add(0);
    several = new AVLTree<Integer>();
    several.add(5);
    several.add(2);
    several.add(1);
    several.add(9);
    several.add(8);
    several.add(10);
    duplicates = new AVLTree<Integer>();
    duplicates.add(5);
    duplicates.add(3);
    duplicates.add(3);
    duplicates.add(5);
    duplicates.add(8);
    duplicates.add(5);
    duplicates.add(10);
  }
}
