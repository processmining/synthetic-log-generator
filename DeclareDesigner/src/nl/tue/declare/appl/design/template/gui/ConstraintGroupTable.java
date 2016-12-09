package nl.tue.declare.appl.design.template.gui;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.template.*;

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
public class ConstraintGroupTable
    extends TTable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 9021829222538651619L;

public ConstraintGroupTable(ConstraintGroupTableModel dm) {
    super(dm);
  }

  public ConstraintGroup getSelected() {
    ConstraintGroup group = null;
    Object selected = super.getSelected();
    if (selected != null) {
      if (selected instanceof ConstraintGroup) {
        group = (ConstraintGroup) selected;
      }
    }
    return group;
  }
}
