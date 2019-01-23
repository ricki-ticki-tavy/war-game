import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Test1 {
  Map<String, Integer> stat = new HashMap();

  @Test
  public void test1() {
    File inf = new File("/home/dsporykhin/projects/test.txt");
    try {
      BufferedReader isr = Files.newBufferedReader(inf.toPath());
      isr.lines().forEach(line -> {
        for (char symb : line.toCharArray()){

        }
      });
    } catch (IOException ie) {

    }
  }

  public class CharInfo implements Comparable<CharInfo>{
    char symb;
    int stat;

    @Override
    public int compareTo(CharInfo o) {
      return 0;
    }
  }
}
