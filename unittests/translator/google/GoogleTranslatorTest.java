package translator.google;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import translator.base.Translator;
import translator.google.Languages.Language;

import com.google.common.collect.ImmutableList;

@RunWith(JUnit4.class)
public class GoogleTranslatorTest {

  private Translator translator;
  private Language es;
  private Language tr;
  private Language en;
  private Language ar;
  private String text;
  private String translated;
  
  @Before
  public void setUp() throws Exception {
    translator = new GoogleTranslator();
    es = Language.create("es");
    tr = Language.create("tr");
    en = Language.create("en");
    ar = Language.create("ar");
  }

  @Test
  public void testTranslate_Single() {
    text = "Hello world";
    translated = translator.translate(text, en, es);
    assertEquals("Hola mundo", translated);
  }
  
//  @Test
  public void testTranslate_DoubleBack() {
    text = "Hello world";
    translated = translator.translate(text, en, tr);
    translated = translator.translate(translated, tr, es);
    assertEquals("Hola mundo", translated);
    translated = translator.translate(translated, es, en);
    assertEquals(text.toLowerCase(), translated.toLowerCase());
  }
  
//  @Test
  public void testTranslate_Arabic() {
    text = "مرحبا العالم";
    translated = translator.translate(text, ar, tr);
    translated = translator.translate(translated, tr, en);
    assertEquals("Hello world", translated);
  }
  
//  @Test
  public void testTranslateBack() {
    text = "Hola mundo";
    assertEquals("Hello World", translator.translateBack(text, es));
  }
  
  @Test
  public void testTranslate_MultipleLanguages() {
    text = "Hello world";
    List<String> translatedTexts = translator.translate(text, en,
        ImmutableList.<Language>of(es, tr, ar));
    assertEquals(3, translatedTexts.size());
    assertEquals("Hola mundo", translatedTexts.get(0));
    assertEquals("Merhaba Dünya", translatedTexts.get(1));
    assertEquals("مرحبا العالم", translatedTexts.get(2));
  }
  
  @Test
  public void testTranslateBack_MultipleLanguages() {
    text = "hello world";
    List<String> originals = ImmutableList.<String>of("Hola mundo", "Merhaba Dünya",
        "مرحبا العالم");
    List<String> translatedBacks = translator.translateBack(originals,
        ImmutableList.<Language>of(es, tr, ar));
    assertEquals(3, translatedBacks.size());
    assertEquals(text, translatedBacks.get(0).toLowerCase());
    assertEquals(text, translatedBacks.get(1).toLowerCase());
    assertEquals(text, translatedBacks.get(2).toLowerCase());
  }
}
