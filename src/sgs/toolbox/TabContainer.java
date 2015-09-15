package toolbox;

import processing.core.*;
import processing.opengl.*;
import processing.javafx.*;
import processing.event.*;
import processing.data.*;
import processing.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

public class TabContainer extends GUIObject implements PConstants {
  
  private ArrayList<Tab> tabs;
  private ArrayList<String> tabNames;
  private int selectedTab = 0;
  private int col, bgCol;
  private int hovered = -1, clicked = -1;
  
  public TabContainer(PApplet p, int x, int y, int w, int h) {
    super(p, x, y, w, h);
    tabs = new ArrayList<Tab>();
    tabNames = new ArrayList<String>();
  }
  
  public void setColor(int c) {
    col = c;
  }
  
  public void setBackgroundColor(int c) {
    bgCol = c;
  }
  
  public void newTab(String n) {
    tabs.add(new Tab(parent, n));
    tabNames.add(n);
  }
  
  public void removeTab(String n) {
    int i = tabNames.indexOf(n);
    tabs.remove(i);
    tabNames.remove(i);
  }
  
  public int getSelectedTab() {
    return selectedTab;
  }
  
  public void chooseTab(int i) {
    selectedTab = i;
    if (selectedTab < 0) {
      selectedTab = tabs.size() - 1;
    } // end of if
    if (selectedTab >= tabs.size()) {
      selectedTab = 0;
    } // end of if
  }
  
  public void addObject(String n, GUIObject o) {
    int i = tabNames.indexOf(n);
    tabs.get(i).addObject(o);
  }
  
  public void draw() {
    parent.noStroke();  
    parent.fill(bgCol);
    parent.rect(xPos, yPos, width, height);
    parent.fill(col);
    parent.textAlign(CENTER, CENTER);
    
    for (int i = 0; i < tabs.size() ; i++ ) {
      if (i == hovered && i != selectedTab) {
        parent.textSize(11);
      } else {
        parent.textSize(12);
      } // end of if-else
      parent.text(tabNames.get(i), xPos + (i * 70), yPos, 70, height); 
    } // end of for
    parent.rect(xPos + (selectedTab * 70), yPos + height - 2, 70, 2);
    
    if (selectedTab < tabs.size()) {
      tabs.get(selectedTab).draw();
    } // end of if
  }
  
  public void onResize(float xFactor, float yFactor) {
    super.onResize(xFactor, yFactor);
    for (int i = 0; i < tabs.size(); i++) {
      tabs.get(i).onResize(xFactor, yFactor);
    } // end of if
  }
  
  public void setResizable(boolean x, boolean y, boolean w, boolean h, boolean p) {
    super.setResizable(x, y, w, h, p); 
    for (int i = 0; i < tabs.size(); i++) {
      tabs.get(i).setResizable(x, y, w, h, p); 
    } // end of for
  }
  
  public boolean mouseEvent(MouseEvent e) {
    if (e.getAction() == MouseEvent.MOVE) {
      mouseOver(e.getX(), e.getY());
      if (hovered > -1) {
        return true;
      } // end of if
    } else if (hovered > -1 && e.getAction() == MouseEvent.RELEASE) {
      selectedTab = hovered;
      return true;
    } else if (isBarHovered(e.getX(), e.getY()) && e.getAction() == MouseEvent.WHEEL) {
      chooseTab(selectedTab - e.getCount());
    }
    if (selectedTab < tabs.size()) {  
      if (tabs.get(selectedTab).mouseEvent(e)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isBarHovered(int x, int y) {
    if (x > xPos && x < xPos + width && y > yPos && y < yPos + height) {
      return true;
    } else {
      return false;
    }
  }
  
  public void keyEvent(KeyEvent e) {
    if (selectedTab < tabs.size()) {  
      tabs.get(selectedTab).keyEvent(e);
    }
  }
  
  private void mouseOver(int x, int y) {
    for (int i = 0; i < tabs.size() ; i++ ) {
      if (x > xPos + (i * 70) && x < xPos + (i * 70) + 70 && y > yPos && y < yPos + height) {
        hovered = i;
        return;
      } // end of if
    } // end of for
    hovered = -1;
  }
  
  private class Tab extends GUIObject implements PConstants {
    
    private String name;
    private ArrayList<GUIObject> objects;
    
    public Tab(PApplet p, String n) {
      super(p, 0, 0);
      name = n;
      objects = new ArrayList<GUIObject>();
    }
    
    public void addObject(GUIObject o) {
      objects.add(o);
    }
    
    public void draw() {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).draw();
      } // end of for
    }
    
    public void onResize(float xFactor, float yFactor) {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).onResize(xFactor, yFactor);
      } // end of for
    }
    
    public void setResizable(boolean x, boolean y, boolean w, boolean h, boolean p) {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).setResizable(x, y, w, h, p);
      } // end of for
    }
    
    public void keyEvent(KeyEvent e) {
      for (int i = 0; i < objects.size() ; i++ ) {
        objects.get(i).keyEvent(e);
      } // end of for
    } 
    
    public boolean mouseEvent(MouseEvent e) {
      for (int i = 0; i < objects.size() ; i++ ) {
        if (objects.get(i).mouseEvent(e)) {
          return true;
        }
      } // end of for
      return false;
    }
  }
  
  
  
}