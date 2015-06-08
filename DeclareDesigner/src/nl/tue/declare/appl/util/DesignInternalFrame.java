package nl.tue.declare.appl.util;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;

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
public abstract class DesignInternalFrame
    extends JInternalFrame implements ActionListener, MouseListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 7054042280371433113L;
static final int xOffset = 30, yOffset = 30;

  protected DesignInternalFrame(String title) {
    super(title,
          true, //resizable
          true, //closable
          true, //maximizable
          true); //iconifiable
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //...Create the GUI and put it in the window...

    //...Then set the window size or call pack...
    setSize(300, 300);
  }

  public abstract void actionPerformed(ActionEvent e);

  /**
   * Invoked when the mouse button has been clicked (pressed and released) on a
   * component.
   *
   * @param e MouseEvent
   * @todo Implement this java.awt.event.MouseListener method
   */
  public void mouseClicked(MouseEvent e) {
  }

  /**
   * Invoked when the mouse enters a component.
   *
   * @param e MouseEvent
   * @todo Implement this java.awt.event.MouseListener method
   */
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * Invoked when the mouse exits a component.
   *
   * @param e MouseEvent
   * @todo Implement this java.awt.event.MouseListener method
   */
  public void mouseExited(MouseEvent e) {
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   *
   * @param e MouseEvent
   * @todo Implement this java.awt.event.MouseListener method
   */
  public void mousePressed(MouseEvent e) {
  }

  /**
   * Invoked when a mouse button has been released on a component.
   *
   * @param e MouseEvent
   * @todo Implement this java.awt.event.MouseListener method
   */
  public void mouseReleased(MouseEvent e) {
  }

  /**
   * fillList
   *
   * @param anList List
   * @param anJList JList
   */
  protected void fillList(Collection<Object> anList, JList anJList) {
    FrameUtil.fillList(anList, anJList);
  }

  /**
   * fillList
   *
   * @param anJList JList
   * @return Object
   */
  protected Object getSelecetdList(JList anJList) {
    return FrameUtil.getSelecetdList(anJList);
  }

  /**
   */
  public void maximize() {
    try {
      this.setVisible(true);
      this.setMaximum(true);
      this.setSelected(true);
    }
    catch (java.beans.PropertyVetoException e) {}
  }
}
