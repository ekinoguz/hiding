package crowdsourcing.pushers;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.util.PropertiesClientConfig;

import crowdsourcing.base.TranslationFixParser;
import crowdsourcing.base.Utils;
import crowdsourcing.tasks.TranslationFixTask;

public class TranslationFixPusher {

  private boolean SUBMISSION  = true;
  private boolean TEST    = false;
  //private int LOOP_SIZE   = 1;
  
  // Defining the attributes of the HIT to be created
  private int MaxAssignments = 2;
  private Long LifetimeInSeconds = new Long(604800);
  private Long AutoApprovalDelayInSeconds = new Long(1814400);
  private Double reward = new Double(0.1);
  
  /** Public readable properties of task. */
  private String title = "Fix a Review";
  private String description = "Read, compare two reviews in terms of meanings and fix the review.\n" +
      "Read the instructions carefully before submission and make sure you satisfy all " +
      "the the requirements.";
  private String keywords = "compare, read, writing, review, rewriting";

  /** Properties */
  private RequesterService service;
  private String[] hittypes;
  
  /** Constructor. */
  public TranslationFixPusher() {
    service = new RequesterService(new PropertiesClientConfig("mturk.properties"));
    hittypes = new String[5];
  }

  /** Creates the rewrite review hit type */
  public void createHITType() {
    for (int i = 0; i < hittypes.length; i++) {
      hittypes[i] = service.registerHITType(AutoApprovalDelayInSeconds, LifetimeInSeconds, reward * (i+1), title, keywords, description, null);
    }
  }
  
  /** Creates the simple HIT. */
  public void publishHITs(List<TranslationFixTask> designs) {
    ArrayList<String> HITids = new ArrayList<String>();
    createHITType();
    System.out.println("input file size: " + designs.size());
    System.out.println("-------------Starting--------------");
    int count = 0;
    int totalRemaining = 0;
    double totalMoney = 0.0;
    for (TranslationFixTask task : designs) {
      totalRemaining += MaxAssignments;
      String design = task.getDesign();

      totalMoney += reward * task.getSizeMultipler() * MaxAssignments;
      System.out.println("author:review=" + task.author + ":" + task.review);
      System.out.println(design);
      if (SUBMISSION) {
        try {
          String question = RequesterService.getBasicFreeTextQuestion(design);
          // The createHIT method is called using a convenience static method of
          // RequesterService.getBasicFreeTextQuestion that generates the QAP for
          // the HIT.
          HIT hit = service.createHIT(hittypes[task.getRewardIndex()], "title", "desciption", keywords, question, 
              reward, LifetimeInSeconds, AutoApprovalDelayInSeconds, LifetimeInSeconds, MaxAssignments, 
              null, null, null, null, null, null) ;
  
          System.out.println("Created HIT: " + hit.getHITId());
          HITids.add(hit.getHITId());
          System.out.println("You may see your HIT with HITTypeId '" 
              + hit.getHITTypeId() + "' here: ");
          System.out.println(service.getWebsiteURL() 
              + "/mturk/preview?groupId=" + hit.getHITTypeId());
          System.out.println("------------------------------------");
          count += 1;
          if(TEST) return;
        } catch (ServiceException e) {
          System.err.println(e.getLocalizedMessage());
          System.exit(0);
        } catch (Exception e) {
          System.out.println("Exception @publishHITS in MT: " + e.toString());
        }
      }
    }
//    System.out.println("Word Count: " + readability.getWordCount());
    System.out.println("Number of HITs published: " + count);
    System.out.println("Total remaining: " + totalRemaining);
    System.out.println("Total money: " + totalMoney);
    System.out.println("HIT IDs: \n");
    for (int i = 0; i < HITids.size(); i++)
      System.out.println(HITids.get(i));
  }

  public static void main(String[] args) {
    TranslationFixPusher app = new TranslationFixPusher();
    //List<TranslationFixTask> tasks = TranslatorFix.getTasks(38, 40);
    List<TranslationFixTask> tasks = TranslationFixParser.getTasks();
    if (Utils.hasEnoughFund(app.service, app.reward)) {
      app.publishHITs(tasks);
      System.out.println("Success.");
    } else {
      System.out.println("You do not have enough funds to create the HIT.");
    }
  }
}
