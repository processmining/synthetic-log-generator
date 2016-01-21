package nl.tue.declare.graph.model;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.graph.*;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.Font;

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
public class ActivityDefinitonCell
    extends DVertex {

  /**
	 * 
	 */
	private static final long serialVersionUID = -319493519832010439L;

/**
   *
   * @param anActivityDefinition ActivityDefinition
   */
  public ActivityDefinitonCell(ActivityDefinition anActivityDefinition, int x,
                               int y) {
    super(anActivityDefinition, x, y, null, false, false);
    addPort();
    if (anActivityDefinition.isExternal()){
      Border border = DGraphConstants.getBorder(getAttributes());
      Font old = DGraphConstants.getFont(getAttributes());
      Font font = new Font(old.getName(), Font.BOLD, old.getSize()-2);
      DGraphConstants.setBorder(this.getAttributes(),BorderFactory.createTitledBorder(border,"YAWL",TitledBorder.LEADING,TitledBorder.TOP,font));
    }
  }



  /**
   *
   * @param anActivityDefinition ActivityDefinition
   */
  protected ActivityDefinitonCell(DVertex vertex) {
    super(vertex);
    addPort();
  }

 /**
   *
   * @return ActivityDefinition
   */
  public ActivityDefinition getActivityDefinition() {
    ActivityDefinition activityDefinition = null;
    Object userObject = getUserObject();
    if (userObject != null) {
      if (userObject instanceof ActivityDefinition) {
        activityDefinition = (ActivityDefinition) userObject;
      }
    }
    return activityDefinition;
  }

  /**
   * addPort
 * @return 
   */
  public Object addPort() {
    this.add(new ActivityDefinitionPort(this));
	return attributes; //Tartu
  }

  public double getDiameter() {
    double diameter = Math.sqrt(Math.pow(super.getHeight(), 2) +
                                Math.pow(super.getWidth(), 2)) + 20;
    return diameter;
  }
}
