package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.RadixSort;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class RadixSortTest implements SortProvider {

  void testSort() {
    int[] a = {00, 11, 22, 33, 44, 55, 66, 77, 88, 99};
    int[] clone = a.clone();
    RadixSort sorter = new RadixSort(a);
    sorter.sort();
    Arrays.sort(clone);
    assertArrayEquals(clone, a, ELEMENTS_SORTED_EQUAL);
  }

  void testSortRandom() {
    int[] arr = new int[MYRIAD];
    int[] ord = new int[MYRIAD];
    Random random = new Random();
    try (IntStream stream = random.ints(0, Integer.MAX_VALUE).limit(MYRIAD)) {
      int[] ints = stream.toArray();
      System.arraycopy(ints, 0, arr, 0, arr.length);
      System.arraycopy(ints, 0, ord, 0, ord.length);
    }
    RadixSort sorter = new RadixSort(arr);
    sorter.sort();
    Arrays.sort(ord);
    assertArrayEquals(ord, arr, ELEMENTS_SORTED_EQUAL);
  }
}
