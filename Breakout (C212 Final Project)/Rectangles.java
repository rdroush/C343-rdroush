/*
 * Reagan Roush, rdroush
 * Lab 8 - Rectangle Class extended from Shape Class
 * March 3, 2016
 * Updated March 10, 2016 - Added draw method
 * Updated April 11, 2016 - Lots of simplification
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Rectangles extends Shape {
  
    private int width;
    private int height;

    // constructor
    public Rectangles(Color fillColor, int x, int y, int dx, int dy, int w, int h) { 
      super(fillColor, x, y, dx, dy);
      this.width = w;
      this.height = h;
    }

    // returns a rectangle's width
    public int getWidth() {
      return this.width;
    }

    // returns a rectangle's height
    public int getHeight() {
      return this.height;
    }

    // returns a Rectangle setting the rectangle's bounds
    @Override
    public Rectangle getRect() {
      return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
    
    // draw a filled Rectangle or Square with its border
    // overriding abstract method declared in Shape class
    @Override
    public void draw(Graphics g) {
      g.setColor(this.getFillColor());
      g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
      g.setColor(this.getBorderColor());
      g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

}
