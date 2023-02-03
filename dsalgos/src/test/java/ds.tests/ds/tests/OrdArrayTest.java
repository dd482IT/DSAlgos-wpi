package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static ds.tests.TestUtils.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ds.IArray;
import ds.OrdArray;
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
class OrdArrayTest {

  private static final String MUST_NOT_BE_AVAILABLE = " must not be available.";

  class InsertTests {
    void insertDuplicate(IArray arr) {
      assertTrue(6 == arr.insert(66L) && isSorted(arr), "Index 6 expected");
    }

    void insertDuplicateElements(IArray arr) {
      assertTrue(21 == arr.count() && isSorted(arr), "21 sorted elements expected");
    }

    void testInsert(IArray arr) {
      assertTrue(TEN == arr.count() && isSorted(arr), TEN + " elements inserted and sorted.");
    }

    void testInsertAtStartExists(IArray arr) {
      int count = arr.count();
      long val = ZERO;
      int index = arr.findIndex(val);
      int insertIndex = arr.insert(val);
      assertTrue(
          insertIndex >= index
              && insertIndex <= index + 1
              && arr.count() == count + 1
              && isSorted(arr),
          "11 elements expected, indexes 0 or 1 expected.");
    }

    void testInsertAtEndExists(IArray arr) {
      int count = arr.count();
      long val = 99L;
      int index = arr.findIndex(val);
      int insertIndex = arr.insert(val);
      assertTrue(
          insertIndex >= index
              && insertIndex <= index + 1
              && arr.count() == count + 1
              && isSorted(arr),
          "11 elements expected, indexes 9 or 10 expected.");
    }

    void testInsertAtEnd(IArray arr) {
      int count = arr.count();
      long val = HUNDRED;
      int insertIndex = arr.insert(val);
      assertTrue(
          insertIndex == count && arr.count() == count + 1 && isSorted(arr),
          () -> (count + 1) + " elements expected, index " + count + " expected.");
    }

    void testInsertAtStart(IArray arr) {
      int count = arr.count();
      long val = -1L;
      int insertIndex = arr.insert(val);
      assertTrue(
          insertIndex == 0 && arr.count() == count + 1 && isSorted(arr),
          "11 elements expected, index 0 expected.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testInsertSorted(IArray arr) {
      int count = arr.count();
      int res = arr.insert(99L);
      boolean sorted = isSorted(arr);
      assertTrue(
          res == 8 && arr.count() == count + 1 && sorted, "Sorted and insert at 8 expected.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testInsertAllSameSorted(IArray arr) {
      int count = arr.count();
      int res = arr.insert(99L);
      boolean sorted = isSorted(arr);
      assertTrue(res == TEN && sorted && arr.count() == count + 1, "Insert must succeed.");
    }

    void testException(IArray ordArray) {
      assertThrows(
          ArrayIndexOutOfBoundsException.class,
          () -> ordArray.insert(SCORE),
          "Array index out of bounds exception expected.");
    }
  }

  class DeleteTests {

    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.JUnitTestContainsTooManyAsserts"})
    void testDeleteTrue(IArray arr) {
      int count = arr.count();
      assertTrue(
          arr.delete(ZERO) && arr.delete(55L) && arr.delete(99L),
          "Elements 0, 55 and 99 must be deleted.");
      assertEquals(count - THREE, arr.count(), "Count must be " + (count - THREE) + ".");
      assertTrue(isSorted(arr), "Array must be sorted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testDeleteFalse(IArray arr) {
      int count = arr.count();
      assertFalse(
          arr.delete(12L) || arr.delete(6L) || arr.delete(5L) && arr.count() != count,
          "Elements 12, 6, 5 must not be found or deleted.");
    }

    void testDeleteStart(IArray arr) {
      int count = arr.count();
      long searchKey = ZERO;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1 && isSorted(arr),
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteEnd(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1 && isSorted(arr),
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteOverflow(IArray arr) {
      long searchKey = ZERO;
      arr.delete(searchKey);
      int count = arr.count();
      assertFalse(
          arr.delete(searchKey) && arr.count() != count, () -> searchKey + MUST_NOT_BE_AVAILABLE);
    }

    void testDeleteEndArray(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.delete(searchKey) && arr.count() == count - 1 && isSorted(arr),
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
      assertEquals(count - THREE, arr.count(), "Count must be " + (count - THREE) + ".");
      assertTrue(isSorted(arr), "Array must be sorted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteTrueIndividual(
        IArray arr) {
      int count = arr.count();
      assertTrue(
          arr.syncDelete(ZERO) && arr.count() == count - 1 && isSorted(arr),
          "Element 0 must not be found.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteFalse(IArray arr) {
      int count = arr.count();
      assertFalse(
          arr.syncDelete(12L) || arr.syncDelete(6L) || arr.syncDelete(5L) && arr.count() != count,
          "Elements 12, 6, 5 must not be found or deleted.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testSyncDeleteFalseIndividual(
        IArray arr) {
      int count = arr.count();
      assertFalse(arr.syncDelete(12L) && arr.count() != count, "Elements 12 not found or deleted.");
    }

    void insertSyncDuplicate(IArray arr) {
      assertTrue(6 == arr.syncInsert(66L) && isSorted(arr), "7 elements expected.");
    }
  }

  class ConstructorTests {
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testConstructorParameterNegative() {
      IllegalArgumentException iae =
          assertThrows(
              IllegalArgumentException.class,
              () -> new OrdArray(-1),
              "IllegalArgumentException expected for " + -1);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("-1"), "Parameter -1 expected");
    }

    void testConstructorParameterOK() {
      IArray arr = new OrdArray(TEN);
      assertEquals(TEN, arr.get().length, "Length " + TEN + " expected.");
    }

    void testEmptyConstructor() {
      IArray arr = new OrdArray();
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
              () -> new OrdArray(0),
              "IllegalArgumentException expected for " + 0);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("0"), "Parameter 0 expected");
    }
  }

  class ModCountTests {
    void testInsertModCount() {
      IArray arr = new OrdArray(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.insert(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 1 && arr.count() == count + 1,
          "modCount must be  incremented.");
    }

    void testClearModCount() {
      IArray arr = new OrdArray(HUNDRED);
      arr.insert(TEN);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0,
          "modCount must be  incremented.");
    }

    void testClearEmptyModCount() {
      IArray arr = new OrdArray(HUNDRED);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == 0,
          "modCount must not be incremented.");
    }

    void testDeleteModCount() {
      IArray arr = new OrdArray(HUNDRED);
      arr.insert(TEN);
      int modCount = getModCount(arr);
      arr.delete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0,
          "modCount must be  incremented.");
    }

    void testDeleteNotFoundModCount() {
      IArray arr = new OrdArray(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.delete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == count,
          "modCount must not be incremented.");
    }
  }

  class MiscTests {
    void testClear(IArray arr) {
      int origCount = arr.count();
      arr.clear();
      long[] copy = new long[origCount];
      long[] origTrunc = Arrays.copyOf(arr.get(), origCount);
      assertTrue(
          0 == arr.count() && Arrays.equals(copy, origTrunc), () -> "Array must be cleared.");
    }

    void testClearEmpty() {
      IArray arr = new OrdArray(HUNDRED);
      arr.clear();
      long[] copy = new long[HUNDRED];
      assertTrue(0 == arr.count() && Arrays.equals(arr.get(), copy), () -> "Array must be clear.");
    }

    void testGet(IArray arr) {
      long[] vals = arr.get();
      assertTrue(
          vals != null && vals.length == HUNDRED, "Non-Null array and length " + HUNDRED + ".");
    }

    void testExtentArray(IArray arr) {
      long[] vals = arr.getExtentArray();
      assertTrue(vals != null && vals.length == TEN, "Non-Null array and length " + TEN + ".");
    }

    void testExtentArrayEmpty() {
      IArray arr = new OrdArray();
      long[] vals = arr.getExtentArray();
      assertTrue(vals != null && vals.length == 0, "Non-Null array and length zero.");
    }

    void testCountZero(IArray arr) {
      arr.clear();
      assertEquals(0, arr.count(), "Count must be zero!");
    }

    void testCountPositive(IArray arr) {
      assertEquals(TEN, arr.count(), "Count must be 10!");
    }
  }

  class FindTests {

    void testFindIndexFalse(IArray arr) {
      long searchKey = 35L;
      assertEquals(-4, arr.findIndex(searchKey) + 1, () -> searchKey + " not available.");
    }

    void testFindFalse(IArray arr) {
      long searchKey = 35L;
      assertFalse(arr.find(searchKey), () -> searchKey + MUST_NOT_BE_AVAILABLE);
    }

    void testFindIndexTrue(IArray arr) {
      long searchKey = 11L;
      assertEquals(1, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexStart(IArray arr) {
      long searchKey = ZERO;
      assertEquals(0, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexStartTrue(IArray arr) {
      long searchKey = ZERO;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 0,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexEndTrue(IArray arr) {
      long searchKey = 99L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 9,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexEnd(IArray arr) {
      long searchKey = 99L;
      assertEquals(9, arr.findIndex(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindTrue(IArray arr) {
      long searchKey = 11L;
      assertTrue(arr.find(searchKey), () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindSeqBefore(IArray arr) {
      long searchKey = 14L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == THREE,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindSeqAfter(IArray arr) {
      long searchKey = 16L;
      assertTrue(
          arr.find(searchKey) && arr.findIndex(searchKey) == 5,
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testFindIndexOverflow(IArray arr) {
      long searchKey = ZERO;
      arr.delete(searchKey);
      assertEquals(-1, arr.findIndex(searchKey), () -> searchKey + MUST_NOT_BE_AVAILABLE);
    }

    void testFindEmpty() {
      IArray arr = new OrdArray(TEN);
      long searchKey = ZERO;
      assertEquals(-1, arr.findIndex(searchKey), () -> searchKey + MUST_NOT_BE_AVAILABLE);
    }
  }

  class ToStringTests {
    void testToString(IArray arr) {
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder(34);
      sb.append(OrdArray.class.getName())
          .append(lineSeparator)
          .append("nElems = ")
          .append(THREE)
          .append(lineSeparator)
          .append("44 77 99 ");
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testToStringSpan(IArray arr) {
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder(57);
      sb.append(OrdArray.class.getName())
          .append(lineSeparator)
          .append("nElems = ")
          .append(TEN)
          .append(lineSeparator)
          .append("0 11 22 33 44 55 66 77 88 99 ")
          .append(lineSeparator);
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testToStringEmpty() {
      IArray arr = new OrdArray(TEN);
      String lineSeparator = System.lineSeparator();
      StringBuilder sb = new StringBuilder(25);
      sb.append(OrdArray.class.getName())
          .append(lineSeparator)
          .append("nElems = 0")
          .append(lineSeparator);
      assertEquals(sb.toString(), arr.toString(), "Strings must be equal.");
    }

    void testDisplay(IArray arr) {
      IArray ordArray = spy(arr);

      doAnswer(
              i -> {
                i.callRealMethod();
                return null;
              })
          .when(ordArray)
          .display();
      ordArray.display();
      doCallRealMethod().when(ordArray).display();
      ordArray.display();
      verify(ordArray, times(2)).display();
    }
  }

  class EqualsVerifierTests {
    /** Added tests for code coverage completeness. */
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void equalsContract() {
      EqualsVerifier.forClass(OrdArray.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(OrdArrayExt.class)
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }

    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void leafNodeEquals() {
      EqualsVerifier.forClass(OrdArray.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(OrdArrayExt.class)
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }
  }

  static class OrdArrayExt extends OrdArray {
    OrdArrayExt(int size) {
      super(size);
    }

    @Override
    public boolean canEqual(Object obj) {
      return obj instanceof OrdArrayExt;
    }
  }
}
