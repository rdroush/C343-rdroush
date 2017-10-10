public class StringMatch {

  /**
   * Returns the result of running the naive algorithm to match pattern in text.
   * 
   * @param pattern The pattern to search for in the text.
   * @param text The text to find the pattern in.
   * 
   * @return The result of running the naive algorithm.
   */
  public static Result matchNaive(String pattern, String text) {  
	int m = pattern.length(), n = text.length();
	int i = 0, j = 0, comps = 0;
	
	while(i < m && j <= (n - m + i)) {
		comps++;
		if(pattern.charAt(i) == text.charAt(j)) {
			i++;
			j++;
		}
		else {
			j = j - i + 1;
			i = 0;
		}
	}
	
	if(i == m)
		return new Result(j - m, comps);
	
    return new Result(-1, comps);
  }
  
  /**
   * Populates flink with the failure links for the KMP machine associated with the
   * given pattern, and returns the cost in terms of the number of character comparisons.
   * 
   * @param pattern The pattern to build the failure link from.
   * @param flink The failure link to be populated.
   * 
   * @return The cost in terms of the number of character comparisons.
   */
  public static int buildKMP(String pattern, int[] flink) {
	if(flink.length >= 1)
		flink[0] = -1;
	if(flink.length >= 2)
		flink[1] = 0;
	
	int i = 2, comps = 0;
	
	while(i < flink.length) {
		int j = flink[i-1];
		
		while(j != -1 && pattern.charAt(j) != pattern.charAt(i - 1)) {
			comps++;
			j = flink[j];
		}

		if(j != -1)
			comps++;
		flink[i] = j + 1;
		i++;
	}
	
    return comps;
  }
  
  /**
   * Returns the result of running the KMP machine specified by flink (built for the
   * given pattern) on the text.
   * 
   * @param pattern The pattern to search for in the text.
   * @param text The text to find the pattern in.
   * @param flink The failure link for the given pattern.
   * 
   * @return The result of running the KMP machine.
   */
  public static Result runKMP(String pattern, String text, int[] flink) {
	int m = pattern.length(), n = text.length();
	int state = -1, j = -1;
	int comps = 0;
	
	while(true) {
		if(state != -1)
			comps++;
		
		if(state == -1 || text.charAt(j) == pattern.charAt(state)) {
			
			state++;
			if(state == m) return new Result(j - m + 1, comps);
			
			j++;
			if(j == n) break;
		}
		else state = flink[state];
	}
	
    return new Result(-1, comps);
  }
  
  /**
   * Returns the result of running the KMP algorithm to match pattern in text. The number
   * of comparisons includes the cost of building the machine from the pattern.
   * 
   * @param pattern The pattern to search for in the text.
   * @param text The text to find the pattern in.
   * 
   * @return The result of running the KMP algorithm to match pattern in text.
   */
  public static Result matchKMP(String pattern, String text) {
    int m = pattern.length();
    int[] flink = new int[m + 1];
    int comps = buildKMP(pattern, flink);
    Result ans = runKMP(pattern, text, flink);
    return new Result(ans.pos, comps + ans.comps);
  }
  
  /**
   * Populates delta1 with the shift values associated with each character in the
   * alphabet. Assume delta1 is large enough to hold any ASCII value.
   * 
   * @param pattern The pattern to build the delta1 table from.
   * @param delta1 The table to add shift values into.
   */
  public static void buildDelta1(String pattern, int[] delta1) {
	  int m = pattern.length();
	  
	  for(int i = 0; i < delta1.length; i++)
		  delta1[i] = m;
	  
	  for(int j = 1; j <= m; j++)
		  delta1[pattern.charAt(j-1)] = m - j;
  }

  /**
   * Returns the result of running the simplified Boyer-Moore algorithm using the
   * delta1 table from the pre-processing phase.
   * 
   * @param pattern The pattern to search for in the text.
   * @param text The text to find the pattern in.
   * @param delta1 The table to use shift values from.
   * 
   * @return The result of running the simplified Boyer-Moore algorithm.
   */
  public static Result runBoyerMoore(String pattern, String text, int[] delta1) {
	int m = pattern.length();
	int i = m-1;
	int j = m-1;
	int comps = 0;
	
	if(i == -1)
		return new Result(0, 0);
	
	while(j < text.length()) {
		comps++;
		
		if(pattern.charAt(i) == text.charAt(j)) {
			int tempI = i-1;
			int tempJ = j-1;
			int pMatch = 0;
			
			while(tempI >= -1) {
				if(tempI == -1) {
					return new Result(j - m + 1, comps);
				}
				comps++;
				if(pattern.charAt(tempI) == text.charAt(tempJ)) {
					tempI--;
					tempJ--;
					pMatch++;
				}
				else {
					j += Math.max(1, delta1[text.charAt(tempJ)] - pMatch - 1);
					break;
				}
			}
		}
		else if(text.charAt(j) >= delta1.length)
			j += m;
		else 
			j += delta1[text.charAt(j)];
	}
	
    return new Result(-1, comps);
  }
  
  /**
   * Returns the result of running the simplified Boyer-Moore algorithm to match 
   * pattern in text. 
   * 
   * @param pattern The pattern to search for in the text.
   * @param text The text to find the pattern in.
   * 
   * @return The result of running the simplified Boyer-Moore algorithm.
   */
  public static Result matchBoyerMoore(String pattern, String text) {
    int[] delta1 = new int[Constants.SIGMA_SIZE];
    buildDelta1(pattern, delta1);
    return runBoyerMoore(pattern, text, delta1);
  }

}
