package base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilsTest {

  @Test
  public void test_GetFileNames() {
    List<String> fileNames = Utils.getAuthors40();
    assertEquals(40, fileNames.size());
  }
  
  @Test
  public void test_NumberOfLines() {
    String s = "";
    assertEquals(0, Utils.getNumberOfLines(s));
    s = "a";
    assertEquals(1, Utils.getNumberOfLines(s));
    s = "a\n";
    assertEquals(2, Utils.getNumberOfLines(s));
    s = "a\na";
    assertEquals(2, Utils.getNumberOfLines(s));
  }
  
  @Test
  public void test_GetRandomARs() {
    Map<Integer, Integer> randoms = Utils.getRandomARs(40);
    assertEquals(40, randoms.size());
    for (Integer key : randoms.keySet()) {
      assertTrue(key >= 0);
      assertTrue(key <= 39);
      assertTrue(randoms.get(key) >= 1);
      assertTrue(randoms.get(key) <= 5);
    }
  }
}
