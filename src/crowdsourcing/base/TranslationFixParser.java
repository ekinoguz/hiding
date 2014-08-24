package crowdsourcing.base;

import java.util.List;

import base.ReviewReader;

import com.google.common.collect.ImmutableList;

import crowdsourcing.tasks.TranslationFixTask;

public class TranslationFixParser {

  private final static String FILE = "files/translation_fix.sofar";
  
  public static ImmutableList<TranslationFixTask> getTasks() {
    List<String> lines = base.Utils.readFile(FILE);
    ImmutableList.Builder<TranslationFixTask> builder = new ImmutableList.Builder<TranslationFixTask>();
    for (int author = 0; author < 40; author++) {
      String[] numbers = lines.get(author).split(" ");
      int review = 0;
      for (String number : numbers) {
        review += 1;
        int remaining = Integer.parseInt(number);
        if (remaining == 0) {
          String original = ReviewReader.readInputReview(author, review);
          String translated = ReviewReader.readTranslatedBackReview(author,
              TranslationFix.TRANSLATION_NUMBER, review, 9);
          builder.add(new TranslationFixTask(original, translated, author, review));
        }
      }
    }
    return builder.build();
  }

  public static void main(String[] args) {
    System.out.println(getTasks().size());
    for (TranslationFixTask task : getTasks()) {
      System.out.println(task.author + ":" + task.review + ":" + task.getRewardIndex() + ":" + task.numberOfWordsOriginal);
    }
  }
}
