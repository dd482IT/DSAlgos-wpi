package ds.tests;

import static ds.ArrayUtils.*;
import static ds.MathUtils.*;
import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.BrickSortParallel;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.OrdArray;
import ds.RandomUtils;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "checkstyle:classfanoutcomplexity"})
class BrickSortParallelTest implements SortProvider {

  private static final String MUST_BE_EQUAL = "Must be equal";
  private static final String ZERO_TASKS_EXPECTED = "Zero tasks expected.";
  private static final String ONE_TASK_EXPECTED = "One task expected.";
  private static final String HALF_TASKS_EXPECTED = "Half tasks expected.";
  private static final String ILLEGAL_LENGTH_EXPECTED = "Illegal length expected.";

  void testReverseSortedOdd255() {
    IArray high = new HighArray(255);
    revRange(1, 255).forEach(i -> high.insert(i));
    BrickSortComplex sorter = new BrickSortComplex();
    IArray sorted = sorter.sort(high);
    final int innerLoopCount = sorter.getInnerLoopCount();
    final int outerLoopCount = sorter.getOuterLoopCount();
    int length = high.count();
    final int oddCount = computeOddPairCount(length);
    final int evenCount = computeEvenPairCount(length);
    assertEquals(
        (oddCount + evenCount) * (outerLoopCount - 1) + oddCount, innerLoopCount, MUST_BE_EQUAL);
    assertEquals(
        sorter.getSwapCount(),
        sorter.getComparisonCount(),
        "Comparison count must be same as swap count in reverse ordered array.");
    assertTrue(isSorted(sorted), "Array must be sorted");
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
  }

  void testToStringClass() {
    AbstractSort sorter = new BrickSortParallel();
    String className = BrickSortParallel.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
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

  void testNMinusOneTimeComplexity() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortNMinusOneLengthArray();
    assertEquals(3, bsc.getTimeComplexity(), "Time Complexity must be three.");
    assertTrue(bsc.isSorted(), SORTED_MUST_BE_SET);
  }

  void testReset() {
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

  void testStateAfterReset() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.sortOdd();
    final int oldInnerLoopCount = bsc.getInnerLoopCount();
    final int oldOuterLoopCount = bsc.getOuterLoopCount();
    bsc.sortEven();
    final int innerLoopCount = bsc.getInnerLoopCount();
    final int outerLoopCount = bsc.getOuterLoopCount();
    assertNotEquals(oldInnerLoopCount, innerLoopCount, "Inner loop count must not be same.");
    assertEquals(oldOuterLoopCount, outerLoopCount, "Outer loop count must be same.");
  }

  class MyriadTests {
    void testSortRandom() {
      HighArray arr = new HighArray(MYRIAD);
      try (LongStream stream = RandomUtils.longStream().limit(MYRIAD)) {
        stream.forEach(i -> arr.insert(i));
      }
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(arr);
      assertTrue(isSorted(sorted), "Array must be sorted.");
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      final int innerLoopCount = sorter.getInnerLoopCount();
      final int outerLoopCount = sorter.getOuterLoopCount();
      int length = high.count();
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals(
          (oddCount + evenCount) * (outerLoopCount - 1) + oddCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(
          sorter.getSwapCount(),
          sorter.getComparisonCount(),
          "Comparison count must be same as swap count in reverse ordered array.");
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
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
      final int oddCount = computeOddPairCount(length);
      final int evenCount = computeEvenPairCount(length);
      assertEquals((oddCount + evenCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
      assertEquals(MYRIAD - 1, sorter.getTimeComplexity(), "Time complexity must be twenty.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSortedOddException() {
      IArray high = new HighArray(MYRIAD + 1);
      revRange(1, MYRIAD + 1).forEach(i -> high.insert(i));
      ISort sorter = new BrickSortExceptionable();
      assertThrows(
          CompletionException.class, () -> sorter.sort(high), "CompletionException expected.");
    }

    void testReverseSortedOddInterruption() throws InterruptedException, ExecutionException {
      IArray high = new HighArray(MYRIAD + 1);
      revRange(1, MYRIAD + 1).forEach(i -> high.insert(i));
      BrickSortInterruptible sorter = new BrickSortInterruptible();
      assertThrows(
          CompletionException.class, () -> sorter.sort(high), "CompletionException expected.");
    }
  }

  class CornerCasesTest {
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
  }

  class ComputeCountTest {

    void testZeroLength() {
      assertEquals(0, computeOddPairCount(0), ZERO_TASKS_EXPECTED);
      assertEquals(0, computeEvenPairCount(0), ZERO_TASKS_EXPECTED);
    }

    void testMinusOneLength() {
      assertThrows(
          IllegalArgumentException.class, () -> computeOddPairCount(-1), ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class, () -> computeEvenPairCount(-1), ILLEGAL_LENGTH_EXPECTED);
    }

    void testMinusTwoLength() {
      assertThrows(
          IllegalArgumentException.class, () -> computeOddPairCount(-2), ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class, () -> computeEvenPairCount(-2), ILLEGAL_LENGTH_EXPECTED);
    }

    void testMinValueLength() {
      assertThrows(
          IllegalArgumentException.class,
          () -> computeOddPairCount(Integer.MIN_VALUE),
          ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class,
          () -> computeEvenPairCount(Integer.MIN_VALUE),
          ILLEGAL_LENGTH_EXPECTED);
    }

    void testOneValueLength() {
      assertEquals(0, computeOddPairCount(1), ZERO_TASKS_EXPECTED);
      assertEquals(0, computeEvenPairCount(1), ZERO_TASKS_EXPECTED);
    }

    void testTwoValueLength() {
      assertEquals(0, computeOddPairCount(2), ZERO_TASKS_EXPECTED);
      assertEquals(1, computeEvenPairCount(2), ONE_TASK_EXPECTED);
    }

    void testThreeValueLength() {
      assertEquals(1, computeOddPairCount(3), ONE_TASK_EXPECTED);
      assertEquals(1, computeEvenPairCount(3), ONE_TASK_EXPECTED);
    }

    void testFourValueLength() {
      assertEquals(1, computeOddPairCount(4), ONE_TASK_EXPECTED);
      assertEquals(2, computeEvenPairCount(4), "Two tasks expected");
    }

    void testMaxValueLength() {
      assertEquals(
          Integer.MAX_VALUE / 2, computeOddPairCount(Integer.MAX_VALUE), HALF_TASKS_EXPECTED);
      assertEquals(
          Integer.MAX_VALUE / 2, computeEvenPairCount(Integer.MAX_VALUE), HALF_TASKS_EXPECTED);
    }

    void testMaxValueLengthEven() {
      assertEquals(
          (Integer.MAX_VALUE - 2) / 2,
          computeOddPairCount(Integer.MAX_VALUE - 1),
          HALF_TASKS_EXPECTED);
      assertEquals(
          (Integer.MAX_VALUE - 1) / 2,
          computeEvenPairCount(Integer.MAX_VALUE - 1),
          HALF_TASKS_EXPECTED);
    }
  }

  static class BrickSortExceptionable extends BrickSortParallel {
    @Override
    protected void bubble(long[] ignored, int ignoredInt) {
      throw new IllegalStateException("Error in " + BrickSortExceptionable.class + ".bubble");
    }
  }

  static class BrickSortInterruptible extends BrickSortParallel {
    @Override
    protected void sortInterruptibly(
        long[] ignoredArr, int ignoredLength, ExecutorService ignoredService)
        throws InterruptedException, ExecutionException {
      throw new InterruptedException(
          "Error in " + BrickSortInterruptible.class + ".sortInterruptible");
    }
  }

  // CPD-OFF
  static class BrickSortComplex extends BrickSortParallel {

    void sortZeroLengthArray() {
      long[] a = {2, 5, 6, 8, 0};
      sort(a, 0);
    }

    void sortOneLengthArray() {
      long[] a = {2, 5, 6, 8, 0};
      sort(a, 1);
    }

    void sortNMinusOneLengthArray() {
      long[] a = {2, 5, 6, 8, 0};
      sort(a, 4);
    }

    void sortEmptyArray() {
      long[] a = {};
      sort(a, 0);
    }

    void sortSingleElementArray() {
      long[] a = {10};
      sort(a, 1);
    }

    void resetInternals() {
      reset();
    }

    void sortAndSetInternals() {
      long[] a = {12, 9, 7, 22, 25};
      sort(a, a.length);
    }

    void resetInternalsAfterSort() {
      sortAndSetInternals();
      reset();
    }

    void sortOdd() {
      long[] a = {12, 9, 7, 22, 25};
      sort(a, a.length);
    }

    void sortEven() {
      long[] a = {12, 9, 7, 22, 25, 19};
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
