package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.QuickSelectMedian;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class QuickSelectMedianTest {

  private static final String EXPECTED = " expected.";

  void testEmptyArray() {
    QuickSelectMedian median = new QuickSelectMedian(new long[0]);
    assertEquals(Double.NaN, median.find(), () -> Double.NaN + EXPECTED);
  }

  void testSingleElementArray() {
    long[] arr = {23};
    QuickSelectMedian median = new QuickSelectMedian(arr);
    assertEquals(23, median.find(), () -> 23 + EXPECTED);
  }

  void testTwoElementArray() {
    long[] arr = {23, 24};
    QuickSelectMedian median = new QuickSelectMedian(arr);
    assertEquals(23.5, median.find(), () -> 23.5 + EXPECTED);
  }

  void testThreeElementArray() {
    long[] arr = {23, 21, 24};
    QuickSelectMedian median = new QuickSelectMedian(arr);
    assertEquals(23, median.find(), () -> 23 + EXPECTED);
  }

  void testMedianEven() {
    long[] arr = {23, 21, 20, 18, 17, 16, 0, 10, 9, 10};
    QuickSelectMedian median = new QuickSelectMedian(arr.clone());
    Arrays.sort(arr);
    double expected = 0.5f * (arr[4] + arr[5]);
    assertEquals(expected, median.find(), () -> expected + EXPECTED);
  }

  void testMedianOdd() {
    long[] arr = {23, 21, 20, 18, 18, 17, 16, 0, 10, 9, 10};
    QuickSelectMedian median = new QuickSelectMedian(arr.clone());
    Arrays.sort(arr);
    assertEquals(arr[5], median.find(), () -> arr[5] + EXPECTED);
  }

  void testRandomEven() {
    long[] arr = new long[10_000];
    Random random = new Random();
    for (int i = 0; i < 10_000; i++) arr[i] = random.nextInt();
    QuickSelectMedian median = new QuickSelectMedian(arr.clone());
    Arrays.sort(arr);
    double medianVal = 0.5f * (arr[4999] + arr[5000]);
    assertEquals(medianVal, median.find(), () -> medianVal + EXPECTED);
  }

  void testRandomOdd() {
    long[] arr = new long[10_001];
    Random random = new Random();
    for (int i = 0; i < 10_001; i++) arr[i] = random.nextInt();
    QuickSelectMedian median = new QuickSelectMedian(arr.clone());
    Arrays.sort(arr);
    long medianVal = arr[5000];
    assertEquals(medianVal, median.find(), () -> medianVal + EXPECTED);
  }
}
