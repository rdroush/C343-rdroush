/*
 * Reagan Roush, rdroush
 * Final Project - Breakout
 * Based on earlier Pong assignment
 * April 20, 2016
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

public class Breakout extends JFrame {
  
  JPanel breakoutPanel;
  
  //constructor
  public Breakout() {
        super("Breakout!");

        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(new BreakoutPanel());
        this.pack();
        this.setVisible(true);
  }

  //test client
  public static void main(String[] args) { 
    JFrame breakout = new Breakout();
  }
}
