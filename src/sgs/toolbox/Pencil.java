package toolbox;

import processing.core.*;
import processing.opengl.*;
import processing.javafx.*;
import processing.event.*;
import processing.data.*;
import processing.awt.*;
import java.util.ArrayList;

public class Pencil extends GUIObject implements PConstants {
  
  private boolean isDown, fillBG, snapToM;
  private int oldX, oldY, penX, penY, initW, initH;
  private float angle = 0;
  private ArrayList<Line> drawing = new ArrayList<Line>();
  private int col, bgCol;
  private int weight = 1;
  
  public Pencil(PApplet p, int x, int y, int w, int h) {
    super(p, x, y, w, h);
    col = parent.color(0, 0, 0);
    penX = x;
    penY = y;
    initW = w;
    initH = h;
  }
  
  public void snapToMouse(boolean s) {
    snapToM = s;
  }
  
  public boolean mouseEvent(MouseEvent e) {
    if (snapToM) {
      if (e.getAction() == MouseEvent.PRESS && inBounds(e.getX(), e.getY())) {
        if (isUp()) {
          moveTo(e.getX(), e.getY());
          down();
        } 
        return true;
      } else if (e.getAction() == MouseEvent.RELEASE) {
        up();
        return true;
      } else if (e.getAction() == MouseEvent.DRAG && !isUp()) {
        moveTo(e.getX(), e.getY());
        return true;
      } else if (inBounds(e.getX(), e.getY())) {
        return true;
      } else {
        return false;
      } // end of if-else
    } else {
      return false;
    } // end of if-else
  }
  
  private boolean inBounds(int x, int y) {
    return (x > xPos && x < xPos + width && y > yPos && y < yPos + height);
  }
  
  public void keyEvent(KeyEvent e) {
    //nothing
  }
  
  public void onResize(float xFactor, float yFactor) {
    super.onResize(xFactor, yFactor);
    float widthFactor = (float)width / initW;
    float heightFactor = (float)height / initH; 
    for (int i = 0; i < drawing.size(); i++) {
      drawing.get(i).onResize(widthFactor, heightFactor);
    } // end of if
  }
  
  public void draw() {
    if (fillBG) {
      parent.noStroke();
      parent.fill(bgCol);
      parent.rect(xPos, yPos, width, height);  
    } // end of if
    parent.stroke(col);
    parent.strokeWeight(weight);
    for (int i = 0; i < drawing.size(); i++) {
      drawing.get(i).draw(xPos, yPos);
    }
  }
  
  public void down() {
    isDown = true;
  }
  
  public void up() {
    isDown = false;
  }
  
  public void moveTo(int x, int y) {
    oldX = penX;
    oldY = penY;
    penX = x;
    penY = y;
    if (isDown && checkBorders()) {
      drawing.add(new Line(oldX - xPos, oldY - yPos, penX - xPos, penY - yPos));   
    }
  }
  
  private boolean checkBorders() {
    float xNew, yNew;
    if ((penX <= xPos || penX >= xPos + width || penY <= yPos || penY >= yPos + height)
    && (oldX <= xPos || oldX >= xPos + width || oldY <= yPos || oldY >= yPos + height)) {
      return false; 
    }
    if (!(penX < xPos && oldX < xPos)) {
      if (penX < xPos) {
        yNew = (parent.parseFloat(xPos - oldX) / parent.parseFloat(penX - oldX)) * (penY - oldY) + oldY;
        xNew = xPos;
        penX = (int)xNew;
        penY = (int)yNew;
        return true;
      } else if (oldX < xPos) {
        yNew = (parent.parseFloat(xPos - penX) / parent.parseFloat(oldX - penX)) * (oldY - penY) + penY;
        xNew = xPos;
        oldX = (int)xNew;
        oldY = (int)yNew;
        return true;
      }
    } else {
      return false; 
    }  
    if (!(penX > xPos + width && oldX > xPos + width)) {
      if (penX > xPos + width) {
        yNew = (parent.parseFloat(xPos + width - oldX) / parent.parseFloat(penX - oldX)) * (penY - oldY) + oldY; 
        xNew = xPos + width;
        penX = (int)xNew;
        penY = (int)yNew;
        return true;
      } else if (oldX > xPos + width) {
        yNew = (parent.parseFloat(xPos + width - penX) / parent.parseFloat(oldX - penX)) * (oldY - penY) + penY; 
        xNew = xPos + width;
        oldX = (int)xNew;
        oldY = (int)yNew;
        return true;
      } 
    } else {
      return false;
    }
    if (!(penY < yPos && oldY < yPos)) {
      if (penY < yPos) {
        xNew = (parent.parseFloat(yPos - oldY) / (penY - oldY)) * (penX - oldX) + oldX;
        yNew = yPos;
        penX = (int)xNew;
        penY = (int)yNew;
        return true;
      } else if (oldY < yPos) {
        xNew = (parent.parseFloat(yPos - penY) / (oldY - penY)) * (oldX - penX) + penX;
        yNew = yPos;
        oldX = (int)xNew;
        oldY = (int)yNew;
        return true;
      } 
    } else {
      return false;
    }
    if (!(penY > yPos + height && oldY > yPos + height)) {
      if (penY > yPos + height) {
        xNew = (parent.parseFloat(yPos + height - oldY) / (penY - oldY)) * (penX - oldX) + oldX;
        yNew = yPos + height;
        penX = (int)xNew;
        penY = (int)yNew;
        return true;
      } else if (oldY > yPos + height) {
        xNew = (parent.parseFloat(yPos + height - penY) / (oldY - penY)) * (oldX - penX) + penX;
        yNew = yPos + height;
        oldX = (int)xNew;
        oldY = (int)yNew;
        return true;
      } 
    } else {
      return false;
    }
    return true;
    
  }
  
  public void moveBy(int x, int y) {
    moveTo(penX + x, penY + y);
  }
  
  public void moveBy(int r) {
    moveTo(penX + PApplet.parseInt(r * PApplet.cos(angle)), penY + PApplet.parseInt(r * PApplet.sin(angle)));
  }
  
  public void turnBy(int w) {
    angle += w;
    angle %= 360;
  }
  
  public void turnTo(int w) {
    angle = w;
    angle %= 360;
  }
  
  public void setColor(int c) {
    col = c;
  }
  
  public void setBackgroundColor(int c) {
    bgCol = c;
    fillBG = true;
  }
  
  public void setStrokeWidth(int b) {
    weight = b;
  }
  
  public boolean isUp() {
    return !isDown;
  }
  
  public int getColor() {
    return col;
  }
  
  public void clearDrawing() {
    drawing.clear();
  }
  
  private class Line {
    
    private int x1, y1, x2, y2, initX, initX2, initY, initY2;
    private float flipFactorY = 0;
    private float flipFactorX = 0;
    
    public Line(int xa, int ya, int xb, int yb) {
      x1 = xa;
      y1 = ya;
      x2 = xb;
      y2 = yb;
      initY = y1;
      initY2 = y2;
      initX = x1;
      initX2 = x2;
    }
    
    public void onResize(float xFactor, float yFactor) {
      if (flipFactorX == 0) {
        flipFactorX = 1 / xFactor;
      } // end of if
      if (flipFactorY == 0) {
        flipFactorY = 1 / yFactor;
      } // end of if                
      x1 = (int)(initX * xFactor * flipFactorX);
      x2 = (int)(initX2 * xFactor * flipFactorX); 
      
      y1 = (int)(initY * yFactor * flipFactorY);
      y2 = (int)(initY2 * yFactor * flipFactorY); 
    }
    
    public void draw(int x, int y) {
      parent.line(x + x1, y + y1, x + x2, y + y2);
    }
    
  }
}
  