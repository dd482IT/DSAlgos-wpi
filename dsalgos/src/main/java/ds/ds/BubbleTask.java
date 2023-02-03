package ds;

import java.util.concurrent.Callable;

final class BubbleTask implements Cloneable, Callable<Void> {
  long[] a;
  int i;
  AbstractBrickSort sorter;

  @SuppressWarnings("PMD.ArrayIsStoredDirectly")
  BubbleTask(AbstractBrickSort sorter, long[] a, int i) {
    this.sorter = sorter;
    this.a = a;
    this.i = i;
  }

  @SuppressWarnings("checkstyle:NoClone")
  @Override
  public BubbleTask clone() {
    try {
      return (BubbleTask) super.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new AssertionError("Shouldn't get here.." + cnse.getMessage(), cnse);
    }
  }

  @Override
  public Void call() {
    sorter.bubble(a, i);
    return null;
  }

  public static BubbleTask createCopy(BubbleTask bt) {
    return bt.clone();
  }
}
