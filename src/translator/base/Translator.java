package translator.base;

import java.util.List;

import translator.google.Languages.Language;

/**
 * @author ekin
 * 
 * Translator interface that will be implemented by each translator.
 */
public interface Translator {

  /**
   * Translate text from source language to target language
   * @param text Text that will be translated
   * @param source Source language code
   * @param target Target language code
   * @return Translated text
   */
  String translate(final String text, Language source, Language target);
  
  /**
   * Translate given text into given languages in consecutive and return all translations
   * @param text
   * @param source
   * @param languages
   * @return
   */
  List<String> translate(final String text, Language source, List<Language> languages);
  
  /**
   * Translate given text to English.
   * @param text Text that will be translated
   * @param source Source language of the text
   * @return translated text to English
   */
  String translateBack(final String text, Language source);
  
  
  /**
   * Translate given texts back into English.
   * @param texts that will be translated back
   * @param sources source language of each text
   * @return
   */
  List<String> translateBack(List<String> texts, List<Language> sources);
}
