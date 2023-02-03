package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.BubbleSort;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.OrdArray;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("PMD.LawOfDemeter")
class BubbleSortTest implements SortProvider {

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    ISort sorter = new BubbleSort();
    IArray sorted = arr.sort(sorter);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new BubbleSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    ISort sorter = new BubbleSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
  }

  void testSortSmallData(IArray arr) {
    ISort sorter = new BubbleSort();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, "Arrays must be sorted and equal.");
    assertEquals(5, sorter.getSwapCount(), "Swap count will be five.");
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  void testStreamUnSorted() {
    IArray high = new HighArray();
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .unordered()
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new BubbleSort();
    IArray sorted = high.sort(sorter);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testReset() {
    IArray high = new HighArray();
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new BubbleSort();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(
        SCORE - 1, sorter.getComparisonCount(), "Comparison count must be " + (SCORE - 1) + ".");
  }

  void testComparisonCountSorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new BubbleSort();
    high.sort(sorter);
    int compCount = sorter.getComparisonCount();
    assertEquals(SCORE - 1, compCount, "Comparison count must be " + (SCORE - 1) + ".");
  }

  void testComparisonCountUnsorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).parallel().unordered().forEach(i -> high.insert(i));
    ISort sorter = new BubbleSort();
    high.sort(sorter);
    int compCount = sorter.getComparisonCount();
    assertTrue(
        SCORE - 1 <= compCount && compCount <= (SCORE * (SCORE - 1)) >> 1,
        "Comparison count must be in range "
            + (SCORE - 1)
            + " and "
            + ((SCORE * (SCORE - 1)) >> 1)
            + ".");
  }

  void testReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new BubbleSort();
    high.sort(sorter);
    assertEquals(
        sorter.getSwapCount(),
        sorter.getComparisonCount(),
        "Comparison count must be same as swap count in reverse ordered array.");
  }

  void testStreamSorted() {
    IArray high = new HighArray();
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new BubbleSort();
    IArray sorted = high.sort(sorter);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSwapCount() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new BubbleSort();
    high.sort(sorter);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  void testTimeComplexity() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new BubbleSort();
    high.sort(sorter);
    assertEquals(
        SCORE - 1, sorter.getTimeComplexity(), "Time complexity must be " + (SCORE - 1) + ".");
  }

  void testToStringClass() {
    AbstractSort sorter = new BubbleSort();
    String className = BubbleSort.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
  }

  void testPreReset() {
    ISort sorter = new BubbleSort();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
  }

  void testSortEmptyArray() {
    BubbleSortComplex sorter = new BubbleSortComplex();
    sorter.sortEmptyArray();
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getOuterLoopCount(), "Outer loop never entered.");
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
  }

  void testSortSingleElementArray() {
    BubbleSortComplex sorter = new BubbleSortComplex();
    sorter.sortSingleElementArray();
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getOuterLoopCount(), "Outer loop never entered.");
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
  }

  void testSortTwoElementArraySorted() {
    BubbleSortComplex sorter = new BubbleSortComplex();
    sorter.sortTwoLengthArraySorted();
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getOuterLoopCount(), "Outer loop count incremented by one.");
    assertEquals(1, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
  }

  void testSortTwoElementArrayUnsorted() {
    BubbleSortComplex sorter = new BubbleSortComplex();
    sorter.sortTwoLengthArrayUnsorted();
    assertEquals(1, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getOuterLoopCount(), "Outer loop count incremented by one.");
    assertEquals(1, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
  }

  void testSortNegativeLengthArray() {
    BubbleSortComplex sorter = new BubbleSortComplex();
    assertThrows(
        IllegalArgumentException.class,
        () -> sorter.sortNegativeLengthArray(),
        "Array length cannot be negative.");
  }

  static class BubbleSortComplex extends BubbleSort {
    void sortEmptyArray() {
      long[] a = {};
      sort(a, 0);
    }

    void sortSingleElementArray() {
      long[] a = {1};
      sort(a, 1);
    }

    void sortNegativeLengthArray() {
      long[] a = {1};
      sort(a, -1);
    }

    void sortTwoLengthArraySorted() {
      long[] a = {1, 2};
      sort(a, 2);
    }

    void sortTwoLengthArrayUnsorted() {
      long[] a = {2, 1};
      sort(a, 2);
    }

    int getOuterLoopCount() {
      return outerLoopCount;
    }
  }
}
