package ds.tests;

import static ds.tests.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.Power;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class PowerTest {
  private static final String EXCEPTION_EXPECTED = "Exception expected.";

  void testConstructor() {
    Power pow = new Power(2, SCORE);
    assertEquals(2, pow.getBase(), "Value equals constructor parameter.");
    assertEquals(SCORE, pow.getExponent(), "Value equals constructor parameter.");
  }

  void testException() {
    assertThrows(IllegalArgumentException.class, () -> new Power(0, -1), EXCEPTION_EXPECTED);
  }

  void testZeroBase() {
    Power pow = new Power(0, TEN);
    assertEquals(0, pow.compute(), "Zero value expected.");
  }

  void testTwoTen() {
    Power pow = new Power(2, 10);
    assertEquals(1024.0f, pow.compute(), "1024 expected.");
  }

  void testTwoSixteen() {
    Power pow = new Power(2, 16);
    assertEquals(65_536.0f, pow.compute(), "65536 expected.");
  }

  void testTwoEleven() {
    Power pow = new Power(2, 11);
    assertEquals(2048.0f, pow.compute(), "2048 expected.");
  }

  void testTwoNegativeEleven() {
    Power pow = new Power(2, -11);
    assertEquals(0.000_488_281_25f, pow.compute(), "0.00048828125 expected.");
  }
}
