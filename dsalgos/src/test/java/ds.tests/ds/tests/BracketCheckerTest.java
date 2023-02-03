package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.BracketChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

class BracketCheckerTest {

  private static final String INVALID_RES_EXPECTED = "Invalid result expected.";

  void testEmptyString() {
    String empty = "";
    BracketChecker r = new BracketChecker(empty);
    assertTrue(r.check(), "Empty string works.");
  }

  void testNoBrackets() {
    String palindrome = "MALAYALAM";
    BracketChecker r = new BracketChecker(palindrome);
    assertTrue(r.check(), "No brackets works.");
  }

  void testSingleLetter() {
    String letter = "M";
    BracketChecker r = new BracketChecker(letter);
    assertTrue(r.check(), "Letter works.");
  }

  void testEmptyParentheses() {
    String input = "()";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Empty parentheses valid.");
  }

  void testEmptyCurlyBraces() {
    String input = "{}";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Empty parentheses valid.");
  }

  void testEmptySquareBrackets() {
    String input = "[]";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Empty parentheses valid.");
  }

  void testValidInput() {
    String input = "{call(a[i]);}";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Valid result expected.");
  }

  void testValidInput2() {
    String input = "[call{a(i)};]";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Valid result expected.");
  }

  void testValidInput3() {
    String input = "(call[a{i};])";
    BracketChecker r = new BracketChecker(input);
    assertTrue(r.check(), "Valid result expected.");
  }

  void testInvalidInput() {
    String input = "{call(a[i]);]";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testInvalidInput2() {
    String input = "[call{a(i)];}";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testInvalidInput3() {
    String input = "[call[a{i};])";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testInvalidInput4() {
    String input = "[call{a{i};])";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testInvalidInput5() {
    String input = "{calla[i]);]";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testInvalidInput6() {
    String input = "[call{a(i];}";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }

  void testNoLeftBrackets() {
    String input = ")]}";
    BracketChecker r = new BracketChecker(input);
    assertFalse(r.check(), INVALID_RES_EXPECTED);
  }
}
