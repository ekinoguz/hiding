package translator;

import java.util.List;
import java.util.Scanner;

import translator.base.Translator;
import translator.google.GoogleTranslator;
import translator.google.Languages;
import translator.google.Languages.Language;
import base.ReviewReader;
import base.ReviewWriter;
import base.Utils;

public class Main {

  private static int MAX_LANGUAGE_NUMBER = 10;
  private static int TRANSLATION_NUMBER = 4;
  
  private static boolean TRANSLATION_ENABLED = true;
  private static boolean WRITE_ENABLED = true;
  
  public static void main(String[] args) {
    /** Get the languages. */
    List<Language> languages = Languages.getRandomLanguagesFromFile(MAX_LANGUAGE_NUMBER);
    System.out.println(languages);
    languages.clear();
    languages.add(Language.create("ca"));
    languages.add(Language.create("bg"));
    languages.add(Language.create("no"));
    languages.add(Language.create("sr"));
    languages.add(Language.create("vi"));
    languages.add(Language.create("hr"));
    languages.add(Language.create("af"));
    languages.add(Language.create("fi"));
    languages.add(Language.create("cy"));
    languages.add(Language.create("eo"));
    
    /** Get the translator. */
    Translator googleTranslator = new GoogleTranslator();
    
    // Information about this run instance
    StringBuilder builder = new StringBuilder();
    builder.append("Translation experiment with languageNumber:" + MAX_LANGUAGE_NUMBER);
    builder.append(", translationNumber:" + TRANSLATION_NUMBER);
    builder.append("\nLanguages: " + languages);
    System.out.println(builder.toString());
    
    // Make sure we want to run the test
    Scanner scan = new Scanner(System.in);
    System.out.print("Are you sure to run give experiment(y/n):");
    String areWeSure = scan.next();
    if (!areWeSure.equals("y")) {
      System.out.println("We are not sure...exiting");
      System.exit(0);
    }
    scan.close();
    
    // Save information about this instance
    String informationFile = Utils.getTranslatedFilepath("information", null, TRANSLATION_NUMBER,
        MAX_LANGUAGE_NUMBER);
    System.out.println(informationFile);
    Utils.saveFile(informationFile, builder.toString());
    
    // For each author Parameters.AUTHOR_SIZE
    for (int authorNo = 15; authorNo < 16; authorNo++) {
      // For each review
      System.out.println("\tauthor:" + authorNo + " started");
      for (int reviewNo = 2; reviewNo <= 5; reviewNo++) {
        String review = ReviewReader.readInputReview(authorNo, reviewNo);
        System.out.println("\t\treview:" + reviewNo);
        if (TRANSLATION_ENABLED) {
          /** Translate the review into given languages and back. */
          List<String> translatedReviews = googleTranslator.translate(review, Language.create("en"),
              languages);
          List<String> translatedBackReviews = googleTranslator.translateBack(translatedReviews,
              languages);
          if (WRITE_ENABLED) {
            /** Write translated reviews into disk. */
            for (int i = 0; i < translatedReviews.size(); i++) {
              ReviewWriter.writeTranslatedReview(translatedReviews.get(i), authorNo,
                  TRANSLATION_NUMBER, reviewNo, (i+1));
            }
            for (int i = 0; i < translatedBackReviews.size(); i++) {
              ReviewWriter.writeTranslatedBackReview(translatedBackReviews.get(i), authorNo,
                  TRANSLATION_NUMBER, reviewNo, (i+1));
            }
          }
        }
      }
      System.out.println("\tauthor:" + authorNo + " ended");
    }
    System.out.println(Main.class.toString() + " finished.");
  }
}
