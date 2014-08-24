package base;

import java.util.List;

/**
 * @author ekin
 * 
 * Class to write reviews into filesystem
 */
public class ReviewWriter {

  /** List of all fileNames. */
  private static List<String> fileNames = null;
  
  private static final int TRANSLATED = 0;
  private static final int TRANSLATED_BACK = 1;
  private static final int TRANSLATED_FIXED = 2;
  
  /** Writes given translated text to disk. */
  public static void writeTranslatedReview(String translated, int authorNo, int translationNumber,
      int reviewNo, int languageNumber) {
    write(translated, authorNo, translationNumber, reviewNo, languageNumber, TRANSLATED);
  }
  
  /** Writes back translated text to disk. */
  public static void writeTranslatedBackReview(String translated, int authorNo,
      int translationNumber, int reviewNo, int languageNumber) {
    write(translated, authorNo, translationNumber, reviewNo, languageNumber, TRANSLATED_BACK);
  }
  
  /** Write translated and fixed review to disk. */
  public static void writeTranslatedFixedReview(String fixed, int author, int review) {
    write(fixed, author, 0, review, 0, TRANSLATED_FIXED);
  }
  
  /** Helper function write translated review into disk. */
  private static void write(String text, int author, int translation, int review, int language,
      int operation) {
    Utils.checkAuthorNo(author);
    Utils.checkReviewNo(review);
    if (fileNames == null) {
      fileNames = Utils.getAuthors40();
    }
    
    String filename = null;
    if (operation == TRANSLATED) {
      filename = Utils.getTranslatedFilepath(fileNames.get(author), translation, review, language);
    } else if (operation == TRANSLATED_BACK) {
      filename = Utils.getBackTranslatedFilename(fileNames.get(author), translation, review, language);
    } else if (operation == TRANSLATED_FIXED) {
      filename = Utils.getTranslatedFixedFilename(fileNames.get(author), review);
      if (filename == null || !Utils.appendToFile(filename, text)) {
        System.err.print("Exception in ReviewWriter() while writing authorNo:" + author + 
            " reviewNo:" + review + " languageNumber: " + language);
      }
      return;
    }
    if (filename == null || !Utils.saveFile(filename, text)) {
      System.err.print("Exception in ReviewWriter() while writing authorNo:" + author + 
          " reviewNo:" + review + " languageNumber: " + language);
    }
  }
}