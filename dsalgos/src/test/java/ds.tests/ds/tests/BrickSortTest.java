package ds.tests;

import static ds.ArrayUtils.*;
import static ds.MathUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.BrickSort;
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
class BrickSortTest implements SortProvider {

  private static final String COUNT_EQUALS =
      "Comparison count must be same as swap count in reverse ordered array.";
  private static final String ARRAYS_SORTED = "Arrays must be sorted.";
  private static final String MUST_BE_EQUAL = "Must be equal";

  void testSort(IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    BrickSort sorter = new BrickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortDuplicates(IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    BrickSort sorter = new BrickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortAllSame(IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    BrickSort sorter = new BrickSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, ELEMENTS_SORTED_EQUAL);
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSortSmallData(IArray arr) {
    BrickSort sorter = new BrickSort();
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
    BrickSort sorter = new BrickSort();
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
    BrickSort sorter = new BrickSort();
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
    BrickSort sorter = new BrickSort();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(SCORE - 1, sorter.getComparisonCount(), "Comparison count must be n -1.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testComparisonCountSorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSort sorter = new BrickSort();
    sorter.sort(high);
    int compCount = sorter.getComparisonCount();
    assertEquals(SCORE - 1, compCount, "Comparison count must be " + (SCORE - 1) + ".");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testComparisonCountUnsorted() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).parallel().unordered().forEach(i -> high.insert(i));
    BrickSort sorter = new BrickSort();
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
    BrickSort sorter = new BrickSort();
    IArray sorted = sorter.sort(high);
    assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
    assertTrue(isSorted(sorted), ARRAYS_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testReverseSortedOdd() {
    IArray high = new HighArray();
    revRange(1, SCORE + 1).forEach(i -> high.insert(i));
    BrickSort sorter = new BrickSort();
    IArray sorted = sorter.sort(high);
    assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
    assertTrue(isSorted(sorted), ARRAYS_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testSwapCount() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSort sorter = new BrickSort();
    sorter.sort(high);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testTimeComplexity() {
    IArray high = new HighArray();
    LongStream.rangeClosed(1, SCORE).forEach(i -> high.insert(i));
    BrickSort sorter = new BrickSort();
    sorter.sort(high);
    assertEquals(
        SCORE - 1, sorter.getTimeComplexity(), "Time complexity must be " + (SCORE - 1) + ".");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testToStringClass() {
    AbstractSort sorter = new BrickSort();
    String className = BrickSort.class.getName();
    assertTrue(
        sorter.toString().startsWith(className),
        () -> "ToString must start with " + className + ".");
  }

  void testPreReset() {
    BrickSort sorter = new BrickSort();
    assertEquals(0, sorter.getComparisonCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getSwapCount(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getTimeComplexity(), INITIAL_VALUE_ZERO);
    assertEquals(0, sorter.getCopyCount(), INITIAL_VALUE_ZERO);
    assertFalse(sorter.isSorted(), "Sorted must not be set.");
  }

  void testZeroTimeComplexity() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortZeroLengthArray();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be zero.");
    assertTrue(bsc.isSorted(), SORTED_MUST_BE_SET);
  }

  void testOneTimeComplexity() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortOneLengthArray();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be zero.");
    assertTrue(bsc.isSorted(), SORTED_MUST_BE_SET);
  }

  void testResetSubClass() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.resetInternals();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertFalse(bsc.isSorted(), "sorted must be reset.");
  }

  void testResetAfterSort() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.resetInternalsAfterSort();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertFalse(bsc.isSorted(), "sorted must be reset.");
  }

  void testInternalsAfterSort() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortAndSetInternals();
    assertNotEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertNotEquals(0, bsc.getComparisonCount(), "Comparison count must be reset.");
    assertNotEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertTrue(bsc.isSorted(), "sorted must be reset.");
  }

  void testInnerLoopAfterOddSort() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortOdd();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testInnerLoopAfterEvenSort() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortEven();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testStateAfterReset() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortOdd();
    final int oldInnerLoopCount = bsc.getInnerLoopCount();
    bsc.sortEven();
    final int innerLoopCount = bsc.getInnerLoopCount();
    assertNotEquals(oldInnerLoopCount, innerLoopCount, "Inner loop count must not be same.");
  }

  void testEmptyArray() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortEmptyArray();
    assertTrue(bsc.isSorted(), SORTED);
  }

  void testSingleElementArray() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortSingleElementArray();
    assertTrue(bsc.isSorted(), SORTED);
  }

  class MyriadTests {

    void testSortRandom() {
      HighArray arr = new HighArray(MYRIAD);
      try (LongStream stream = RandomUtils.longStream().limit(MYRIAD)) {
        stream.forEach(i -> arr.insert(i));
      }
      BrickSort sorter = new BrickSort();
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
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      long[] extentSorted = sorted.getExtentArray();
      long[] extent = ord.getExtentArray();
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      long[] extentSorted = sorted.getExtentArray();
      long[] extent = ord.getExtentArray();
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertArrayEquals(extentSorted, extent, "Elements must be sorted and equal.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testComparisonCountSorted() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      int compCount = sorter.getComparisonCount();
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(MYRIAD - 1, compCount, "Comparison count must be " + (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testComparisonCountUnsorted() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).parallel().unordered().forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      int compCount = sorter.getComparisonCount();
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertTrue(
          MYRIAD - 1 <= compCount && compCount <= (MYRIAD * MYRIAD - 1),
          "Comparison count must be in range " + (MYRIAD - 1) + " and " + MYRIAD * (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSorted() {
      IArray high = new HighArray(MYRIAD);
      revRange(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
      assertTrue(isSorted(sorted), "Array must be sorted");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSortedOdd() {
      IArray high = new HighArray(MYRIAD + 1);
      revRange(1, MYRIAD + 1).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals(
          (oddTaskCount + evenTaskCount) * (outerLoopCount - 1) + oddTaskCount,
          innerLoopCount,
          MUST_BE_EQUAL);
      assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
      assertTrue(isSorted(sorted), "Array must be sorted");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testSwapCount() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testTimeComplexity() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddTaskCount = computeOddPairCount(length);
      final int evenTaskCount = computeEvenPairCount(length);
      assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(MYRIAD - 1, sorter.getTimeComplexity(), "Time complexity must be twenty.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }
  }

  static class BrickSortComplex extends BrickSort {
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
