package base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * @author ekin
 * 
 * Class to find and read reviews from files.
 */
public class ReviewReader {
  
  /** List of all fileNames. */
  private static List<String> fileNames = null;
  
  /** Reads the whole input review. */
  public static String readInputReview(int authorNo) {
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    String review = null;
    try {
      String filename = Parameters.INPUT + "/" + fileNames.get(authorNo);

      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line = null;
      while ((line = br.readLine()) != null) {
        review += line + '\n';
      }
      br.close();
    } catch (Exception e) {
      System.err.print("Exception in ReviewReader() while reading authorNo:" + authorNo + 
          " " + e.toString());
    }
    return review;
  }
  
  /**
   * Finds and reads an input review specified by the parameters
   * @param authorNo [0, 39]
   * @param reviewNo [1, 40]
   * @returns review if it is not null
   */
  public static String readInputReview(int authorNo, int reviewNo) {
    return readReview(authorNo, reviewNo, Parameters.INPUT, null, null);
  }
  
  /**
   * Finds and reads a rewritten review specified by the parameters
   * @param authorNo
   * @param reviewNo
   * @param rewrittenNo
   * @return
   */
  public static String readRewrittenReview(int authorNo, int reviewNo, int rewrittenNo) {
    return readReview(authorNo, rewrittenNo, Parameters.OUTPUT, Parameters.REWRITTEN_EXT, reviewNo);
  }
  
  /**
   * Finds and reads a rewritten review that is used in linkability experiments.
   * @param authorNo
   * @param reviewNo
   * @return
   */
  public static String readRewrittenReview(int authorNo, int reviewNo) {
    return readCreatedReview(authorNo, reviewNo, Parameters.REWRITTEN_EXT);
  }
  
  /** Finds the translated review from disk given with parameters and returns it. */
  public static String readTranslatedReview(int authorNo, int translationNumber, int reviewNo,
      int languageNumber) {
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    String filename = Utils.getTranslatedFilepath(fileNames.get(authorNo), translationNumber,
        reviewNo, languageNumber);
    return Utils.readFileLine(filename, 1);
  }
  
  /** Finds the translated back review from disk given with parameters and returns it. */
  public static String readTranslatedBackReview(int authorNo, int translationNumber, int reviewNo,
      int languageNumber) {
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    String filename = Utils.getBackTranslatedFilename(fileNames.get(authorNo), translationNumber,
        reviewNo, languageNumber);
    return Utils.readFileLine(filename, 1);
  }
  
  /** Finds the translated fixed review from disk given with parameters and returns it. */
  public static List<String> readTranslatedFixedReview(int authorNo, int reviewNo) {
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    String filename = Utils.getTranslatedFixedFilename(fileNames.get(authorNo), reviewNo);
    return Utils.readFile(filename);
  }
  
  /** Returns sentences of the review. */
  public static ArrayList<String> getSentences(String review)  {
    ArrayList<String> sentences = new ArrayList<String>();
    Pattern pattern = Pattern.compile(
        "# Match a sentence ending in punctuation or EOS.\n" +
        "[^.!?\\s]    # First char is non-punct, non-ws\n" +
        "[^.!?]*      # Greedily consume up to punctuation.\n" +
        "(?:          # Group for unrolling the loop.\n" +
        "  [.!?]      # (special) inner punctuation ok if\n" +
        "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n" +
        "  [^.!?]*    # Greedily consume up to punctuation.\n" +
        ")*           # Zero or more (special normal*)\n" +
        "[.!?]?       # Optional ending punctuation.\n" +
        "['\"]?       # Optional closing quote.\n" +
        "(?=\\s|$)", 
        Pattern.MULTILINE | Pattern.COMMENTS);
    Matcher matcher = pattern.matcher(review.toString());
    while (matcher.find()) {
      sentences.add(matcher.group());
    }
    return sentences;
  }

  /**
   * Reads the review according to given inputs
   * @param authorNo
   * @param reviewNo
   * @param path
   * @param extension
   * @param extensionNo
   * @return
   */
  private static String readReview(int authorNo, int reviewNo, String path,
      @Nullable String extension, @Nullable Integer extensionNo) {
    Utils.checkAuthorNo(authorNo);
    Utils.checkReviewNo(reviewNo);
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    
    String review = null;
    try {
      String filename = path + "/" + fileNames.get(authorNo);
      if (extension != null) {
        filename += extension + extensionNo;
      }
      BufferedReader br = new BufferedReader(new FileReader(filename));
      while ((reviewNo--) > 0) {
        review = br.readLine();
      }
      br.close();
    } catch (Exception e) {
      System.err.print("Exception in ReviewReader() while reading authorNo:" + authorNo + 
          " reviewNo:" + reviewNo + ", exception: " + e.toString());
    }
    if (review == null) {
      throw new NullPointerException("Review is null while reading authorNo:" + authorNo + 
          " reviewNo:" + reviewNo);
    }
    return review;
  }
  
  /** Reads a review from CREATED folder. */
  private static String readCreatedReview(int authorNo, int reviewNo, @Nullable String extension) {
    Utils.checkAuthorNo(authorNo);
    Utils.checkReviewNo(reviewNo);
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    
    String review = null;
    try {
      String filename = Parameters.CREATED + "/" + Utils.strip40FromAuthor(fileNames.get(authorNo));
      if (extension != null) {
        filename += extension + reviewNo;
      }
      BufferedReader br = new BufferedReader(new FileReader(filename));
      while ((reviewNo--) > 0) {
        review = br.readLine();
      }
      br.close();
    } catch (Exception e) {
      System.err.print("Exception in ReviewReader() while reading authorNo:" + authorNo + 
          " reviewNo:" + reviewNo + ", exception: " + e.toString());
    }
    if (review == null) {
      throw new NullPointerException("Review is null while reading authorNo:" + authorNo + 
          " reviewNo:" + reviewNo);
    }
    return review;
  }
//  
//  public static void main(String[] args) {
//    List<String> authors = Utils.getAuthors40();
//    System.out.println(authors.get(9));
//    System.out.println(ReviewReader.readInputReview(9));
//    System.out.println("\n\n");
//    String s = ReviewReader.readInputReview(9);
//  }
}
