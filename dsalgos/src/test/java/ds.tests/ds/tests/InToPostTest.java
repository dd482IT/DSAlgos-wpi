package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.InToPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class InToPostTest {

  private static final String POSTFIX_NOTATION_EXPECTED = "Postfix notation must be as expected.";

  void testEmptyString() {
    String empty = "";
    InToPost r = new InToPost(empty);
    assertEquals("", r.translate(), "Empty string expected.");
  }

  void testSingleLetter() {
    String letter = "M";
    InToPost r = new InToPost(letter);
    assertEquals(letter, r.translate(), "Letter expected.");
  }

  void testInfix() {
    String infix = "A*(B+C)-D/(E+F)";
    InToPost r = new InToPost(infix);
    assertEquals("ABC+*DEF+/-", r.translate(), POSTFIX_NOTATION_EXPECTED);
  }

  void testSimpleInfix() {
    String infix = "2+3*4";
    InToPost r = new InToPost(infix);
    assertEquals("234*+", r.translate(), POSTFIX_NOTATION_EXPECTED);
  }

  void testSimplerInfix() {
    String infix = "5+7";
    InToPost r = new InToPost(infix);
    assertEquals("57+", r.translate(), POSTFIX_NOTATION_EXPECTED);
  }

  void testOnlyParentheses() {
    String infix = "()";
    InToPost r = new InToPost(infix);
    assertEquals("", r.translate(), "Empty string expected.");
  }

  void testOnlyOperators() {
    String infix = "*+-/";
    InToPost r = new InToPost(infix);
    assertEquals("*+/-", r.translate(), POSTFIX_NOTATION_EXPECTED);
  }

  void testOnlyOperatorsAgain() {
    String infix = "+-*/";
    InToPost r = new InToPost(infix);
    assertEquals("+*/-", r.translate(), POSTFIX_NOTATION_EXPECTED);
  }

  void testOnlyRightParenthesis() {
    String infix = ")";
    InToPost r = new InToPost(infix);
    assertEquals("", r.translate(), "Empty string expected.");
  }
}
