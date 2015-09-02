//package sgs.baukasten;

import processing.core.*;
import processing.opengl.*;
import processing.javafx.*;
import processing.event.*;
import processing.data.*;
import processing.awt.*;
import java.util.ArrayList;

public class Wahlbox extends GUIObjekt implements PConstants {
  
  public final static int CHECKBOX = 1;
  public final static int CHECKBOX_ROUND = 2;
  public final static int TOGGLE_BUTTON = 3;
  
  private String name;
  private int col, bgCol, disCol;
  private int textGroesse, stil, breite, hoehe;
  private boolean hovered, pressed, checked, disabled;
  
  public Wahlbox(PApplet p, String n, int x, int y, boolean c) {
    super(p, x, y);
    name = n;
    col = parent.color(0, 0, 0);
    bgCol = parent.color(255, 255, 255);
    disCol = parent.color(30, 30, 30, 150);
    stil = CHECKBOX;
    textGroesse = 15;
    breite = hoehe = 30;
    checked = c;
  }
  
  public void setzeFarbe(int c) {
    col = c;
  }
  
  public void setzeHintergrundFarbe(int c) {
    bgCol = c;
  }
  
  public void setzeTextGroesse(int t) {
    textGroesse = t;
  }
  
  public void setzeStil(int i) {
    if (i == CHECKBOX || i == CHECKBOX_ROUND) {
      stil = i;
      hoehe = breite;
    } else if (i == TOGGLE_BUTTON) {
      stil = i;
    }
  }
  
  public void draw() {
    parent.textSize(textGroesse);
    parent.strokeWeight(2);
    
    if (pressed) {
      if (checked) {
        parent.fill(bgCol);
        parent.stroke(col);
        drawBody(xPos + 2, yPos + 2, breite - 4, hoehe - 4);
      } else {
        parent.fill(bgCol);
        parent.stroke(col);
        drawBody(xPos + 2, yPos + 2, breite - 4, hoehe - 4);
        parent.fill(col);
        parent.noStroke();
        drawBody(xPos + 7, yPos + 7, breite - 13, hoehe - 13);
      }
    } else if ((hovered && !checked) || (!hovered && checked)) {
      parent.fill(bgCol);
      parent.stroke(col);
      drawBody(xPos, yPos, breite, hoehe);
      parent.fill(col);
      parent.noStroke();
      drawBody(xPos + 5, yPos + 5, breite - 9, hoehe - 9);
    } else {
      parent.fill(bgCol);
      parent.stroke(col);
      drawBody(xPos, yPos, breite, hoehe);
    }
    
    if (disabled) {
      parent.fill(disCol);
      parent.stroke(disCol);
      drawBody(xPos, yPos, breite, hoehe);
      if (stil == TOGGLE_BUTTON) {
        parent.fill(255, 255, 255);
        parent.textAlign(CENTER, CENTER);
        parent.text("DISABLED", xPos + (breite / 2), yPos + (hoehe / 2));
      }
    }
  }
  
  private void drawBody(int x, int y, int b, int h) {
    if (stil == CHECKBOX) { 
      parent.textAlign(CENTER, CENTER);
      parent.rect(x, y, b, h);
      parent.fill(col);
      parent.text(name, xPos + 35, yPos + (hoehe / 2));
    } else if (stil == CHECKBOX_ROUND) {
      parent.textAlign(LEFT, CENTER);
      parent.ellipseMode(CORNER);
      parent.ellipse(x, y, b, h);
      parent.fill(col);
      parent.text(name, xPos + 35, yPos + (hoehe / 2));
    } else if (stil == TOGGLE_BUTTON) {
      parent.textAlign(CENTER, CENTER);
      parent.rect(x, y, b, h);
      parent.text(name, xPos + (breite / 2), yPos + (hoehe / 2));
    }
  }
  
  public void mouseEvent(MouseEvent e) {
    if (e.getAction() == MouseEvent.MOVE) {
      mouseOver(e.getX(), e.getY());
    } else if (e.getAction() == MouseEvent.PRESS) {
      pressed(e.getX(), e.getY());
    } else if (e.getAction() == MouseEvent.RELEASE) {
      clicked(e.getX(), e.getY());
    }
  }
  
  private void mouseOver(int x, int y) {
    if (stil == CHECKBOX || stil == TOGGLE_BUTTON) {
      if (x > xPos && x < xPos + breite && y > yPos && y < yPos + hoehe) {
        hovered = true;
      } else {
        hovered = false;
      }
    } else if (stil == CHECKBOX_ROUND) {
      if (PApplet.sqrt(PApplet.pow(x - (xPos + (breite/2)), 2) + PApplet.pow(y - (yPos + (breite/2)), 2)) < breite/2) {
        hovered = true;
      } else {
        hovered = false;
      }
    }
  }
  
  private void pressed(int x, int y) {
    if (disabled) {
      pressed = false;
      return;
    }
    mouseOver(x, y);
    if (hovered && parent.mousePressed) {
      hovered = false;
      pressed = true;
    } else {
      pressed = false;
    }
  }
  
  private void clicked(int x, int y) {
    if (!disabled) {
      mouseOver(x, y);
      if (hovered) {
        checked = !checked;
      }
    }
    pressed = false;
  }
  
  public boolean istGewaehlt() {
    return checked;
  }
  
  public boolean isMouseOver() {
    return hovered;
  }
  
  public boolean istGedrueckt() {
    return pressed;
  }
  
  public void keyEvent(KeyEvent e) {
    //nothing
  }
  
  public boolean isEnabled() {
    return !disabled;
  }
  
  public void disable() {
    disabled = true;
    hovered = false;
    pressed = false;
  }
  
  public void enable() {
    disabled = false;
  }
  
  protected void check() {
    checked = true;
  }
  
  protected void uncheck() {
    checked = false;
  }
  
}