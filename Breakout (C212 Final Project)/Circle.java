/*
 * Reagan Roush, rdroush
 * Lab 8 - Circle Class extended from Shape Class
 * March 3, 2016
 * Updated March 10, 2016 - Added draw method
 * Updated March 23, 2016 - Added move method
 * Updated April 11, 2016 - Lots of simplification
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Circle extends Shape {
  
    private int radius;

    // set fillColor to random and border color to black 
    public Circle(Color fillColor, int x, int y, int dx, int dy, int r) { 
      super(fillColor, x, y, dx, dy);
      this.radius = r;
    }

    // returns a circle's radius
    public int getRadius() {
      return this.radius;
    }

    //returns a Rectangle setting the circle's bounds
    @Override
    public Rectangle getRect() {
      return new Rectangle(getX(), getY(), getRadius()*2, getRadius()*2);
    }
    
    // draw a filled Circle with its border
    // overriding abstract method declared in Shape class
    @Override
    public void draw(Graphics g) {
      g.setColor(this.getFillColor());
      g.fillOval(this.getX(), this.getY(), this.getRadius() * 2, this.getRadius() * 2);
      g.setColor(this.getBorderColor());
      g.drawOval(this.getX(), this.getY(), this.getRadius() * 2, this.getRadius() * 2);
    }

}
