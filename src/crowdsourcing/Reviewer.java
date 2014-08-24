package crowdsourcing;

import java.util.ArrayList;
import java.util.List;

import base.Utils;

import com.amazonaws.mturk.addon.HITDataCSVReader;
import com.amazonaws.mturk.addon.HITDataCSVWriter;
import com.amazonaws.mturk.addon.HITDataInput;
import com.amazonaws.mturk.addon.HITTypeResults;
import com.amazonaws.mturk.dataschema.QuestionFormAnswers;
import com.amazonaws.mturk.dataschema.QuestionFormAnswersType;
import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.AssignmentStatus;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.PropertiesClientConfig;

import crowdsourcing.base.ReviewHIT;
import crowdsourcing.base.TranslationFixParser;
import crowdsourcing.tasks.TranslationFixTask;

/**
 * The Reviewer sample application will retrieve the completed assignments for a given HIT,
 * output the results and approve the assignment.
 *
 * mturk.properties must be found in the current file path.
 * You will need to have the HIT ID of an existing HIT that has been accepted, completed and
 * submitted by a worker.
 * You will need to have the .success file generated from bulk loading several HITs (i.e. Site Category sample application).
 *
 * The following concepts are covered:
 * - Retrieve results for a HIT
 * - Output results for several HITs to a file
 * - Approve assignments
 */
public class Reviewer {

  private RequesterService service;
  public int size = 0;
  public static List<String> filenames;
  
  /**
   * Constructor
   *
   */
  public Reviewer() {
    service = new RequesterService(new PropertiesClientConfig("mturk.properties"));
    filenames = Utils.getAuthors40();
  }

  /**
   * Prints the submitted results of HITs when provided with a .success file.
   * @param successFile The .success file containing the HIT ID and HIT Type ID
   * @param outputFile The output file to write the submitted results to
   */
  public void printResults(String successFile, String outputFile) {

    try {

      //Loads the .success file containing the HIT IDs and HIT Type IDs of HITs to be retrieved.
      HITDataInput success = new HITDataCSVReader(successFile);

      //Retrieves the submitted results of the specified HITs from Mechanical Turk
      HITTypeResults results = service.getHITTypeResults(success);
      results.setHITDataOutput(new HITDataCSVWriter(outputFile));

      //Writes the submitted results to the defined output file.
      //The output file is a tab delimited file containing all relevant details
      //of the HIT and assignments.  The submitted results are included as the last set of fields
      //and are represented as tab separated question/answer pairs
      results.writeResults();

      System.out.println("Results have been written to: " + outputFile);

    } catch (Exception e) {
      System.err.println("ERROR: Could not print results: " + e.getLocalizedMessage());
    }
  }

  @SuppressWarnings("unchecked")
  /**
   * Prints the submitted results of a HIT when provided with a HIT ID.
   * @param hitId The HIT ID of the HIT to be retrieved.
   */
  public void reviewAnswers(ReviewHIT hit) {
    Assignment[] assignments = service.getAllAssignmentsForHIT(hit.id);

    System.out.println("--[Reviewing HITs]----------");
    System.out.println("author:review=" + hit.author + ":" + hit.review);
    System.out.println("  HIT Id: " + hit.id);
    for (Assignment assignment : assignments) {
      String path = Utils.getTranslatedFixedFilename(filenames.get(hit.author), hit.review);
      System.out.println(path.substring(path.lastIndexOf('/')+1));
      //Only assignments that have been submitted will contain answer data
      if (assignment.getAssignmentStatus() == AssignmentStatus.Submitted) {
        
        //By default, answers are specified in XML
        String answerXML = assignment.getAnswer();

        //Calling a convenience method that will parse the answer XML and extract out the question/answer pairs.
        QuestionFormAnswers qfa = RequesterService.parseAnswers(answerXML);
        List<QuestionFormAnswersType.AnswerType> answers =
          (List<QuestionFormAnswersType.AnswerType>) qfa.getAnswer();
        for (QuestionFormAnswersType.AnswerType answer : answers) {

          String assignmentId = assignment.getAssignmentId();
          String answerValue = RequesterService.getAnswerValue(assignmentId, answer);

          if (answerValue != null) {
            if (answerValue.equals("emptyanswer") ||
                answerValue.equals("fair") ||
                answerValue.equals("nice") ||
                answerValue.equals("poor")) {
//              service.rejectAssignment(assignmentId, "");
              System.out.println("BAD ANSWER");
            } else {
//              service.rejectAssignment(assignmentId, "");
              System.out.println("Got an answer \"" + answerValue
                  + "\" from worker " + assignment.getWorkerId() + ".");
              size += 1;
            }
          }
//          ReviewWriter.writeTranslatedFixedReview(answerValue, hit.author, hit.review);
          /** Reject bad workers here. */
//          if (false) {
//            System.out.println("BAD WORKER");
//            service.rejectAssignment(assignmentId, "Do not follow the submission instructions: Submit a result(explained below) followed by the explanation of differences.");
//          }
        }
//        Approving the assignment.
//        service.approveAssignment(assignment.getAssignmentId(), "Well Done!");
//        System.out.println("Approved.");
//        service.disposeHIT(hit.id);
//        service.disableHIT(hit.id);
//
      }
    }
    System.out.println("--[End Reviewing HITs]----------");
  }

  public static void main(String[] args) {
    int authorMin = 1;
    int authorMax = 40;
    int offset = authorMin - 1;
    
    List<TranslationFixTask> remaining = TranslationFixParser.getTasks();
    
    Reviewer app = new Reviewer();
    args = new String[1];
//    args[0] = "files/readability_published_translationfix.success";
    args[0] = "files/translation_fix_" + authorMin + "_" + authorMax + "_3.success";
//    args[1] = "files/readability_published_1_to_3.results";
    List<String> hitIDs = Utils.readFile(args[0]);
    List<ReviewHIT> hits = new ArrayList<ReviewHIT>();
    
    /** Get from unordered list. */
    int i = 0;
    for (TranslationFixTask task : remaining) {
      hits.add(new ReviewHIT(task.author, task.review, hitIDs.get(i++)));
    }
    
    /** Get the sublist of all the hits. */
//    hits = hits.subList(10, 20);

    /** Get from nice list. */
//    for (int author = authorMin; author <= authorMax; author++) {
//      for (int review = 1; review <= 5; review++)
//      hits.add(new ReviewHIT(author-1, review, hitIDs.get((author-offset-1) * 5 + (review-1))));
//    }
    
    // Readability
//    for (int j = 0; j < hitIDs.size(); j++) {
//      hits.add(new ReviewHIT(1, 1, hitIDs.get(j)));
//    }
//    hits = hits.subList(174, 175);
    if (args.length == 1 && !args[0].equals("")) {
      for (ReviewHIT hit : hits) {
        app.reviewAnswers(hit);
      }
      System.out.println(app.size);
//      app.service.deleteHITs(hits.toArray(new String[hits.size()]), true, true, null);
    } else if (args.length == 2 && !args[0].equals("") && !args[1].equals("")) {
      app.printResults(args[0], args[1]);
    }
  }
}
