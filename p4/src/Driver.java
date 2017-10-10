/**
 * Running this driver will print some statistics about a small sample
 * text.
 */

public class Driver {
  public static void main(String[] args) {
    System.out.println(Constants.TITLE);
    /*
    String text = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        + "bbbbbbbbbbbbbb"
        + "cccccccccccc"
        + "ddddddddddddddddddddd" 
        + "eeeeeeeee"
        + "fffff";
    */
    // String text = Util.loadFile(Constants.ALICE);
    // String text = Util.loadFile(Constants.MOBY_DICK);
    String text = Util.loadFile(Constants.TOM_SAWYER);
    
    System.out.println();
    System.out.println("The original text has " + text.length() + " characters.");
    CodeBook book = new CodeBook(text);
    System.out.println(String.format("The average length of a code is %.2f bits.",
        book.getWeightedAverage()));
    Zipper zipper = new Zipper(book);
    String bits = zipper.encode(text);
    System.out.println("The text is encoded in " + bits.length() + " bits.");
    System.out.println("The savings is " + 
        (text.length() * Constants.BITESIZE - bits.length()) + " bits.");
    String packing = zipper.compress(bits);
    System.out.println("The compressed text is encoded in " + packing.length() + " characters.");
    System.out.println("The savings is " + 
        (text.length() - packing.length()) + " characters.");    
    String unpacking = zipper.decompress(packing);
    System.out.println("Decompressing yields " + unpacking.length() + " bits.");
    String recoveredText = zipper.decode(unpacking);
    System.out.println("The recovered text has " + recoveredText.length() + " characters.");
    assert recoveredText.equals(text);    
    System.out.println("\nAll tests passed...");
  }
}

