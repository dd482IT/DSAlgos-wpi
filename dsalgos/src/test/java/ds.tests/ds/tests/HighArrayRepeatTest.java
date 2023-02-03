package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.HighArray;
import ds.IArray;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

@SuppressWarnings("PMD.LawOfDemeter")
class HighArrayRepeatTest {
  IArray array;

  HighArrayRepeatTest() {
    array = new HighArray(THOUSAND, true);
    try (LongStream nos = LongStream.rangeClosed(1L, THOUSAND)) {
      nos.forEach(i -> array.insert(i));
    }
  }

  void repeatedTestWithRepetitionInfo(RepetitionInfo repetitionInfo) {
    int current = repetitionInfo.getCurrentRepetition();
    if (!array.delete(current)) fail(() -> "Element " + current + " not found.");
  }
}
