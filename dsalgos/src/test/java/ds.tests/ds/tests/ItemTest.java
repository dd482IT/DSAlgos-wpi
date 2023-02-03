package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.knapsack.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class ItemTest {
  Item item;
  Item item2;

  ItemTest() {
    item = new Item("E1", 20, 2);
    item2 = new Item("E2", 20, 3);
  }

  void testMoreThan() {
    assertEquals(-1, item.compareTo(item2), "Greater than expected.");
  }

  void testLessThan() {
    assertEquals(1, item2.compareTo(item), "Less than expected.");
  }

  void testEqualTo() {
    assertEquals(0, item.compareTo(item), "Equal to expected.");
  }
}
