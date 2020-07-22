package ds;

import static ds.ArrayUtils.*;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicInteger;

/** Demonstrates array class with high-level interface. */
@SuppressWarnings("PMD.LawOfDemeter")
public class OrdArray {
  @SuppressWarnings("all")
  private static final java.util.logging.Logger LOGGER =
      java.util.logging.Logger.getLogger(OrdArray.class.getName());

  private final long[] a;
  private final AtomicInteger nElems;
  private final Object lock = new Object();
  private final boolean strict;
  private AtomicInteger modCount;
  private boolean sorted = true;
  private boolean dirty;

  public OrdArray() {
    this(100, false);
  }

  public OrdArray(int max) {
    this(max, false);
  }

  public OrdArray(int max, boolean strict) {
    if (max <= 0) throw new IllegalArgumentException("Invalid size: " + max);
    a = new long[max];
    nElems = new AtomicInteger();
    modCount = new AtomicInteger();
    this.strict = strict;
  }

  public long[] get() {
    return a.clone();
  }

  private void checkSorted() {
    sorted = isSorted(this);
  }

  public int findIndex(long searchKey) {
    return findIndex(searchKey, nElems.intValue());
  }

  private int findIndex(long searchKey, int length) {
    int lowerBound = 0;
    int upperBound = length - 1;
    while (lowerBound <= upperBound) {
      int mid = lowerBound + ((upperBound - lowerBound) >>> 1);
      long midVal = a[mid];
      if (midVal == searchKey) return mid;
      if (midVal < searchKey) lowerBound = mid + 1;
      else upperBound = mid - 1;
    }
    // key not found
    return -(lowerBound + 1);
  }

  // -----------------------------------------------------------
  public boolean find(long searchKey) {
    return findIndex(searchKey) >= 0;
  }

  /**
   * Insert element into array.
   *
   * @param value element to insert
   * @return index of inserted element.
   */
  public int insert(long value) {
    int length = nElems.intValue();
    if (length == a.length) throw new ArrayIndexOutOfBoundsException(length);
    if (dirty) checkSorted();
    if (sorted) {
      int expectedCount = modCount.intValue();
      int count = nElems.intValue();
      int j = findIndex(value, count);
      j = j < 0 ? -1 * j - 1 : j;
      if (strict && expectedCount < modCount.intValue()) {
        dirty = true;
        throw new ConcurrentModificationException("Error inserting value: " + value);
      }
      modCount.incrementAndGet();
      int numMoved = count - j;
      System.arraycopy(a, j, a, j + 1, numMoved);
      nElems.getAndIncrement();
      a[j] = value;
      return j;
    }
    return -1;
  }

  public int syncInsert(long value) {
    synchronized (lock) {
      return insert(value);
    }
  }

  public void clear() {
    int length = nElems.intValue();
    if (length == 0) return;
    modCount.incrementAndGet();
    Arrays.fill(a, 0, length, 0L);
    nElems.set(0);
  }

  private void fastDelete(int index, int length) {
    modCount.incrementAndGet();
    // move higher ones down
    int numMoved = length - index - 1;
    System.arraycopy(a, index + 1, a, index, numMoved);
    a[nElems.decrementAndGet()] = 0;
  }

  public boolean syncDelete(long value) {
    synchronized (lock) {
      return delete(value);
    }
  }

  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public boolean delete(long value) {
    int length = nElems.intValue();
    int expectedModCount = modCount.intValue();
    int j = findIndex(value, nElems.intValue());
    if (j < 0) return false;
    if (strict && expectedModCount < modCount.intValue())
      throw new ConcurrentModificationException("Error deleting value: " + value);
    fastDelete(j, length);
    return true;
  }

  @SuppressWarnings({"PMD.SystemPrintln", "PMD.LawOfDemeter"})
  public void display() {
    System.out.println(this);
  }

  @Override
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public String toString() {
    int length = nElems.intValue();
    StringBuilder sb = new StringBuilder();
    sb.append("nElems = ").append(length).append(System.lineSeparator());
    long[] newArray = a.clone();
    for (int j = 0; j < length; j++) sb.append(newArray[j]).append(' ');
    return sb.toString();
  }

  public int count() {
    return nElems.intValue();
  }

  @Override
  @SuppressWarnings("all")
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof OrdArray)) return false;
    final OrdArray other = (OrdArray) o;
    if (!other.canEqual((Object) this)) return false;
    if (!java.util.Arrays.equals(this.a, other.a)) return false;
    final Object this$nElems = this.nElems;
    final Object other$nElems = other.nElems;
    if (this$nElems == null ? other$nElems != null : !this$nElems.equals(other$nElems))
      return false;
    return true;
  }

  @SuppressWarnings("all")
  protected boolean canEqual(final Object other) {
    return other instanceof OrdArray;
  }

  @Override
  @SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = PRIME + java.util.Arrays.hashCode(this.a);
    final Object $nElems = this.nElems;
    result = result * PRIME + ($nElems == null ? 43 : $nElems.hashCode());
    return result;
  }
}
