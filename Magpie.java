import java.util.Random;

/**
 * A program to carry on conversations with a human user.
 * This is the initial version that:  
 * <ul><li>
 *       Uses indexOf to find strings
 * </li><li>
 *       Handles responding to simple words and phrases 
 * </li></ul>
 * This version uses a nested if to handle default responses.
 * @author Laurie White
 * @version April 2012
 */
public class Magpie
{
  /**
   * Get a default greeting  
   * @return a greeting
   */
  public String getGreeting()
  {
    return "Hello, let's talk.";
  }
  
  /**
   * Gives a response to a user statement
   * 
   * @param statement
   *            the user statement
   * @return a response based on the rules given
   */
  public String getResponse(String statement)
  {
    String response = "";
    if (statement.trim().length() == 0)
    {
      response = "Say something, please.";
    }
    else if (findKeyword(statement, "I will") >= 0
               || findKeyword(statement, "you will") >= 0
               || findKeyword(statement, "it will") >= 0
               || findKeyword(statement, "he will") >= 0
               || findKeyword(statement, "she will") >= 0
               || findKeyword(statement, "we will") >= 0
               || findKeyword(statement, "they will") >= 0)
    {
      response = "How can you be so sure of the future?";
    }
    else if (findKeyword(statement,"mr. kiang") >= 0
               || findKeyword(statement, "mr. landgraf") >= 0)
    {
      response = "Oh, he's a chill guy.";
    }
    else if (findKeyword(statement,"no")>= 0)
    {
      response = "Why so negative?";
    }
    else if (findKeyword(statement,"bobby") >= 0)
    {
      response = "Oh, Bobby? He's a cool guy, don't you think?";
    }
    else if (findKeyword(statement,"ting") >= 0)
    {
      response = "Oh, Ting? I heard he's usually toast.";
    }
    else if (findKeyword(statement,"melanie") >= 0
               ||findKeyword(statement,"mel") >= 0)
    {
      response = "Oh, Melanie? I heard she's an infinite elephant.";
    }
    else if (findKeyword(statement,"mother") >= 0
               || findKeyword(statement,"father") >= 0
               || findKeyword(statement,"sister") >= 0
               || findKeyword(statement,"brother") >= 0)
    {
      response = "Tell me more about your family.";
    }
    else if (findKeyword(statement,"cat") >= 0
               || findKeyword(statement,"dog") >= 0)
    {
      response = "Tell me more about your pets.";
    }
    
    // Responses which require transformations
       
    //Look for a (I am <something>) pattern
    else if (findKeyword(statement, "I am", 0) >= 0)
    {
      response = transformIAmStatement(statement);
    }
    
    //Look for a two word (I <something> you) pattern
    else if (findKeyword(statement, "i", 0) >= 0 && findKeyword(statement, "you", findKeyword(statement, "i", 0)) >= 0)
    {
      response = transformIYouStatement(statement);
    }
    
    //Look for a (I want to <something>) pattern
    else if (findKeyword(statement, "I want to", 0) >= 0)
    {
      response = transformIWantToStatement(statement);
    }
    
    //Look for a (I want <something>) pattern
    else if (findKeyword(statement, "I want", 0) >= 0)
    {
      response = transformIWantStatement(statement);
    }
    
    //Look for a (You <something> me) pattern
    else if (findKeyword(statement, "you", 0) >= 0 && findKeyword(statement, "me", findKeyword(statement, "you", 0)) >= 0)
    {
      response = transformYouMeStatement(statement);
    }

    //else returns default response
    else
    {
      response = getRandomResponse();
    }
    return response;
  }
  
  /**
   * Take a statement with "I want to <something>." and transform it into 
   * "What would it mean to <something>?"
   * @param statement the user statement, assumed to contain "I want to"
   * @return the transformed statement
   */
  private String transformIWantToStatement(String statement)
  {
    //  Remove the final period, if there is one
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword (statement, "I want to", 0);
    String restOfStatement = statement.substring(psn + 9).trim();
    return "What would it mean to " + restOfStatement + "?";
  }
  
  private String transformIWantStatement(String statement)
  {
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword(statement, "I want", 0);
    String restOfStatement = statement.substring(psn + 6).trim();
    return "Would you really be happy if you had " + restOfStatement + "?";
  }
  
  
 /**
   * Take a statement with "I am <something>." and transform it into 
   * "Do you like being <something>?"
   * In case it uses the "I am <verb>ing to" pattern, it returns a default response
   */
  private String transformIAmStatement(String statement)
  {
    if (statement.indexOf("ing") >= 0) // checks if it's not in "I am <non-gerund>"
      return getRandomResponse();
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword(statement, "I am", 0);
    String restOfStatement = statement.substring(psn + 4).trim();
    return "Do you like being " + restOfStatement + "?";
  }
  
  /**
   * Take a statement with "you <something> me" and transform it into 
   * "What makes you think that I <something> you?"
   * @param statement the user statement, assumed to contain "you" followed by "me"
   * @return the transformed statement
   */
  private String transformYouMeStatement(String statement)
  {
    //  Remove the final period, if there is one
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    
    int psnOfYou = findKeyword (statement, "you", 0);
    int psnOfMe = findKeyword (statement, "me", psnOfYou + 3);
    
    String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe).trim();
    return "What makes you think that I " + restOfStatement + " you?";
  }
  
  
  private String transformIYouStatement(String statement)
  {
    //  Remove the final period, if there is one
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    
    int psnOfI = findKeyword (statement, "i", 0);
    int psnOfYou = findKeyword (statement, "you", psnOfI + 1);
    
    String restOfStatement = statement.substring(psnOfI + 1, psnOfYou).trim();
    return "Why do you " + restOfStatement + " me?";
  }
  
  
  /**
   * Search for one word in phrase. The search is not case
   * sensitive. This method will check that the given goal
   * is not a substring of a longer string (so, for
   * example, "I know" does not contain "no").
   * 
   * @param statement
   *            the string to search
   * @param goal
   *            the string to search for
   * @param startPos
   *            the character of the string to begin the
   *            search at
   * @return the index of the first occurrence of goal in
   *         statement or -1 if it's not found
   */
  private int findKeyword(String statement, String goal,
                          int startPos)
  {
    String phrase = statement.trim();
    // The only change to incorporate the startPos is in
    // the line below
    int psn = phrase.toLowerCase().indexOf(
                                           goal.toLowerCase(), startPos);
    
    // Refinement--make sure the goal isn't part of a
    // word
    while (psn >= 0)
    {
      // Find the string of length 1 before and after
      // the word
      String before = " ", after = " ";
      if (psn > 0)
      {
        before = phrase.substring(psn - 1, psn)
          .toLowerCase();
      }
      if (psn + goal.length() < phrase.length())
      {
        after = phrase.substring(
                                 psn + goal.length(),
                                 psn + goal.length() + 1)
          .toLowerCase();
      }
      
      // If before and after aren't letters, we've
      // found the word
      if (((before.compareTo("a") < 0) || (before
                                             .compareTo("z") > 0)) // before is not a
            // letter
            && ((after.compareTo("a") < 0) || (after
                                                 .compareTo("z") > 0)))
      {
        return psn;
      }
      
      // The last position didn't work, so let's find
      // the next, if there is one.
      psn = phrase.indexOf(goal.toLowerCase(),
                           psn + 1);
      
    }
    
    return -1;
  }
  
  /**
   * Search for one word in phrase. The search is not case
   * sensitive. This method will check that the given goal
   * is not a substring of a longer string (so, for
   * example, "I know" does not contain "no"). The search
   * begins at the beginning of the string.
   * 
   * @param statement
   *            the string to search
   * @param goal
   *            the string to search for
   * @return the index of the first occurrence of goal in
   *         statement or -1 if it's not found
   */
  private int findKeyword(String statement, String goal)
  {
    return findKeyword(statement, goal, 0);
  }
  
  /**
   * Pick a default response to use if nothing else fits.
   * @return a non-committal string
   */
  private String getRandomResponse ()
  {
    Random r = new Random ();
    return randomResponses [r.nextInt(randomResponses.length)];
  }
  
  private String [] randomResponses = {
    "Interesting, tell me more",
    "Hmmm.",
    "Do you really think so?",
    "You don't say.",
    "Uh huh.",
    "I see.",
    "If you say so.",
    "Is that so."
  };
}