package ds;

// highArray.java
// demonstrates array class with high-level interface
// to run this program: C>java HighArrayApp
////////////////////////////////////////////////////////////////
public class HighArray {
  private long[] a;
  // ref to array a
  private int nElems;
  // number of data items

  // constructor
  // -----------------------------------------------------------
  public HighArray(int max) {
    a = new long[max];
    // create the array
    nElems = 0;
    // no items yet
  }

  // -----------------------------------------------------------
  public boolean find(long searchKey) {
    // find specified value
    int j;
    for (j = 0; j < nElems; j++) if (a[j] == searchKey) return true;
    return false;
  }

  // put element into array
  // -----------------------------------------------------------
  public void insert(long value) {
    a[nElems] = value;
    // insert it
    nElems++;
    // increment size
  }

  // -----------------------------------------------------------
  public boolean delete(long value) {
    int j;
    for (j = 0; j < nElems; j++)
      // look for it
      if (value == a[j]) break;
    if (j == nElems)
      // can’t find it
      return false;
    else {
      // found it
      for (int k = j; k < nElems; k++)
        // move higher ones down
        a[k] = a[k + 1];
      nElems--;
      // decrement size
      return true;
    }
  }

  // displays array contents
  // -----------------------------------------------------------
  @SuppressWarnings({"PMD.SystemPrintln", "PMD.LawOfDemeter"})
  public void display() {
    for (int j = 0; j < nElems; j++) System.out.print(a[j] + " ");
    System.out.println("");
  }
}
