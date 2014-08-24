package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Utils {
  
  /** List of files which will be ignored. */
  private static ImmutableSet<String> IGNORE = ImmutableSet.<String>of(".DS_Store");
  
  /**
   * @param filePath
   * @param data
   * @return
   */
  public static boolean saveFile(String filePath, String data){
    return writeToFile(filePath, data, false);
  }
  
  /**
   * @param filePath
   * @param data
   * @return
   */
  public static boolean appendToFile(String filePath, String data){
    return writeToFile(filePath, data, true);
  }
  
  /**
   * @param filePath
   * @param data
   * @param append
   * @return
   */
  private static boolean writeToFile(String filePath, String data, boolean append){
    BufferedWriter writer;
    try {
//      File theDir = new File(filePath);
//      theDir = new File(theDir.getParent());
//      // if the directory does not exist, create it
//      if (!theDir.exists())
//        theDir.mkdirs();
      writer = new BufferedWriter(new FileWriter(filePath, append));
      writer.write(data);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  public static String readFileLine(String inputPath, int line) {
    String output="";
    try {
      BufferedReader br = new BufferedReader(new FileReader(inputPath));
      while ((line--) != 0) {
        output = br.readLine();
      }
      br.close();
    } catch (Exception e) {
      System.out.println("Exception in readFileLine(): " + e.toString());
    }
    return output;
  }
  
  public static List<String> readFile(String inputPath) {
    List<String> output = new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(inputPath));
      String line;
      while ((line = br.readLine()) != null) {
        output.add(line);
      }
      br.close();
    } catch (Exception e) {
      System.out.println("Exception in readFileLine(): " + e.toString());
    }
    return output;
  }
  
  /** Returns number of words in {@code text}. */
  public static int getNumberOfWords(String text) {
    return text.split("\\s").length; 
  }

  /** Returns sorted list of all filenames from input folder. */
  public static ImmutableList<String> getAuthors40() {
    ArrayList<String> files = new ArrayList<String>();
    try {
      BufferedReader reader = new BufferedReader(
          new FileReader(Parameters.UBUNTU_MAIN + "information/authors_clear_rct_only_40"));
      String line = null;
      while ((line = reader.readLine()) != null) {
        files.add(line);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println("GetAuthors40() exception:" + e.toString());
    }
    Collections.sort(files);
    return ImmutableList.<String>copyOf(files);
  }
  
  /** Strips "-40" from author name. */
  public static String strip40FromAuthor(String author) {
    return author.substring(0, author.length()-3);
  }
  
  /** Returns path to translated text specified by parameters. */
  public static String getTranslatedFilepath(String path, @Nullable Integer translationNumber,
      int reviewNumber, @Nullable Integer languageNumber) {
    String builder = "";
    if (translationNumber != null) {
      builder = Parameters.OUTPUT + Parameters.TRANSLATE_PATH + "/";
      builder = appendTranslationNumber(builder, translationNumber);
      builder += path;
    } else {
      builder = Parameters.OUTPUT + Parameters.TRANSLATE_PATH + "/" + path;
    }
    builder = appendReviewNumber(builder, reviewNumber);
    if (languageNumber != null) {
      builder = appendLanguageNumber(builder, languageNumber);
    }
    return builder;
  }
  
  /** Appends translation number, "/languages{number}" to given input. */
  public static String appendTranslationNumber(String input, int translationNumber) {
    return input + Parameters.TRANSLATION_PATH + translationNumber + '/';
  }
  
  public static String appendReviewNumber(String input, int reviewNumber) {
    return input + Parameters.TRANSLATED_EXT + reviewNumber;
  }
  
  /** Appends language number, "-languages-{number} to given input. */
  public static String appendLanguageNumber(String input, int languageNumber) {
    return input + Parameters.LANGUAGES_EXT + languageNumber;
  }
  
  /** Returns filename of back translated text. */
  public static String getBackTranslatedFilename(String path, int translationNumber,
      int reviewNumber, int languageNumber) {
    return getTranslatedFilepath(path, translationNumber, reviewNumber, languageNumber) +
        Parameters.BACK_TRANSLATED_EXT;
  }
  
  /** Returns filename of back translated text. */
  public static String getTranslatedFixedFilename(String path, int reviewNumber) {
    String builder = Parameters.OUTPUT + Parameters.TRANSLATE_FIXED_PATH + "/" + path;
    builder = appendReviewNumber(builder, reviewNumber);
    builder += Parameters.TRANSLATED_FIXED_EXT;
    return builder;
  }
  
  /** Checks if {@code authorNo} is valid or not. */
  public static void checkAuthorNo(int authorNo) {
    if (authorNo < 0 || authorNo >= Parameters.AUTHOR_SIZE) {
      throw new IllegalArgumentException("Invalid authorNo: " + authorNo);
    }
  }
  
  /** Checks if {@code reviewNo} is valid or not. */
  public static void checkReviewNo(int reviewNo) {
    if (reviewNo < 1 || reviewNo > 40) {
      throw new IllegalArgumentException("Invalid reviewNo: " + reviewNo);
    }
  }

  /** Returns number of '\n' in given string. */
  public static int getNumberOfLines(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    int counter = 1;
    for( int i=0; i<s.length(); i++ ) {
        if( s.charAt(i) == '\n' ) {
            counter++;
        } 
    }
    return counter;
  }
  
  /**
   * Selects random author from [0, 39] and a random AR review [1,5].
   * @param size of the output
   * @return map of random <Author, Review> pairs
   */
  public static Map<Integer, Integer> getRandomARs(int size) {
    Map<Integer, Integer> output = new HashMap<Integer, Integer>();
    Random rndm = new Random();
    while (output.size() < size) {
      int author = rndm.nextInt(40);
      int review = rndm.nextInt(5) + 1;
      output.put(author, review);
    }
    return output;
  }
}
