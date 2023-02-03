package ds;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.Objects;

@SuppressWarnings({"nullness", "PMD.AvoidFieldNameMatchingMethodName", "PMD.LawOfDemeter"})
public class TreeNode<T extends Comparable<T>> implements ITreeNode<T> {
  /** Data object reference. */
  T val;

  /** Left and right child nodes. */
  ITreeNode<T> left;

  ITreeNode<T> right;

  // height of the subtree
  int height;
  // number of nodes in subtree
  int size;
  // number of copies in node
  int refCount;
  // priority
  int priority;

  /**
   * Constructor for TreeNode.
   *
   * @param val data object reference
   * @param left left child node reference or null
   * @param right right child node reference or null
   */
  TreeNode(T val, TreeNode<T> left, TreeNode<T> right) {
    this.val = val;
    this.left = left;
    this.right = right;
    this.refCount = 1;
  }

  TreeNode(T val) {
    this(val, null, null);
    this.size = 1;
    this.height = 0;
  }

  @Override
  public void incrementRefCount() {
    ++refCount;
    ++size;
  }

  @Override
  public void decrementRefCount() {
    --refCount;
    --size;
  }

  @Override
  public int refCount() {
    return refCount;
  }

  @Override
  public int height() {
    return this.height;
  }

  private int height(ITreeNode<T> x) {
    return isNull(x) ? -1 : x.height();
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public void setHeight(int ht) {
    this.height = ht;
  }

  @Override
  public int balanceFactor() {
    return height(left()) - height(right());
  }

  @Override
  public ITreeNode<T> left() {
    return left;
  }

  @Override
  public ITreeNode<T> right() {
    return right;
  }

  @Override
  public void setRight(ITreeNode<T> right) {
    this.right = right;
  }

  @Override
  public void setLeft(ITreeNode<T> left) {
    this.left = left;
  }

  @Override
  public void setValue(T val) {
    this.val = val;
  }

  @Override
  public T value() {
    return val;
  }

  /**
   * Insert an object into the tree.
   *
   * @param obj object to insert into tree.
   */
  @Override
  public void insert(T obj) {
    requireNonNull(obj);
    if (val.compareTo(obj) < 0) {
      if (isNull(right)) right = new TreeNode<>(obj, null, null);
      else right.insert(obj);
    } else {
      if (isNull(left)) left = new TreeNode<>(obj, null, null);
      else left.insert(obj);
    }
  }

  /**
   * Find an object in the tree. Objects are compared using the compareTo method, so must conform to
   * type Comparable. Two objects are equal if they represent the same value.
   *
   * @param obj Object representing value to find in tree.
   * @return reference to matching node or null.
   */
  @Override
  public ITreeNode<T> find(T obj) {
    int temp = val.compareTo(obj);
    if (temp == 0) return this;

    if (temp < 0) return isNull(right) ? null : right.find(obj);

    return isNull(left) ? null : left.find(obj);
  }

  /**
   * Remove the node referencing an object representing the same value as the argument object. This
   * recursive method essentially restructures the tree as necessary and returns a reference to the
   * new root. The algorithm is straightforward apart from the case where the node to be removed has
   * two children. In that case the left-most leaf node of the right child is moved up the tree to
   * replace the removed node. Hand work some examples to see how this works.
   *
   * @param obj Object representing value to remove from tree.
   * @return reference to the (possibly new) root node of the sub-tree being examined or null if no
   *     node.
   */
  @SuppressWarnings("PMD.LawOfDemeter")
  @Override
  public ITreeNode<T> remove(T obj) {
    requireNonNull(obj);
    ITreeNode<T> t = this;
    ITreeNode<T> left = t.left();
    ITreeNode<T> right = t.right();
    T value = t.value();
    int cmp = obj.compareTo(value);
    if (cmp == 0) {
      if (isNull(left) || isNull(right)) t = isNull(left) ? right : left;
      else {
        t.setValue(findMin(right).value());
        t.setRight(right.remove(t.value()));
      }
    } else if (cmp < 0) t.setLeft(left.remove(obj));
    else t.setRight(right.remove(obj));
    return t;
  }

  /**
   * Helper method to find the left most leaf node in a sub-tree.
   *
   * @param t TreeNode to be examined.
   * @return reference to left most leaf node or null.
   */
  private ITreeNode<T> findMin(ITreeNode<T> node) {
    ITreeNode<T> t = node;
    while (nonNull(t.left())) t = t.left();
    return t;
  }

  @SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
  @Override
  public String toString() {
    return Objects.toString(val);
  }

  @Override
  public ITreeNode<T> clone() {
    try {
      return (ITreeNode<T>) super.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new AssertionError("Shouldn't reach here...", cnse);
    }
  }
}
