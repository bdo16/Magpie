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
    String statement = expandContraction(temp); //expands contractions
    statement = statement.trim(); // to stop Brent, trims at the beginning
    if (statement.length() == 0)
    {
      response = "Say something, please.";
    }
    // just a funny thing
    else if (statement.length() > 30) { //will fall asleep if the statement is too long
      int halfwaypoint = statement.length() / 2;
      String lastpart = statement.substring(halfwaypoint);
      int lastword = lastpart.indexOf(" ");
      response = "Sorry, I fell asleep around \"" + statement.substring(0, halfwaypoint + lastword) + "...\"";
    }
    else if (findKeyword(statement, "thou") >= 0)
    {
      String [] thouresponse = {
        "Are we in 16th century England?",
        "Shakespeare called. He wants his words back.",
        "THOU ART NOT AN ENGLISH MAJOR!"
      };
      response = thouresponse[r.nextInt(thouresponse.length)];
    }
    else if (findKeyword(statement, "ain't") >= 0)
    {
      String [] aintresponse = {
        "We ain't need no bad grammar here.",
        "Don't you dare use that kind of language in front of me!",
        "Watch your mouth.",
        "Howdy y'all"
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
        "He's pretty cool.",
        "He's usually toast, but sometimes bread."
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
    
    //Look for a (I/You/we/they <aux verb>(optional) <verb>) pattern
    
    else if (findKeyword(statement, "I") == 0 || 
            findKeyword(statement, "You") == 0 ||
             findKeyword(statement, "they") == 0 ||
            findKeyword(statement, "we") == 0 )
             //statement.substring(statement.indexOf(" ") - 1, statement.indexOf(" ")) == "s")
      //experimental code that will check to see if it is a plural noun (Monkeys)
    {
      response = transformPluralVerbStatement(statement);
    }
   
    //Look for a (Why is <something>) pattern
    else if (findKeyword(statement, "Why is", 0) == 0)
    {
      response = transformWhyIsStatement(statement);
    }
    
    //Look for a (<something> is <something>) pattern
    else if (findKeyword(statement, "is", 0) >= 0)
    {
      response = transformIsStatement(statement);
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
  
  
  
  
  
  /**
   *transforms a "<plural noun> <aux> <statement>" statement to "Why <aux> <plural> <statement>?"
   * for example, "I will eat pie" becomes "Why will you eat pie?"
   */
  private String transformPluralVerbStatement(String statement) // for a I <aux> <verb> statement
  {
    statement = statement.trim();
    String statementsubject = ""; // the subject in the sentence (*I* eat cake)
    String subject = ""; // the subject used in the response (Why do *you* eat cake?)
    if (findKeyword(statement, "I") == 0)
    {
      statementsubject = "I";
      subject = "you";
    }
    if (findKeyword(statement, "You") == 0)
    {
      statementsubject = "you";
      subject = "I";
    }
    if (findKeyword(statement, "They") == 0)
    {
      statementsubject = "they";
      subject = "they";
    }
    if (findKeyword(statement, "we") == 0)
    {
      statementsubject = "we";
      subject = "we";
    }
  // if (statement.substring(statement.indexOf(" ") - 1, statement.indexOf(" ")) == "s") { // just a plural subject (Monkeys eat pie)
  //    statementsubject = statement.substring(0, statement.indexOf(" "));
  //    subject = statement.substring(0, statement.indexOf(" "));
  // }
    String restofsentence = ""; // the part of the sentence after the subject and aux verb
    String auxiliary = "do"; // the auxiliary verb - defaults to "do" for sentences like "I eat pizza"
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    
    String willbreak = "break"; //this weird while loop is used to ensure that the aux verb is only checked once
    while (willbreak == "break") { //as the for loop was messing up the else if statements
      
      String [] auxiliaryverbs = {
        "do",
        "can",
        "could",
        "might",
        "must",
        "shall",
        "will",
        "would",
        "are" // will not be tested for "you" as the subject
      };
      
      if (findKeyword(statement, "I am") == 0){ // if the statement begins with (I am hungry)
        auxiliary = "are"; // the response should be "Why *are* you hungry"
        statement = replaceYouOrMe(statement, 1); //switches "you" and "me" 
        restofsentence = statement.substring(findKeyword(statement, "am") + 2); // the " hungry" part
        break;
      }
      
      if (findKeyword(statement, "You are") == 0){ // if the statement begins with (You are hungry)
        auxiliary = "am"; // the response should be "Why *am* I hungry"
        statement = replaceYouOrMe(statement, 1);
        restofsentence = statement.substring(findKeyword(statement, "are") + 3); // the " hungry" part
        break;
      }
      
      for (int i = 0; i < auxiliaryverbs.length; i++) { //checks through the array of aux verbs
        if (findKeyword(statement, auxiliaryverbs[i]) == statementsubject.length() + 1){ // if there is the aux verb
          auxiliary = auxiliaryverbs[i]; 
          statement = replaceYouOrMe(statement, 1);
          restofsentence = statement.substring(findKeyword(statement, auxiliaryverbs[i]) + auxiliaryverbs[i].length());
          break;  
        }
        else
          statement = replaceYouOrMe(statement, 1);
          restofsentence = statement.substring(statementsubject.length());
      }
      willbreak = "notbreak"; // breaks out of the while loop
    } 
    
    return "Why " + auxiliary + " " + subject + restofsentence + "?"; // for example, "Why are you hungry?"
  }
  
  
  /**
   * Take a statement with "Why is <something>." and transform it into 
   * "Is <something>? I humbly suggest you go Google it."
   * replaces the object "you" with "me", and the object "me" with "you"
   */
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
    statement = replaceYouOrMe(statement, psn);
    String restOfStatement = statement.substring(psn + 6).trim();
    return "Is " + restOfStatement + "? I humbly suggest you go Google it.";
  }
  
  
  
  
  /**
   * Take a statement with "<something1> is <something2>." and transform it into 
   * "Why is <something1> <something2>?"
   * If object is "you", it is replaced with "me"
   * If object is "me", it is replaced with "you"
   */
  private String transformIsStatement(String statement)
  {
    //  Remove the final period, if there is one
    statement = statement.trim();
    String lastChar = statement.substring(statement.length() - 1);
    if (lastChar.equals("."))
    {
      statement = statement.substring(0, statement.length() - 1);
    }
    int psn = findKeyword (statement, "is", 0);
    statement = replaceYouOrMe(statement, psn);
    String isstart = statement.substring(0,psn);
    isstart = isstart.substring(0,1).toLowerCase() + isstart.substring(1); // lowercases the first part
    String isend = statement.substring(psn + 3);
    return "Why is " + isstart + isend + "?";
  }
  
  
  
  
  
  //checks from the starting index psn and switches all "you"s and "me"s
  private String replaceYouOrMe (String statement, int psn) {     
    int psnofyoume = psn;
    while (findKeyword(statement, "me", psnofyoume) > 0 || findKeyword(statement, "you", psnofyoume) > 0)
    { // while there is still a "me"or "you" that was unchanged 
      String whichisfirst = ""; // "me" if "me" comes first, and "" if "you" comes first
      Boolean meishere = false; //whether "me" is found from the index psnofyoume
      Boolean youishere = false;//whether "you" is found from the index psnofyoume
      if (findKeyword(statement, "me", psnofyoume) >= 0)
        meishere = true; // there is "me"
      if (findKeyword(statement, "you", psnofyoume) >= 0)
        youishere = true; //there is "you"
      if (meishere && youishere && findKeyword(statement, "me", psnofyoume) < findKeyword(statement, "you", psnofyoume))
        whichisfirst = "me"; // if both "me" and "you" are there, but "me" is first
      if (meishere && youishere && findKeyword(statement, "you", psnofyoume) < findKeyword(statement, "me", psnofyoume))
        whichisfirst = ""; // if both "me" and "you" are there, but "you" is first
      if (meishere && !youishere)
        whichisfirst = "me"; //if only "me" is there
      if (youishere && !meishere)
        whichisfirst = ""; //if only "you" is there
      if (whichisfirst == "me") { // if the first keyword is "me"
        int psnofme = findKeyword(statement, "me", psnofyoume);
        String beforeme = statement.substring(0,psnofme); //part before "me"
        String afterme = statement.substring(psnofme + 2); //part after "me"
        statement =  beforeme + "you" + afterme;
        psnofyoume = findKeyword(statement, "you", psnofyoume) + 1; 
      }
      else {
        int psnofyou = findKeyword(statement, "you", psnofyoume); // "you" will be replaced with "me"
        String beforeyou = statement.substring(0,psnofyou); // part before "you"
        String afteryou = statement.substring(psnofyou + 3); // part after "you"
        statement = beforeyou + "me" + afteryou;
        psnofyoume = findKeyword(statement, "me", psnofyoume) + 1;
      }
    }
    return statement;
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