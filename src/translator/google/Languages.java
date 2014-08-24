package translator.google;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import translator.base.Request;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author ekin
 *
 * Class to provide interface for language selection. Followings will be part of API
 * <li>GetAllLanguages:Set returns all supported languages
 * <li>GetRandomLanguages(count):List returns #count random languages.
 */
public class Languages {

  /** Operation to get all supported languages */
  private static final String OPERATION_SUPPORTED_LANGUAGES = "languages";
  
  /** File where all languages are stored. */
  private final static String ALL_LANGUAGES_FILE = "files/allLanguages.json";
  
  /** Returns Set of all languages. */
  public static Set<Language> getAllLanguages() {
    String allLanguages = Request.makeGetRequest(OPERATION_SUPPORTED_LANGUAGES, Query.create());
    return getAllLanguages(allLanguages);
  }
  
  /**
   * @param count denotes number of languages to be returned
   * @return List of randomly selected languages
   */
  public static List<Language> getRandomLanguages(int count) {
    return getRandomLanguages(count, Languages.getAllLanguages());
  }
  
  /**
   * @param count denotes number of languages to be returned
   * @return List of randomly selected languages
   */
  public static List<Language> getRandomLanguagesFromFile(int count) {
    return getRandomLanguages(count, Languages.getAllLanguagesFromFile());
  }
  
  /** Function to be used in testing. Does not require a REST call. */
  @VisibleForTesting
  protected static Set<Language> getAllLanguages(String allLanguages) {
    JsonElement jelement = new JsonParser().parse(allLanguages);
    JsonObject  jobject = jelement.getAsJsonObject();
    jobject = jobject.getAsJsonObject("data");
    JsonArray jarray = jobject.getAsJsonArray("languages");

    Set<Language> output = new HashSet<Language>();
    for (int i = 0; i < jarray.size(); i++) {
      jobject = jarray.get(i).getAsJsonObject();
      String code = jobject.get("language").toString();
      output.add(Language.create(code.replaceAll("\"", "")));
    }
    return output;
  }
  
  /**
   * Returned list will only contain unique languages.
   * Function to be used in testing. Does not require a REST call.
   */
  @VisibleForTesting
  protected static List<Language> getRandomLanguages(int count, Set<Language> languages) {
    List<Language> list = new ArrayList<Language>();
    List<Language> allLanguages = new ArrayList<Language>(languages);
    if (count > allLanguages.size()) {
      throw new IllegalArgumentException("Do not have that many languages");
    }
    
    // Select random languages
    for (int i = 0; i < count; i++) {
      int size = allLanguages.size();
      int item = new Random().nextInt(size);
      list.add(allLanguages.remove(item));
    }
    return list;
  }
  
  @VisibleForTesting
  protected static Set<Language> getAllLanguagesFromFile() {
    String allLanguages = "";
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(ALL_LANGUAGES_FILE));
      String line = null;
      while ((line = reader.readLine()) != null) {
        allLanguages += line + '\n';
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return getAllLanguages(allLanguages);
  }
  
  /** Language class to denote <LanguageName, LanguageCode>. */
  public static class Language {
    
    public final String languageName;
    public final String languageCode;
    
    public static Language create(String languageCode) {
      return new Language(languageCode);
    }
    
    public Language(Language other) {
      this(other.languageName, other.languageCode);
    }
    
    private Language(String languageCode) {
      this("NameIsNotSupported", languageCode);
    }
    
    private Language(String languageName, String languageCode) {
      this.languageName = languageName;
      this.languageCode = languageCode;
    }
    
    @Override
    public boolean equals(Object other) {
      if (other instanceof Language) {
        Language lang = (Language) other;
        return this.languageCode.equals(lang.languageCode)
            && this.languageName.equals(lang.languageName);
      }
      return false;
    }
    
    @Override
    public int hashCode() {
        return languageCode.hashCode();
    }
    
    @Override
    public String toString() {
      return languageCode;
    }
  }
}
