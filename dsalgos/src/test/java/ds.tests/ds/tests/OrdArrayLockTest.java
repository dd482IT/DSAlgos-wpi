package ds.tests;

import static ds.ArrayUtils.*;
import static ds.tests.TestConstants.*;
import static ds.tests.TestData.*;
import static ds.tests.TestUtils.*;
import static org.joor.Reflect.*;
import static org.junit.jupiter.api.Assertions.*;

import ds.IArray;
import ds.OrdArrayLock;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
class OrdArrayLockTest {
  class ConstructorTests {
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testConstructorParameterNegative() {
      IllegalArgumentException iae =
          assertThrows(
              IllegalArgumentException.class,
              () -> new OrdArrayLock(-1),
              "IllegalArgumentException expected for " + -1);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("-1"), "Parameter -1 expected");
    }

    void testConstructorParameterOK() {
      IArray arr = new OrdArrayLock(TEN);
      assertEquals(TEN, arr.get().length, "Length " + TEN + " expected");
    }

    void testEmptyConstructor() {
      IArray arr = new OrdArrayLock();
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
              () -> new OrdArrayLock(0),
              "IllegalArgumentException expected for " + 0);
      Optional<String> msg = Optional.ofNullable(iae.getMessage());
      String val = msg.orElse("");
      assertTrue(val.contains("0"), "Parameter 0 expected");
    }
  }

  class InsertTests {
    void insertDuplicate(IArray arr) {
      assertTrue(6 == arr.syncInsert(66L) && isSorted(arr), "Index 6 expected.");
    }

    void insertDuplicateElements(IArray arr) {
      assertTrue(21 == arr.count() && isSorted(arr), "21 elements expected");
    }

    void testInsert(IArray arr) {
      assertTrue(TEN == arr.count() && isSorted(arr), TEN + " elements inserted and sorted.");
    }

    void testInsertAtStartExists(IArray arr) {
      int count = arr.count();
      long val = 0L;
      int index = arr.findIndex(val);
      int insertIndex = arr.syncInsert(val);
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
      int insertIndex = arr.syncInsert(val);
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
      int insertIndex = arr.syncInsert(val);
      assertTrue(
          insertIndex == count && arr.count() == count + 1 && isSorted(arr),
          () -> (count + 1) + " elements expected, index " + count + " expected.");
    }

    void testInsertAtStart(IArray arr) {
      int count = arr.count();
      long val = -1L;
      int insertIndex = arr.syncInsert(val);
      assertTrue(
          insertIndex == 0 && arr.count() == count + 1 && isSorted(arr),
          "11 elements expected, index 0 expected.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testInsertSorted(IArray arr) {
      int count = arr.count();
      int res = arr.syncInsert(99L);
      assertTrue(res == 8 && arr.count() == count + 1, "Sorted and insert at 8 expected.");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testInsertAllSameSorted(IArray arr) {
      int count = arr.count();
      int res = arr.syncInsert(99L);
      assertTrue(res == TEN && arr.count() == count + 1, "Insert must succeed.");
    }

    void testException(IArray ordArray) {
      assertThrows(
          ArrayIndexOutOfBoundsException.class,
          () -> {
            ordArray.syncInsert(HUNDRED);
          });
    }
  }

  class DeleteTests {

    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.JUnitTestContainsTooManyAsserts"})
    void testDeleteTrue(IArray arr) {
      int count = arr.count();
      assertTrue(
          arr.syncDelete(00L) && arr.syncDelete(55L) && arr.syncDelete(99L),
          "Elements 0, 55 and 99 must be deleted");
      assertEquals(count - 3, arr.count(), "Count must be " + (count - 3));
      assertTrue(isSorted(arr), "Array must be sorted");
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    void testDeleteFalse(IArray arr) {
      int count = arr.count();
      assertFalse(
          arr.syncDelete(12L) || arr.syncDelete(6L) || arr.syncDelete(5L) && arr.count() != count,
          "Elements 12, 6, 5 found and deleted");
    }

    void testDeleteStart(IArray arr) {
      int count = arr.count();
      long searchKey = 00L;
      assertTrue(
          arr.syncDelete(searchKey) && arr.count() == count - 1 && isSorted(arr),
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteEnd(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.syncDelete(searchKey) && arr.count() == count - 1 && isSorted(arr),
          () -> String.format(NOT_AVAILABLE, searchKey));
    }

    void testDeleteOverflow(IArray arr) {
      long searchKey = 0L;
      arr.syncDelete(searchKey);
      int count = arr.count();
      assertFalse(
          arr.syncDelete(searchKey) && arr.count() != count, () -> searchKey + " still available");
    }

    void testDeleteEndArray(IArray arr) {
      int count = arr.count();
      long searchKey = 33L;
      assertTrue(
          arr.syncDelete(searchKey) && arr.count() == count - 1 && isSorted(arr),
          () -> String.format(NOT_AVAILABLE, searchKey));
    }
  }

  class ModCountTests {
    void testInsertModCount() {
      IArray arr = new OrdArrayLock(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.syncInsert(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 1 && arr.count() == count + 1,
          "modcount incremented.");
    }

    void testClearModCount() {
      IArray arr = new OrdArrayLock(HUNDRED);
      arr.syncInsert(TEN);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0, "modcount incremented.");
    }

    void testClearEmptyModCount() {
      IArray arr = new OrdArrayLock(HUNDRED);
      int modCount = getModCount(arr);
      arr.clear();
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == 0,
          "modcount must not be incremented.");
    }

    void testDeleteModCount() {
      IArray arr = new OrdArrayLock(HUNDRED);
      arr.syncInsert(TEN);
      int modCount = getModCount(arr);
      arr.syncDelete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount < newModCount && newModCount == 2 && arr.count() == 0, "modcount incremented.");
    }

    void testDeleteNotFoundModCount() {
      IArray arr = new OrdArrayLock(HUNDRED);
      int count = arr.count();
      int modCount = getModCount(arr);
      arr.syncDelete(TEN);
      int newModCount = getModCount(arr);
      assertTrue(
          modCount == newModCount && modCount == 0 && arr.count() == count,
          "modcount must not be incremented.");
    }
  }

  class EqualsVerifierTests {
    /** Added tests for code coverage completeness. */
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void equalsContract() {
      EqualsVerifier.forClass(OrdArrayLock.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT, WRITE)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(OrdArrayLockExt.class)
          .withPrefabValues(
              Lock.class,
              new ReentrantReadWriteLock().writeLock(),
              new ReentrantReadWriteLock().readLock())
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }

    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void leafNodeEquals() {
      EqualsVerifier.forClass(OrdArrayLock.class)
          .withIgnoredFields(MOD_COUNT, LOCK, STRICT, WRITE)
          .withRedefinedSuperclass()
          .withRedefinedSubclass(OrdArrayLockExt.class)
          .withPrefabValues(
              Lock.class,
              new ReentrantReadWriteLock().writeLock(),
              new ReentrantReadWriteLock().readLock())
          .withIgnoredAnnotations(NonNull.class)
          .verify();
    }
  }

  static class OrdArrayLockExt extends OrdArrayLock {
    OrdArrayLockExt(int size) {
      super(size);
    }

    @Override
    public boolean canEqual(Object obj) {
      return obj instanceof OrdArrayLockExt;
    }
  }
}
