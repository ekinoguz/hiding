package translator.google;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import translator.google.Languages.Language;

@RunWith(JUnit4.class)
public class LanguagesTest {
  
  private final static int LANGUAGE_SIZE = 54;

  @Test
  public void test_GetAllLanguages() {
    Set<Language> languages = Languages.getAllLanguagesFromFile();
    assertEquals(LANGUAGE_SIZE, languages.size());
    assertTrue(languages.contains(Language.create("af")));
    assertTrue(languages.contains(Language.create("de")));
    assertTrue(languages.contains(Language.create("en")));
    assertTrue(languages.contains(Language.create("tr")));
  }
  
  /** This tests getting languages from google. */
  //@Test
  public void test_GetAllLanguagesWithRequest() {
    Set<Language> languages = Languages.getAllLanguages();
    assertEquals(LANGUAGE_SIZE, languages.size());
    assertTrue(languages.contains(Language.create("af")));
    assertTrue(languages.contains(Language.create("de")));
    assertTrue(languages.contains(Language.create("en")));
    assertTrue(languages.contains(Language.create("tr")));
  }
  
  @Test
  public void testGetRandomLanguages_Empty() {
    List<Language> languages = Languages.getRandomLanguages(0,
        Languages.getAllLanguagesFromFile());
    assertTrue(languages.isEmpty());
  }
  
  @Test
  public void testGetRandomLanguages_Five() {
    List<Language> languages = Languages.getRandomLanguages(5,
        Languages.getAllLanguagesFromFile());
    assertEquals(5, languages.size());
  }
  
  @Test
  public void testGetRandomLanguages_Max() {
    List<Language> languages = Languages.getRandomLanguages(LANGUAGE_SIZE,
        Languages.getAllLanguagesFromFile());
    assertEquals(LANGUAGE_SIZE, languages.size());
    
    // Test languages equals all languages
    for (Language lang : Languages.getAllLanguagesFromFile()) {
      languages.remove(lang);
    }
    assertTrue(languages.isEmpty());
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testGetRandomLanguages_Oversize() {
    Languages.getRandomLanguages(LANGUAGE_SIZE+1, Languages.getAllLanguagesFromFile());
  }
}
