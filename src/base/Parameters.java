package base;

public class Parameters {
  /** Used to determine price for each task depending on task's size. */
  public static final double SIZE_MULTIPLIER = 250.0;
  
  /** Total number of authors. */
  public final static int AUTHOR_SIZE = 40;

  /** MAC parameters.
  public static final String UBUNTU_MAIN = "/Users/ekinoguz/Dropbox/repos/STA/hiding/";
  public static final String INPUT = "/Users/ekinoguz/Dropbox/repos/STA/hiding/input_files";
  public static final String OUTPUT = "/Users/ekinoguz/Dropbox/repos/STA/hiding/output_files";
  public static final String CREATED = "/Users/ekinoguz/Dropbox/repos/STA/hiding/created";
  */
  /** Ubuntu parameters. */
  public static final String UBUNTU_MAIN = "/home/ekin/Dropbox/repos/STA/hiding/";
  public static final String INPUT = "/home/ekin/Dropbox/repos/STA/hiding/input_files";
  public static final String OUTPUT = "/home/ekin/Dropbox/repos/STA/hiding/output_files";
  public static final String CREATED = "/home/ekin/Dropbox/repos/STA/hiding/created";
//  */
  
  public static final String TRANSLATE_PATH =  "/translated";
  public static final String TRANSLATE_FIXED_PATH =  "/translated_fixed";
  public static final String TRANSLATION_PATH =  "translation";
  public static final String REWRITTEN_EXT =  "-rewritten-";
  public static final String TRANSLATED_EXT = "-translate-";
  public static final String BACK_TRANSLATED_EXT = "-eng";
  public static final String TRANSLATED_FIXED_EXT = "-fixed";
  public static final String RSENTENCE_EXT =  "-rsentence-";
  public static final String LANGUAGES_EXT =  "-languages-";
  
  /** How many rewritings left for each review of author. */
  public static final int[] ONE_LEFT_ARRAY = {
    0, 2, 0, 0, 0, 0, 0, 3, 1, 0,  
    0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 
    0, 0, 0, 0, 2, 0, 3, 3, 0, 0, 
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };

  public static final int[] TWO_LEFT_ARRAY = {
    0, 1, 3, 1, 0, 0, 0, 0, 1, 0,  
    0, 0, 0, 3, 3, 4, 2, 0, 0, 0, 
    0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 
    0, 0, 0, 2, 3, 1, 0, 0, 0, 0
  };

  public static final int[] THREE_LEFT_ARRAY = {
    0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
    0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
    0, 0, 0, 2, 0, 0, 3, 0, 0, 0,
    1, 0, 3, 0, 0, 0, 0, 0, 0, 1
  };

  public static final int[] FOUR_LEFT_ARRAY = {
    0, 3, 0, 0, 0, 0, 3, 1, 1, 0,
    1, 1, 0, 0, 0, 0, 0, 0, 0, 1,
    0, 0, 0, 0, 3, 0, 2, 2, 0, 0,
    0, 0, 0, 0, 1, 0, 0, 0, 1, 0
  };

  public static final int[] FIVE_LEFT_ARRAY = {
    4, 3, 2, 1, 1, 0, 0, 0, 2, 0, 
    0, 1, 0, 3, 4, 0, 4, 1, 0, 1,
    1, 0, 1, 2, 2, 0, 1, 3, 0, 0,
    0, 0, 2, 1, 0, 0, 1, 0, 0, 1
  };
}
