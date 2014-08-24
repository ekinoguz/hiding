package crowdsourcing.base;

import javax.annotation.Nullable;

import base.Parameters;
import base.Utils;

import com.google.common.base.Preconditions;

public abstract class Task {

  public final String original;
  public final String other;
  public int author;
  public int review;
  public final int numberOfWordsOriginal;
  
  /** Creates a task with a single string. */
  public Task(String original) {
    this(original, null);
  }
  
  /**
   * Creates a task with two string
   * @param original Text
   * @param other Rewritten or other text
   */
  public Task(String original, @Nullable String other) {
    this(original, other, -1, -1);
    
  }
  
  /** Creates a task with a single string. */
  public Task(String original, @Nullable String other, int author, int review) {
    Preconditions.checkNotNull(original);
    this.original = original;
    this.other = other;
    this.author = author;
    this.review = review;
    numberOfWordsOriginal = Utils.getNumberOfWords(original);
  }
  
  /** Design of the task. */
  public abstract String getDesign();
  
  /** Returns cost multiplier according to size of review. */
  public int getSizeMultipler() {
    return (int) Math.ceil(numberOfWordsOriginal / Parameters.SIZE_MULTIPLIER);
  }
  
  /** Returns the index of reward according to {@code getSizeMultiplier}. */
  public int getRewardIndex() {
    return this.getSizeMultipler() - 1;
  }
  
  /** Returns string representation of a readability task. */
  public String toString() {
    return getDesign();
  }
}
