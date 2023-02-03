package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.OrdArray;
import ds.QuickSort;
import java.util.Random;
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
class QuickSortTest implements SortProvider {

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortRandom() {
    HighArray high = new HighArray(MYRIAD);
    Random random = new Random();
    try (LongStream stream = random.longs().limit(MYRIAD)) {
      stream.forEach(i -> high.insert(i));
    }
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(high);
    assertTrue(isSorted(sorted), "Elements are sorted.");
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortBigDuplicates() {
    HighArray high = new HighArray(MYRIAD);
    for (int i = 0; i < MYRIAD; i++) high.insert(i & 1023);
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(high);
    assertTrue(isSorted(sorted), "Elements are sorted.");
  }

  void testSortSmallData(IArray arr) {
    ISort sorter = new QuickSort();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, "Arrays must be sorted and equal.");
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
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
    ISort sorter = new QuickSort();
    sorter.sort(high);
    sorter.sort(ord);
    int comparisonCount = sorter.getComparisonCount();
    assertTrue(
        SCORE * Math.log(SCORE) < comparisonCount && comparisonCount <= SCORE * (SCORE - 1),
        "Comparison count must be in range nlogn to n(n-1)");
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
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
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
    ISort sorter = new QuickSort();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  void testSwapCount() {
    IArray high = new HighArray();
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new QuickSort();
    sorter.sort(high);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  void testTimeComplexity() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new QuickSort();
    sorter.sort(high);
    assertNotEquals(0, sorter.getTimeComplexity(), "Time complexity must not be zero.");
  }

  void testTimeComplexityReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new QuickSort();
    sorter.sort(high);
    assertNotEquals(0, sorter.getTimeComplexity(), "Time complexity must not be zero.");
  }

  void testReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new QuickSort();
    sorter.sort(high);
    assertNotEquals(0, sorter.getSwapCount(), "Swap count must not be zero.");
  }

  void testSingleElementArray() {
    IArray high = new HighArray(1);
    high.insert(1L);
    ISort sorter = new QuickSort();
    sorter.sort(high);
    assertTrue(isSorted(high), "Array is sorted.");
    assertEquals(0, sorter.getCopyCount(), "Copy count must be zero.");
    assertEquals(0, sorter.getTimeComplexity(), "Time complexity must be zero.");
    assertEquals(0, sorter.getComparisonCount(), "Comparison count must be zero.");
  }

  void testEmptyArray() {
    long[] a = {};
    QuickSortSub sorter = new QuickSortSub();
    sorter.sortArray(a, 0);
    assertTrue(isSorted(a), "Array is sorted.");
    assertEquals(0, sorter.getCopyCount(), "Copy count must be zero.");
    assertEquals(0, sorter.getTimeComplexity(), "Time complexity must be zero.");
    assertEquals(0, sorter.getComparisonCount(), "Comparison count must be zero.");
  }

  void testIllegalArgumentException() {
    long[] a = {};
    QuickSortSub sorter = new QuickSortSub();
    assertThrows(
        IllegalArgumentException.class,
        () -> sorter.sortArray(a, -1),
        "IllegalArgumentException expected.");
  }

  void testToStringClass() {
    AbstractSort sorter = new QuickSort();
    String className = QuickSort.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
  }

  void testPreReset() {
    ISort sorter = new QuickSort();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
  }

  static final class QuickSortSub extends QuickSort {
    public void sortArray(long[] a, int length) {
      super.sort(a, length);
    }
  }
}
