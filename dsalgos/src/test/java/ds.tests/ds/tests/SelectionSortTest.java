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
import ds.SelectionSort;
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
class SelectionSortTest implements SortProvider {

  private static final String SORTED_AND_EQUAL = "Elements must be sorted and equal.";

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, SORTED_AND_EQUAL);
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, SORTED_AND_EQUAL);
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, SORTED_AND_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
  }

  void testSortSmallData(IArray arr) {
    ISort sorter = new SelectionSort();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, SORTED_AND_EQUAL);
    assertEquals(5, sorter.getSwapCount(), "Swap count will be five.");
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  void testReset() {
    IArray high = new HighArray(SCORE);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(
        (SCORE * (SCORE - 1)) >> 1,
        sorter.getComparisonCount(),
        "Comparison count must be " + ((SCORE * (SCORE - 1)) >> 1) + ".");
    assertEquals(0, sorter.getSwapCount(), "Swap count must be 0.");
  }

  void testStreamUnSorted() {
    IArray high = new HighArray(SCORE);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .parallel()
        .unordered()
        .forEach(
            i -> {
              high.insert(i);
              ord.syncInsert(i);
            });
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, SORTED_AND_EQUAL);
  }

  void testStreamSorted() {
    IArray high = new HighArray(SCORE);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, SCORE)
        .forEach(
            i -> {
              high.insert(i);
              ord.syncInsert(i);
            });
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, SORTED_AND_EQUAL);
  }

  void testReverseSorted() {
    IArray high = new HighArray(SCORE);
    revRange(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] a = sorted.get();
    assertEquals(
        (SCORE * (SCORE - 1)) >> 1,
        sorter.getTimeComplexity(),
        "Time complexity must be same as n * (n - 1) / 2.");
    assertTrue(
        a[0] == 1 && a[SCORE - 1] == SCORE, "First element must be 1 and last element must be 20.");
  }

  void testSortedTimeComplexity() {
    IArray high = new HighArray(SCORE);
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertEquals(
        (SCORE * (SCORE - 1)) >> 1,
        sorter.getTimeComplexity(),
        "Time complexity must be same as n(n-1)/2.");
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  void testComparisonCountUnsorted() {
    IArray high = new HighArray(SCORE);
    LongStream.rangeClosed(1, SCORE).unordered().parallel().forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] a = sorted.get();
    assertEquals(
        (SCORE * (SCORE - 1)) >> 1,
        sorter.getComparisonCount(),
        "Comparison count must be " + ((SCORE * (SCORE - 1)) >> 1) + " .");
    assertTrue(
        a[0] == 1 && a[SCORE - 1] == SCORE, "First element must be 1 and last element must be 20.");
  }

  void testSwapCountUnsorted() {
    IArray high = new HighArray(SCORE);
    LongStream.rangeClosed(1, SCORE).unordered().parallel().forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertTrue(sorter.getSwapCount() < SCORE, "Swap count must be less than array length.");
  }

  void testSwapCountSorted() {
    IArray high = new HighArray(SCORE);
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  void testToStringClass() {
    AbstractSort sorter = new SelectionSort();
    String className = SelectionSort.class.getName();
    assertTrue(
        sorter.toString().startsWith(className),
        () -> "ToString must start with " + className + ".");
  }

  void testPreReset() {
    ISort sorter = new SelectionSort();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
  }

  void testEmptyArraySort() {
    SelectionSub sorter = new SelectionSub();
    sorter.sortEmptyArray();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testSingleElementArraySort() {
    SelectionSub sorter = new SelectionSub();
    sorter.sortSingleElementArray();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testTwoElementArraySorted() {
    SelectionSub sorter = new SelectionSub();
    sorter.sortTwoElementArraySorted();
    assertEquals(1, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testTwoElementArrayUnsorted() {
    SelectionSub sorter = new SelectionSub();
    sorter.sortTwoElementArrayUnsorted();
    assertEquals(1, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(1, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  static class SelectionSub extends SelectionSort {

    void sortEmptyArray() {
      long[] a = {};
      sort(a, 0);
    }

    void sortSingleElementArray() {
      long[] a = {0};
      sort(a, 0);
    }

    void sortTwoElementArraySorted() {
      long[] a = {1, 2};
      sort(a, 2);
    }

    void sortTwoElementArrayUnsorted() {
      long[] a = {2, 1};
      sort(a, 2);
    }
  }
}
