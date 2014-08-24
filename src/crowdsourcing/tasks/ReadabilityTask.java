package crowdsourcing.tasks;

import crowdsourcing.base.Task;

/**
 * @author ekin
 *
 * Class to create a readability task.
 */
public class ReadabilityTask extends Task {

  public ReadabilityTask(String original, String other, int author, int review) {
    super(original, other, author, review);
  }
  
	/** Returns design of this readability task. */
	public String getDesign() {
	   return "Compare the following original and alternative reviews. Determine how close alternative review is to original review in terms of:\n" +
         "\t* Similarity (which means how similar is the meaning).\n" +
         "\t* Comprehensive (which means to what extend they cover the same subject).\n\n" +
         "Submission Rules:\n" +
         "\t* Submit a result(explained below) followed by the explanation of differences.\n" +
           "\t* There are 5 possible results:\n" +
           "\t\t- Poor(they are completely different)\n" +
           "\t\t- Fair(alternative review has some completely different points)\n" +
           "\t\t- Average(although some ideas are same, alternative review is missing some main points)\n" +
           "\t\t- Good(they are somehow same, but alternative review is missing some small points)\n" +
           "\t\t- Excellent(they are completely same)\n" +
           "\t* Example submission: Good, alternative review is missing some points though.\n" +
           "\t* Poor/Irrelevant submissions will not be accepted\n\n" +
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