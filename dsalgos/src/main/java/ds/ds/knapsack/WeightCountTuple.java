package ds.knapsack;

import ds.Generated;

public class WeightCountTuple {
  private final int weight;
  private final int count;

  public WeightCountTuple(int weight, int count) {
    this.weight = weight;
    this.count = count;
  }

  @Override
  @SuppressWarnings("all")
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof WeightCountTuple)) return false;
    final WeightCountTuple other = (WeightCountTuple) o;
    if (!other.canEqual((Object) this)) return false;
    if (this.weight != other.weight) return false;
    if (this.count != other.count) return false;
    return true;
  }

  @SuppressWarnings("all")
  protected boolean canEqual(final Object other) {
    return other instanceof WeightCountTuple;
  }

  @Override
  @SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.weight;
    result = result * PRIME + this.count;
    return result;
  }
}
