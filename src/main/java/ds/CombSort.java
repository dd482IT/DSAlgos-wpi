package ds;

public class CombSort extends AbstractSort {

  private int getNextGap(int gap) {
    // Shrink gap by Shrink factor
    int newGap = (gap * 10) / 13;
    return newGap <= 1 ? 1 : newGap;
  }

  @Override
  protected void sort(long[] a, int length) {
    int gap = length;
    boolean swapped = true;
    reset();
    while (gap > 1 || swapped) {
      ++outerLoopCount;
      gap = getNextGap(gap);
      swapped = false;
      for (int i = 0; i < length - gap; i++) {
        ++innerLoopCount;
        ++comparisonCount;
        if (a[i] > a[i + gap]) {
          swap(a, i, i + gap);
          swapped = true;
          ++swapCount;
        }
      }
    }
  }
}
