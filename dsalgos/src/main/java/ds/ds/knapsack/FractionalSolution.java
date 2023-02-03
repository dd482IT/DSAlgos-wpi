package ds.knapsack;

import ds.Generated;
import java.util.List;

public class FractionalSolution<T> extends Solution<T> {

  public FractionalSolution(List<Item> items, T value) {
    super(items, value);
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
        sb.append("- ").append(toString(item)).append(lineSeparator);
        totalWeight += item.bounding;
      }
      sb.append("Total weight = ").append(totalWeight).append(lineSeparator);
    }
    return sb.toString();
  }

  private String toString(Item item) {
    return item.name + " [val = " + item.value + ", weight = " + item.bounding + "]";
  }
}
