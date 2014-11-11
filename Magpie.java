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
  public String getResponse(String temp)
  {
    Random r = new Random ();
    String response = "";
    String statement = expandContraction(temp);
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
      String [] willresponse = {
        "How can you be so sure of the future?",
        "You never know what the future will bring.",
        "An old Chinese proverb says the future is like the mist hovering over Mt. Song..."
      };
      response = willresponse[r.nextInt(willresponse.length)];
    }
    else if (findKeyword(statement, "thou") >= 0)
    {
      String [] thouresponse = {
        "Are we in 16th century England?",
        "Shakespeare called. He wants his words back."
      };
      response = thouresponse[r.nextInt(thouresponse.length)];
    }
    else if (findKeyword(statement, "ain't") >= 0)
    {
      String [] aintresponse = {
        "We ain't need no bad grammar here.",
        "Don't you dare use that kind of language in front of me!",
        "Watch your mouth."
      };
      response = aintresponse[r.nextInt(aintresponse.length)];
    }
    else if (findKeyword(statement,"mr. kiang") >= 0
               || findKeyword(statement, "mr. landgraf") >= 0)
    {
      String [] teacherresponse = {
        "Oh, he's a chill guy.",
        "Comp Sci is so much fun when he teaches!"
      };
      response = teacherresponse[r.nextInt(teacherresponse.length)];
    }
    else if (findKeyword(statement,"no")>= 0)
    {
      response = "Why so negative?";
    }
    else if (findKeyword(statement,"bobby") >= 0)
    {
      response = "He's awesome, nuff said.";
    }
    else if (findKeyword(statement,"ting") >= 0)
    {
      String [] tingresponse = {
        "He's toast.",
        "He's pretty cool",
        "He's usually toast, but sometimes cereal."
      };
      response = tingresponse[r.nextInt(tingresponse.length)];
    }
    else if (findKeyword(statement,"melanie") >= 0
               ||findKeyword(statement,"mel") >= 0)
    {
      response = "INFINITELEPHANT!!!";
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
    
    //Look for a (Why is <something>) pattern
    else if (findKeyword(statement, "Why is", 0) == 0)
    {
      response = transformWhyIsStatement(statement);
    }
    
    //Look for a (You are <something>) pattern
    else if (findKeyword(statement, "You are", 0) >= 0)
    {
      response = transformYouAreStatement(statement);
    }
    
    //Look for a (I am <something>) pattern
    else if (findKeyword(statement, "I am", 0) >= 0)
    {
      response = transformIAmStatement(statement);
    }
    
    //Look for a two word (I <something> you) pattern
    else if (findKeyword(statement, "I", 0) >= 0 && findKeyword(statement, "you", findKeyword(statement, "i", 0)) >= 0)
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
  
  
  
  //expands contractions
  public String expandContraction(String statement)
  {
    String temp = statement;
    
    //expands can't <- exception
    while (findKeyword(temp, "can't") >= 0) {
      int psn = findKeyword(temp, "can't"); //the position of the contraction
      String beginning = temp.substring(0,psn); //the piece before the contraction
      String contraction = "can't"; //the contraction
      String end = temp.substring(psn + 5); // the piece after the contraction
      contraction = contraction.substring(0,contraction.length() - 2) + "not"; //expands the contraction
      temp = beginning + contraction + end; //pieces together the new contraction
    }
    
    //expands I'm <- exception
    while (findKeyword(temp, "I'm") >= 0) {
      int psn = findKeyword(temp, "I'm"); //the position of the contraction
      String beginning = temp.substring(0,psn); //the piece before the contraction
      String contraction = "I'm"; //the contraction
      String end = temp.substring(psn + 3); // the piece after the contraction
      contraction = contraction.substring(0,contraction.length() - 2) + " am"; //expands the contraction
      temp = beginning + contraction + end; //pieces together the new contraction
    }
    
    String [] nPattern = { // the words that follow the pattern <something>n't = <something> not
      "aren't",
      "couldn't",
      "didn't",
      "doesn't",
      "don't",
      "hadn't",
      "hasn't",
      "haven't",
      "shouldn't",
      "weren't",
      "wouldn't",
      "isn't",
      "mustn't",
      "mightn't"
    };
    
    //expands all the contractions with the <something>n't pattern
    for (int i = 0; i < nPattern.length; i++) {
      while (findKeyword(temp, nPattern[i]) >= 0) { //while there are still contractions
        int psn = findKeyword(temp, nPattern[i]); //the position of the contraction
        String beginning = temp.substring(0,psn); //the piece before the contraction
        String contraction = temp.substring(psn,psn + nPattern[i].length()); //the contraction
        String end = temp.substring(psn + contraction.length()); // the piece after the contraction
        contraction = contraction.substring(0,contraction.length() - 3) + " not"; //expands the contraction
        temp = beginning + contraction + end; //pieces together the new contraction
      }
    }
    
    String [] rePattern = { // the words that follow the pattern <something>'re = <something> are
      "you're",
      "we're",
      "they're"
    };
    
    //expands all the contractions with the <something>'re pattern
    for (int i = 0; i < rePattern.length; i++) {
      while (findKeyword(temp, rePattern[i]) >= 0) { //while there are still contractions
        int psn = findKeyword(temp, rePattern[i]); //the position of the contraction
        String beginning = temp.substring(0,psn); //the piece before the contraction
        String contraction = temp.substring(psn,psn + rePattern[i].length()); //the contraction
        String end = temp.substring(psn + contraction.length()); // the piece after the contraction
        contraction = contraction.substring(0,contraction.length() - 3) + " are"; //expands the contraction
        temp = beginning + contraction + end; //pieces together the new contraction
      }
    }
    
    String [] sPattern = { // the words that follow the pattern <something>'s = <something> is
      "he's",
      "she's",
      "it's",
      "that's",
      "who's",
      "what's",
      "when's",
      "where's",
      "why's",
      "how's"
    };
    
    //expands all the contractions with the <something>'s pattern
    for (int i = 0; i < sPattern.length; i++) {
      while (findKeyword(temp, sPattern[i]) >= 0) { //while there are still contractions
        int psn = findKeyword(temp, sPattern[i]); //the position of the contraction
        String beginning = temp.substring(0,psn); //the piece before the contraction
        String contraction = temp.substring(psn,psn + sPattern[i].length()); //the contraction
        String end = temp.substring(psn + contraction.length()); // the piece after the contraction
        contraction = contraction.substring(0,contraction.length() - 2) + " is"; //expands the contraction
        temp = beginning + contraction + end; //pieces together the new contraction
      }
    }
    
    String [] willPattern = { // the words that follow the pattern <something>'ll = <something> will
      "I'll",
      "you'll",
      "he'll",
      "she'll",
      "it'll",
      "we'll",
      "they'll",
      "that'll",
      "who'll",
      "what'll",
      "when'll",
      "where'll",
      "why'll",
      "how'll"
    };
    
    //expands all the contractions with the <something>'ll pattern
    for (int i = 0; i < willPattern.length; i++) {
      while (findKeyword(temp, willPattern[i]) >= 0) { //while there are still contractions
        int psn = findKeyword(temp, willPattern[i]); //the position of the contraction
        String beginning = temp.substring(0,psn); //the piece before the contraction
        String contraction = temp.substring(psn,psn + willPattern[i].length()); //the contraction
        String end = temp.substring(psn + contraction.length()); // the piece after the contraction
        contraction = contraction.substring(0,contraction.length() - 3) + " will"; //expands the contraction
        temp = beginning + contraction + end; //pieces together the new contraction
      }
    }
    
    String [] vePattern = { // the words that follow the pattern <something>'ve = <something> have
      "I've",
      "you've",
      "we've",
      "they've",
      "would've",
      "should've",
      "could've",
      "might've",
      "must've"
    };
    
    //expands all the contractions with the <something>'ve pattern
    for (int i = 0; i < vePattern.length; i++) {
      while (findKeyword(temp, vePattern[i]) >= 0) { //while there are still contractions
        int psn = findKeyword(temp, vePattern[i]); //the position of the contraction
        String beginning = temp.substring(0,psn); //the piece before the contraction
        String contraction = temp.substring(psn,psn + vePattern[i].length()); //the contraction
        String end = temp.substring(psn + contraction.length()); // the piece after the contraction
        contraction = contraction.substring(0,contraction.length() - 3) + " have"; //expands the contraction
        temp = beginning + contraction + end; //pieces together the new contraction
      }
    }
    
    return temp;
  }
  
  private String transformWhyIsStatement(String statement)
  {
    //  Remove the final period, if there is one
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("?"))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword (statement, "Why is", 0);
    String restOfStatement = statement.substring(psn + 6).trim();
    return "Is " + restOfStatement + "? I humbly suggest you go Google it.";
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
   * Take a statement with "You are <something>." and transform it into 
   * "How am I <something>?"
   */
  private String transformYouAreStatement(String statement)
  {
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword(statement, "You are", 0);
    if (findKeyword(statement, "me", psn) > 0) { // if the sentence has "me" after "You are"
      int metoyou = findKeyword(statement, "me"); // "me" will be replaced with "you"
      String youarestart = statement.substring(0,metoyou); // part before "me"
      String youareend = statement.substring(metoyou + 2); // part after "me"
      statement = youarestart + "you" + youareend;
    }
    String restOfStatement = statement.substring(psn + 7).trim();
    return "How am I " + restOfStatement + "?";
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
    Random q = new Random ();
    return randomResponses [q.nextInt(randomResponses.length)];
  }
  
  private String [] randomResponses = {
    "Interesting, tell me more",
    "Hmmm.",
    "Do you really think so?",
    "You don't say.",
    "Uh huh.",
    "I see.",
    "If you say so.",
    "Is that so.",
    "...."
  };
}