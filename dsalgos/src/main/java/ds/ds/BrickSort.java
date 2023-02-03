package ds;

import static ds.ArrayUtils.swapIfGreaterThan;

public class BrickSort extends AbstractBrickSort {

  protected boolean sorted;

  @Override
  public boolean isSorted() {
    return sorted;
  }

  @Override
  protected void reset() {
    super.reset();
    sorted = false;
  }

  @Override
  protected void sort(long[] a, int length) {
    reset();
    if (length <= 1) {
      sorted = true;
      return;
    }
    final int maxComparisons = computeMaxComparisons(length);
    while (!sorted) {
      ++outerLoopCount;
      sorted = true;
      oddSort(a, length);
      if (swapCount == maxComparisons) {
        sorted = true;
        break;
      }
      evenSort(a, length);
      if (swapCount == maxComparisons) sorted = true;
    }
  }

  protected void oddSort(long[] a, int length) {
    for (int i = 1; i < length - 1; i = i + 2) {
      ++innerLoopCount;
      bubble(a, i);
    }
  }

  protected void evenSort(long[] a, int length) {
    for (int i = 0; i < length - 1; i = i + 2) {
      ++innerLoopCount;
      bubble(a, i);
    }
  }

  @Override
  protected void bubble(long[] a, int i) {
    ++comparisonCount;
    if (swapIfGreaterThan(a, i, i + 1)) {
      sorted = false;
      ++swapCount;
    }
  }
}
