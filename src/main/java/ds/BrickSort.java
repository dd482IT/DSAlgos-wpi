package ds;

public class BrickSort extends AbstractSort {

  @Override
  protected void sort(long[] a, int length) {
    boolean isSorted = false;
    resetCounts();
    final int maxComparisons =
        (length & 1) == 1 ? length * ((length - 1) >>> 1) : (length >>> 1) * (length - 1);
    while (!isSorted) {
      ++outerLoopCount;
      isSorted = true;
      // Perform Bubble sort on odd indexed element
      for (int i = 1; i < end; i = i + 2) {
        ++innerLoopCount;
        ++comparisonCount;
        if (a[i] > a[i + 1]) {
          swap(a, i, i + 1);
          isSorted = false;
          ++swapCount;
        }
      }
      if (swapCount == maxComparisons) {
        isSorted = true;
        break;
      }
      // Perform Bubble sort on even indexed element
      for (int i = 0; i < end; i = i + 2) {
        ++innerLoopCount;
        ++comparisonCount;
        if (a[i] > a[i + 1]) {
          swap(a, i, i + 1);
          isSorted = false;
          ++swapCount;
        }
      }
      if (swapCount == maxComparisons) isSorted = true;
    }
  }
}
