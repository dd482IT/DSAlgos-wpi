package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.ArrayUtils;
import java.util.Optional;
import java.util.Random;
import java.util.stream.LongStream;
import org.joor.ReflectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
class ArrayUtilsTest {
  private static final String EXCEPTION_EXPECTED = "Exception expected.";

  void testPrivateConstructor() {
    assertThrows(
        ReflectException.class,
        () -> on(ArrayUtils.class).create(),
        "Private constructor throws exception.");
  }

  void testSorted() {
    long[] arr = LongStream.rangeClosed(1, HUNDRED).toArray();
    assertTrue(isSorted(arr, HUNDRED >> 1), "Array is sorted!");
  }

  void testUnSorted() {
    Random random = new Random();
    long[] arr = random.longs().limit(HUNDRED).toArray();
    assertFalse(isSorted(arr, HUNDRED), "Array is unsorted!");
  }

  void testEmptyArray() {
    long[] arr = new long[0];
    assertTrue(isSorted(arr), "Empty array is sorted!");
  }

  void testFullArray() {
    long[] arr = LongStream.rangeClosed(1, HUNDRED).toArray();
    assertTrue(isSorted(arr), "Full array is sorted!");
  }

  void testFullArrayUnsorted() {
    Random random = new Random();
    long[] arr = random.longs().limit(HUNDRED).toArray();
    assertFalse(isSorted(arr), "Full array is unsorted!");
  }

  void testLessThanFullArray() {
    long[] arr = LongStream.rangeClosed(1, HUNDRED).toArray();
    int length = HUNDRED >> 1;
    assertTrue(isSorted(arr, length), "Less than full array is sorted!");
  }

  void testLengthZero() {
    long[] arr = new long[TEN];
    int length = 0;
    assertTrue(isSorted(arr, length), "Length zero array is sorted!");
  }

  void testLengthOne() {
    long[] arr = new long[TEN];
    arr[0] = 1;
    int length = 1;
    assertTrue(isSorted(arr, length), "One element array is sorted!");
  }

  void testSizeOne() {
    long[] arr = new long[1];
    int length = 1;
    arr[0] = 1;
    assertTrue(isSorted(arr, length), "Size one array is sorted!");
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testNegativeLength() {
    long[] arr = new long[TEN];
    int length = -1;
    IllegalArgumentException iae =
        assertThrows(
            IllegalArgumentException.class, () -> isSorted(arr, length), EXCEPTION_EXPECTED);
    Optional<String> msg = Optional.ofNullable(iae.getMessage());
    String errMsg = msg.orElse("");
    int val = Integer.parseInt(errMsg.replaceAll("[A-Za-z. ]", ""));
    assertTrue(val < 0 || val > arr.length, " -1 expected.");
  }

  void testExcessiveLength() {
    long[] arr = new long[TEN];
    int length = TEN + 1;
    IllegalArgumentException iae =
        assertThrows(
            IllegalArgumentException.class, () -> isSorted(arr, length), EXCEPTION_EXPECTED);
    Optional<String> msg = Optional.ofNullable(iae.getMessage());
    String errMsg = msg.orElse("");
    int val = Integer.parseInt(errMsg.replaceAll("[\\D]", ""));
    assertTrue(val < 0 || val > arr.length, (TEN + 1) + " expected.");
  }

  void testDoubleCapacity() {
    long[] arr = getDoubleCapacity(TEN);
    assertEquals(SCORE, arr.length, "Double the value expected.");
  }

  void testDoubleCapacityOdd() {
    long[] arr = getDoubleCapacity(TEN + 1);
    assertEquals(SCORE + 2, arr.length, "Double the value expected.");
  }

  void testDoubleCapacityZero() {
    long[] arr = getDoubleCapacity(0);
    assertEquals(0, arr.length, "Zero value expected.");
  }

  void testMaxInt() {
    assertThrows(
        IllegalStateException.class,
        () -> getDoubleCapacity(Integer.MAX_VALUE),
        EXCEPTION_EXPECTED);
  }

  void testNegativeSize() {
    assertThrows(
        IllegalStateException.class,
        () -> getDoubleCapacity(Integer.MIN_VALUE),
        EXCEPTION_EXPECTED);
  }

  void testNegativeSizeScore() {
    assertThrows(
        IllegalStateException.class, () -> getDoubleCapacity(-1 * SCORE), EXCEPTION_EXPECTED);
  }

  void testSwapLessThanTrue() {
    long[] a = {1, 2, 3};
    assertTrue(swapIfLessThan(a, 1, 2), "Less than swap expected.");
  }

  void testSwapLessThanFalse() {
    long[] a = {1, 2, 2};
    assertFalse(swapIfLessThan(a, 1, 2), "Less than swap not expected.");
  }
}
