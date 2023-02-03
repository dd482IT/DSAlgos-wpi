package ds.tests;

import static ds.ExecutorUtils.terminateExecutor;
import static ds.tests.TestConstants.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.ExecutorUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.joor.ReflectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class ExecutorUtilsTest {

  private static final String EXECUTOR_SHUTDOWN = "Executor is shutdown!";

  void testPrivateConstructor() {
    assertThrows(
        ReflectException.class,
        () -> on(ExecutorUtils.class).create(),
        "Private constructor throws exception.");
  }

  void testNormalTerminate() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    terminateExecutor(es, THOUSAND, TimeUnit.MILLISECONDS);
    assertTrue(es.isTerminated(), "All tasks completed!");
  }

  void testImmediateTerminate() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    terminateExecutor(es, 0, TimeUnit.NANOSECONDS);
    assertTrue(es.isShutdown(), "Service shutdown!");
    assertTrue(es.isTerminated(), "All tasks completed!");
  }

  void testForceShutdown() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.execute(
        () -> {
          while (true) {
            try {
              Thread.sleep(THOUSAND);
            } catch (InterruptedException ignored) {
              Thread.currentThread().interrupt();
            }
          }
        });
    terminateExecutor(es, TEN, TimeUnit.MILLISECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
    assertFalse(es.isTerminated(), "Not all tasks complete expected!");
  }

  @SuppressWarnings("PMD.SystemPrintln")
  void testForceShutdownNormal() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.submit(
        () -> {
          System.out.println("Executed task.");
        });
    terminateExecutor(es, TEN, TimeUnit.MILLISECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
    assertTrue(es.isTerminated(), "All tasks complete expected!");
  }

  void testInterruptedAwait() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    es.submit(new InterruptThread(Thread.currentThread()));
    terminateExecutor(es, THOUSAND >> 1, TimeUnit.MILLISECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
    assertTrue(Thread.interrupted(), "Thread interrupted!");
  }

  void testAwaitTerminationTrue() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.submit(new InterruptThread(Thread.currentThread()));
    terminateExecutor(es, THOUSAND >> 1, TimeUnit.MILLISECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
    assertTrue(Thread.interrupted(), "Thread interrupted!");
  }

  void testAwaitTerminatedLong() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.submit(new LongThread());
    terminateExecutor(es, HUNDRED, TimeUnit.NANOSECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
  }

  void testAwaitTerminatedLongImmediate() {
    ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    es.submit(new LongThread());
    terminateExecutor(es, 0L, TimeUnit.NANOSECONDS);
    assertTrue(es.isShutdown(), EXECUTOR_SHUTDOWN);
    assertFalse(es.isTerminated(), "Executor is not terminated!");
  }

  static class InterruptThread extends Thread {
    Thread parentThread;

    InterruptThread(Thread parent) {
      parentThread = parent;
    }

    @Override
    public void run() {
      try {
        TimeUnit.MILLISECONDS.sleep(HUNDRED);
        parentThread.interrupt();
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
      }
    }
  }

  static class LongThread extends Thread {

    @Override
    public void run() {
      try {
        TimeUnit.NANOSECONDS.sleep(Long.MAX_VALUE);
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
