package nl.tue.declare.datamanagement;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.graph.model.*;

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
public interface AssignmentViewBroker
    extends AssignmentBroker {

  /**
   * addAssignmentAndView
   *
   * @param model AssignmentModel
   * @param view AssignmentModelView
   * @param path String
   */
  public void addAssignmentAndView(AssignmentModel model,
                                   AssignmentModelView view);

  /**
   * readAssignment
   *
   * @return AssignmentModel
   * @param path String
   */
  public void readAssignmentGraphical(AssignmentModel model,
                                      AssignmentModelView view
                                      /*, String path*/);

  void readAssignmentGraphicalFromString(AssignmentModel model,
                                         AssignmentModelView view,
                                         String documentString);
}
