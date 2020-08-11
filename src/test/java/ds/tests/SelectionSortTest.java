package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AbstractSort;
import ds.HighArray;
import ds.IArray;
import ds.ISort;
import ds.OrdArray;
import ds.SelectionSort;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@SuppressWarnings("PMD.LawOfDemeter")
class SelectionSortTest implements SortProvider {

  @ParameterizedTest
  @CsvSource(INIT_DATA)
  void testSort(@AggregateWith(HighArrayArgumentsAggregator.class) IArray arr) {
    long[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, "Elements must be sorted and equal.");
  }

  @ParameterizedTest
  @CsvSource(INIT_DUPLICATE_DATA)
  void testSortDuplicates(@AggregateWith(HighArrayArgumentsAggregator.class) IArray arr) {
    long[] a = {00, 00, 00, 00, 11, 11, 11, 22, 22, 33, 33, 44, 55, 66, 77, 77, 77, 88, 88, 99, 99};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, "Elements must be sorted and equal.");
  }

  @ParameterizedTest
  @CsvSource(INIT_ALL_SAME_DATA)
  void testSortAllSame(@AggregateWith(HighArrayArgumentsAggregator.class) IArray arr) {
    long[] a = {43, 43, 43, 43, 43, 43, 43, 43, 43, 43};
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(arr);
    long[] extent = sorted.getExtentArray();
    assertArrayEquals(a, extent, "Elements must be sorted and equal.");
    assertEquals(0, sorter.getSwapCount(), "Swap count will be zero.");
  }

  @ParameterizedTest
  @CsvSource(INIT_SELECTION_SORT_DATA)
  void testSortSmallData(@AggregateWith(HighArrayArgumentsAggregator.class) IArray arr) {
    ISort sorter = new SelectionSort();
    OrdArray ord = new OrdArray();
    long[] a = arr.getExtentArray();
    for (int i = 0; i < arr.count(); i++) ord.insert(a[i]);
    a = ord.getExtentArray();
    IArray sorted = sorter.sort(arr);
    long[] internal = sorted.getExtentArray();
    assertArrayEquals(a, internal, "Arrays must be sorted and equal.");
    assertEquals(5, sorter.getSwapCount(), "Swap count will be five.");
    assertTrue(isSorted(sorted), "Array must be sorted.");
  }

  @Test
  void testReset() {
    IArray high = new HighArray(20);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, 20)
        .forEach(
            i -> {
              high.insert(i);
              ord.insert(i);
            });
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    sorter.sort(ord);
    assertEquals(190, sorter.getComparisonCount(), "Comparison count must be (n * n -1) / 2.");
    assertEquals(0, sorter.getSwapCount(), "Swap count must be 0.");
  }

  @Test
  void testStreamUnSorted() {
    IArray high = new HighArray(20);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, 20)
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
    assertArrayEquals(extentSorted, extent, "Elements must be sorted and equal.");
  }

  @Test
  void testStreamSorted() {
    IArray high = new HighArray(20);
    IArray ord = new OrdArray();
    LongStream.rangeClosed(1, 20)
        .forEach(
            i -> {
              high.insert(i);
              ord.syncInsert(i);
            });
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] extentSorted = sorted.getExtentArray();
    long[] extent = ord.getExtentArray();
    assertArrayEquals(extentSorted, extent, "Elements must be sorted and equal.");
  }

  @Test
  void testReverseSorted() {
    IArray high = new HighArray(20);
    revRange(1, 20).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] a = sorted.get();
    assertEquals(
        (20 * 19) / 2, sorter.getTimeComplexity(), "Time complexity must be same as n squared.");
    assertTrue(a[0] == 1 && a[19] == 20, "First element must be 1 and last element must be 20.");
  }

  @Test
  void testSortedTimeComplexity() {
    IArray high = new HighArray(20);
    LongStream.rangeClosed(1, 20).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertEquals(
        (20 * 19) / 2, sorter.getTimeComplexity(), "Time complexity must be same as n squared.");
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  @Test
  void testComparisonCountUnsorted() {
    IArray high = new HighArray(20);
    LongStream.rangeClosed(1, 20).unordered().parallel().forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    IArray sorted = sorter.sort(high);
    long[] a = sorted.get();
    assertEquals(190, sorter.getComparisonCount(), "Comparison count must be 190.");
    assertTrue(a[0] == 1 && a[19] == 20, "First element must be 1 and last element must be 20.");
  }

  @Test
  void testSwapCountUnsorted() {
    IArray high = new HighArray(20);
    LongStream.rangeClosed(1, 20).unordered().parallel().forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertTrue(sorter.getSwapCount() < 20, "Swap count must be less than array length.");
  }

  @Test
  void testSwapCountSorted() {
    IArray high = new HighArray(20);
    LongStream.rangeClosed(1, 20).forEach(i -> high.insert(i));
    ISort sorter = new SelectionSort();
    sorter.sort(high);
    assertEquals(0, sorter.getSwapCount(), "Swap count must be zero.");
  }

  @Test
  void testToStringClass() {
    AbstractSort sorter = new SelectionSort();
    String className = SelectionSort.class.getName();
    assertTrue(
        sorter.toString().startsWith(className), () -> "ToString must start with " + className);
  }

  @Test
  void testPreReset() {
    ISort sorter = new SelectionSort();
    assertEquals(0, sorter.getComparisonCount(), "Initial value must be zero.");
    assertEquals(0, sorter.getSwapCount(), "Initial value must be zero.");
    assertEquals(0, sorter.getTimeComplexity(), "Initial value must be zero.");
    assertEquals(0, sorter.getCopyCount(), "Initial value must be zero.");
  }
}
