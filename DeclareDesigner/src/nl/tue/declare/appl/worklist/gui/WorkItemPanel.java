package nl.tue.declare.appl.worklist.gui;

import java.awt.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.execution.*;

public class WorkItemPanel
    extends DefaultPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1139779288513023880L;

	private  WorkItem workItem;


  WorkItemPanel(WorkItem workItem) {
    super(new BorderLayout());
    this.workItem = workItem;
  }
  
  protected WorkItem getWorkItem(){
	  return workItem;
  }

  /**
   * buildInterface
   *
   * @todo Implement this nl.tue.declare.appl.util.swing.DefaultPanel method
   */
  protected void buildInterface() {

  }
}
