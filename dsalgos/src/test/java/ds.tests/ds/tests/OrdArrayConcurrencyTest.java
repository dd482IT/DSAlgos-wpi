package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.IArray;
import ds.OrdArray;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("PMD.LawOfDemeter")
class OrdArrayConcurrencyTest implements ConcurrencyProvider {
  private static final Logger LOGGER = Logger.getLogger(OrdArrayConcurrencyTest.class.getName());

  @SuppressWarnings({"PMD.JUnitTestContainsTooManyAsserts", "PMD.DataflowAnomalyAnalysis"})
  void testConcurrent(int size) {
    IArray ordArray = new OrdArray(size, true);
    LongStream.rangeClosed(1L, size >> 1)
        .unordered()
        .parallel()
        .forEach(i -> ordArray.syncInsert(i));
    CountDownLatch cdl = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(size >> 1);
    AtomicInteger excCount = new AtomicInteger();
    Random random = new Random();
    ExecutorService service = Executors.newFixedThreadPool(TEN);
    LongStream.rangeClosed(1L, (long) size >> 1)
        .unordered()
        .parallel()
        .forEach(
            i ->
                service.execute(
                    () -> {
                      try {
                        cdl.await();
                        if (random.nextBoolean()) ordArray.insert(i);
                        else ordArray.delete(i);
                        done.countDown();
                      } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        done.countDown();
                      } catch (ConcurrentModificationException cme) {
                        LOGGER.fine(() -> "Error: " + cme.getMessage());
                        excCount.incrementAndGet();
                        done.countDown();
                      }
                    }));
    try {
      cdl.countDown();
      done.await();
      service.shutdown();
      service.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
    int excs = excCount.get();
    assertFalse(isSorted(ordArray), "Array is sorted!");
    assertNotEquals(0, excs, () -> excs + " is number of concurrent exceptions.");
  }

  @SuppressWarnings({"PMD.JUnitTestContainsTooManyAsserts", "PMD.DataflowAnomalyAnalysis"})
  void testConcurrentInserts(int size) {
    CountDownLatch cdl = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(size);
    AtomicInteger excCount = new AtomicInteger();
    IArray ordArray = new OrdArray(size, true);
    ExecutorService service = Executors.newFixedThreadPool(TEN);
    LongStream.rangeClosed(1L, (long) size)
        .unordered()
        .parallel()
        .forEach(
            i ->
                service.execute(
                    () -> {
                      try {
                        cdl.await();
                        ordArray.insert(i);
                        done.countDown();
                      } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        done.countDown();
                      } catch (ConcurrentModificationException cme) {
                        LOGGER.fine(() -> "Error: " + cme.getMessage());
                        excCount.incrementAndGet();
                        done.countDown();
                      }
                    }));
    try {
      cdl.countDown();
      done.await();
      service.shutdown();
      service.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
    int excs = excCount.get();
    assertFalse(isSorted(ordArray), "Array is sorted!");
    assertNotEquals(0, excs, () -> excs + " is number of concurrent exceptions.");
  }

  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void testSyncInserts() {
    IArray ordArray = new OrdArray(MYRIAD, true);
    LongStream.rangeClosed(1L, MYRIAD).unordered().parallel().forEach(i -> ordArray.syncInsert(i));
    assertTrue(isSorted(ordArray), "Array is unsorted!");
    assertEquals(MYRIAD, ordArray.count(), MYRIAD + " elements not inserted.");
  }

  @SuppressWarnings({"PMD.JUnitTestContainsTooManyAsserts", "PMD.DataflowAnomalyAnalysis"})
  void testConcurrentDeletes(int size) {
    CountDownLatch cdl = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(size);
    AtomicInteger excCount = new AtomicInteger();
    IArray ordArray = new OrdArray(size, true);
    try (LongStream nos = LongStream.rangeClosed(1L, (long) size).unordered()) {
      nos.forEach(i -> ordArray.insert(i));
    }
    ExecutorService service = Executors.newFixedThreadPool(10);
    try (LongStream nosParallel = LongStream.rangeClosed(1L, (long) size).unordered().parallel()) {
      nosParallel.forEach(
          i ->
              service.execute(
                  () -> {
                    try {
                      cdl.await();
                      ordArray.delete(i);
                      done.countDown();
                    } catch (ConcurrentModificationException ignored) {
                      LOGGER.fine(() -> "Error deleting " + i);
                      excCount.incrementAndGet();
                      done.countDown();
                    } catch (InterruptedException ignored) {
                      Thread.currentThread().interrupt();
                      done.countDown();
                    }
                  }));
    }
    try {
      cdl.countDown();
      done.await();
      service.shutdown();
      service.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
    int excs = excCount.get();
    assertFalse(isSorted(ordArray), "Array is sorted!");
    assertNotEquals(0, excs, () -> excs + " is number of concurrent exceptions.");
  }

  void testSequentialDeletes() {
    IArray ordArray = new OrdArray(MYRIAD, true);
    try (LongStream nos = LongStream.rangeClosed(1L, MYRIAD)) {
      nos.forEach(
          i -> {
            ordArray.insert(i);
            ordArray.delete(i);
          });
    }
    assertEquals(0, ordArray.count(), () -> MYRIAD + " elements deleted: " + ordArray.toString());
  }

  void testConcurrentSyncDeletes() {
    IArray ordArray = new OrdArray(HUNDRED);
    try (LongStream nos = LongStream.rangeClosed(1L, MYRIAD)) {
      nos.forEach(
          i -> {
            ordArray.insert(i);
            ordArray.syncDelete(i);
          });
    }
    assertEquals(0, ordArray.count(), () -> "All elements deleted: " + ordArray.toString());
  }

  void testConcurrentSyncInsertsDeletes() {
    IArray ordArray = new OrdArray(HUNDRED);
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
