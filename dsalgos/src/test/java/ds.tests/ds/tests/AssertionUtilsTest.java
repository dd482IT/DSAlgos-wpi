package ds.tests;

import static ds.AssertionUtils.*;
import static ds.tests.TestConstants.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.AssertionUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.joor.ReflectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class AssertionUtilsTest {

  void testPrivateConstructor() {
    assertThrows(
        ReflectException.class,
        () -> on(AssertionUtils.class).create(),
        "Private constructor throws exception.");
  }

  void testInequality() {
    assertThrows(
        AssertionError.class, () -> assertEquality(SCORE, MYRIAD), "Inequality throws exception.");
  }

  void testServiceTerminated() {
    ExecutorService service =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    assertThrows(
        AssertionError.class,
        () -> assertServiceTerminated(service),
        "Service not terminated throws exception.");
  }
}
