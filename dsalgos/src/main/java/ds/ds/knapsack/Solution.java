package ds.knapsack;

import ds.Generated;
import java.util.List;

public class Solution<T> {

  // list of items to put in the bag to have the maximal value
  final List<Item> items;
  // maximal value possible
  final T value;

  public Solution(List<Item> items, T value) {
    this.items = items;
    this.value = value;
  }

  @SuppressWarnings("all")
  public void display() {
    System.out.println(this);
  }

  @SuppressWarnings("fenum")
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String lineSeparator = System.lineSeparator();
    if (items != null && !items.isEmpty()) {
      int totalWeight = 0;
      sb.append(lineSeparator)
          .append("Knapsack solution")
          .append(lineSeparator)
          .append("Value = ")
          .append(value)
          .append(lineSeparator)
          .append("Items to pick :")
          .append(lineSeparator);
      for (Item item : items) {
        sb.append("- ").append(item).append(lineSeparator);
        totalWeight += item.weight * item.bounding;
      }
      sb.append("Total weight = ").append(totalWeight).append(lineSeparator);
    }
    return sb.toString();
  }

  public T getValue() {
    return value;
  }

  public List<Item> getItems() {
    return items;
  }
}
