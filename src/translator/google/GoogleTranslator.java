package translator.google;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import translator.base.Request;
import translator.base.Translator;
import translator.google.Languages.Language;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author ekin
 * 
 * Implementation of Google Translator.
 */
public class GoogleTranslator implements Translator {

  @Override
  public String translate(final String text, Language source, Language target) {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(-1);
    }
    String response = Request.makePostRequest(null, Query.create(source, target, text));
    JsonElement jelement = new JsonParser().parse(response);
    JsonObject  jobject = jelement.getAsJsonObject();
    jobject = jobject.getAsJsonObject("data");
    JsonArray jarray = jobject.getAsJsonArray("translations");

    if (jarray.size() != 1) {
      error("Nothing is returned.", text, target, response);
    }
    jobject = jarray.get(0).getAsJsonObject();
    String translatedText = jobject.get("translatedText").toString().replaceAll("\"", "");
    return Query.escapeHtml(translatedText);
  }
  
  @Override
  public List<String> translate(String text, Language source, List<Language> languages) {
    List<String> translatedTexts = new ArrayList<String>();
    String translated = text;
    for (Language target : languages) {
      translated = translate(translated, source, target);
      translatedTexts.add(translated);
      source = target;
    }
    return translatedTexts;
  }
  
  @Override
  public String translateBack(final String text, Language source) {
    return translate(text, source, Language.create("en"));
  }
  

  @Override
  public List<String> translateBack(List<String> texts, List<Language> sources) {
    if (texts.size() != sources.size()) {
      throw new IllegalArgumentException("Size of texts and sources must be equal @translateBack");
    }
    List<String> translatedBacks = new ArrayList<String>();
    for (int i = 0; i < texts.size(); i++) {
      translatedBacks.add(translateBack(texts.get(i), sources.get(i)));
    }
    return translatedBacks;
  }
  
  /** Prints all the information and exit in case of an error. */
  private void error(String message, String text, Language target, String response) {
    System.out.println("Should not see this");
    System.out.println("message: " + message);
    System.out.println("text: "  + text);
    System.out.println("target_language: "  + target);
    System.out.println("response: "  + response);
    System.exit(1);
  }
}
