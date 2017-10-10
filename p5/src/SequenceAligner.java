import java.util.Random;

/**
 * This class implements the SequenceAligner, used to find 
 * the optimal alignment of two DNA strings.
 * 
 * @author Reagan Roush
 */

public class SequenceAligner {
  private static Random gen = new Random();

  private String x, y;
  private int n, m;
  private String alignedX, alignedY;
  private Result[][] cache;
  private Judge judge;

  /**
   * Generates a pair of random DNA strands, where x is of length n and
   * y has some length between n/2 and 3n/2, and aligns them using the 
   * default judge.
   * 
   * @param n The length of strand x.
   */
  public SequenceAligner(int n) {
    this(randomDNA(n), randomDNA(n - gen.nextInt(n / 2) * (gen.nextInt(2) * 2 - 1)));
  }

  /**
   * Aligns the given strands using the default judge.
   * 
   * @param x The first string to align.
   * @param y The second string to align.
   */
  public SequenceAligner(String x, String y) {
    this(x, y, new Judge());
  }
  
  /**
   * Aligns the given strands using the specified judge.
   * 
   * @param x The first string to align.
   * @param y The second string to align.
   * @param judge The custom judge to use.
   */
  public SequenceAligner(String x, String y, Judge judge) {
    this.x = x.toUpperCase();
    this.y = y.toUpperCase();
    this.judge = judge;
    n = x.length();
    m = y.length();
    cache = new Result[n + 1][m + 1];
    fillCache();
    traceback();
  }

  /**
   * Returns the x strand.
   * 
   * @return The x strand.
   */
  public String getX() {
    return x;
  }

  /**
   * Returns the y strand.
   * 
   * @return The y strand.
   */
  public String getY() {
    return y;
  }
  
  /**
   * Returns the judge associated with this pair.
   * 
   * @return The judge associated with this pair.
   */
  public Judge getJudge() {
    return judge;
  }
  
  /**
   * Returns the aligned version of the x strand.
   * 
   * @return The aligned version of the x strand.
   */
  public String getAlignedX() {
    return alignedX;
  }

  /**
   * Returns the aligned version of the y strand.
   * 
   * @return The aligned version of the y strand.
   */
  public String getAlignedY() {
    return alignedY;
  }

  /**
   *  Builds up the cache, such that cache[i][j] will hold the 
   *  result of solving the alignment problem for the first i 
   *  characters in x and the first j characters in y.
   *  
   *  We establish the following preferred order of operations: 
   *  M (diag), I (left), D (up). 
   */
  private void fillCache() {
	  cache[0][0] = new Result(0, Direction.NONE);
	  for(int i = 1; i < cache.length; i++)
		  cache[i][0] = new Result((i*judge.getGapCost()), Direction.UP);
	  for(int j = 1; j < cache[0].length; j++)
		  cache[0][j] = new Result((j*judge.getGapCost()), Direction.LEFT);
	  for(int a = 1; a < cache.length; a++) {
		  for(int b = 1; b < cache[a].length; b++) {
			  int diagVal = cache[a-1][b-1].getScore() + judge.score(x.charAt(a-1), y.charAt(b-1));
			  int leftVal = cache[a][b-1].getScore() + judge.getGapCost();
			  int upVal = cache[a-1][b].getScore() + judge.getGapCost();
			  int maxVal = Math.max(diagVal, Math.max(leftVal, upVal));
			  
			  Direction maxDir = Direction.NONE;
			  if(maxVal == diagVal)
				  maxDir = Direction.DIAGONAL;
			  else if(maxVal == leftVal)
				  maxDir = Direction.LEFT;
			  else if(maxVal == upVal)
				  maxDir = Direction.UP;
			  
			  cache[a][b] = new Result(maxVal, maxDir);
		  }
	  }
  }
  
  /**
   * Returns the result of solving the alignment problem for the 
   * first i characters in x and the first j characters in y.
   * 
   * @return The result of solving the alignment problem for the 
   * first i characters in x and the first j characters in y.
   */
  public Result getResult(int i, int j) {
    return cache[i][j];
  }
  
  /**
   * Marks the path of the optimal alignment by tracing back 
   * through parent pointers, starting with the Result in the
   * lower right corner of the cache. 
   * 
   * The GUI will highlight all such marked cells when you check 'Show path'.
   *  
   * The aligned strings are built in this process, adding a 
   * gap character when moving up (to X) or left (to Y).
   */
  private void traceback() {
	  int xPos = n;
	  int yPos = m;
	  StringBuilder xStr = new StringBuilder();
	  StringBuilder yStr = new StringBuilder();
	  Result curResult = cache[n][m];
	  while(curResult.getParent() != Direction.NONE) {
		  curResult.markPath();
		  if(curResult.getParent() == Direction.DIAGONAL) {
			  xStr.append(x.charAt(xPos-1));
			  yStr.append(y.charAt(yPos-1));
			  xPos--;
			  yPos--;
		  }
		  else if(curResult.getParent() == Direction.LEFT) {
			  xStr.append(Constants.GAP_CHAR);
			  yStr.append(y.charAt(yPos-1));
			  yPos--;
		  }
		  else if(curResult.getParent() == Direction.UP) {
			  xStr.append(x.charAt(xPos-1));
			  yStr.append(Constants.GAP_CHAR);
			  xPos--;
		  }
		  
		  curResult = cache[xPos][yPos];
	  }
	  cache[0][0].markPath();
	  alignedX = xStr.reverse().toString();
	  alignedY = yStr.reverse().toString();
  }

  /**
   * Returns true iff these strands are seemingly aligned.
   * 
   * @return true iff these strands are seemingly aligned.
   */
  public boolean isAligned() {
    return alignedX != null && alignedY != null &&
        alignedX.length() == alignedY.length();
  }
  
  /**
   * Returns the score associated with the current alignment.
   * 
   * @return the score associated with the current alignment.
   */
  public int getScore() {
    if (isAligned())
      return judge.score(alignedX, alignedY);
    return 0;
  }

  /**
   * Returns a nice textual version of this alignment.
   * 
   * @return a nice textual version of this alignment.
   */
  public String toString() {
    if (!isAligned())
      return "[X=" + x + ",Y=" + y + "]";
    final char GAP_SYM = '.', MATCH_SYM = '|', MISMATCH_SYM = ':';
    StringBuilder ans = new StringBuilder();
    ans.append(alignedX).append('\n');
    int n = alignedX.length();
    for (int i = 0; i < n; i++)
      if (alignedX.charAt(i) == Constants.GAP_CHAR || alignedY.charAt(i) == Constants.GAP_CHAR)
        ans.append(GAP_SYM);
      else if (alignedX.charAt(i) == alignedY.charAt(i))
        ans.append(MATCH_SYM);
      else
        ans.append(MISMATCH_SYM);
    ans.append('\n').append(alignedY).append('\n').append("score = ").append(getScore());
    return ans.toString();
  }

  /**
   * Returns a DNA strand of length n with randomly selected nucleotides.
   * 
   * @return a DNA strand of length n with randomly selected nucleotides.
   */
  private static String randomDNA(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++)
      sb.append("ACGT".charAt(gen.nextInt(4)));
    return sb.toString();
  }

}
