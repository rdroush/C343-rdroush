/*
 * Reagan Roush, rdroush
 * Lab 8 - Base Shape Class
 * March 3, 2016
 * Updated March 10, 2016 - Added draw method
 * Updated March 23, 2016 - Added move method
 * Updated April 11, 2016 - Lots of simplification
 */
import java.awt.Color;  
import java.awt.Graphics;
import java.awt.Rectangle;

abstract class Shape {

    private Color fillColor;
    private Color borderColor;
    private int x;
    private int y;
    private int dx;
    private int dy;

    // constructor
    public Shape(Color fillColor, int x, int y, int dx, int dy) { 
      this.fillColor = fillColor;
      this.borderColor = Color.black;
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
    }

    // set fill color of a shape
    public void setFillColor(Color c) { 
      this.fillColor = c;
    }

    // return fill color of a shape
    public Color getFillColor() { 
      return this.fillColor;
    }

    // return border color of a shape
    public Color getBorderColor() { 
      return this.borderColor;
    }

    // return x point of a shape
    public int getX() { 
      return this.x;
    }
 
    public void setX(int i) {
      this.x = i;
    }
    
    // return y point of a shape
    public int getY() {
      return this.y;
    }
 
    public void setY(int i) {
      this.y = i;
    }
    
    public int getDX() {
      return this.dx;
    }
    
    // reverse direction on x-axis
    public void setDX(int i) {
      this.dx = i;
    }
    
    public int getDY() {
      return this.dy;
    }
        
    // reverse direction on y-axis
    public void setDY(int i) {
      this.dy = i;
    }

    // moves a given Shape by its dx and dy
    public void move() { 
      this.x = getX() + dx;
      this.y = getY() + dy;
    }

    // draw a Shape
    // abstract method overridden in subclasses
    abstract void draw(Graphics g); { 
    }   
    
    // returns the Rectangle field of a shape
    // abstract method overridden in subclasses
    abstract Rectangle getRect(); {
    }

}