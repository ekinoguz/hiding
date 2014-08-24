package crowdsourcing.tasks;

import crowdsourcing.base.Task;

public class TranslationFixTask extends Task {

  /**
   * Creates a translation fix task
   * @param original Text
   * @param other Rewritten or other text
   */
  public TranslationFixTask(String original, String other, int author, int review) {
    super(original, other, author, review);
  }
  
  /** Returns design of this readability task. */
  public String getDesign() {
    int atLeast = super.numberOfWordsOriginal- 20;
    int atMost = super.numberOfWordsOriginal + 20;
    return "Compare the following original and alternative reviews in terms of their meaning and fix " +
    		"the different/missing/extra/meaningless points in *ALTERNATIVE* review with your *OWN WORDS* and" +
    		" *OWN SENTENCES*. After the fix, *ALTERNATIVE* review should be similar and comprehensive to original" +
    		" review in terms of meaning.\n\n" +
         "Submissions that do not follow the below rules will be rejected:\n" +
          "\t* Submit a fixed version of *ALTERNATIVE* review.\n" +
          "\t* You have to keep meaning similar to original review.\n" +
          "\t* You have to use your own words and sentences. Sentences copied from original review will not be accepted.\n" + // Changing couple of words does not mean rewriting
          "\t* Your submission must be at least " + atLeast + " words long but no more than " + atMost + " words.\n" +
          "\t* Your writing must be original and can not simply be a copy of part of a website.\n" +
          "\t* Please do not change proper names.\n\n" +
           //"PLEASE READ BOTH REVIEWS BEFORE SUBMISSION, OTHERWISE YOUR SUBMISSION MAY NOT BE ACCEPTED\n" +
           "Thanks for your time\n\n\n" +
           "*********************Original Review Starts Below***********************\n" +
           original + "\n" +
           "*********************Original Review Finishes Here**********************\n\n" + 
           "********************Alternative Review Starts Below*********************\n" +
           other + "\n" +
           "********************Alternative Review Finishes Here********************";
  }
}
