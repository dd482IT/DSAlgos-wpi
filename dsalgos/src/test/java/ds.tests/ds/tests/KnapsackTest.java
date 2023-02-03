package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.knapsack.AbstractKnapsack;
import ds.knapsack.BoundedKnapsack;
import ds.knapsack.BoundedNaiveKnapsack;
import ds.knapsack.FractionalKnapsack;
import ds.knapsack.Item;
import ds.knapsack.Knapsack;
import ds.knapsack.NaiveKnapsack;
import ds.knapsack.Solution;
import ds.knapsack.UnboundedKnapsack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class KnapsackTest {
  private static final String VALUES_EQUAL = "Values must be equal.";
  private final Item[] items;
  private final Item[] boundedItems;
  private final Item[] unboundedItems;
  private final Item[] unboundedItems2;
  private final Item[] fractionalItems;

  KnapsackTest() {
    items =
        new Item[] {
          new Item("Elt1", 4, 12),
          new Item("Elt2", 2, 1),
          new Item("Elt3", 2, 2),
          new Item("Elt4", 1, 1),
          new Item("Elt5", 10, 4)
        };
    boundedItems =
        new Item[] {
          new Item("map", 150, 9, 1),
          new Item("compass", 35, 13, 1),
          new Item("water", 200, 153, 3),
          new Item("sandwich", 60, 50, 2),
          new Item("glucose", 60, 15, 2),
          new Item("tin", 45, 68, 3),
          new Item("banana", 60, 27, 3),
          new Item("apple", 40, 39, 3),
          new Item("cheese", 30, 23, 1),
          new Item("beer", 10, 52, 3),
          new Item("suntan cream", 70, 11, 1),
          new Item("camera", 30, 32, 1),
          new Item("t-shirt", 15, 24, 2),
          new Item("trousers", 10, 48, 2),
          new Item("umbrella", 40, 73, 1),
          new Item("waterproof trousers", 70, 42, 1),
          new Item("waterproof overclothes", 75, 43, 1),
          new Item("note-case", 80, 22, 1),
          new Item("sunglasses", 20, 7, 1),
          new Item("towel", 12, 18, 2),
          new Item("socks", 50, 4, 1),
          new Item("book", 10, 30, 2)
        };
    unboundedItems =
        new Item[] {
          new Item("Element1", 10, 5, 0),
          new Item("Element2", 30, 10, 0),
          new Item("Element3", 20, 15, 0)
        };
    unboundedItems2 =
        new Item[] {
          new Item("E1", 40, 12, 0), new Item("E2", 60, 20, 0), new Item("E3", 50, 15, 0)
        };
    fractionalItems =
        new Item[] {
          new Item("beef", 36, 38),
          new Item("pork", 43, 54),
          new Item("ham", 90, 36),
          new Item("greaves", 45, 24),
          new Item("flitch", 30, 40),
          new Item("brawn", 56, 25),
          new Item("welt", 67, 37),
          new Item("salami", 95, 30),
          new Item("sausage", 98, 59)
        };
  }

  void testKnapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new Knapsack(items, 15);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(15, solution.getValue(), VALUES_EQUAL);
  }

  void testFractionalKnapsack() {
    AbstractKnapsack<Solution<Double>> knapsack = new FractionalKnapsack(fractionalItems, 150);
    knapsack.display();
    Solution<Double> solution = knapsack.solve();
    solution.display();
    assertEquals(349, Math.round(solution.getValue()), VALUES_EQUAL);
  }

  void testFractionalKnapsack2() {
    AbstractKnapsack<Solution<Double>> knapsack = new FractionalKnapsack(fractionalItems, 343);
    knapsack.display();
    Solution<Double> solution = knapsack.solve();
    solution.display();
    assertEquals(560, Math.round(solution.getValue()), VALUES_EQUAL);
  }

  void testFractionalKnapsackZeroCapacity() {
    AbstractKnapsack<Solution<Double>> knapsack = new FractionalKnapsack(fractionalItems, 0);
    knapsack.display();
    Solution<Double> solution = knapsack.solve();
    solution.display();
    assertEquals(0, Math.round(solution.getValue()), VALUES_EQUAL);
  }

  void testFractionalKnapsackNegativeCapacity() {
    AbstractKnapsack<Solution<Double>> knapsack = new FractionalKnapsack(fractionalItems, -10);
    knapsack.display();
    Solution<Double> solution = knapsack.solve();
    solution.display();
    assertEquals(0, Math.round(solution.getValue()), VALUES_EQUAL);
  }

  void testFractionalKnapsackEmptyList() {
    AbstractKnapsack<Solution<Double>> knapsack = new FractionalKnapsack(new Item[0], 10);
    knapsack.display();
    Solution<Double> solution = knapsack.solve();
    solution.display();
    assertEquals(0, Math.round(solution.getValue()), VALUES_EQUAL);
  }

  void testBoundedKnapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new BoundedKnapsack(boundedItems, 400);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(1010, solution.getValue(), VALUES_EQUAL);
  }

  void testBoundedNaiveKnapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new BoundedNaiveKnapsack(boundedItems, 400);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(1010, solution.getValue(), VALUES_EQUAL);
  }

  void testUnboundedKnapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new UnboundedKnapsack(unboundedItems, 100);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(300, solution.getValue(), VALUES_EQUAL);
  }

  void testUnbounded2Knapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new UnboundedKnapsack(unboundedItems2, 45);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(150, solution.getValue(), VALUES_EQUAL);
  }

  void testKnapsackNoItems() {
    AbstractKnapsack<Solution<Integer>> knapsack = new Knapsack(new Item[0], 15);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(0, solution.getValue(), VALUES_EQUAL);
  }

  void testKnapsackOneItem() {
    Item[] oneItems = {new Item("Elt1", 4, 12)};
    AbstractKnapsack<Solution<Integer>> knapsack = new Knapsack(oneItems, 12);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(4, solution.getValue(), VALUES_EQUAL);
  }

  void testKnapsackWeightZero() {
    AbstractKnapsack<Solution<Integer>> knapsack = new Knapsack(items, 0);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(0, solution.getValue(), VALUES_EQUAL);
  }

  void testNaiveKnapsack() {
    AbstractKnapsack<Solution<Integer>> knapsack = new NaiveKnapsack(items, 15);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(15, solution.getValue(), VALUES_EQUAL);
  }

  void testNaiveKnapsackWeightZero() {
    AbstractKnapsack<Solution<Integer>> knapsack = new NaiveKnapsack(items, 0);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(0, solution.getValue(), VALUES_EQUAL);
  }

  void testNaiveKnapsackNoItems() {
    AbstractKnapsack<Solution<Integer>> knapsack = new NaiveKnapsack(new Item[0], 15);
    knapsack.display();
    Solution<Integer> solution = knapsack.solve();
    solution.display();
    assertEquals(0, solution.getValue(), VALUES_EQUAL);
  }
}
