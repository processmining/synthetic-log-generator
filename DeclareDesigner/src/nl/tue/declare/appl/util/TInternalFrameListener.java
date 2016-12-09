package nl.tue.declare.appl.util;

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
import javax.swing.event.*;

public class TInternalFrameListener
    implements InternalFrameListener {
  public TInternalFrameListener() {
    super();
  }

  public void internalFrameDeactivated(InternalFrameEvent e) {}

  public void internalFrameActivated(InternalFrameEvent e) {}

  public void internalFrameDeiconified(InternalFrameEvent e) {}

  public void internalFrameIconified(InternalFrameEvent e) {}

  public void internalFrameClosed(InternalFrameEvent e) {}

  public void internalFrameClosing(InternalFrameEvent e) {}

  public void internalFrameOpened(InternalFrameEvent e) {}

}
