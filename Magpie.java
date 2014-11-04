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
    else if (statement.indexOf("Mr. Kiang") >= 0
               || statement.indexOf("Mr. Landgraf") >= 0)
    {
      response = "He sounds like a chill guy.";
    }
    else if (statement.indexOf("no") >= 0)
    {
      response = "Why so negative?";
    }
    else if (statement.indexOf("Bobby") >= 0)
    {
      response = "Oh, Bobby? He's a cool guy, don't you think?";
    }
    else if (statement.indexOf("Ting") >= 0)
    {
      response = "Oh, Ting? I heard he's usually toast.";
    }
    else if (statement.indexOf("Melanie") >= 0
               || statement.indexOf("Mel") >= 0)
    {
      response = "Oh, Melanie? I heard she's an infinite elephant.";
    }
    else if (statement.indexOf("mother") >= 0
               || statement.indexOf("father") >= 0
               || statement.indexOf("sister") >= 0
               || statement.indexOf("brother") >= 0)
    {
      response = "Tell me more about your family.";
    }
    else if (statement.indexOf("cat") >= 0
               || statement.indexOf("dog") >= 0)
    {
      response = "Tell me more about your pets.";
    }
    else
    {
      response = getRandomResponse();
    }
    return response;
  }
  
  /**
   * Pick a default response to use if nothing else fits.
   * @return a non-committal string
   */
  private String getRandomResponse()
  {
    final int NUMBER_OF_RESPONSES = 6;
    double r = Math.random();
    int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
    String response = "";
    
    if (whichResponse == 0)
    {
      response = "Interesting, tell me more.";
    }
    else if (whichResponse == 1)
    {
      response = "Hmmm.";
    }
    else if (whichResponse == 2)
    {
      response = "Do you really think so?";
    }
    else if (whichResponse == 3)
    {
      response = "You don't say.";
    }
    else if (whichResponse == 4)
    {
      response = "Uh huh.";
    }
    else if (whichResponse == 5)
    {
      response = "I see.";
    }
    return response;
  }
}
