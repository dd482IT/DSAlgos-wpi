package ds.tests;

import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static ds.tests.TestUtils.getModCount;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ds.HighArray;
import ds.IArray;
import java.util.Arrays;
import java.util.Optional;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("PMD.LawOfDemeter")
class HighArrayTest {
  class ConstructorTests {
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testConstructorParameterNegative() {
      IllegalArgumentException iae =
          assertThrows(
              IllegalArgumentException.class,
              () -> new HighArray(-1),
              "IllegalArgumentException expected for " + -1);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("-1"), "Parameter -1 expected");
    }

    void testConstructorParameterOK() {
      IArray arr = new HighArray(SCORE);
      assertEquals(SCORE, arr.get().length, "Length " + SCORE + " expected");
    }

    void testEmptyConstructor() {
      IArray arr = new HighArray();
      boolean strict = (boolean) on(arr).get(STRICT);
      assertTrue(
          arr.get().length == HUNDRED && !strict,
          "Length " + HUNDRED + " and strict false expected.");
    }

    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testConstructorParameterZero() {
      IllegalArgumentException iae =
          assertThrows(
              IllegalArgumentException.class,
              () -> new HighArray(0),
              "IllegalArgumentException expected for " + 0);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("0"), "Parameter 0 expected");
    }
  }

  class InsertTests {
    void testException(IArray highArray) {
      assertThrows(
          ArrayIndexOutOfBoundsException.class,
          () -> highArray.insert(SCORE),
          "Index out of bounds exception expected.");
    }

    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testInsertAggregate(IArray arr) {
      long[] a = {77, 99, 44, 55, 22, 88, 11, 00, 66, 33};
      long[] extent = arr.getExtentArray();
      assertEquals(TEN, arr.count(), TEN + " elements inserted.");
      assertArrayEquals(a, extent, "Elements must be equal.");
    }

    void testInsert() {
      HighArray arr = new HighArray(TEN);
      arr.insert(HUNDRED);
      int index = arr.insert(MYRIAD);
      assertEquals(1, index, "2 elements inserted.");
    }
  }

  class ModCountTests {
    void testInsertModCount() {
      IArray arr = new HighArray(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.insert(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 1 && arr.count() == count + 1,
          "modcount not incremented.");
    }

    void testClearModCount() {
      IArray arr = new HighArray(HUNDRED);
      arr.insert(TEN);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0,
          "modcount not incremented.");
    }

    void testClearEmptyModCount() {
      IArray arr = new HighArray(HUNDRED);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == 0,
          "modcount must not be incremented.");
    }

    void testDeleteModCount() {
      IArray arr = new HighArray(HUNDRED);
      arr.insert(TEN);
      int modCount = getModCount(arr);
      arr.delete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0,
          "modcount not incremented.");
    }

    void testDeleteNotFoundModCount() {
      IArray arr = new HighArray(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.delete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == count,
          "modcount must not be incremented.");
    }
  }

  class DeleteTests {
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.JUnitTestContainsTooManyAsserts"})
    void testDeleteTrue(IArray arr) {
      int count = arr.count();
      assertTrue(
          arr.delete(ZERO) && arr.delete(55L) && arr.delete(99L),
          "Elements 0, 55 and 99 must be deleted.");
      assertFalse(
          arr.find(ZERO) || arr.find(55L) || arr.find(99L),
          "Elements 0, 55 and 99 must not be found.");
      assertEquals(count - THREE, arr.count(), "Three elements should have been deleted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testDeleteFalse(IArray arr) {
      assertFalse(
          arr.delete(12L) || arr.delete(6L) || arr.delete(5L),
          "Elements 12, 6, 5 are not expected!");
    }

    void testDeleteEnd(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteEndArray(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteOverflow(IArray arr) {
      int count = arr.count();
      long searchKey = 0L;
      arr.delete(searchKey);
      assertFalse(
          arr.delete(searchKey) && arr.count() != count - 1,
          () -> searchKey + " must not be available.");
    }

    void testDeleteStart(IArray arr) {
      int count = arr.count();
      long searchKey = 77L;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }
  }

  class SyncTests {
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.JUnitTestContainsTooManyAsserts"})
    void testSyncDeleteTrue(IArray arr) {
      int count = arr.count();
      assertTrue(
          arr.syncDelete(ZERO) && arr.syncDelete(55L) && arr.syncDelete(99L),
          "Elements 0, 55 and 99 must be deleted.");
      assertFalse(
          arr.find(ZERO) || arr.find(55L) || arr.find(99L),
          "Elements 0, 55 and 99 must not be found.");
      assertEquals(count - THREE, arr.count(), "Three elements should have been deleted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteTrueIndividual(
        IArray arr) {
      int count = arr.count();
      assertTrue(arr.syncDelete(ZERO) && arr.count() == count - 1, "Element 0 deleted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteFalseIndividual(
        IArray arr) {
      int count = arr.count();
      assertFalse(
          arr.syncDelete(12L) && arr.count() == count, "Element 12 must not be found or deleted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteFalse(IArray arr) {
      assertFalse(
          arr.syncDelete(12L) || arr.syncDelete(6L) || arr.syncDelete(5L),
          "Elements 12, 6, 5 not expected!");
    }
  }

  class FindTests {
    void testFindIndexFalse(IArray arr) {
      long searchKey = 35L;
      assertEquals(-1, arr.findIndex(searchKey), () -> searchKey + " must not be available.");
    }

    void testFindFalse(IArray arr) {
      long searchKey = 35L;
      assertFalse(arr.find(searchKey), () -> searchKey + " must not be available");
    }

    void testFindIndex(IArray arr) {
      long searchKey = 11L;
      assertEquals(6, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexTrue(IArray arr) {
      long searchKey = 11L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 6,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexStart(IArray arr) {
      long searchKey = 77L;
      assertEquals(0, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexStartTrue(IArray arr) {
      long searchKey = 77L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 0,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexEnd(IArray arr) {
      long searchKey = 33L;
      assertEquals(9, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexEndTrue(IArray arr) {
      long searchKey = 33L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 9,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexOverflow(IArray arr) {
      long searchKey = 0L;
      arr.delete(0L);
      assertEquals(-1, arr.findIndex(searchKey), () -> searchKey + " must not be available");
    }

    void testFindEmpty() {
      IArray arr = new HighArray(TEN);
      long searchKey = 0L;
      assertEquals(-1, arr.findIndex(searchKey), () -> searchKey + " must not be available.");
    }

    void testFindTrue(IArray arr) {
      long searchKey = 11L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 6,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }
  }

  class MiscTests {
    void testGet(IArray arr) {
      long[] vals = arr.get();
      assertTrue(vals != null && vals.length == HUNDRED, "Non-null array and length " + HUNDRED);
    }

    void testExtentArray(IArray arr) {
      long[] vals = arr.getExtentArray();
      assertTrue(vals != null && vals.length == TEN, "Non-Null array and length " + TEN);
    }

    void testExtentArrayEmpty() {
      IArray arr = new HighArray();
      long[] vals = arr.getExtentArray();
      assertTrue(vals != null && vals.length == 0, "Non-Null array and length zero.");
    }

    void testCountZero() {
      IArray arr = new HighArray(TEN, true);
      assertEquals(0, arr.count(), "Count must be zero!");
    }

    void testCountPositive(IArray arr) {
      assertEquals(TEN, arr.count(), "Count must be " + TEN + "!");
    }

    void testClear(IArray arr) {
      int origCount = arr.count();
      arr.clear();
      long[] copy = new long[origCount];
      long[] origTrunc = Arrays.copyOf(arr.get(), origCount);
      assertTrue(
          0 == arr.count() && Arrays.equals(copy, origTrunc), () -> "Array must be cleared.");
    }

    void testClearEmpty() {
      IArray arr = new HighArray(HUNDRED);
      arr.clear();
      long[] copy = new long[HUNDRED];
      assertTrue(0 == arr.count() && Arrays.equals(arr.get(), copy), () -> "Array not cleared.");
    }
  }

  class ToStringTests {
    void testToString(IArray arr) {
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder(34);
      sb.append(HighArray.class.getName())
          .append(lineSeparator)
          .append("nElems = ")
          .append(THREE)
          .append(lineSeparator)
          .append("77 99 44 ");
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testToStringSpan(IArray arr) {
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder(38);
      sb.append(HighArray.class.getName())
          .append(lineSeparator)
          .append("nElems = ")
          .append(TEN)
          .append(lineSeparator)
          .append("77 99 44 55 22 88 11 0 66 33 ")
          .append(lineSeparator);
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testToStringEmpty(IArray arr) {
      arr.clear();
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder();
      sb.append(HighArray.class.getName())
          .append(lineSeparator)
          .append("nElems = ")
          .append(ZERO)
          .append(System.lineSeparator());
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testDisplay(IArray arr) {
      IArray highArray = spy(arr);

      doAnswer(
              i -> {
                i.callRealMethod();
                return null;
              })
          .when(highArray)
          .display();
      highArray.display();
      doCallRealMethod().when(highArray).display();
      highArray.display();
      verify(highArray, times(2)).display();
    }
  }

  class EqualsVerifierTests {
    /** Added tests for code coverage completeness. */
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void equalsContract() {
      EqualsVerifier.forClass(HighArray.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(HighArrayExt.class)
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }

    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void leafNodeEquals() {
      EqualsVerifier.forClass(HighArray.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(HighArrayExt.class)
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }
  }

  static class HighArrayExt extends HighArray {
    HighArrayExt(int size) {
      super(size);
    }

    @Override
    public boolean canEqual(Object obj) {
      return obj instanceof HighArrayExt;
    }
  }
}
