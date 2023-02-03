package ds;

import static java.util.Objects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
  private static final int TWO = 2;
  private char[] arrChar;
  private final List<String> anagrams;

  @SuppressWarnings("PMD.LawOfDemeter")
  public AnagramGenerator(String input) {
    requireNonNull(input, "Input cannot be null!");
    if (!input.matches("^[A-Za-z]*$"))
      throw new IllegalArgumentException("String should contain only alphabetic characters.");
    arrChar = input.toCharArray();
    anagrams = new ArrayList<>();
  }

  public void generate() {
    generate(arrChar.length);
  }

  public void generate(int size) {
    if (size == 1) {
      addWordToList();
      return;
    }
    for (int j = 0; j < size; j++) {
      generate(size - 1);
      if (size == TWO) addWordToList();
      rotate(size);
    }
  }

  private void rotate(int newSize) {
    int size = arrChar.length;
    int position = size - newSize;
    char temp = arrChar[position];
    int j = position + 1;
    for (; j < size; j++) arrChar[j - 1] = arrChar[j];
    arrChar[j - 1] = temp;
  }

  private void addWordToList() {
    String word = new String(arrChar);
    if (!anagrams.contains(word)) anagrams.add(word);
  }

  public List<String> getAnagrams() {
    return anagrams;
  }

  public List<String> getValidAnagrams() throws IOException {
    List<String> validAnagrams = new ArrayList<>();
    for (String anagram : anagrams) {
      if (DictionaryLookup.isDictionaryWord(anagram)) validAnagrams.add(anagram);
    }
    return validAnagrams;
  }
}
