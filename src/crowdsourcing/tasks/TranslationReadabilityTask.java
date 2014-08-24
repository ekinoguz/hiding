package crowdsourcing.tasks;

import java.util.List;
import java.util.Map;

import base.ReviewReader;
import base.Utils;

import com.google.common.collect.ImmutableList;


public class TranslationReadabilityTask {

  private static final int LANGUAGE_NUMBER = 9;
  private static final int TRANSLATION_NUMBER = -1;
  private static final String FILE_PREFIX = "readability_translation_";
  
  /** Return random readability tasks for translation fold {@code translationNumber}. */
  public static ImmutableList<ReadabilityTask> getTasks(int translationNumber) {
    ImmutableList.Builder<ReadabilityTask> builder = new ImmutableList.Builder<ReadabilityTask>();
    List<String> lines = Utils.readFile("files/" + FILE_PREFIX + translationNumber);
    for (String line : lines) {
      String[] tokens = line.split(",");
      int author = Integer.parseInt(tokens[0]);
      int review = Integer.parseInt(tokens[1]);
      String original = ReviewReader.readInputReview(author, review);
      String translated = ReviewReader.readTranslatedBackReview(author, translationNumber, review,
          LANGUAGE_NUMBER);
      builder.add(new ReadabilityTask(original, translated, author, review));
    }
    return builder.build();
  }
  
  /** Create random readability tasks and save them. */
  public static void main(String[] args) {
    if (TRANSLATION_NUMBER > 0) {
      int size = 20; // Number of readability tasks
      Map<Integer, Integer> randoms = null;
      while (true) {
        randoms = Utils.getRandomARs(size);
        int total = 0;
        for (Integer key : randoms.keySet()) {
          total += Utils.getNumberOfWords(ReviewReader.readInputReview(key, randoms.get(key)));
        }
        System.out.println((total/(double)size));
        if ((total/(double)size) <= 150) {
          break;
        }
      }
      StringBuilder data = new StringBuilder();
      for (Integer key : randoms.keySet()) {
        data.append(key + "," + randoms.get(key) + "\n");
      }
      Utils.saveFile("files/" + FILE_PREFIX + TRANSLATION_NUMBER,
          data.toString().trim());
    }
  }
}
