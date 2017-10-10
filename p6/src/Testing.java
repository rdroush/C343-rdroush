import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class Testing {
  private static Random random = new Random();
  private static String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
  
  @Test
  public void testNaive() {
    String[] pats = new String[] {
        "AAAA",
        "BAAA",
        "AAAB",
        "AAAC",
        "ABAB",
    };
    String text = "AAAAAAAAABAAAAAAAAAB";
    assertEquals(20, text.length());
    Result[] results = new Result[] {
        new Result(0, 4),
        new Result(9, 13),
        new Result(6, 28),
        new Result(-1, 62),
        new Result(-1, 35),
    };
    int i = 0;
    for (String pat : pats) {
      Result res = StringMatch.matchNaive(pat, text);
      assertEquals(res.pos, results[i].pos);
      assertEquals(res.comps, results[i].comps);
      i++;
    }
  }
  
  @Test 
  public void smallBuildKMP() {
    String[] pats = new String[] {
        "",
        "A",
        "AB",
        "AA",
        "AAAA",
        "BAAA",
        "AAAB",
        "AAAC",
        "ABAB",
        "ABCD",
        "ABBA",
        "AABC",
        "ABAAB",
        "AABAACAABABA",
        "ABRACADABRA",
    };
    int[][] flinks = new int[][] {
        { -1 },
        { -1, 0 },
        { -1, 0, 0 },
        { -1, 0, 1 },
        { -1, 0, 1, 2, 3 },
        { -1, 0, 0, 0, 0 },
        { -1, 0, 1, 2, 0 },
        { -1, 0, 1, 2, 0 },
        { -1, 0, 0, 1, 2 },
        { -1, 0, 0, 0, 0 },
        { -1, 0, 0, 0, 1 },
        { -1, 0, 1, 0, 0 },
        { -1, 0, 0, 1, 1, 2 },
        { -1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1 },
        { -1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4 },
    };
    int[] comps = new int[] { 0, 0, 1, 1, 3, 3, 5, 5, 3, 3, 3, 4, 5, 16, 12 };
    int i = 0;
    for (String pat : pats) {
      int[] flink = new int[pat.length() + 1];
      assertEquals(comps[i], StringMatch.buildKMP(pat, flink));
      assertArrayEquals(flinks[i], flink);
      i++;
    }
  }

  @Test
  public void lec13bKMP() {
    String[] pats = new String[] {
        "AABC",
        "ABCDE",
        "AABAACAABABA",
        "ABRACADABRA",
    };
    int[][] flinks = new int[][] {
        { -1, 0, 1, 0, 0 },
        { -1, 0, 0, 0, 0, 0 },
        { -1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1 },
        { -1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4 },
    };
    String text = "AAAAAABRACADABAAAAAAAAAAAAAAAAAAAAAAABCAAAAAAAAAAABAABAAAAAAAAAAAAAAA";
    Result results[] = new Result[] {
        new Result(35, 68),
        new Result(-1, 128),
        new Result(-1, 123),
        new Result(-1, 126),
    };
    int i = 0;
    for (String pat : pats) {
      Result res = StringMatch.runKMP(pat, text, flinks[i]);
      assertEquals(results[i].pos, res.pos);
      assertEquals(results[i].comps, res.comps);
      i++;
    }
  }
  
  @Test
  public void lec13bBM() {
	String[] pats = new String[] {
		"ABBEBB",
		"BDCDDCD",
		"ABBCBB"
	};
	int[][] deltas = new int[][] {
		{5, 0, 6, 6, 2 },
		{7, 6, 1, 0, 7 },
		{5, 0, 2, 6, 6 }
	};
	for (int i = 0; i < 3; i++) {
		int[] curDelta = new int[Constants.SIGMA_SIZE];
		int startDelta = (int) 'A';
		
		StringMatch.buildDelta1(pats[i], curDelta);
		
		for(int j = startDelta; j <= (int) 'E'; j++)
			assertEquals(curDelta[j], deltas[i][j - startDelta]);
	}
  }
  
  @Test
  public void prelec14aBM() {
	  System.out.println("prelec14a");
	  String zeros = "00000000000000000000";
	  String[] strings = new String[] {
		"00000",
		"11111",
		"00011",
		"10100",
		"10000"
	  };
	  int[] expected = new int[] {5, 4, 8, 48, 80};
	  int i = 0;
	  for(String s : strings) {
		  int curComps = StringMatch.matchBoyerMoore(s, zeros).comps;
		  System.out.println(s + " comparisons: " + curComps);
		  assertEquals(expected[i], curComps);
		  i++;
	  }
	  System.out.println();
  }
  
  @Test 
  public void testEmpty() {
    System.out.println("testEmpty");
    match("", "");
    match("", "ab");
    match("a", "");
    System.out.println();
  }

  @Test 
  public void testOneChar() {
    System.out.println("testOneChar");
    match("a", "a");
    match("a", "b");
    System.out.println();
  }

  @Test 
  public void testRepeat() {
    System.out.println("testRepeat");
    match("aaa", "aaaaa");
    match("aaa", "abaaba");
    match("abab", "abacababc");
    match("abab", "babacaba");
    System.out.println();
  }

  @Test 
  public void testPartialRepeat() {
    System.out.println("testPartialRepeat");
    match("aaacaaaaac", "aaacacaacaaacaaaacaaaaac");
    match("ababcababdabababcababdaba", "ababcababdabababcababdaba");
    System.out.println();
  }

  @Test 
  public void testRandomly() {
    System.out.println("testRandomly");
    for (int i = 0; i < 100; i++) {
      String pattern = makeRandomPattern();
      for (int j = 0; j < 100; j++) {
        String text = makeRandomText(pattern);
        match(pattern, text);
      }
    }
    System.out.println();
  }

  /* Helper functions */

  private static String makeRandomPattern() {
    StringBuilder sb = new StringBuilder();
    int steps = random.nextInt(10) + 1;
    for (int i = 0; i < steps; i++) {
      if (sb.length() == 0 || random.nextBoolean()) {  // Add literal
        int len = random.nextInt(5) + 1;
        for (int j = 0; j < len; j++)
          sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
      } 
      else {  // Repeat prefix
        int len = random.nextInt(sb.length()) + 1;
        int reps = random.nextInt(3) + 1;
        if (sb.length() + len * reps > 1000)
          break;
        for (int j = 0; j < reps; j++)
          sb.append(sb.substring(0, len));
      }
    }
    return sb.toString();
  }

  private static String makeRandomText(String pattern) {
    StringBuilder sb = new StringBuilder();
    int steps = random.nextInt(100);
    for (int i = 0; i < steps && sb.length() < 10000; i++) {
      if (random.nextDouble() < 0.7) {  // Add prefix of pattern
        int len = random.nextInt(pattern.length()) + 1;
        sb.append(pattern.substring(0, len));
      } 
      else {  // Add literal
        int len = random.nextInt(30) + 1;
        for (int j = 0; j < len; j++)
          sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
      }
    }
    return sb.toString();
  }

  private static void match(String pattern, String text) {
    // run all three algorithms and test for correctness
    Result ansNaive = StringMatch.matchNaive(pattern, text);
    int expected = text.indexOf(pattern);
    assertEquals(expected, ansNaive.pos);
    Result ansKMP = StringMatch.matchKMP(pattern, text);
    assertEquals(expected, ansKMP.pos);
    Result ansBoyerMoore = StringMatch.matchBoyerMoore(pattern, text);
    assertEquals(expected, ansBoyerMoore.pos);
    System.out.println(String.format("%5d %5d %5d : %s", 
        ansNaive.comps, ansKMP.comps, ansBoyerMoore.comps,
        (ansNaive.comps < ansKMP.comps && ansNaive.comps < ansBoyerMoore.comps) ?
            "Naive" :
              (ansKMP.comps < ansNaive.comps && ansKMP.comps < ansBoyerMoore.comps) ?
                  "KMP" : "Boyer-Moore"));
  }
}
