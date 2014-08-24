package translator.google;

import translator.base.Translator;
import translator.google.Languages.Language;

public class HumanReadableTest {

  public static void main(String[] args) {
    
    String text = "Stop #2: Dinner I've always";
    Language lang1 = Language.create("mt");
    Language lang2 = Language.create("en");
    Translator translator = new GoogleTranslator();
    String translated = translator.translate(text, lang2, lang1);
//      String back = translator.translate(text, ImmutableList.<Language>of(lang2));
    String back = translator.translateBack(translated, lang1);
    System.out.println(text);
    System.out.println(translated);
    System.out.println(back);

  }

}
