package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.IArray;
import ds.OrdArrayLock;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class OrdArrayLockConcurrencyTest implements ConcurrencyProvider {

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testSyncInserts() {
    IArray ordArray = new OrdArrayLock(THOUSAND, true);
    LongStream.rangeClosed(1L, THOUSAND)
        .unordered()
        .parallel()
        .forEach(i -> ordArray.syncInsert(i));
    assertTrue(isSorted(ordArray), "Array is sorted!");
    assertEquals(THOUSAND, ordArray.count(), THOUSAND + " elements  inserted.");
  }

  void testSequentialDeletes() {
    IArray ordArray = new OrdArrayLock(THOUSAND, true);
    try (LongStream nos = LongStream.rangeClosed(1L, THOUSAND)) {
      nos.forEach(
          i -> {
            ordArray.insert(i);
            ordArray.delete(i);
          });
    }
    assertEquals(0, ordArray.count(), () -> "All elements deleted: " + ordArray.toString());
  }

  void testConcurrentSyncDeletes() {
    IArray ordArray = new OrdArrayLock(HUNDRED);
    try (LongStream nos = LongStream.rangeClosed(1L, THOUSAND)) {
      nos.forEach(
          i -> {
            ordArray.insert(i);
            ordArray.syncDelete(i);
          });
    }
    assertEquals(0, ordArray.count(), () -> "All elements deleted: " + ordArray.toString());
  }

  void testConcurrentSyncInsertsDeletes() {
    IArray ordArray = new OrdArrayLock(HUNDRED);
    try (LongStream nos = LongStream.rangeClosed(1L, HUNDRED).unordered().parallel()) {
      nos.forEach(
          i -> {
            ordArray.syncInsert(i);
            ordArray.syncDelete(i);
          });
    }
    assertEquals(0, ordArray.count(), () -> "Elements cleared.");
  }
}
