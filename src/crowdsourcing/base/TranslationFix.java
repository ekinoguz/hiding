package crowdsourcing.base;

import base.ReviewReader;

import com.google.common.collect.ImmutableList;

import crowdsourcing.tasks.TranslationFixTask;

public class TranslationFix {

  public static final int TRANSLATION_NUMBER = 3;
  
  public static ImmutableList<TranslationFixTask> getTasks(int minAuthor, int maxAuthor) {
    ImmutableList.Builder<TranslationFixTask> builder = new ImmutableList.Builder<TranslationFixTask>();
    for (int author = minAuthor; author < maxAuthor; author++) {
      for (int review = 1; review <= 5; review++) {
        String original = ReviewReader.readInputReview(author, review);
        String translated = ReviewReader.readTranslatedBackReview(author, TRANSLATION_NUMBER, review, 9);
        builder.add(new TranslationFixTask(original, translated, author, review));
      }
    }
    return builder.build();
  }
}