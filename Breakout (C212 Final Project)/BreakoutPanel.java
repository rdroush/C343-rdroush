/*
 * Reagan Roush, rdroush
 * Final Project - Breakout
 * Based on earlier Pong assignment
 * April 20, 2016
 */
import java.util.Random;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class BreakoutPanel extends JPanel implements ActionListener {
    
   public final int FRAME_WIDTH = 1000;
   public final int FRAME_HEIGHT = 800;

   private int timerSpeed;
   private int boxesWidth;
   private int boxesHeight;
   private Color boxColor;
   private Random random;
   private Dimension size;  
   private Timer timer;
   private Circle ball;
   private Rectangles paddle;
   private ArrayList<Rectangles> boxes;
   private ResultFrame resultFrame;

   //constructor
   public BreakoutPanel() {
        super(); 

        addKeyListener(new PaddleKeyListener());

        this.timerSpeed = 5;
        this.boxesWidth = 10;
        this.boxesHeight = 8;
        this.random = new Random(4500);   
        this.size = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
        this.timer = new Timer(timerSpeed, this);
        this.ball = new Circle(Color.white, (FRAME_WIDTH / 2), 400, 0, 2, 20);
        this.paddle = new Rectangles(Color.red, 400, 750, 0, 0, 150, 10);
        this.boxes = new ArrayList<Rectangles>(0);
        this.resultFrame = new ResultFrame();
        
        for(int i = 0; i < boxesHeight; i++) {
          for(int j = 0; j < boxesWidth; j++) {
            int x = j * 100;
            int y = 100 + (i * 25);
            
            if(i < 2)
              this.boxColor = Color.red;
            else if((i >= 2) && (i < 4))
              this.boxColor = Color.orange;
            else if((i >= 4) && (i < 6))
              this.boxColor = Color.yellow;
            else
              this.boxColor = Color.green;
            
            boxes.add(new Rectangles(boxColor, x, y, 0, 0, 100, 25));
          }
        }

        this.setPreferredSize(this.size);
        this.setBackground(Color.black);
        this.setFocusable(true);
        timer.start();     
    }
   
  //action listener
  public void actionPerformed(ActionEvent e) {
    this.ball.move();

    // bounce ball off of sides
    if(this.ball.getRect().getX() + this.ball.getRect().getWidth() >= FRAME_WIDTH || this.ball.getRect().getX() <= 0) {
      this.ball.setDX(-ball.getDX());
    }
     
    // bounce ball off of top
    if(this.ball.getRect().getY() <= 0) {
      this.ball.setDY(-ball.getDY());
    }

    // bounce ball off of paddle
    if(ball.getRect().intersects(paddle.getRect())) {
      // ball on left side, move left
      if((ball.getRect().getX() + ball.getRadius()) < (paddle.getRect().getX() + (paddle.getRect().getWidth() / 3))) {
        ball.setDX(-1);
        ball.setDY(-2);
      }
     
      // ball on right side, move right
      else if((ball.getRect().getX() + ball.getRadius()) > (paddle.getRect().getX() + (paddle.getRect().getWidth() - (paddle.getRect().getWidth() / 3)))) {
        ball.setDX(1);
        ball.setDY(-2);
      }
      
      // ball on center, move straight
      else {
        ball.setDX(0);
        ball.setDY(-2);
      }
    } 

    // remove boxes when hit by ball
    for(int i = 0; i < boxes.size(); i++) {
      if(ball.getRect().intersects(boxes.get(i).getRect())) {  
        boxes.remove(i);
        ball.setDY(-ball.getDY());
      }
    }
    
    // give winner message when all boxes removed
    if(boxes.isEmpty()) {
      resultFrame.setText("You win!");
      resultFrame.setVisible(true);
      timer.stop();
    }
    
    // give loser message when ball hits bottom
    if(this.ball.getRect().getY() >= FRAME_HEIGHT) {
      resultFrame.setText("Game over!"); 
      resultFrame.setVisible(true);
      timer.stop();
    }
    
    repaint();
  }
  
  //paint the ball, paddle, and boxes
  public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      this.ball.draw(g);
      this.paddle.draw(g);

      for(int i = 0; i < boxes.size(); i++) {
        boxes.get(i).draw(g);
      }
  }
 
  //key listener for moving paddle
  private class PaddleKeyListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent e){
      switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          paddle.setX(paddle.getX() - 20);
        break;
        case KeyEvent.VK_RIGHT:
          paddle.setX(paddle.getX() + 20);
        break;
      }
      repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent event){
    }
    
    @Override
    public void keyReleased(KeyEvent event){
    }
  }
}
