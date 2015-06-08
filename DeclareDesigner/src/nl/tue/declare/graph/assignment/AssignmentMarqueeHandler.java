package nl.tue.declare.graph.assignment;

import java.awt.event.*;
import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

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
public class AssignmentMarqueeHandler
    extends BasicMarqueeHandler {

  JGraph graph = null;
  IAssignmentMarqueeListener listener = null;

  public AssignmentMarqueeHandler(JGraph graph) {
    super();
    this.graph = graph;
  }

  /**
   * Check if the mouse event is the one that starts the activity
   * @param e MouseEvent
   * @return boolean
   */
  private boolean startActivityMouseEvet(MouseEvent e) {
    return SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2);
  }

  // Override to Gain Control (for starting the activities)
  public boolean isForceMarqueeEvent(MouseEvent e) {
    // If startActivityMouseEvet then force

    if (this.startActivityMouseEvet(e)) {
      // Return Immediately
      return true;
    }
    // Else Call Superclass
    return super.isForceMarqueeEvent(e);
  }

  // Display PopupMenu or Remember Start Location and First Port
  public void mousePressed(final MouseEvent e) {
    // If startActivityMouseEvet then start activity
    if (this.startActivityMouseEvet(e)) {
      // Find Cell in Model Coordinates
      Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
      if (cell != null) {
        if ( (cell instanceof DefaultGraphCell) && listener != null) {
          listener.cellDoubleClicked( (DefaultGraphCell) cell);
        }
      }
    }
    else {
      // Call Superclass
      super.mousePressed(e);
    }
  }

  /**
   * setListener
   */
  public void setListener(IAssignmentMarqueeListener listener) {
    this.listener = listener;
  }

}
