package base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;

@RunWith(JUnit4.class)
public class ReviewReaderTest {

  @Test
  public void test_GetFileNames() {
    ImmutableList<String> list = Utils.getAuthors40();
    assertEquals(Parameters.AUTHOR_SIZE, list.size());
    assertEquals("processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-40", list.get(0));
    assertEquals("processed-Xj0O2l0bp633ebmG468aZw-clear-rct-only-40", list.get(22));
    assertEquals("processed-vx74uzB1PEcla7csBxUyUQ-clear-rct-only-40", list.get(39));
  }
  
  @Test
  public void test_ReviewReader() {
    assertEquals("Oooo Cheese. Great little neighborhood market with a wide selection of wines, " +
    		"cheeses and delicious to-go deli sandwiches. I was impressed by the Happy Thanksgiving " +
    		"(brie, turkey and cranberry on a dutch crunch roll). Don't forget to get your sandwich " +
    		"toasted! soooo yummy.", ReviewReader.readInputReview(0, 1));
    assertTrue(ReviewReader.readInputReview(0, 5).startsWith("The best part of the Pescado con Pipian"));
    assertTrue(ReviewReader.readInputReview(0, 25).startsWith("5 stars for happy hour! How can"));
    assertTrue(ReviewReader.readInputReview(0, 40).startsWith("Here is the info on how to get a "));
  }
  
  @Test
  public void test_GetSentences() {
    String review = ReviewReader.readInputReview(0, 1);
    assertEquals(5, ReviewReader.getSentences(review).size());
    review = ReviewReader.readInputReview(0, 8);
    assertEquals(4, ReviewReader.getSentences(review).size());
    review = ReviewReader.readInputReview(0, 9);
    assertEquals(2, ReviewReader.getSentences(review).size());
  }
  
  @Test
  public void test_ReviewReaderException() {
    try {
      ReviewReader.readInputReview(-1, 0);
      fail();
    } catch (Exception e) {
      // expected
    }
    try {
      ReviewReader.readInputReview(0, 41);
      fail();
    } catch (Exception e) {
      // expected
    }
    try {
      ReviewReader.readInputReview(40, 4);
      fail();
    } catch (Exception e) {
      // expected
    }
  }
  
  @Test
  public void test_ReadRewrittenReview() {
    String review = ReviewReader.readRewrittenReview(0, 1);
    assertTrue(review.contains("Oh, cheese!"));
    review = ReviewReader.readRewrittenReview(21, 1);
    assertTrue(review.contains("At night"));
    review = ReviewReader.readRewrittenReview(21, 3);
    assertTrue(review.contains("I went to the New Belgium Hub."));
    review = ReviewReader.readRewrittenReview(21, 5);
    assertTrue(review.contains("I am in complete agreement with Tysie C"));
  }
}
