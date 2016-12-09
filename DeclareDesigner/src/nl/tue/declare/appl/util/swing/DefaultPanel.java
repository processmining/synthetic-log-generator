package nl.tue.declare.appl.util.swing;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public abstract class DefaultPanel
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 824556080673312891L;
	private int minimumWidth = 0;

 public DefaultPanel(LayoutManager manager) {
    super(manager);
    buildInterface();
    setMaximumSize(getPreferredSize());
  }
 
   public void setMinimumWidth(int w){
	   this.minimumWidth = w;
   }

  protected abstract void buildInterface();
  
  public void componentResized(ComponentEvent e) {
	  
	  if (this.minimumWidth > 0){
	  
      int width = getWidth();
    //we check if either the width
    //or the height are below minimum
    boolean resize = false;
      if (width < minimumWidth) {
           resize = true;
           width = minimumWidth;
    }
    if (resize) {
          setSize(width, getHeight());
    }
	  }
   }
}
