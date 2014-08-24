//package crowdsourcing.pushers;
///*
// * Copyright 2007-2012 Amazon Technologies, Inc.
// * 
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at:
// * 
// * http://aws.amazon.com/apache2.0
// * 
// * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
// * OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and
// * limitations under the License.
// */ 
//
//import java.util.ArrayList;
//import crowdsourcing.base.Utils;
//
//import com.amazonaws.mturk.requester.HIT;
//import com.amazonaws.mturk.service.axis.RequesterService;
//import com.amazonaws.mturk.service.exception.ServiceException;
//import com.amazonaws.mturk.util.PropertiesClientConfig;
//
///**
// * The MTurk Hello World sample application creates a simple HIT via the Mechanical Turk 
// * Java SDK. mturk.properties must be found in the current file path.
// */
//public class RewritingPusher {
//	
//	private boolean SUBMISSION 	= false;
//	private boolean TEST		= false;
//	private int LOOP_SIZE		= 1;
//	
//
//	
//	private RequesterService service;
//	private String[] hittypes;
//  // review number which will be published in MT
//  private final int REVIEW_NO = 5;
//  
//  // Defining the attributes of the HIT to be created
//  private int MaxAssignments = 5;
//  private Long LifetimeInSeconds = new Long(1814400);
//  private Long AutoApprovalDelayInSeconds = new Long(1814400);
//  private Double reward = new Double(0.1);
//  private String title = "Rewrite a Review";
//  private String description = 
//		  	"Rewrite a review with your own words while keeping the meaning similar. Poor rewritings will not be accepted.\n" +
//			"Read the instructions carefully before submission and make sure you satisfy all the the requirements.";
//  private String keywords = "writing, rewrite, rewriting, review";
//
//
//  /**
//   * Constructor
//   * 
//   */
//  public RewritingPusher() {
//    service = new RequesterService(new PropertiesClientConfig("../mturk.properties"));
//    hittypes = new String[5];
//  }
//
//  /** Creates the rewrite review hit type. */
//  public void createHITType() {
//	  for (int i = 0; i < hittypes.length; i++) {
//		  hittypes[i] = service.registerHITType(AutoApprovalDelayInSeconds, LifetimeInSeconds, reward * (i+1), title, keywords, description, null);
//	  }
//  }
//  
//  /**
//   * Creates the simple HIT.
//   * 
//   */
//  public void publishHITs() {
//	  ArrayList<String> HITids = new ArrayList<String>();
//	  createHITType();
//	  // inputPaths = Read the input file paths from input_files directory
//	  Paths paths = new Paths("input");
//	  System.out.println("input file size: " + paths.size());
//	  System.out.println("-------------Starting--------------");
//	  // for each path in inputPaths
//	  int count = 0;
//	  int totalRemaining = 0;
//	  double totalMoney = 0.0;
//	  //for (int i = LOOP_SIZE-1; i < LOOP_SIZE; i++) {
//	  for (int i = 0; i < paths.size(); i++) {
//		  String path = paths.getFilePath(i);
//		  int rewardIndex = 0;
//		  // discard if file is a Dropbox file
//		  if (path.equalsIgnoreCase(".DS_Store"))
//			  continue;
//		  MaxAssignments = FIVE_LEFT_ARRAY[count++];
//		  System.out.println("Number: " + i + "\tRemaining: " + MaxAssignments);
//		  System.out.println(path);
//		  
//		//total prize calculate
//		  MaxAssignments = 5 - MaxAssignments;
//		  
//		  totalRemaining += MaxAssignments;
//		// get the review from (path, number)
//		  Review review = new Review(path, REVIEW_NO, "input");
//		  String design = review.getDesign();
//		  
//		  
//		  
//		  if (MaxAssignments <= 0) {
//			  System.out.println("------------------------------------");
//			  HITids.add("Remaning 0");
//			  continue;
//		  }
//		  //else if (MaxAssignments == 5) {
//			  //System.out.println("size: " + review.size + "\t" + review.getSizeMultipler());
//			  rewardIndex = review.getSizeMultipler() - 1;
//		  //}
//		  totalMoney += (review.getSizeMultipler() * reward * MaxAssignments);
//		  System.out.println(design);
//	  if (SUBMISSION) {
//		  // public the hit in mechanical turk
//		  try {
//			  String question = RequesterService.getBasicFreeTextQuestion(design);
//			  // The createHIT method is called using a convenience static method of
//			  // RequesterService.getBasicFreeTextQuestion that generates the QAP for
//			  // the HIT.
//			  
//			  
//			  HIT hit = service.createHIT(hittypes[rewardIndex], "title", "desciption", keywords, question, 
//					  reward, LifetimeInSeconds, AutoApprovalDelayInSeconds, LifetimeInSeconds, MaxAssignments, 
//					  null, null, null, null, null, null) ;
//
////			  System.out.println("Created HIT: " + hit.getHITId());
////			  HITids.add(hit.getHITId());
////			  System.out.println("You may see your HIT with HITTypeId '" 
////			      + hit.getHITTypeId() + "' here: ");
////			  System.out.println(service.getWebsiteURL() 
////			      + "/mturk/preview?groupId=" + hit.getHITTypeId());
////			  System.out.println("------------------------------------");
//			  if(TEST) return;
//			} catch (ServiceException e) {
//			  System.err.println(e.getLocalizedMessage());
//			} catch (Exception e) {
//				System.out.println("Exception @publishHITS in MT: " + e.toString());
//			}
//	  }
//	  
//	  }
//	  System.out.println("Number of HITs published: " + count);
//	  System.out.println("Total remaining: " + totalRemaining);
//	  System.out.println("Total money: " + totalMoney);
//	  System.out.println("HIT IDs: \n");
////	  for (int i = 0; i < HITids.size(); i++)
////		  System.out.println(HITids.get(i));
//
//  }
//
//  /**
//   * Main method
//   * 
//   * @param args
//   */
//  public static void main(String[] args) {
//
//	  //new Paths("input");
//    RewritingPusher app = new RewritingPusher();
//
//    if (Utils.hasEnoughFund(app.service, app.reward)) {
//      app.publishHITs();
//    	
//      System.out.println("Success.");
//    } else {
//      System.out.println("You do not have enough funds to create the HIT.");
//    }
//  }
//}
