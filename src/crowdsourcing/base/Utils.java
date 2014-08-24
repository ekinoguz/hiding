package crowdsourcing.base;

import com.amazonaws.mturk.service.axis.RequesterService;

public class Utils {

  /**
   * Check if there are enough funds in your account in order to create the HIT
   * on Mechanical Turk
   * 
   * @return true if there are sufficient funds. False if not.
   */
  public static boolean hasEnoughFund(RequesterService service, Double reward) {
    double balance = service.getAccountBalance();
    System.out.println("Got account balance: " + RequesterService.formatCurrency(balance));
    return balance > reward;
  }
}
