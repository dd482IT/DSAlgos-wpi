package ds;

import static java.util.Objects.*;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

@SuppressWarnings({"nullness", "PMD.LawOfDemeter", "PMD.GodClass"})
public class DoublyLinkedList<T> extends AbstractList<T> {

  private static final String DATA_NON_NULL = "Data cannot be null.";
  private int length;

  @SuppressWarnings("initialization.fields.uninitialized")
  private INode<T> head;

  @SuppressWarnings("initialization.fields.uninitialized")
  private INode<T> tail;

  @Override
  protected void linkFirst(T data) {
    if (isNull(head)) head = tail = new DoubleNode<>(data);
    else {
      INode<T> node = new DoubleNode<>(data, head);
      head.setPrev(node);
      head = node;
    }
    ++length;
  }

  @Override
  protected void linkBefore(T data, INode<T> next) {
    INode<T> prev = next.getPrev();
    INode<T> node = new DoubleNode<>(prev, data, next);
    next.setPrev(node);
    if (isNull(prev)) {
      head = node;
    } else {
      prev.setNext(node);
    }
    ++length;
  }

  @Override
  protected void linkLast(T data) {
    INode<T> node = new DoubleNode<>(data);
    INode<T> last = tail;
    if (nonNull(last)) {
      last.setNext(node);
      node.setPrev(last);
      tail = node;
    } else head = tail = node;
    ++length;
  }

  @Override
  protected T unlinkFirst() {
    INode<T> node = head;
    final T data = node.getData();
    final INode<T> next = node.getNext();
    if (isNull(next)) head = tail = null;
    else head = next;
    node.setPrev(null);
    node.setNext(null);
    node.setData(null);
    --length;
    return data;
  }

  @Override
  protected T unlink(INode<T> node) {
    INode<T> prev = node.getPrev();
    final T data = node.getData();
    final INode<T> next = node.getNext();
    if (nonNull(prev)) {
      prev.setNext(next);
      if (nonNull(next)) next.setPrev(prev);
      else tail = prev;
    } else if (isNull(next)) head = tail = null;
    else {
      head = next;
      head.setPrev(null);
    }
    node.setData(null);
    node.setNext(null);
    node.setPrev(null);
    --length;
    return data;
  }

  @Override
  @SuppressWarnings("nullness:argument.type.incompatible")
  public INode<T> find(T data) {
    requireNonNull(data, DATA_NON_NULL);
    INode<T> node = new DoubleNode<>(data);
    if (head.equals(node)) return head;
    INode<T> startNode = next(head);
    while (!node.equals(startNode) && nonNull(startNode)) startNode = next(startNode);
    return startNode;
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "nullness:argument.type.incompatible"})
  @Override
  public boolean delete(T data) {
    requireNonNull(data, DATA_NON_NULL);
    if (isEmpty()) return false;
    INode<T> node = new DoubleNode<>(data);
    if (head.equals(node)) {
      unlinkFirst();
      return true;
    }
    INode<T> currNode = next(head);
    while (!node.equals(currNode) && nonNull(currNode)) {
      currNode = next(currNode);
    }
    if (isNull(currNode)) return false;
    unlink(currNode);
    return true;
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "nullness:argument.type.incompatible"})
  @Override
  public T deleteAt(int index) {
    checkIndex(index, length);
    if (index == 0) return unlinkFirst();
    return unlink(get(index));
  }

  /**
   * Add element at end.
   *
   * @param data - data to be added to list.
   */
  @Override
  @SuppressWarnings("nullness:argument.type.incompatible")
  public void add(T data) {
    requireNonNull(data, DATA_NON_NULL);
    linkLast(data);
  }

  /**
   * Add element at specified index.
   *
   * @param data - data to be added at index.
   * @param index - index at which element to be added.
   */
  @SuppressWarnings({"PMD.LawOfDemeter", "nullness:argument.type.incompatible"})
  @Override
  public void add(T data, int index) {
    requireNonNull(data, DATA_NON_NULL);
    if (index == 0) {
      addAtFirst(data);
      return;
    }
    if (index == this.length) add(data);
    else if (index < this.length) {
      INode<T> rightNode = get(index);
      linkBefore(data, rightNode);
    } else throw new IndexOutOfBoundsException("Index not available.");
  }

  /**
   * Add element at first node. Set the newly created node as root node.
   *
   * @param data Add data node at beginning.
   */
  @Override
  @SuppressWarnings("nullness:argument.type.incompatible")
  public void addAtFirst(T data) {
    requireNonNull(data, DATA_NON_NULL);
    linkFirst(data);
  }

  @Override
  public INode<T> get(int index) {
    checkIndex(index, this.length);
    if (index == 0) return this.head;
    if (index == this.length - 1) return this.tail;
    int midPoint = this.length >> 1;
    if (index < midPoint) return getFromHead(index);
    else return getFromTail(index);
  }

  private INode<T> getFromHead(int index) {
    int pointer = 0;
    INode<T> pointerNode = this.head;
    while (pointer != index) {
      pointerNode = next(pointerNode);
      ++pointer;
    }
    return pointerNode;
  }

  private INode<T> getFromTail(int index) {
    int pointer = length - 1;
    INode<T> pointerNode = this.tail;
    while (pointer != index) {
      pointerNode = prev(pointerNode);
      --pointer;
    }
    return pointerNode;
  }

  private INode<T> next(INode<T> node) {
    return node.getNext();
  }

  private INode<T> prev(INode<T> node) {
    return node.getPrev();
  }

  @Override
  public int size() {
    return this.length;
  }

  @Override
  public boolean isEmpty() {
    return this.length == 0;
  }

  @Override
  public INode<T> getHead() {
    return head;
  }

  @Override
  public INode<T> getTail() {
    return tail;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    INode<T> nextNode = this.head;
    while (nonNull(nextNode)) {
      sb.append(nextNode);
      nextNode = next(nextNode);
      if (nonNull(nextNode)) sb.append(',');
    }
    sb.append(']');
    return sb.toString();
  }

  @Override
  public Iterator<T> iterator() {
    return getIterator();
  }

  @Override
  public ListIterator<T> getIterator() {
    return new ListIter();
  }

  @Override
  public ListIterator<T> getIteratorFromIndex(int idx) {
    return new ListIter(idx);
  }

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  final class ListIter implements ListIterator<T> {
    private INode<T> lastReturned;
    private INode<T> nextNode;
    private int nextIndex;

    ListIter() {
      this(0);
    }

    @SuppressWarnings("PMD.NullAssignment")
    ListIter(int index) {
      checkIndex(index, length + 1);
      nextNode = (index == length) ? null : get(index);
      nextIndex = index;
    }

    @Override
    public T next() {
      if (!hasNext()) throw new NoSuchElementException();
      lastReturned = nextNode;
      nextNode = nextNode.getNext();
      nextIndex++;
      return lastReturned.getData();
    }

    @Override
    public T previous() {
      if (!hasPrevious()) throw new NoSuchElementException();
      lastReturned = nextNode = isNull(nextNode) ? tail : nextNode.getPrev();
      nextIndex--;
      return lastReturned.getData();
    }

    @SuppressWarnings("PMD.NullAssignment")
    @Override
    public void add(T data) {
      lastReturned = null;
      if (isNull(nextNode)) linkLast(data);
      else linkBefore(data, nextNode);
      nextIndex++;
    }

    @SuppressWarnings("PMD.NullAssignment")
    @Override
    public void remove() {
      if (isNull(lastReturned)) throw new IllegalStateException();
      INode<T> lastNext = lastReturned.getNext();
      unlink(lastReturned);
      if (lastReturned.isSame(nextNode)) nextNode = lastNext;
      else nextIndex--;
      lastReturned = null;
    }

    @Override
    public void set(T data) {
      if (isNull(lastReturned)) throw new IllegalStateException();
      lastReturned.setData(data);
    }

    @Override
    public boolean hasNext() {
      return nextIndex < length;
    }

    @Override
    public boolean hasPrevious() {
      return nextIndex > 0;
    }

    @Override
    public int nextIndex() {
      return nextIndex;
    }

    @Override
    public int previousIndex() {
      return nextIndex - 1;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder(50);
      String lineSeparator = System.lineSeparator();
      sb.append("Last returned = ")
          .append(lastReturned)
          .append(lineSeparator)
          .append("Next node = ")
          .append(nextNode)
          .append(lineSeparator)
          .append("Next index = ")
          .append(nextIndex);
      return sb.toString();
    }
  }
}
