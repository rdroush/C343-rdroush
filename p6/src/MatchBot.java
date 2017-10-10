import java.util.ArrayList;
import java.util.List;

public class MatchBot extends TwitterBot {
  /**
   * Constructs a MatchBot to operate on the last numTweets of the given user.
   */
  public MatchBot(String user, int numTweets) {
    super(user, numTweets);
  }
  
  /**
   * Employs the KMP string matching algorithm to add all tweets containing 
   * the given pattern to the provided list. Returns the total number of 
   * character comparisons performed.
   * 
   * @param pattern The pattern to find in each tweet.
   * @param ans The list to add tweets containing the pattern.
   * 
   * @return The total number of character comparisons performed.
   */
  public int searchTweetsKMP(String pattern, List<String> ans) {
	int comps = 0;
	
	for(String tweet : tweets) {
		Result tweetResult = StringMatch.matchKMP(pattern, tweet);
		
		comps += tweetResult.comps;
		
		if(tweetResult.pos != -1)
			ans.add(tweet);
	}
	
    return comps;
  }
  
  /**
   * Employs the naive string matching algorithm to find all tweets containing 
   * the given pattern to the provided list. Returns the total number of 
   * character comparisons performed.
   * 
   * @param pattern The pattern to find in each tweet.
   * @param ans The list to add tweets containing the pattern.
   * 
   * @return The total number of character comparisons performed.
   */
  public int searchTweetsNaive(String pattern, List<String> ans) {
	int comps = 0;
	
	for(String tweet : tweets) {
		Result tweetResult = StringMatch.matchNaive(pattern, tweet);
		
		comps += tweetResult.comps;
		
		if(tweetResult.pos != -1)
			ans.add(tweet);
	}
	
    return comps;
  }
  
  /**
   * Employs the Boyer-Moore string matching algorithm to find all tweets containing 
   * the given pattern to the provided list. Returns the total number of 
   * character comparisons performed.
   * 
   * @param pattern The pattern to find in each tweet.
   * @param ans The list to add tweets containing the pattern.
   * 
   * @return The total number of character comparisons performed.
   */
  public int searchTweetsBM(String pattern, List<String> ans) {
	int comps = 0;
	
	for(String tweet : tweets) {
		Result tweetResult = StringMatch.matchBoyerMoore(pattern, tweet);
		
		comps += tweetResult.comps;
		
		if(tweetResult.pos != -1)
			ans.add(tweet);
	}
	
    return comps;
  }
    
  public static void main(String... args) {
    String handle = "realDonaldTrump", pattern = "North Korea";
    MatchBot bot = new MatchBot(handle, 50);
   
    // Search all tweets for the pattern.
    List<String> ansNaive = new ArrayList<>();
    int compsNaive = bot.searchTweetsNaive(pattern, ansNaive); 
    List<String> ansKMP = new ArrayList<>();
    int compsKMP = bot.searchTweetsKMP(pattern, ansKMP);  
    List<String> ansBM = new ArrayList<>();
    int compsBM = bot.searchTweetsBM(pattern, ansBM);  
    
    System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", BM comps = " + compsBM);

    for (int i = 0; i < ansKMP.size(); i++) {
      String tweet = ansKMP.get(i);
      assert tweet.equals(ansNaive.get(i));
      assert tweet.equals(ansBM.get(i));
      System.out.println(i++ + ". " + tweet);
      System.out.println(pattern + " appears at index " + 
          tweet.toLowerCase().indexOf(pattern.toLowerCase()));
    }

    for (int i = 0; i < ansBM.size(); i++) {
      String tweet = ansBM.get(i);
      assert tweet.equals(ansNaive.get(i));
      assert tweet.equals(ansKMP.get(i));
      System.out.println(i++ + ". " + tweet);
      System.out.println(pattern + " appears at index " + 
          tweet.toLowerCase().indexOf(pattern.toLowerCase()));
    }
    
  }
}
