package ds;

import static java.util.Objects.isNull;

public final class TreeUtils {
  private TreeUtils() {
    throw new IllegalStateException("Private constructor");
  }

  /**
   * Returns the number of nodes in the subtree.
   *
   * @param x the subtree
   * @return the number of nodes in the subtree
   */
  public static <T extends Comparable<T>> int size(ITreeNode<T> x) {
    return isNull(x) ? 0 : x.size();
  }
}
