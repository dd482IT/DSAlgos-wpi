package ds;

import java.util.Objects;

@SuppressWarnings({"PMD.DataClass", "nullness"})
public class DoubleNode<T> implements INode<T> {
  private T data;
  private INode<T> prev;
  private INode<T> next;

  public DoubleNode(T data) {
    Objects.requireNonNull(data, "Data cannot be null.");
    this.data = data;
  }

  public DoubleNode(T data, INode<T> next) {
    this(data);
    this.next = next;
  }

  public DoubleNode(INode<T> prev, T data, INode<T> next) {
    this(data, next);
    this.prev = prev;
  }

  @Override
  public INode<T> getPrev() {
    return prev;
  }

  @Override
  public INode<T> getNext() {
    return next;
  }

  @Override
  public void setNext(INode<T> node) {
    this.next = node;
  }

  @Override
  public void setPrev(INode<T> node) {
    this.prev = node;
  }

  @Override
  public T getData() {
    return data;
  }

  @Override
  public void setData(T data) {
    this.data = data;
  }

  @Override
  @SuppressWarnings("fenum:argument.type.incompatible")
  public String toString() {
    return Objects.toString(data);
  }

  @Override
  @SuppressWarnings("all")
  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof DoubleNode)) return false;
    final DoubleNode<?> other = (DoubleNode<?>) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$data = this.getData();
    final Object other$data = other.getData();
    if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
    return true;
  }

  @SuppressWarnings("all")
  protected boolean canEqual(final Object other) {
    return other instanceof DoubleNode;
  }

  @Override
  @SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $data = this.getData();
    result = result * PRIME + ($data == null ? 43 : $data.hashCode());
    return result;
  }
}
