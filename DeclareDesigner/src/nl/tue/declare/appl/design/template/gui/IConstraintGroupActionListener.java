package nl.tue.declare.appl.design.template.gui;

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
public interface IConstraintGroupActionListener {

  /**
   *
   * @return ConstraintGroup
   */
  ConstraintGroup add();

  boolean edit(ConstraintGroup group);

  boolean delete(ConstraintGroup group);

  ConstraintWarningLevel warning();
}
