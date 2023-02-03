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
import ds.RandomUtils;
import ds.ShellSortByThree;
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
class ShellSortByThreeTest implements SortProvider {

  private static final String COPY_COUNT_ZERO = "Copy count must be zero.";

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    ISort sorter = new ShellSortByThree();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortRandom() {
    HighArray arr = new HighArray(MYRIAD);
    OrdArray ord = new OrdArray(MYRIAD);
    try (LongStream stream = RandomUtils.longStream().limit(MYRIAD)) {
      stream.forEach(
          i -> {
            arr.insert(i);
            ord.insert(i);
          });
    }
    ISort sorter = new ShellSortByThree();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    long[] a = ord.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new ShellSortByThree();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortSmallData(IArray arr) {
    ISort sorter = new ShellSortByThree();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, "Arrays must be sorted and equal.");
    assertEquals(10, sorter.getCopyCount(), "Copy count will be ten.");
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    ISort sorter = new ShellSortByThree();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
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
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(42, sorter.getComparisonCount(), "Comparison count must be 42.");
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
    ISort sorter = new ShellSortByThree();
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
    ISort sorter = new ShellSortByThree();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testCopyCount() {
    IArray high = new HighArray();
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    assertEquals(0, sorter.getCopyCount(), COPY_COUNT_ZERO);
  }

  void testTimeComplexity() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    assertEquals(42, sorter.getTimeComplexity(), "Time complexity must be 42.");
  }

  void testTimeComplexityReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    assertEquals(30, sorter.getTimeComplexity(), "Time complexity must be thirty.");
  }

  void testReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    assertEquals(30, sorter.getCopyCount(), "Copy count must be 30 in reverse ordered array.");
  }

  void testSingleElementArray() {
    IArray high = new HighArray(1);
    high.insert(1L);
    ISort sorter = new ShellSortByThree();
    sorter.sort(high);
    assertTrue(isSorted(high), "Array is sorted.");
    assertEquals(0, sorter.getCopyCount(), COPY_COUNT_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), "Time complexity must be zero.");
    assertEquals(0, sorter.getComparisonCount(), "Comparison count must be zero.");
  }

  void testEmptyArray() {
    long[] a = {};
    ShellSortByThreeSub sorter = new ShellSortByThreeSub();
    sorter.sortArray(a, 0);
    assertTrue(isSorted(a), "Array is sorted.");
    assertEquals(0, sorter.getCopyCount(), COPY_COUNT_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), "Time complexity must be zero.");
    assertEquals(0, sorter.getComparisonCount(), "Comparison count must be zero.");
  }

  void testToStringClass() {
    AbstractSort sorter = new ShellSortByThree();
    String className = ShellSortByThree.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
  }

  void testPreReset() {
    ISort sorter = new ShellSortByThree();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
  }

  void testEmptyArraySort() {
    ShellSortByThreeSub sorter = new ShellSortByThreeSub();
    sorter.sortEmptyArray();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testSingleElementArraySort() {
    ShellSortByThreeSub sorter = new ShellSortByThreeSub();
    sorter.sortSingleElementArray();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testSortTwoElementArraySorted() {
    ShellSortByThreeSub sorter = new ShellSortByThreeSub();
    sorter.sortTwoElementArraySorted();
    assertEquals(1, sorter.getComparisonCount(), "Comparison count must be 2.");
    assertEquals(0, sorter.getCopyCount(), COPY_COUNT_ZERO);
    assertEquals(1, sorter.getTimeComplexity(), "Time complexity must be one.");
  }

  void testSortTwoElementArrayUnsorted() {
    ShellSortByThreeSub sorter = new ShellSortByThreeSub();
    sorter.sortTwoElementArrayUnsorted();
    assertEquals(1, sorter.getComparisonCount(), "Comparison count must be 2.");
    assertEquals(1, sorter.getCopyCount(), "Copy count must be one.");
    assertEquals(1, sorter.getTimeComplexity(), "Time complexity must be one.");
  }

  static final class ShellSortByThreeSub extends ShellSortByThree {
    void sortArray(long[] a, int length) {
      super.sort(a, length);
    }

    void sortEmptyArray() {
      long[] a = {};
      sort(a, 0);
    }

    void sortSingleElementArray() {
      long[] a = {0};
      sort(a, 0);
    }

    void sortTwoElementArraySorted() {
      long[] a = {1L, 2L};
      sort(a, 2);
    }

    void sortTwoElementArrayUnsorted() {
      long[] a = {2L, 1L};
      sort(a, 2);
    }
  }
}
