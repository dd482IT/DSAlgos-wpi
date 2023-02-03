package ds;

import java.util.concurrent.ExecutorService;

@SuppressWarnings("PMD.LawOfDemeter")
public final class AssertionUtils {

  private AssertionUtils() throws InstantiationException {
    throw new InstantiationException("Private constructor for: " + AssertionUtils.class.getName());
  }

  public static void assertEquality(int first, int second) {
    if (first != second)
      throw new AssertionError(Integer.toString(first) + " is not the same as " + second);
  }

  public static void assertServiceTerminated(ExecutorService service) {
    if (!service.isShutdown()) throw new AssertionError("ExecutorService must be shutdown.");
    if (!Thread.currentThread().isInterrupted() && !service.isTerminated())
      throw new AssertionError("ExecutorService must terminate cleanly.");
  }
}
