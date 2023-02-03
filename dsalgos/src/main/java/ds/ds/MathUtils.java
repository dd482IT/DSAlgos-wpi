package ds;

import static java.lang.Math.abs;

public final class MathUtils {

  private static final String INVALID_ARGUMENT = "Invalid argument: ";

  private MathUtils() throws InstantiationException {
    throw new InstantiationException("Private constructor for: " + MathUtils.class.getName());
  }

  public static int computeOddPairCount(int length) {
    if (length < 0) throw new IllegalArgumentException("Illegal argument value: " + length);
    return isOdd(length) ? length >> 1 : abs(length - 1) >> 1;
  }

  public static int computeEvenPairCount(int length) {
    if (length < 0) throw new IllegalArgumentException("Illegal argument value: " + length);
    return length >> 1;
  }

  public static boolean isOdd(int num) {
    return (num & 1) == 1;
  }

  public static boolean isInRangeInclusive(int min, int max, int val) {
    return Math.max(min, val) == Math.min(val, max);
  }

  public static boolean isInRange(int min, int max, int val) {

    return Math.max(min, val) == Math.min(val, max - 1);
  }

  public static boolean isGreaterThan(int min, int val) {
    return val > min;
  }

  public static boolean isLessThan(int max, int val) {
    return val < max;
  }

  public static void requireLessThan(int max, int val) {
    if (!isLessThan(max, val)) throw new IllegalArgumentException(INVALID_ARGUMENT + val);
  }

  public static void requireGreaterThan(int min, int val) {
    if (!isGreaterThan(min, val)) throw new IllegalArgumentException(INVALID_ARGUMENT + val);
  }

  public static void requireInRangeInclusive(int min, int max, int val) {
    if (!isInRangeInclusive(min, max, val))
      throw new IllegalArgumentException(INVALID_ARGUMENT + val);
  }

  public static void requireInRange(int min, int max, int val) {
    if (!isInRange(min, max, val)) throw new IllegalArgumentException(INVALID_ARGUMENT + val);
  }
}
