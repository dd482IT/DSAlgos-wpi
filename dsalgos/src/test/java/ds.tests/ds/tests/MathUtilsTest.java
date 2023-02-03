package ds.tests;

import static ds.MathUtils.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.MathUtils;
import org.joor.ReflectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
class MathUtilsTest {

  private static final String MUST_RETURN_TRUE = "Must return true.";
  private static final String MUST_RETURN_FALSE = "Must return false.";
  private static final String EXCEPTION_EXPECTED = "Exception expected.";
  private static final String EXCEPTION_NOT_EXPECTED = "Exception not expected.";
  private static final String ONE_EXPECTED = "One expected!";

  void testPrivateConstructor() {
    assertThrows(
        ReflectException.class,
        () -> on(MathUtils.class).create(),
        "Private constructor throws exception.");
  }

  void testComputeOddPairCount() {
    assertEquals(0, computeOddPairCount(0), "Zero expected!");
  }

  void testComputeOddPairCountOne() {
    assertEquals(0, computeOddPairCount(1), "Zero expected.");
  }

  void testComputeOddPairCountTwo() {
    assertEquals(0, computeOddPairCount(2), "Zero expected.");
  }

  void testComputeEvenPairCount() {
    assertEquals(0, computeEvenPairCount(0), "Zero expected.");
  }

  void testComputeEvenPairCountTwo() {
    assertEquals(1, computeEvenPairCount(2), ONE_EXPECTED);
  }

  void testComputeEvenPairCountNegative() {
    assertThrows(
        IllegalArgumentException.class, () -> computeEvenPairCount(-1), EXCEPTION_EXPECTED);
  }

  void testComputeOddPairCountNegative() {
    assertThrows(IllegalArgumentException.class, () -> computeOddPairCount(-1), EXCEPTION_EXPECTED);
  }

  void testZero() {
    assertFalse(isOdd(0), "Zero is even!");
  }

  void testOne() {
    assertTrue(isOdd(1), "One is odd!");
  }

  void testMinusOne() {
    assertTrue(isOdd(-1), "Minus One is odd!");
  }

  void testTwo() {
    assertFalse(isOdd(2), "Two is even!");
  }

  void testMinusTwo() {
    assertFalse(isOdd(-2), "-2 is not odd.");
  }

  void testMaxValue() {
    assertTrue(isOdd(Integer.MAX_VALUE), () -> Integer.MAX_VALUE + " is odd!");
  }

  void testMinValue() {
    assertFalse(isOdd(Integer.MIN_VALUE), () -> Integer.MIN_VALUE + " is even!");
  }

  void testIsInRangeLeft() {
    assertTrue(
        isInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE), MUST_RETURN_TRUE);
  }

  void testIsInRangeRight() {
    assertTrue(
        isInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE - 1), MUST_RETURN_TRUE);
  }

  void testIsNotInRangeRight() {
    assertFalse(
        isInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), MUST_RETURN_FALSE);
  }

  void testIsNotInRangeLeft() {
    assertFalse(
        isInRangeInclusive(Integer.MIN_VALUE + 1, Integer.MAX_VALUE, Integer.MIN_VALUE),
        MUST_RETURN_FALSE);
  }

  void testIsInRangeMid() {
    assertTrue(isInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, 0), MUST_RETURN_TRUE);
  }

  void testIsInRangeInclusiveMid() {
    assertTrue(isInRangeInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE, 0), MUST_RETURN_TRUE);
  }

  void testIsInRangeInclusiveLeft() {
    assertTrue(
        isInRangeInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE),
        MUST_RETURN_TRUE);
  }

  void testIsInRangeInclusiveRight() {
    assertTrue(
        isInRangeInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
        MUST_RETURN_TRUE);
  }

  void testGreaterThan() {
    assertTrue(isGreaterThan(Integer.MIN_VALUE, Integer.MAX_VALUE), MUST_RETURN_TRUE);
  }

  void testNotGreaterThan() {
    assertFalse(isGreaterThan(Integer.MAX_VALUE, Integer.MIN_VALUE), MUST_RETURN_FALSE);
  }

  void testNotLessThan() {
    assertFalse(isLessThan(Integer.MIN_VALUE, Integer.MAX_VALUE), MUST_RETURN_FALSE);
  }

  void testLessThan() {
    assertTrue(isLessThan(Integer.MAX_VALUE, Integer.MIN_VALUE), MUST_RETURN_TRUE);
  }

  void testRequireNotGreaterThan() {
    assertThrows(
        IllegalArgumentException.class,
        () -> requireGreaterThan(Integer.MAX_VALUE, Integer.MIN_VALUE),
        EXCEPTION_EXPECTED);
  }

  void testRequireGreaterThan() {
    assertDoesNotThrow(
        () -> requireGreaterThan(Integer.MIN_VALUE, Integer.MAX_VALUE), EXCEPTION_NOT_EXPECTED);
  }

  void testRequireNotLessThan() {
    assertThrows(
        IllegalArgumentException.class,
        () -> requireLessThan(Integer.MIN_VALUE, Integer.MAX_VALUE),
        EXCEPTION_EXPECTED);
  }

  void testRequireLessThan() {
    assertDoesNotThrow(
        () -> requireLessThan(Integer.MAX_VALUE, Integer.MIN_VALUE), EXCEPTION_NOT_EXPECTED);
  }

  void testRequireIsInRange() {
    assertDoesNotThrow(
        () -> requireInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE),
        EXCEPTION_NOT_EXPECTED);
  }

  void testRequireNotInRange() {
    assertThrows(
        IllegalArgumentException.class,
        () -> requireInRange(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
        EXCEPTION_EXPECTED);
  }

  void testRequireIsInRangeInclusive() {
    assertDoesNotThrow(
        () -> requireInRangeInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
        EXCEPTION_NOT_EXPECTED);
  }

  void testRequireNotInRangeInclusive() {
    assertThrows(
        IllegalArgumentException.class,
        () -> requireInRangeInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
        EXCEPTION_EXPECTED);
  }
}
