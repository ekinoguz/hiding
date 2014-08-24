package translator.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import translator.base.Parameters;
import translator.google.Languages.Language;

import com.google.common.collect.ImmutableList;

public class Query {
  
  /** Returns a base query only with API key. */
  public static String create() {
    return getFormattedParameters(ImmutableList.<String>of());
  }
  
  /**
   * Creates a query with given parameters.
   * @param sourceLanguage
   * @param targetLanguage
   * @param text
   * @return query 
   */
  public static String create(Language sourceLanguage, Language targetLanguage, String text) {
//    if (text.length() > 5000) {
//      throw new IllegalArgumentException("IllegalArgumentException for sourceLanguage:" +
//          sourceLanguage + ", targetLanguage:" + targetLanguage + ", text:" + text + ", " +
//          		"text can be at most 5000 characters");
//    }
    ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();
    builder.add("source=" + sourceLanguage.languageCode);
    builder.add("target=" + targetLanguage.languageCode);
    try {
      String encodedText = URLEncoder.encode(text, "UTF-8");
      builder.add("q=" + encodedText);
    } catch (UnsupportedEncodingException e) {
      System.out.println("Exception while creating query for: " + targetLanguage.languageCode +
          ", text:" + text + ", exception: " + e.toString());
    }
    return getFormattedParameters(builder.build());
  }
  
  /** Escapes html codes from returned translation. */
  public static String escapeHtml(String text) {
    return StringEscapeUtils.unescapeHtml4(text);
  }
  
  /**
   * @param params Parameters to be formatted.
   * @return String with "&" appended by each param from params
   */
  private static String getFormattedParameters(List<String> params) {
    // Get the API Key
    StringBuilder builder = new StringBuilder();
    Parameters parameters = Parameters.initialize();
    builder.append(parameters.apiKey);
    
    if (params == null) {
      return builder.toString();
    }
    for (String param : params) {
      builder.append("&" + param);
    }
    return builder.toString();
  }
}
