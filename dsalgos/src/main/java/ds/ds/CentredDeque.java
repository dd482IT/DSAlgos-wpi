package ds;

import static ds.ArrayUtils.*;
import static ds.MathUtils.*;

import java.util.Arrays;

/***
 * <p>Centred Deque implementation in Java.</p>
 ***/
public class CentredDeque implements IQueue, IStack, IDeque {
  private static final String QUEUE_UNDERFLOW = "Queue underflow.";
  long[] arr;
  int first;
  int last;

  public CentredDeque(int size) {
    arr = new long[size];
    first = last = -1;
  }

  @Override
  public void push(long j) {
    addFirst(j);
  }

  @Override
  public long pop() {
    return poll();
  }

  @Override
  public void insert(long j) {
    addLast(j);
  }

  @Override
  public long poll() {
    return pollFirst();
  }

  @Override
  public long peek() {
    return peekFirst();
  }

  @Override
  public boolean isFull() {
    return isLeftFull() && isRightFull();
  }

  public boolean isLeftFull() {
    if (arr.length == 0) return true;
    return first == 0;
  }

  public boolean isRightFull() {
    if (arr.length == 1 || arr.length == 0) return true;
    return last == arr.length - 1;
  }

  private int getLeftBoundary(int length) {
    if (isOdd(length)) return ((length + 1) >> 1) - 1;
    else return (length >> 1) - 1;
  }

  private int getRightBoundary(int length) {
    if (isOdd(length)) return (length + 1) >> 1;
    else return length >> 1;
  }

  private int getLeftLength() {
    return first == -1 ? 0 : getLeftBoundary(arr.length) - first + 1;
  }

  private int getRightLength() {
    return last == -1 ? 0 : last - getRightBoundary(arr.length) + 1;
  }

  @Override
  public int size() {
    if (arr.length == 0) return 0;
    return getLeftLength() + getRightLength();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  private void reinitializeLeftPointer() {
    first = -1;
  }

  private void reinitializeRightPointer() {
    last = -1;
  }

  @Override
  public void addFirst(long key) {
    if (isLeftFull()) doubleCapacity();
    if (first == -1) first = getLeftBoundary(arr.length);
    else --first;
    arr[first] = key;
  }

  @Override
  public void addLast(long key) {
    if (isRightFull()) doubleCapacity();
    if (last == -1) last = getRightBoundary(arr.length);
    else ++last;
    arr[last] = key;
  }

  @Override
  public long pollFirst() {
    long val = peekFirst();
    if (first == -1) {
      int leftBoundary = getLeftBoundary(arr.length);
      int rightBoundary = getRightBoundary(arr.length);
      System.arraycopy(arr, rightBoundary, arr, leftBoundary, getRightLength());
      first = leftBoundary;
      --last;
      if (last < rightBoundary) last = -1;
    }
    if (first == getLeftBoundary(arr.length)) {
      reinitializeLeftPointer();
      return val;
    }
    ++first;
    return val;
  }

  @Override
  public long pollLast() {
    long val = peekLast();
    if (last == -1) {
      int leftLength = getLeftLength();
      System.arraycopy(arr, first++, arr, first, leftLength);
      last = getRightBoundary(arr.length);
      if (first > getLeftBoundary(arr.length)) first = -1;
    }
    if (last == getRightBoundary(arr.length)) {
      reinitializeRightPointer();
      return val;
    }
    --last;
    return val;
  }

  @Override
  public long peekFirst() {
    if (first == -1 && last == -1) throw new IllegalStateException(QUEUE_UNDERFLOW);
    if (first == -1) return arr[getRightBoundary(arr.length)];
    return arr[first];
  }

  @Override
  public long peekLast() {
    if (last == -1 && first == -1) throw new IllegalStateException(QUEUE_UNDERFLOW);
    if (last == -1) return arr[getLeftBoundary(arr.length)];
    return arr[last];
  }

  /***
   * <p> Double the capacity of this deque. </p>
   ***/
  private void doubleCapacity() {
    int n = arr.length;
    if (n == 0) throw new IllegalStateException("Initial capacity is zero. Cannot be doubled.");
    long[] a = (n == 1) ? getDoubleCapacity(n * 2) : getDoubleCapacity(n);

    if (first >= 0) {
      int startIndex = getLeftBoundary(a.length) - getLeftLength() + 1;
      System.arraycopy(arr, first, a, startIndex, getLeftLength());
      first = startIndex;
    }
    if (last > 0) {
      System.arraycopy(arr, getRightBoundary(arr.length), a, a.length >> 1, getRightLength());
      last = (a.length >> 1) + getRightLength() - 1;
    }
    arr = a;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String lineSeparator = System.lineSeparator();
    sb.append("First = ")
        .append(first)
        .append(lineSeparator)
        .append("Last = ")
        .append(last)
        .append(lineSeparator)
        .append(Arrays.toString(arr));
    return sb.toString();
  }
}
