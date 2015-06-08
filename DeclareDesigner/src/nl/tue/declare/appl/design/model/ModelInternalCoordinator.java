package nl.tue.declare.appl.design.model;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.model.*;

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
public class ModelInternalCoordinator {

  protected AssignmentPanel panel;
  protected AssignmentModel model;

  public ModelInternalCoordinator(AssignmentPanel panel, AssignmentModel aModel) {
    super();
    this.panel = panel;
    model = aModel;
  }

  protected Control getControl() {
    return Control.singleton();
  }
}
