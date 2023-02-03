package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.HighArray;
import ds.IArray;
import java.util.ConcurrentModificationException;
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
class HighArrayConcurrencyTest implements ConcurrencyProvider {
  private static final Logger LOGGER = Logger.getLogger(HighArrayConcurrencyTest.class.getName());

  void testConcurrentInserts() {
    IArray highArray = new HighArray(MYRIAD);
    LongStream.rangeClosed(1L, MYRIAD).parallel().forEach(i -> highArray.insert(i));
    assertEquals(
        MYRIAD, highArray.count(), () -> MYRIAD + " elements filled: " + highArray.toString());
  }

  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  void testConcurrentDeletes(int size) {
    CountDownLatch cdl = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(size);
    AtomicInteger excCount = new AtomicInteger();
    IArray highArray = new HighArray(size, true);
    try (LongStream nos = LongStream.rangeClosed(1L, (long) size).unordered()) {
      nos.forEach(i -> highArray.insert(i));
    }
    ExecutorService service = Executors.newFixedThreadPool(TEN);
    try (LongStream nosParallel = LongStream.rangeClosed(1L, (long) size).unordered().parallel()) {
      nosParallel.forEach(
          i ->
              service.execute(
                  () -> {
                    try {
                      cdl.await();
                      highArray.delete(i);
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
    assertNotEquals(0, excCount.get(), () -> excCount + " is number of concurrent exceptions.");
  }

  void testSequentialDeletes() {
    IArray highArray = new HighArray(MYRIAD, true);
    try (LongStream nos = LongStream.rangeClosed(1L, MYRIAD)) {
      nos.forEach(
          i -> {
            highArray.insert(i);
            highArray.delete(i);
          });
    }
    assertEquals(0, highArray.count(), () -> MYRIAD + " elements deleted: " + highArray.toString());
  }

  void testConcurrentSyncDeletes() {
    IArray highArray = new HighArray(HUNDRED);
    try (LongStream nos = LongStream.rangeClosed(1L, MYRIAD)) {
      nos.forEach(
          i -> {
            highArray.insert(i);
            highArray.syncDelete(i);
          });
    }
    assertEquals(
        0, highArray.count(), () -> HUNDRED + " elements  deleted: " + highArray.toString());
  }
}
