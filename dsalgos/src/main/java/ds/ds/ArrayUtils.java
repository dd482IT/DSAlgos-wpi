package ds;

import java.util.Arrays;

public final class ArrayUtils {

  private ArrayUtils() throws InstantiationException {
    throw new InstantiationException("Private constructor for: " + ArrayUtils.class.getName());
  }

  public static long[] getDoubleCapacity(int n) {
    if (n < 0) throw new IllegalStateException("Sorry, negative size not possible.");
    int newCapacity = n << 1;
    if (newCapacity < 0) throw new IllegalStateException("Sorry, deque too big.");
    long[] arr = new long[newCapacity];
    Arrays.fill(arr, 0L);
    return arr;
  }

  public static boolean isSorted(long... a) {
    return isSorted(a, a.length);
  }

  public static boolean isSorted(long[] a, int length) {
    if (length < 0 || length > a.length)
      throw new IllegalArgumentException("Length " + length + " is invalid.");
    for (int j = 0; j < length - 1; j++) {
      if (a[j] > a[j + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isSorted(IArray arr) {
    return isSorted(arr.get(), arr.count());
  }

  public static boolean swapIfLessThan(long[] a, int first, int second) {
    requireValidIndex(a.length, first);
    requireValidIndex(a.length, second);
    if (a[first] < a[second]) return quickSwap(a, first, second);
    return false;
  }

  public static boolean swapIfGreaterThan(long[] a, int first, int second) {
    requireValidIndex(a.length, first);
    requireValidIndex(a.length, second);
    if (a[first] > a[second]) return quickSwap(a, first, second);
    return false;
  }

  private static boolean quickSwap(long[] a, int first, int second) {
    if (first == second) return false;
    long temp = a[first];
    a[first] = a[second];
    a[second] = temp;
    return true;
  }

  private static void requireValidIndex(int length, int index) {
    if (index < 0) throw new IllegalArgumentException("Index cannot be " + index);
    if (index > length - 1) throw new IllegalArgumentException("Index cannot be " + index);
  }

  public static boolean swap(long[] a, int first, int second) {
    requireValidIndex(a.length, first);
    requireValidIndex(a.length, second);
    return quickSwap(a, first, second);
  }
}
