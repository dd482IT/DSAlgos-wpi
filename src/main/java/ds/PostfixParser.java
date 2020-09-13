package ds;

import java.util.EmptyStackException;
import java.util.Stack;

public class PostfixParser {

  private final Stack<Integer> theStack;
  private final String input;

  public PostfixParser(String s) {
    input = s;
    theStack = new Stack<>();
  }

  @SuppressWarnings("PMD.SystemPrintln")
  private void displayStack(String s) {
    System.out.println(s + " : ");
    System.out.println(theStack);
  }

  public int parse() {
    char ch;
    int j;
    int interAns = 0;
    for (j = 0; j < input.length(); j++) {
      ch = input.charAt(j);
      displayStack(ch + " ");
      if (ch >= '0' && ch <= '9') theStack.push((int) (ch - '0'));
      else if (theStack.isEmpty()) continue;
      else {
        int num2 = theStack.pop();
        int num1 = theStack.pop();
        switch (ch) {
          case '+':
            interAns = num1 + num2;
            break;
          case '-':
            interAns = num1 - num2;
            break;
          case '*':
            interAns = num1 * num2;
            break;
          case '/':
            interAns = num1 / num2;
            break;
          default:
            interAns = 0;
            break;
        }
        theStack.push(interAns);
      }
    }
    if (!theStack.isEmpty()) interAns = theStack.pop();
    return interAns;
  }
}
