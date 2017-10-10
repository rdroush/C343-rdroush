import java.util.HashMap;

/**
 * A FrequencyTable associates each character in a given text string
 * with its frequency within the string using an extended HashMap.
 */
public class FrequencyTable extends HashMap<Character, Integer> {
  /**
   * Constructs an empty table.
   */
  public FrequencyTable() {
    super();
  }
    
  /**
   * Constructs a table of character counts from the given text string.
   * 
   * @param text A string to construct a FrequencyTable from.
   */
  public FrequencyTable(String text) {
	  char[] chars = text.toCharArray();
	  for(char c : chars) {
		  int count = get(c);
		  if(count == 0)
			  put(c, 1);
		  else
			  put(c, count+1);
	  }
  }
  
  /**
   * Returns the count associated with the given character. In the case that
   * there is no association of ch in the map, return 0.
   * 
   * @param ch The character to get the count of.
   * @return the count associated with the given character.
   */
  @Override
  public Integer get(Object ch) {
	  if(containsKey(ch))
		  return super.get(ch);
	  
	  return 0;
  }

}
