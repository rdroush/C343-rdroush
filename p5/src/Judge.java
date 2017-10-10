/**
 * This class is used to score alignments. 
 * 
 * @author Reagan Roush
 */

public class Judge {

  public static final int DEFAULT_MATCH_COST = 2;
  public static final int DEFAULT_MISMATCH_COST = -2;
  public static final int DEFAULT_GAP_COST = -1;
  
  private int matchCost, mismatchCost, gapCost;
  
  /**
   * Creates the default judge.
   */
  public Judge() {
    this(DEFAULT_MATCH_COST, DEFAULT_MISMATCH_COST, DEFAULT_GAP_COST);
  }
  
  /**
   * Creates a judge using the specified costs.
   * 
   * @param matchCost The match cost to use.
   * @param mismatchCost The mismatch cost to use.
   * @param gapCost The gap cost to use.
   */
  public Judge(int matchCost, int mismatchCost, int gapCost) {
    this.matchCost = matchCost;
    this.mismatchCost = mismatchCost;
    this.gapCost = gapCost;
  }
  
  /**
   * Returns the gap cost used by this judge.
   * 
   * @return the gap cost used by this judge.
   */
  public int getGapCost() {
    return gapCost;
  }
  
  /**
   * Returns the match cost used by this judge.
   * 
   * @return the match cost used by this judge.
   */
  public int getMatchCost() {
    return matchCost;
  }
  
  /**
   * Returns the mismatch cost used by this judge.
   * 
   * @return the mismatch cost used by this judge.
   */
  public int getMismatchCost() {
    return mismatchCost;
  }
  
  /**
   * Returns the score associated with the two characters.
   * 
   * @param a The first character to compare.
   * @param b The second character to compare.
   * @return the score associated with the two characters.
   */
  public int score(char a, char b) {
	if(a == '_' || b == '_')
		return gapCost;
	else if(a == b)
		return matchCost;
	
    return mismatchCost;
  }
  
  /**
   * Returns the score associated with the two strings.
   * 
   * @param a The first string to compare.
   * @param b The second string to compare.
   * @return the score associated with the two strings.
   */
  public int score(String s1, String s2) {
	if(s1.length() != s2.length())
		throw new IllegalArgumentException("Strings are not of equal length!");
	
	int sum = 0;
	
	for(int i = 0; i < s1.length(); i++)
		sum += score(s1.charAt(i), s2.charAt(i));
	
    return sum;
  }
}
