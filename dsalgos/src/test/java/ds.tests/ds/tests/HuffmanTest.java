package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.HuffmanCompressor;
import ds.HuffmanDecompressor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class HuffmanTest {

  private static final String ABRA_FILE = "abra.txt";
  private static final String TINY_TALES_FILE = "tinytales.txt";
  private static final String TALES_FILE = "tale.txt";
  private static final String MED_TALE_FILE = "medtale.txt";
  private static final String MSG = "Decompressed output must be same as input to compressor.";

  private final String abra;
  private final String tinyTales;
  private final String tales;
  private final String medTale;

  public HuffmanTest() throws IOException {
    abra = readFile(ABRA_FILE);
    tinyTales = readFile(TINY_TALES_FILE);
    tales = readFile(TALES_FILE);
    medTale = readFile(MED_TALE_FILE);
  }

  private String readFile(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
  }

  public void testAbraFile() throws IOException {
    HuffmanCompressor hfc = new HuffmanCompressor(new File(ABRA_FILE), new File("abra.huf"));
    hfc.compress();
    HuffmanDecompressor hdc = new HuffmanDecompressor(new File("abra.huf"), new File("abra.dec"));
    hdc.expand();
    String expanded = readFile("abra.dec");
    assertEquals(abra, expanded, MSG);
  }

  public void testTalesFile() throws IOException {
    HuffmanCompressor hfc = new HuffmanCompressor(new File(TALES_FILE), new File("tales.huf"));
    hfc.compress();
    HuffmanDecompressor hdc = new HuffmanDecompressor(new File("tales.huf"), new File("tales.dec"));
    hdc.expand();
    String expanded = readFile("tales.dec");
    assertEquals(tales, expanded, MSG);
  }

  public void testTinyTalesFile() throws IOException {
    HuffmanCompressor hfc =
        new HuffmanCompressor(new File(TINY_TALES_FILE), new File("tinytales.huf"));
    hfc.compress();
    HuffmanDecompressor hdc =
        new HuffmanDecompressor(new File("tinytales.huf"), new File("tinytales.dec"));
    hdc.expand();
    String expanded = readFile("tinytales.dec");
    assertEquals(tinyTales, expanded, MSG);
  }

  public void testMedTaleFile() throws IOException {
    HuffmanCompressor hfc = new HuffmanCompressor(new File(MED_TALE_FILE), new File("medtale.huf"));
    hfc.compress();
    HuffmanDecompressor hdc =
        new HuffmanDecompressor(new File("medtale.huf"), new File("medtale.dec"));
    hdc.expand();
    String expanded = readFile("medtale.dec");
    assertEquals(medTale, expanded, MSG);
  }
}
