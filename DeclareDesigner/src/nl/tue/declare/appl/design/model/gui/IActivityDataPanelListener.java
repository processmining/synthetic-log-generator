package nl.tue.declare.appl.design.model.gui;

/**
 *
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

import nl.tue.declare.domain.model.*;

public interface IActivityDataPanelListener {
  /**
   * add
   *
   * @return ActivityDataDefinition
   */
  public ActivityDataDefinition add(ActivityDefinition activity);

  /**
   * delete
   *
   * @param data ActivityDataDefinition
   */
  public boolean delete(ActivityDataDefinition data,
                        ActivityDefinition activity);

  /**
   * edit
   *
   * @param data ActivityDataDefinition
   */
  public boolean edit(ActivityDataDefinition data, ActivityDefinition activity);
}
