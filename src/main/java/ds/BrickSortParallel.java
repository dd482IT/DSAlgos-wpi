package ds;

import static ds.AssertionUtils.*;
import static ds.ExecutorUtils.terminateExecutor;
import static ds.MathUtils.isOdd;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Not thread-safe with state variables. */
public class BrickSortParallel extends BrickSort {

  private static final int THRESHOLD = 40;
  private final AtomicBoolean sorted = new AtomicBoolean();
  private final AtomicInteger swapCount = new AtomicInteger();

  @Override
  protected void reset() {
    super.reset();
    sorted.getAndSet(false);
    swapCount.set(0);
  }

  private void sequentialSort(long[] a, int length) {
    super.sort(a, length);
    sorted.getAndSet(super.sorted);
    swapCount.set(super.swapCount);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  @Override
  protected void sort(long[] a, int length) {
    if (!shouldSort(length)) {
      sorted.getAndSet(true);
      return;
    }
    if (length <= THRESHOLD) {
      sequentialSort(a, length);
      return;
    }
    ExecutorService service =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    try {
      sortInterruptibly(a, length, service);
    } catch (ExecutionException | InterruptedException ee ) {
      throw new CompletionException(ee);
    } finally {
      terminateExecutor(service, length, TimeUnit.MILLISECONDS);
    }
    assertServiceTerminated(service);
  }

  protected void sortInterruptibly(long[] a, int length, ExecutorService service)
      throws InterruptedException, ExecutionException {
    final int maxComparisons = computeMaxComparisons(length);
    final int oddTaskCount = computeOddTaskCount(length);
    final int evenTaskCount = computeEvenTaskCount(length);
    while (!sorted.get()) {
      ++outerLoopCount;
      sorted.set(true);
      oddSort(a, length, service, oddTaskCount);
      if (swapCount.intValue() == maxComparisons) {
        sorted.set(true);
        break;
      }
      evenSort(a, length, service, evenTaskCount);
      if (swapCount.intValue() == maxComparisons) sorted.set(true);
    }
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.SystemPrintln"})
  protected void oddSort(long[] a, int length, ExecutorService service, int oddTaskCount)
      throws InterruptedException, ExecutionException {
    List<Future<Void>> futures = new ArrayList<>(oddTaskCount);
    BubbleTask bt = new BubbleTask(this, a, 0);
    for (int i = 1; i < length - 1; i += 2) {
      ++innerLoopCount;
      ++comparisonCount;
      BubbleTask task = BubbleTask.createCopy(bt);
      task.i = i; 
      futures.add(service.submit(task));
    }
    assertEquality(futures.size(), oddTaskCount);
    for (Future future : futures) future.get();
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.SystemPrintln"})
  protected void evenSort(long[] a, int length, ExecutorService service, int evenTaskCount)
      throws InterruptedException, ExecutionException{
    List<Future<Void>> futures = new ArrayList<>(evenTaskCount);
    BubbleTask bt = new BubbleTask(this, a, 0);
    for (int i = 0; i < length - 1; i += 2) {
      ++innerLoopCount;
      ++comparisonCount;
      BubbleTask task = BubbleTask.createCopy(bt);
      task.i = i; 
      futures.add(service.submit(task));
    }
    assertEquality(futures.size(), evenTaskCount);
    for (Future future : futures) future.get();
  }

  @Override
  protected void bubble(long[] a, int i) {
    if (a[i] > a[i + 1]) {
      swap(a, i, i + 1);
      sorted.set(false);
      swapCount.incrementAndGet();
    }
  }

  @Override
  public int getSwapCount() {
    return swapCount.intValue();
  }

  @Override
  public boolean isSorted() {
    return sorted.get();
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String lineSeparator = System.lineSeparator();
    sb.append(getClass().getName())
        .append(lineSeparator)
        .append("Comparison count: ")
        .append(comparisonCount)
        .append(lineSeparator)
        .append("Swap count: ")
        .append(swapCount)
        .append(lineSeparator)
        .append("Copy count: ")
        .append(copyCount)
        .append(lineSeparator)
        .append("inner loop count: ")
        .append(innerLoopCount)
        .append(lineSeparator)
        .append("outer loop count: ")
        .append(outerLoopCount)
        .append(lineSeparator)
        .append("sorted: ")
        .append(sorted)
        .append(lineSeparator);
    return sb.toString();
  }

  public static int computeOddTaskCount(int length) {
    if (length < 0) throw new IllegalArgumentException("Illegal argument value: " + length);
    return isOdd(length) ? length >> 1 : abs(length - 1) >> 1;
  }

  public static int computeEvenTaskCount(int length) {
    if (length < 0) throw new IllegalArgumentException("Illegal argument value: " + length);
    return length >> 1;
  }
}
