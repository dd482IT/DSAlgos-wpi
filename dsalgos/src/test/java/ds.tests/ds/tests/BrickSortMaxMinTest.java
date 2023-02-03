package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.BrickSortMaxMin;
import ds.HighArray;
import ds.IArray;
import ds.OrdArray;
import ds.RandomUtils;
import java.util.Random;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("PMD.LawOfDemeter")
class BrickSortMaxMinTest implements SortProvider {

  private static final String ARRAYS_SORTED = "Arrays must be sorted.";

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortSmallData(IArray arr) {
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, "Arrays must be sorted and equal.");
    assertEquals(13, sorter.getSwapCount(), "Swap count will be thirteen.");
    assertTrue(isSorted(sorted), ARRAYS_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
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
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(high);
    IArray sortedOrd = sorter.sort(ord);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = sortedOrd.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
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
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(high);
    IArray sortedOrd = sorter.sort(ord);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = sortedOrd.getExtentArray();
    assertArrayEquals(extentSorted, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
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
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(SCORE - 1, sorter.getComparisonCount(), "Comparison count must be n -1.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testComparisonCountSorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    sorter.sort(high);
    int compCount = sorter.getComparisonCount();
    assertEquals(SCORE - 1, compCount, "Comparison count must be " + (SCORE - 1) + ".");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testComparisonCountUnsorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).parallel().unordered().forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    sorter.sort(high);
    int compCount = sorter.getComparisonCount();
    assertTrue(
        SCORE - 1 <= compCount && compCount <= SCORE * (SCORE - 1),
        "Comparison count must be in range " + (SCORE - 1) + " and " + (SCORE * (SCORE - 1)) + ".");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testReverseSorted() {
    IArray high = new HighArray();
    revRange(1, SCORE).forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(high);
    assertEquals(
        sorter.getSwapCount(),
        sorter.getComparisonCount(),
        "Comparison count must be same as swap count in reverse ordered array.");
    assertTrue(isSorted(sorted), ARRAYS_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testReverseSortedOdd() {
    IArray high = new HighArray();
    revRange(1, SCORE + 1).forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    IArray sorted = sorter.sort(high);
    assertTrue(isSorted(sorted), ARRAYS_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSwapCount() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    sorter.sort(high);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testTimeComplexity() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    sorter.sort(high);
    assertEquals(
        SCORE - 1, sorter.getTimeComplexity(), "Time complexity must be " + (SCORE - 1) + ".");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testToStringClass() {
    AbstractSort sorter = new BrickSortMaxMin();
    String className = BrickSortMaxMin.class.getName();
    assertTrue(
        sorter.toString().startsWith(className),
        () -> "ToString must start with " + className + ".");
  }

  void testPreReset() {
    BrickSortMaxMin sorter = new BrickSortMaxMin();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
    assertFalse(sorter.isSorted(), "Sorted must not be set.");
  }

  void testZeroTimeComplexity() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortZeroLengthArray();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be zero.");
    assertTrue(bsc.isSorted(), SORTED_MUST_BE_SET);
  }

  void testOneTimeComplexity() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortOneLengthArray();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be zero.");
    assertTrue(bsc.isSorted(), SORTED_MUST_BE_SET);
  }

  void testResetSubClass() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.resetInternals();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertFalse(bsc.isSorted(), "sorted must be reset.");
  }

  void testResetAfterSort() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.resetInternalsAfterSort();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertFalse(bsc.isSorted(), "sorted must be reset.");
  }

  void testInternalsAfterSort() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortAndSetInternals();
    assertNotEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertNotEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertNotEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertTrue(bsc.isSorted(), "sorted must be reset.");
  }

  void testInnerLoopAfterOddSort() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortOdd();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testInnerLoopAfterEvenSort() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortEven();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testStateAfterReset() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortOdd();
    final int oldInnerLoopCount = bsc.getInnerLoopCount();
    bsc.sortEven();
    final int innerLoopCount = bsc.getInnerLoopCount();
    assertEquals(oldInnerLoopCount, innerLoopCount, "Inner loop count must be same.");
  }

  void testEmptyArray() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortEmptyArray();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testSingleElementArray() {
    BrickSortMaxMinComplex bsc = new BrickSortMaxMinComplex();
    bsc.sortSingleElementArray();
    assertTrue(bsc.isSorted(), SORTED);
  }

  class MyriadTests {

    void testSortRandom() {
      HighArray arr = new HighArray(MYRIAD);
      try (LongStream stream = RandomUtils.longStream().limit(MYRIAD)) {
        stream.forEach(i -> arr.insert(i));
      }
      BrickSortMaxMin sorter = new BrickSortMaxMin();
      IArray sorted = sorter.sort(arr);
      assertTrue(isSorted(sorted), ARRAYS_SORTED);
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
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      IArray sorted = sorter.sort(high);
      long[] extentSorted = sorted.getExtentArray();
      long[] extent = ord.getExtentArray();
      assertArrayEquals(extentSorted, extent, "Elements must be sorted and equal.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
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
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      IArray sorted = sorter.sort(high);
      long[] extentSorted = sorted.getExtentArray();
      long[] extent = ord.getExtentArray();
      assertArrayEquals(extentSorted, extent, "Elements must be sorted and equal.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testComparisonCountSorted() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      sorter.sort(high);
      int compCount = sorter.getComparisonCount();
      assertEquals(MYRIAD - 1, compCount, "Comparison count must be " + (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testComparisonCountUnsorted() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).parallel().unordered().forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      sorter.sort(high);
      int compCount = sorter.getComparisonCount();
      assertTrue(
          MYRIAD - 1 <= compCount && compCount <= (MYRIAD * MYRIAD - 1),
          "Comparison count must be in range " + (MYRIAD - 1) + " and " + MYRIAD * (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSorted() {
      IArray high = new HighArray(MYRIAD);
      revRange(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      IArray sorted = sorter.sort(high);
      assertEquals(
          sorter.getSwapCount(),
          sorter.getComparisonCount(),
          "Comparison count must be same as swap count in reverse ordered array.");
      assertTrue(isSorted(sorted), "Array must be sorted");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSortedOdd() {
      IArray high = new HighArray(MYRIAD + 1);
      revRange(1, MYRIAD + 1).forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      IArray sorted = sorter.sort(high);
      assertTrue(isSorted(sorted), "Array must be sorted");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testSwapCount() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      sorter.sort(high);
      assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testTimeComplexity() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortMaxMinComplex sorter = new BrickSortMaxMinComplex();
      sorter.sort(high);
      assertEquals(MYRIAD - 1, sorter.getTimeComplexity(), "Time complexity must be twenty.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }
  }

  static class BrickSortMaxMinComplex extends BrickSortMaxMin {
    Random random = new Random();

    void sortZeroLengthArray() {
      long[] a = random.longs().limit(TEN).toArray();
      sort(a, 0);
    }

    void sortOneLengthArray() {
      long[] a = random.longs().limit(TEN).toArray();
      sort(a, 1);
    }

    void sortNMinusOneLengthArray() {
      long[] a = random.longs().limit(TEN).toArray();
      sort(a, TEN - 1);
    }

    void sortEmptyArray() {
      long[] a = {};
      sort(a, 0);
    }

    void sortSingleElementArray() {
      long[] a = {TEN};
      sort(a, 1);
    }

    void resetInternals() {
      reset();
    }

    void sortAndSetInternals() {
      long[] a = random.longs().limit(TEN).toArray();
      sort(a, a.length);
    }

    void resetInternalsAfterSort() {
      sortAndSetInternals();
      reset();
    }

    void sortOdd() {
      long[] a = random.longs().limit(TEN - 1).toArray();
      sort(a, a.length);
    }

    void sortEven() {
      long[] a = random.longs().limit(TEN).toArray();
      sort(a, a.length);
    }

    int getInnerLoopCount() {
      return innerLoopCount;
    }

    int getOuterLoopCount() {
      return outerLoopCount;
    }
  }
}
