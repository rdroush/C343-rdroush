/**
 * @author Reagan Roush
 */

public class Driver {
  
  private static int numCollisions;
  
  /**
   * Return the ColorTable associated with this image, assuming the color key space
   * is restricted to bitsPerChannel. Increment numCollisions after each increment.
   */
  public static ColorTable vectorize(Image image, int bitsPerChannel) {
	  ColorTable table = new ColorTable(1, bitsPerChannel, Constants.QUADRATIC, 0.49);
	  int y = 0;

	  for(int x = 0; x < image.getWidth(); x++) {
		  table.increment(image.getColor(x, y));
		  //System.out.println("Incrementing color " + image.getColor(x, y) + "at x=" + x + ", y=" + y);
		  
		  if(x == image.getWidth()-1 && y < image.getHeight()-1) {
			  y++;
			  x = -1;
		  }
	  }
	  
	  numCollisions = table.getNumCollisions();
	  
    return table;
  }

  /**
   * Return the result of running Util.cosineSimilarity() on the vectorized images.
   * 
   * Note: If you compute the similarity of an image with itself, it should be close to 1.0.
   */
  public static double similarity(Image image1, Image image2, int bitsPerChannel) {
	  ColorTable table1 = vectorize(image1, bitsPerChannel);
	  ColorTable table2 = vectorize(image2, bitsPerChannel);
	  
    return Util.cosineSimilarity(table1, table2);
  }

  /**
   * Uses the Painting images and all 8 bitsPerChannel values to compute and print 
   * out a table of collision counts.
   */
  public static void allPairsTest() {
    Painting[] paintings = Painting.values();
    int n = paintings.length;
    for (int y = 0; y < n; y++) {
      for (int x = y + 1; x < n; x++) {
        System.out.println(paintings[y].get().getName() + 
            " and " + 
            paintings[x].get().getName() + ":");
        for (int bitsPerChannel = 1; bitsPerChannel <= 8; bitsPerChannel++) {
          numCollisions = 0;
          System.out.println(String.format("   %d: %.2f %d", 
              bitsPerChannel,
              similarity(paintings[x].get(), paintings[y].get(), bitsPerChannel),
              numCollisions));
        }
        System.out.println();
      }
    }
  }

  /**
   * Simple testing
   */  
  public static void main(String[] args) {
    System.out.println(Constants.TITLE);
    Image mona = Painting.MONA_LISA.get();
    Image starry = Painting.STARRY_NIGHT.get();
    Image christina = Painting.CHRISTINAS_WORLD.get();
    System.out.println("It looks like all three test images were successfully loaded.");
    System.out.println("mona's dimensions are " + 
        mona.getWidth() + " x " + mona.getHeight());
    System.out.println("starry's dimenstions are " + 
        starry.getWidth() + " x " + starry.getHeight());
    System.out.println("christina's dimensions are " + 
        christina.getWidth() + " x " + christina.getHeight());
    allPairsTest();
  }
}
