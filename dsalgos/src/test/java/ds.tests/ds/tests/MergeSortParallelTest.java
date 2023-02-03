package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.MergeSortParallel;
import ds.OrdArray;
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
class MergeSortParallelTest implements SortProvider {

  void testSortRandom() {
    Random random = new Random();
    HighArray high = new HighArray(MYRIAD);
    try (LongStream stream = random.longs(MYRIAD)) {
      stream.forEach(i -> high.insert(i));
    }
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(high);
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testSortSmallData(IArray arr) {
    ISort sorter = new MergeSortParallel();
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
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getCopyCount(), "Swap count must be zero.");
  }

  void testSortAllSameBigData() {
    HighArray arr = new HighArray(MYRIAD);
    try (LongStream stream =
        LongStream.iterate(
                HUNDRED,
                val -> {
                  return val;
                })
            .limit(MYRIAD)) {
      stream.forEach(i -> arr.insert(i));
    }
    long[] a = arr.getExtentArray();
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getCopyCount(), "Copy count must be zero.");
  }

  void testReset() {
    IArray high = new HighArray(MYRIAD);
    IArray ord = new OrdArray(MYRIAD);
    LongStream.rangeClosed(1, MYRIAD)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new MergeSortParallel();
    sorter.sort(high);
    sorter.sort(ord);
    int comparisonCount = sorter.getComparisonCount();
    assertEquals(MYRIAD - 1, comparisonCount, "Comparison count must be n-1.");
  }

  void testStreamUnSorted() {
    IArray high = new HighArray(MYRIAD);
    IArray ord = new OrdArray(MYRIAD);
    LongStream.rangeClosed(1, MYRIAD)
        .unordered()
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
  }

  void testStreamSorted() {
    IArray high = new HighArray(MYRIAD);
    IArray ord = new OrdArray(MYRIAD);
    LongStream.rangeClosed(1, MYRIAD)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new MergeSortParallel();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getCopyCount(), "Swap count must be zero.");
  }

  void testCopyCount() {
    IArray high = new HighArray(MYRIAD);
    IArray ord = new OrdArray(MYRIAD);
    LongStream.rangeClosed(1, MYRIAD)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new MergeSortParallel();
    sorter.sort(high);
    assertEquals(0, sorter.getCopyCount(), "Swap count must be zero.");
  }

  void testTimeComplexity() {
    IArray high = new HighArray(MYRIAD);
    LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
    ISort sorter = new MergeSortParallel();
    sorter.sort(high);
    assertEquals(0, sorter.getTimeComplexity(), "Time complexity must be zero.");
  }

  void testTimeComplexityReverseSorted() {
    IArray high = new HighArray(MYRIAD);
    revRange(1, MYRIAD).forEach(i -> high.insert(i));
    ISort sorter = new MergeSortParallel();
    sorter.sort(high);
    assertNotEquals(0, sorter.getTimeComplexity(), "Time complexity must not be zero.");
  }

  void testReverseSorted() {
    IArray high = new HighArray(MYRIAD);
    revRange(1, MYRIAD).forEach(i -> high.insert(i));
    ISort sorter = new MergeSortParallel();
    sorter.sort(high);
    assertNotEquals(0, sorter.getCopyCount(), "Copy count must not be zero.");
  }

  void testToStringClass() {
    AbstractSort sorter = new MergeSortParallel();
    String className = MergeSortParallel.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
  }

  void testPreReset() {
    ISort sorter = new MergeSortParallel();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
  }

  void testSortEmptyArray() {
    long[] a = {};
    MergeSortSimple mss = new MergeSortSimple();
    mss.sortArray(a, 0);
    assertTrue(isSorted(a, 0), "Empty array is sorted!");
  }

  void testSortSingleElementArray() {
    long[] a = {1};
    MergeSortSimple mss = new MergeSortSimple();
    mss.sortArray(a, 1);
    assertTrue(isSorted(a, 1), "Single element array is sorted!");
  }

  void testNegativeLengthException() {
    long[] a = {1};
    MergeSortSimple mss = new MergeSortSimple();
    assertThrows(
        IllegalArgumentException.class,
        () -> mss.sortArray(a, -1),
        "Negative length will throw exception.");
  }

  static class MergeSortSimple extends MergeSortParallel {
    public void sortArray(long[] a, int length) {
      sort(a, length);
    }
  }
}
