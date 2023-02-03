package ds.knapsack;

import ds.Generated;
import ds.ISolve;

public abstract class AbstractKnapsack<T> implements ISolve<T> {

  final Item[] items;
  final int capacity;

  public AbstractKnapsack(Item[] items, int capacity) {
    this.items = items.clone();
    this.capacity = capacity;
  }

  @Override
  public abstract T solve();

  @SuppressWarnings("all")
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String lineSeparator = System.lineSeparator();
    if (items != null && items.length > 0) {
      sb.append(getClass().getSimpleName())
          .append(" problem: ")
          .append(lineSeparator)
          .append("Capacity : ")
          .append(capacity)
          .append(lineSeparator)
          .append("Items :")
          .append(lineSeparator);
      for (Item item : items) sb.append("- ").append(item).append(lineSeparator);
    }
    return sb.toString();
  }

  @SuppressWarnings("all")
  public void display() {
    System.out.println(this);
  }
}
