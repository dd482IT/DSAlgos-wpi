package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import ds.BinaryInputStream;
import ds.BinaryOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@SuppressWarnings("PMD.LawOfDemeter")
class BinaryStreamsTest {
  private static final String ABRA_FILE = "abra.txt";
  private static final String TEST_FILE = "test";
  private static final String SUFFIX = ".out";
  private static final int TEN = 10;

  char[] chars = new char[TEN];
  short[] shorts = new short[TEN];
  int[] ints = new int[TEN];
  long[] longs = new long[TEN];
  float[] floats = new float[TEN];
  byte[] bytes = new byte[TEN];
  double[] doubles = new double[TEN];
  boolean[] bools = new boolean[TEN];

  public void init() {
    Random random = new Random();
    for (int i = 0; i < TEN; i++) {
      bools[i] = random.nextBoolean();
      bytes[i] = (byte) random.nextInt();
      chars[i] = (char) random.nextInt(256);
      shorts[i] = (short) random.nextInt();
      ints[i] = random.nextInt();
      longs[i] = random.nextLong();
      floats[i] = random.nextFloat();
      doubles[i] = random.nextDouble();
    }
    BinaryOutputStream out = new BinaryOutputStream(TEST_FILE + SUFFIX);
    for (int i = 0; i < TEN; i++) {
      out.write(bools[i]);
      out.write(bytes[i]);
      out.write(chars[i]);
      out.write(shorts[i]);
      out.write(ints[i]);
      out.write(longs[i]);
      out.write(floats[i]);
      out.write(doubles[i]);
    }
    out.write('R', 16);
    out.write(31, 5);
    out.write("Random");
    out.flush();
  }

  public void testAbraFile() throws IOException {
    BinaryInputStream in = new BinaryInputStream(ABRA_FILE);
    BinaryOutputStream out = new BinaryOutputStream(ABRA_FILE + SUFFIX);

    // read one 8-bit char at a time
    while (!in.isEmpty()) {
      char c = in.readChar();
      out.write(c);
    }
    out.flush();

    assertEquals(getFileSize(ABRA_FILE), getFileSize(ABRA_FILE + SUFFIX), "Files must be equal.");
  }

  @SuppressWarnings("PMD")
  public void testBinary() throws IOException {
    BinaryInputStream in = new BinaryInputStream(TEST_FILE + SUFFIX);

    char[] newChars = new char[TEN];
    short[] newShorts = new short[TEN];
    int[] newInts = new int[TEN];
    long[] newLongs = new long[TEN];
    float[] newFloats = new float[TEN];
    byte[] newBytes = new byte[TEN];
    double[] newDoubles = new double[TEN];
    boolean[] newBools = new boolean[TEN];

    for (int i = 0; i < TEN; i++) {
      newBools[i] = in.readBoolean();
      newBytes[i] = in.readByte();
      newChars[i] = in.readChar();
      newShorts[i] = in.readShort();
      newInts[i] = in.readInt();
      newLongs[i] = in.readLong();
      newFloats[i] = in.readFloat();
      newDoubles[i] = in.readDouble();
    }
    final char ch = in.readChar(16);
    final int val = in.readInt(5);
    final String string = in.readString();

    assertArrayEquals(bools, newBools, "Booleans must be equal.");
    assertArrayEquals(bytes, newBytes, "Bytes must be equal.");
    assertArrayEquals(chars, newChars, "Chars must be equal.");
    assertArrayEquals(shorts, newShorts, "Shorts must be equal.");
    assertArrayEquals(ints, newInts, "Integers must be equal.");
    assertArrayEquals(longs, newLongs, "Longs must be equal.");
    assertArrayEquals(floats, newFloats, "Floats must be equal.");
    assertArrayEquals(doubles, newDoubles, "Doubles must be equal.");
    assertEquals('R', ch, "Value must be R");
    assertEquals(31, val, "Value must be 31");
    assertEquals("Random", string, "String must be random.");
  }

  private long getFileSize(String file) throws IOException {
    Path imageFilePath = Paths.get(file);
    FileChannel imageFileChannel = FileChannel.open(imageFilePath);
    return imageFileChannel.size();
  }
}
