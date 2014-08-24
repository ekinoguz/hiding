package crowdsourcing.tasks;

import crowdsourcing.base.Task;

/**
 * @author ekin
 * 
 * Class to create rewriting task to be submitted to crowdsourcing
 */
public class RewritingTask extends Task {

	public RewritingTask(String review) {
		super(review);
	}
	
	/** Returns the design of this rewriting task. */
	public String getDesign() {
	  int atLeast = super.numberOfWordsOriginal- 20;
	  int atMost = super.numberOfWordsOriginal + 20;
    return
        "Rewrite the following review\n\n" +
        "\t* You have to keep meaning similar to original review.\n" +
        "\t* You have to use your own sentences.\n" + // Changing couple of words does not mean rewriting
        "\t* Your submission must be at least " + atLeast + " words long but no more than " + atMost + " words.\n" +
        "\t* Duplicate submissions will not be accepted.\n" +
        "\t* Your writing must be original and can not simply be a copy of part of a website.\n" +
        "\t* Please do not change proper names\n\n" +
        "Thanks for your time\n\n\n" +
        "**********************Original Review Starts Below***********************\n\n" +
        super.original + "\n\n" +
        "**********************Original Review Finishes Here**********************";
	}
  /*
  design =  "<h3>Rewrite the following review</h3>" +
        "<div class=\"highlight-box\">" +
        "<ul>" +
            "<li>Meaning of the original review has to be similar.</li>" +
            "<li>Duplicate submissions will not be accepted.</li>" +
            "<li>Your submission must be at least " + atLeast + " words long but no more than " + atMost + " words.</li>" +
            "<li>Your writing must be original and can not simply be a copy of part of the website.</li>" +
            "<li>Please do not change proper names</li>" +
        "</ul>" +
        "<p>Thanks for your time</p>" +
        "</div>" +
        "<p>&nbsp;</p>" +
        "<p><font face=\"Arial\">**********************Original Review Starts Below***********************</font></p>" +
        "<p>" + original + "</p>" +
        "<p><span style=\"font-family: Arial; \">**********************Original Review Finishes Here**********************</span></p>" +
        "<p>&nbsp;</p>" +
        "<p><textarea rows=\"4\" cols=\"80\" name=\"summary\"></textarea></p>" +
        "<p><style type=\"text/css\">" +
        "<!--" +
        ".highlight-box { border:solid 0px #98BE10; background:#FCF9CE; color:#222222; padding:4px; text-align:left; font-size: smaller;}" +
        "-->" +
        "</style></p>";
    */
}
