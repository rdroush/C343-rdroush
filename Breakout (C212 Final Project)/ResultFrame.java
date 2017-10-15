/*
 * Reagan Roush, rdroush
 * Final Project - Breakout Result Frame
 * April 20, 2016
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ResultFrame extends JFrame {

  private JLabel resultLabel;
  
  // constructor
  public ResultFrame() {
    super("Your Result");
    
    this.resultLabel = new JLabel("", (int) CENTER_ALIGNMENT);
    
    this.add(resultLabel);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(300,100));
    this.pack();
  }
  
  // set text of the ResultLabel
  public void setText(String s) {
    this.resultLabel.setText(s);
  }
}
