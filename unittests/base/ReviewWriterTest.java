package base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReviewWriterTest {

  @Test
  public void test_writeTranslated() {
    String text = "Test review2";
    ReviewWriter.writeTranslatedReview(text, 0, 0, 40, 1);
    assertEquals(text, ReviewReader.readTranslatedReview(0, 0, 40, 1));
  }

  @Test
  public void test_writeTranslatedBackReview() {
    String text = "Test review for write_TranslatedBackReview";
    ReviewWriter.writeTranslatedBackReview(text, 0, 0, 39, 2);
    assertEquals(text, ReviewReader.readTranslatedBackReview(0, 0, 39, 2));
  }
}