package translator.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import com.google.common.base.Preconditions;

/**
 * @author ekin
 *
 * Get the parameters of translation API.
 */
public class Parameters {
  
  /** Filename that will be used to get parameters. */
  private static final String FILENAME = ".properties";
  
  /** Public singleton instance of parameters. */
  public static Parameters parameters = null;
  
  /** Public parameters to be used later on. */
  public final String apiKey;
  
  /** Tags to be used to find desired properties from {@code FILENAME}. */
  private static final String TAG_APIKEY = "API_KEY";
  
  /** Returns a Singleton version of Parameters. */
  public static Parameters initialize() {
    if (parameters == null) {
      parameters = new Parameters();
    }
    return parameters;
  }
  
  /** Initializes properties. */
  private Parameters() {
    // Reads the API key from file.
    apiKey = get(TAG_APIKEY);
    Preconditions.checkNotNull(apiKey);
  }
  
  /** Gets information related to the line beginning with {@code tag}. */
  private String get(final String tag) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
      String line = null;
      while ((line = reader.readLine()) != null && !line.startsWith(tag))
        ;
      reader.close();
      StringTokenizer tk = new StringTokenizer(line, "'");
      tk.nextToken(); // eat the TAG
      return tk.nextToken();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}