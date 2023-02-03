package ds.tests;

import static ds.ArrayUtils.*;
import static ds.BrickSortUnrolled.*;
import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.BrickSortUnrolled;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.OrdArray;
import ds.RandomUtils;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "checkstyle:classfanoutcomplexity"})
class BrickSortUnrolledTest implements SortProvider {

  private static final String MUST_BE_EQUAL = "Must be equal";
  private static final String ZERO_TASKS_EXPECTED = "Zero tasks expected.";
  private static final String ONE_TASK_EXPECTED = "One task expected.";
  private static final String HALF_TASKS_EXPECTED = "Half tasks expected.";
  private static final String ILLEGAL_LENGTH_EXPECTED = "Illegal length expected.";
  private static final String ARRAY_SORTED = "Array must be sorted.";
  private static final String COUNT_EQUALS =
      "Comparison count must be same as swap count in reverse ordered array.";

  void testReverseSortedOdd255() {
    IArray high = new HighArray(255);
    revRange(1, 255).forEach(i -> high.insert(i));
    BrickSortComplex sorter = new BrickSortComplex();
    IArray sorted = sorter.sort(high);
    final int innerLoopCount = sorter.getInnerLoopCount();
    final int outerLoopCount = sorter.getOuterLoopCount();
    int length = high.count();
    final int oddTaskCount = computeOddTaskCount(length);
    final int evenTaskCount = computeEvenTaskCount(length);
    assertEquals(
        (oddTaskCount + evenTaskCount) * (outerLoopCount - 1) + oddTaskCount,
        innerLoopCount,
        MUST_BE_EQUAL);
    assertTrue(isSorted(sorted), ARRAY_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
  }

  void testReverseSortedEven256() {
    IArray high = new HighArray(256);
    revRange(1, 256).forEach(i -> high.insert(i));
    BrickSortComplex sorter = new BrickSortComplex();
    IArray sorted = sorter.sort(high);
    final int innerLoopCount = sorter.getInnerLoopCount();
    final int outerLoopCount = sorter.getOuterLoopCount();
    int length = high.count();
    final int oddTaskCount = computeOddTaskCount(length);
    final int evenTaskCount = computeEvenTaskCount(length);
    assertEquals((oddTaskCount + evenTaskCount) * outerLoopCount, innerLoopCount, MUST_BE_EQUAL);
    assertTrue(isSorted(sorted), ARRAY_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
  }

  void testReverseSortedOdd309() {
    IArray high = new HighArray(309);
    revRange(1, 309).forEach(i -> high.insert(i));
    BrickSortComplex sorter = new BrickSortComplex();
    IArray sorted = sorter.sort(high);
    final int innerLoopCount = sorter.getInnerLoopCount();
    final int outerLoopCount = sorter.getOuterLoopCount();
    int length = high.count();
    final int oddTaskCount = computeOddTaskCount(length);
    final int evenTaskCount = computeEvenTaskCount(length);
    assertEquals(
        (oddTaskCount + evenTaskCount) * (outerLoopCount - 1) + oddTaskCount,
        innerLoopCount,
        MUST_BE_EQUAL);
    assertTrue(isSorted(sorted), ARRAY_SORTED);
    assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
  }

  void testToStringClass() {
    AbstractSort sorter = new BrickSortUnrolled();
    String className = BrickSortUnrolled.class.getName();
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
    assertEquals(0, bsc.getComparisonCount(), COUNT_EQUALS);
    assertEquals(0, bsc.getSwapCount(), "Swap count must be reset.");
    assertEquals(0, bsc.getCopyCount(), "Copy count must be reset.");
    assertFalse(bsc.isSorted(), "sorted must be reset.");
  }

  void testResetAfterSort() {
    BrickSortComplex bsc = new BrickSortComplex();
    bsc.resetInternalsAfterSort();
    assertEquals(0, bsc.getTimeComplexity(), "Time Complexity must be reset.");
    assertEquals(0, bsc.getComparisonCount(), COUNT_EQUALS);
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
      assertTrue(isSorted(sorted), ARRAY_SORTED);
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
      final int oddTaskCount = computeOddTaskCount(length);
      final int evenTaskCount = computeEvenTaskCount(length);
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
      final int oddTaskCount = computeOddTaskCount(length);
      final int evenTaskCount = computeEvenTaskCount(length);
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
      assertEquals(MYRIAD - 1, compCount, COUNT_EQUALS + (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testComparisonCountUnsorted() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).parallel().unordered().forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      int compCount = sorter.getComparisonCount();
      assertTrue(
          MYRIAD - 1 <= compCount && compCount <= (MYRIAD * MYRIAD - 1),
          COUNT_EQUALS + MYRIAD * (MYRIAD - 1));
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSorted() {
      IArray high = new HighArray(MYRIAD);
      revRange(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
      assertTrue(isSorted(sorted), ARRAY_SORTED);
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testReverseSortedOdd() {
      IArray high = new HighArray(MYRIAD + 1);
      revRange(1, MYRIAD + 1).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      IArray sorted = sorter.sort(high);
      assertEquals(sorter.getSwapCount(), sorter.getComparisonCount(), COUNT_EQUALS);
      assertTrue(isSorted(sorted), ARRAY_SORTED);
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testSwapCount() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
      assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
      assertTrue(sorter.isSorted(), SORTED_MUST_BE_SET);
    }

    void testTimeComplexity() {
      IArray high = new HighArray(MYRIAD);
      LongStream.rangeClosed(1, MYRIAD).forEach(i -> high.insert(i));
      BrickSortComplex sorter = new BrickSortComplex();
      sorter.sort(high);
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

  class ComputeTaskCountTest {

    void testZeroLength() {
      assertEquals(0, computeOddTaskCount(0), ZERO_TASKS_EXPECTED);
      assertEquals(0, computeEvenTaskCount(0), ZERO_TASKS_EXPECTED);
    }

    void testMinusOneLength() {
      assertThrows(
          IllegalArgumentException.class, () -> computeOddTaskCount(-1), ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class, () -> computeEvenTaskCount(-1), ILLEGAL_LENGTH_EXPECTED);
    }

    void testMinusTwoLength() {
      assertThrows(
          IllegalArgumentException.class, () -> computeOddTaskCount(-2), ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class, () -> computeEvenTaskCount(-2), ILLEGAL_LENGTH_EXPECTED);
    }

    void testMinValueLength() {
      assertThrows(
          IllegalArgumentException.class,
          () -> computeOddTaskCount(Integer.MIN_VALUE),
          ILLEGAL_LENGTH_EXPECTED);
      assertThrows(
          IllegalArgumentException.class,
          () -> computeEvenTaskCount(Integer.MIN_VALUE),
          ILLEGAL_LENGTH_EXPECTED);
    }

    void testOneValueLength() {
      assertEquals(0, computeOddTaskCount(1), ZERO_TASKS_EXPECTED);
      assertEquals(0, computeEvenTaskCount(1), ZERO_TASKS_EXPECTED);
    }

    void testTwoValueLength() {
      assertEquals(0, computeOddTaskCount(2), ZERO_TASKS_EXPECTED);
      assertEquals(1, computeEvenTaskCount(2), ONE_TASK_EXPECTED);
    }

    void testThreeValueLength() {
      assertEquals(1, computeOddTaskCount(3), ONE_TASK_EXPECTED);
      assertEquals(1, computeEvenTaskCount(3), ONE_TASK_EXPECTED);
    }

    void testFourValueLength() {
      assertEquals(1, computeOddTaskCount(4), ONE_TASK_EXPECTED);
      assertEquals(2, computeEvenTaskCount(4), "Two tasks expected");
    }

    void testMaxValueLength() {
      assertEquals(
          Integer.MAX_VALUE / 2, computeOddTaskCount(Integer.MAX_VALUE), HALF_TASKS_EXPECTED);
      assertEquals(
          Integer.MAX_VALUE / 2, computeEvenTaskCount(Integer.MAX_VALUE), HALF_TASKS_EXPECTED);
    }

    void testMaxValueLengthEven() {
      assertEquals(
          (Integer.MAX_VALUE - 2) / 2,
          computeOddTaskCount(Integer.MAX_VALUE - 1),
          HALF_TASKS_EXPECTED);
      assertEquals(
          (Integer.MAX_VALUE - 1) / 2,
          computeEvenTaskCount(Integer.MAX_VALUE - 1),
          HALF_TASKS_EXPECTED);
    }
  }

  static class BrickSortExceptionable extends BrickSortUnrolled {
    @Override
    protected void bubble(long[] ignored, int ignoredInt) {
      throw new IllegalStateException("Error in " + BrickSortExceptionable.class + ".bubble");
    }
  }

  static class BrickSortInterruptible extends BrickSortUnrolled {
    @Override
    protected void sortInterruptibly(
        long[] ignoredArr, int ignoredLength, ExecutorService ignoredService)
        throws InterruptedException, ExecutionException {
      throw new InterruptedException(
          "Error in " + BrickSortInterruptible.class + ".sortInterruptible");
    }
  }

  // CPD-OFF
  static class BrickSortComplex extends BrickSortUnrolled {

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
      return innerLoopCount.intValue();
    }

    int getOuterLoopCount() {
      return outerLoopCount;
    }
  }
}
